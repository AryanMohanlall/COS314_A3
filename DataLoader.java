import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private static List<Double> mins = new ArrayList<>();
    private static List<Double> maxs = new ArrayList<>();

    public static List<double[]> loadData(String filePath) {
        List<double[]> data = new ArrayList<>();
        File file = new File(filePath);
        
        System.out.println("Loading file: " + file.getAbsolutePath());
        
        if (!file.exists()) {
            System.err.println("Error: File not found - " + file.getAbsolutePath());
            return data;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                double[] row = new double[6];
                for (int i = 0; i < 6; i++) {
                    row[i] = Double.parseDouble(values[i]);
                }
                data.add(row);
            }
            System.out.println("Loaded " + data.size() + " records");
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    public static void normalizeTrain(List<double[]> trainData) {
        mins.clear();
        maxs.clear();
        
        for (int i = 0; i < 5; i++) {
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (double[] row : trainData) {
                if (row[i] < min) min = row[i];
                if (row[i] > max) max = row[i];
            }
            mins.add(min);
            maxs.add(max);
        }
        normalizeDataset(trainData);
    }

    public static void normalizeTest(List<double[]> testData) {
        normalizeDataset(testData);
    }

    private static void normalizeDataset(List<double[]> dataset) {
        for (double[] row : dataset) {
            for (int i = 0; i < 5; i++) {
                if (maxs.get(i) - mins.get(i) == 0) {
                    row[i] = 0.0;
                } else {
                    row[i] = (row[i] - mins.get(i)) / (maxs.get(i) - mins.get(i));
                }
            }
        }
    }
}