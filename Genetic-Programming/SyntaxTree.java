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



    public void interpret(Node node){
        if(node == null) return;

        interpret(node.getLeft());
        System.out.println(node.toString());
        interpret(node.getRight());
    }
}
