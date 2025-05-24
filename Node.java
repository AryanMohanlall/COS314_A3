abstract class Node {
    public Node left;
    public Node right;

    public Node(Node left, Node right){
        this.left = left;
        this.right = right;
    }

    public Node getLeft(){
        return this.left;
    }

    public Node getRight(){
        return this.right;
    }

    public void setLeft(Node l){
        this.left = l;
    }

    public void setRight(Node r){
        this.right = r;
    }

    abstract public String toString();

};
