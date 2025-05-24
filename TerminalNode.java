public class TerminalNode extends Node{
    private float value;

    public TerminalNode(Node left, Node right){
        super(left, right);
    }

    public TerminalNode(Node left, Node right, float val){
        super(left, right);
        this.value = val;
    }

    public float getValue(){
        return this.value;
    }

    public void setValue(float val){
        this.value = val;
    }

    public String toString(){
        return Float.toString(this.value);
    }

}
