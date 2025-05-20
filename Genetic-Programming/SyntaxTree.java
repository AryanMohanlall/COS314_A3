import java.util.Random;

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

    }



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
        int length = 10;
        
        for(int i=0; i<length; i++){
            float nodeType = random.nextFloat(0f, 1f);
            System.out.println(nodeType);

            if(nodeType < 0.3f){
                Node n = new FunctionNode(null, null, functionSet[random.nextInt(functionSet.length)]);
                append(n, root);
            }//add function node

            if(nodeType < 0.3f && nodeType > 0.6f){
                Node n = new TerminalNode(null, null, random.nextFloat(0, 6969));
                append(n, root);
            }//add terminal node

            if(nodeType > 0.6f){
                Node n= new VariableNode(null, null, (char)(random.nextInt(26) + 'a'));
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

    public boolean validTree(Node cur, int fNodes, int tNodes){
        boolean res = true;
        return res;
    }
}
