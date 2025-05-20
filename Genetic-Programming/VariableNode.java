public class VariableNode extends Node {

    private char variable;

    public VariableNode(Node left, Node right) {
        super(left, right);
        this.variable = 0;
    }

    public VariableNode(Node left, Node right, char v) {
        super(left, right);
        this.variable = v;
    }

    public char getVariable(){
        return this.variable;
    }

    public void setVariable(char v){
        this.variable = v;
    }
        
}
