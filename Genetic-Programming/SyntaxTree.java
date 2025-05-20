public class SyntaxTree {
    private Node root;
    public boolean lastSet; // 1=function 2=terminal

    public SyntaxTree(){
        this.root = null;
    }

    public SyntaxTree(Node node){
        this.root = node;
    }

    public Node getRoot(){
        return this.root;
    }

    public void append(Node node){

    }

    public String interpret(Node node, String res){
        if(node == null) return res;

        interpret(node.getLeft(), res);
        if()
        interpret(node.getRight(), res);

    }
}
