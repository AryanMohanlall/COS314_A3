import java.util.Random;
import java.util.Vector;

public class SyntaxTree {
    public Node root;
    public String funcString;

    public final FunctionSet[] functionSet = {FunctionSet.PLUS, FunctionSet.MINUS, FunctionSet.DIVIDE, FunctionSet.MULTIPLY, FunctionSet.POWER};

    public SyntaxTree(){
        this.root = null;
        this.funcString = "";
    }

    public SyntaxTree(Node node){
        this.root = node;
        this.funcString = "";
    }

    public SyntaxTree(long seed){
        Random random = new Random(seed);
        int randomFunc = random.nextInt(functionSet.length);
        this.root = new FunctionNode(null, null, functionSet[randomFunc]);        
    }

    public Node getRoot(){
        return this.root;
    }

    public void append(Node newNode, Node cur) {

        if(cur == null) return;

        if(((cur.getClass().getName() == "FunctionNode")) && ((cur.getLeft() == null)  || (cur.getRight() == null))){

            if(cur.getLeft() == null){
                cur.setLeft(newNode);
                return;
            }

            if(cur.getRight() == null){
                cur.setRight(newNode);
                return;
            }

        }//any node

        append(newNode, cur.getLeft());
        append(newNode, cur.getRight());

    }// problem here sometimes



    public String interpret(Node node, String reString){
        if(node == null) return "";

        reString = "";
        reString += interpret(node.getLeft(), reString);
        reString += node.toString() + " ";
        reString += interpret(node.getRight(), reString);

        return reString;
    }

        public String interpretPreOrder(Node node, String reString){
        if(node == null) return "";

        reString = "";
        reString += interpret(node.getLeft(), reString);
        reString += interpret(node.getRight(), reString);
        reString += node.toString() + " ";

        return reString;
    }

    public void buildSyntaxTree(long seed){
        Random random = new Random(seed);
        int length = random.nextInt(5,100);
        
        for(int i=0; i<length; i++){
            float nodeType = random.nextFloat(0f, 1f);

            if(nodeType < 0.3f){
                Node n = new FunctionNode(null, null, functionSet[random.nextInt(functionSet.length)]);
                append(n, root);
            }//add function node

            if(nodeType > 0.3f && nodeType < 0.6f){
                Node n = new TerminalNode(null, null, random.nextFloat(0, 10));
                append(n, root);
            }//add terminal node

             if(nodeType > 0.6f){
                Node n = new VariableNode(null, null, (char)(random.nextInt(5) + 'a'));
                append(n, root);
            }//add variable node
        }
    }

    public boolean isFunction(String fString){
        return fString.matches("[+\\-*/^]");
    }

    public boolean isTerminal(String vString){
        return vString.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public boolean isVariable(String aString){
        return aString.matches("[a-z]");
    }

    public float compute(int a, int b, int c, int d, int e){
        Vector<String> stack = new Vector<>();
        int input[] = {a, b, c, d, e};
        funcString = interpret(root, "");
        String equationStr[] = funcString.split(" ");
        float num1;
        String op;
        float num2;

        stack.addElement(equationStr[0]);
        for(int i=1; i<equationStr.length; i++){
            stack.addElement(equationStr[i]);

            if(stack.size() == 3){
                if(isTerminal(stack.get(0))){
                    num1 = Float.parseFloat(stack.get(0));
                }else{
                    char var = stack.get(0).charAt(0);
                    int idx = Character.getNumericValue(var) % 5;
                    num1 = input[idx];
                }

                op = stack.get(1);

                if(isTerminal(stack.get(2))){
                    num2 = Float.parseFloat(stack.get(2));
                }else{
                    char var = stack.get(2).charAt(0);
                    int idx = Character.getNumericValue(var) % 5;
                    num2 = input[idx];
                }
                

                String res = Float.toString(calcOfStringOp(num1, op, num2));
                stack.set(0, res);
                stack.remove(1);
                stack.remove(1);
            }
        }

        return Float.parseFloat(stack.get(0));
    }

    public float calcOfStringOp(float num1, String op, float num2){
        float res = 0;

        if(op.charAt(0) == '+'){
            res = num1 + num2;
        }

        if(op.charAt(0) == '-'){
            res = num1 - num2;
        }

        if(op.charAt(0) == '*'){
            res = num1 * num2;
        }

        if(op.charAt(0) == '/'){
            res = num1 / num2;
        }

        if(op.charAt(0) == '^'){
            res = (float)Math.pow(num1, num2);
        }
        return res;
    }

    public boolean validTree(){
        int t = 0;
        int f= 0;
        String eq[] = interpret(root, "").split(" ");
        for(String s : eq){
            if(isFunction(s)) f+=1;
            if(isTerminal(s) || isVariable(s)) t+=1;
        }
        return (t == (f+1));
    }
}
