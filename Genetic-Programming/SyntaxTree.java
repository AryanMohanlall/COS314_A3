public class SyntaxTree {
    private Node root;
    public String funcString;

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
}
