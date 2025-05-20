class FunctionNode extends Node{

    private FunctionSet function;

    public FunctionNode(Node left, Node right) {
        super(left, right);
    }

    public FunctionNode(Node left, Node right, FunctionSet f){
        super(left, right);
        this.function = f;
    }

    public FunctionSet getFunction(){
        return this.function;
    }

    public void setFunction(FunctionSet function){
        this.function = function;
    }

};
