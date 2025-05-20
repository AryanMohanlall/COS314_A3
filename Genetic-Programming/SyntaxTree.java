import java.util.Random;

public class SyntaxTree {
    private Node root;
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

    public Node getRoot(){
        return this.root;
    }

    public void append(Node newNode, Node cur) {
        if(this.root == null){
            this.root = newNode;
            return;
        }

        if(cur.getLeft() == null){
            cur.setLeft(newNode);
        }else{
            append(newNode, cur.getLeft());
        }
    }



    public String interpret(Node node, String reString){
        if(node == null) return "";

        reString = "";
        reString += interpret(node.getLeft(), reString);
        reString += node.toString() + " ";
        reString += interpret(node.getRight(), reString);

        return reString;
    }

    public void buildSyntaxTree(long seed){
        Random random = new Random(seed);
        int length = random.nextInt(10);
        
        for(int i=0; i<length; i++){
            float nodeType = random.nextFloat(0f, 1f);
            if(nodeType >= 0.5f){
                
            }//add function node

            if(nodeType < 0.5f){

            }//add terminal node
        }
    }

    public boolean isFunction(String fString){
        return fString.matches("[+\\-*/^]");
    }

    public boolean isTerminal(String vString){
        return vString.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
}
