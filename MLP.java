import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MLP {
    private double[][] weightsInputHidden;
    private double[][] weightsHiddenOutput;
    private final double learningRate;
    private final Random random;
    private final int inputSize;
    private final int hiddenSize;
    private final int outputSize;

    public MLP(int inputSize, int hiddenSize, int outputSize, double learningRate, long seed) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.learningRate = learningRate;
        this.random = new Random(seed);
        initializeWeights();
    }

    private void initializeWeights() {
        weightsInputHidden = new double[inputSize][hiddenSize];
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightsInputHidden[i][j] = random.nextGaussian() * Math.sqrt(2.0 / inputSize);
            }
        }
        
        weightsHiddenOutput = new double[hiddenSize][outputSize];
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weightsHiddenOutput[i][j] = random.nextGaussian() * Math.sqrt(2.0 / hiddenSize);
            }
        }
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public void train(List<double[]> trainData, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (double[] data : trainData) {
                double[] inputs = Arrays.copyOf(data, 5);
                double target = data[5];
                
                // Forward pass
                double[] hidden = new double[hiddenSize];
                for (int j = 0; j < hiddenSize; j++) {
                    for (int i = 0; i < inputSize; i++) {
                        hidden[j] += inputs[i] * weightsInputHidden[i][j];
                    }
                    hidden[j] = sigmoid(hidden[j]);
                }
                
                double[] output = new double[outputSize];
                for (int k = 0; k < outputSize; k++) {
                    for (int j = 0; j < hiddenSize; j++) {
                        output[k] += hidden[j] * weightsHiddenOutput[j][k];
                    }
                    output[k] = sigmoid(output[k]);
                }
                
                // Backpropagation
                double[] outputErrors = new double[outputSize];
                for (int k = 0; k < outputSize; k++) {
                    outputErrors[k] = (output[k] - target) * output[k] * (1 - output[k]);
                }
                
                double[] hiddenErrors = new double[hiddenSize];
                for (int j = 0; j < hiddenSize; j++) {
                    double error = 0.0;
                    for (int k = 0; k < outputSize; k++) {
                        error += outputErrors[k] * weightsHiddenOutput[j][k];
                    }
                    hiddenErrors[j] = error * hidden[j] * (1 - hidden[j]);
                }
                
                // Update weights
                for (int j = 0; j < hiddenSize; j++) {
                    for (int k = 0; k < outputSize; k++) {
                        weightsHiddenOutput[j][k] -= learningRate * outputErrors[k] * hidden[j];
                    }
                }
                
                for (int i = 0; i < inputSize; i++) {
                    for (int j = 0; j < hiddenSize; j++) {
                        weightsInputHidden[i][j] -= learningRate * hiddenErrors[j] * inputs[i];
                    }
                }
            }
        }
    }

    public Map<String, Double> evaluate(List<double[]> dataset) {
        int tp = 0, fp = 0, fn = 0, tn = 0;
        
        for (double[] data : dataset) {
            double[] inputs = Arrays.copyOf(data, 5);
            double prediction = predict(inputs);
            int actual = (int) Math.round(data[5]);
            int predicted = prediction >= 0.5 ? 1 : 0;
            
            if (predicted == 1) {
                if (actual == 1) tp++;
                else fp++;
            } else {
                if (actual == 1) fn++;
                else tn++;
            }
        }
        
        double accuracy = (double)(tp + tn) / (tp + tn + fp + fn);
        double precision = tp == 0 ? 0 : (double)tp / (tp + fp);
        double recall = tp == 0 ? 0 : (double)tp / (tp + fn);
        double f1 = (precision + recall) == 0 ? 0 : 2 * (precision * recall) / (precision + recall);
        
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("accuracy", accuracy);
        metrics.put("f1", f1);
        metrics.put("precision", precision);
        metrics.put("recall", recall);
        return metrics;
    }

    private double predict(double[] inputs) {
        double[] hidden = new double[hiddenSize];
        for (int j = 0; j < hiddenSize; j++) {
            for (int i = 0; i < inputSize; i++) {
                hidden[j] += inputs[i] * weightsInputHidden[i][j];
            }
            hidden[j] = sigmoid(hidden[j]);
        }
        
        double[] output = new double[outputSize];
        for (int k = 0; k < outputSize; k++) {
            for (int j = 0; j < hiddenSize; j++) {
                output[k] += hidden[j] * weightsHiddenOutput[j][k];
            }
            output[k] = sigmoid(output[k]);
        }
        return output[0];
    }
}