import java.util.Random;
import java.util.Vector;

public class SyntaxTree {
    public Node root;
    public String funcString;
    int depth;
    public double fitness=0.0;

    public final FunctionSet[] functionSet = {FunctionSet.PLUS, FunctionSet.MINUS, FunctionSet.DIVIDE, FunctionSet.MULTIPLY, FunctionSet.POWER};

    public SyntaxTree(){
        this.root = null;
        this.funcString = "";
    }

    public SyntaxTree(Node node){
        this.root = node;
        this.funcString = "";
    }

    public SyntaxTree(long seed, int depth){
        Random random = new Random(seed);
        int randomFunc = random.nextInt(functionSet.length);
        this.root = new FunctionNode(null, null, functionSet[randomFunc]);
        this.depth = depth;     
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

        }

        append(newNode, cur.getLeft());
        append(newNode, cur.getRight());

    }// problem here sometimes



    public String interpret(Node node, String reString){
        if(node == null) return "";

        reString = "";
        reString += interpret(node.getLeft(), reString);
        reString += node.toString() + " ";
        reString += interpret(node.getRight(), reString);

        this.funcString = reString;
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
        int length = 10000;
        int i=0;
        do{
            if(i%3 == 0){
                Node n = new FunctionNode(null, null, functionSet[random.nextInt(functionSet.length)]);
                append(n, root);
            }//add function node

            if(i%3 == 1){
                Node n = new TerminalNode(null, null, random.nextFloat(-2, 2));
                append(n, root);
            }//add terminal node

             if(i%3 == 2){
                Node n = new VariableNode(null, null, (char)(random.nextInt(5) + 'a'));
                append(n, root);
            }//add variable node
            i++;
        }while(i<length && depth(root) < depth);
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
        if(!validTree()) return 0;

        Vector<String> stack = new Vector<>();
        int input[] = {a, b, c, d, e};
        funcString = interpret(root, "");
        String equationStr[] = funcString.split(" ");
        float num1;
        String op;
        float num2;

        stack.addElement(equationStr[0]);

        if(equationStr.length == 1){
            if(isTerminal(stack.get(0))) return Float.parseFloat(stack.get(0));
            if(isVariable(stack.get(0))){
                char var = stack.get(0).charAt(0);
                int idx = Character.getNumericValue(var) % 5;
                return input[idx];
            }
            if(isFunction(stack.get(0))) return 0;
        }// length=1

        for(int i=1; i<equationStr.length; i++){
            stack.addElement(equationStr[i]);

            if(stack.size() == 3){
                if(isTerminal(stack.get(0))){
                    num1 = Float.parseFloat(stack.get(0));
                }else{
                    char var = stack.get(0).charAt(0);
                    int idx = Character.getNumericValue(var) % 5;
                    num1 = input[Math.abs(idx)];
                }

                op = stack.get(1);

                if(isTerminal(stack.get(2))){
                    num2 = Float.parseFloat(stack.get(2));
                }else{
                    char var = stack.get(2).charAt(0);
                    int idx = Character.getNumericValue(var) % 5;
                    num2 = input[Math.abs(idx)];
                }
                

                String res = Float.toString(calcOfStringOp(num1, op, num2));
                stack.set(0, res);
                stack.remove(1);
                stack.remove(1);
            }
        }

         float res = Float.parseFloat(stack.get(0));
        if((Float.isNaN(res)) || (Float.isInfinite(res))) res = 0;
        return res;
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

    public int depth(Node cur){
        if(cur == null) return -1;

        int lHeight = depth(cur.left);
        int rHeight = depth(cur.right);

        return Math.max(lHeight, rHeight) + 1;
    }

    public int numNodes(Node cur){
        if(cur == null)  return 0;
        int l =numNodes(cur.left);
        int r =numNodes(cur.right);

        return (1 + r + l);
    }

    public void Mutation(long seed){
        Random random = new Random(seed);
        float mutationType = random.nextFloat(0, 1);
        int idx = random.nextInt(2,numNodes(root));

        if(mutationType < 0.5f){
            float terminalType = random.nextFloat(0, 1);
            Node randNode;
            if(terminalType < 0.5) randNode = new TerminalNode(null, null, random.nextFloat(-2, 2));
            else randNode = new VariableNode(null, null, (char)(random.nextInt(5) + 'a'));

            this.root = this.Mutation(idx, new int[]{0}, randNode, root);
        }
        if(mutationType >= 0.5f){
            SyntaxTree subtree = new SyntaxTree(seed, this.depth/2);
            subtree.buildSyntaxTree(seed);

            this.root = this.Mutation(idx, new int[]{0}, subtree.root, root);
        }
    }

    public Node Mutation(int idx, int[] count, Node randNode, Node cur) {
        if (cur == null) return null;

        if (idx == count[0]) {
            return randNode;
        }

        count[0]++;

        cur.left = Mutation(idx, count, randNode, cur.left);
        cur.right = Mutation(idx, count, randNode, cur.right);

        return cur;
    }

    @Override
    public SyntaxTree clone() {
        return new SyntaxTree(cloneTree(this.root));
    }

    private Node cloneTree(Node node) {
        if (node == null) return null;
        Node leftClone = cloneTree(node.getLeft());
        Node rightClone = cloneTree(node.getRight());

        if (node instanceof FunctionNode) {
            return new FunctionNode(leftClone, rightClone, ((FunctionNode) node).getFunction());
        } else if (node instanceof TerminalNode) {
            return new TerminalNode(leftClone, rightClone, ((TerminalNode) node).getValue());
        } else if (node instanceof VariableNode) {
            return new VariableNode(leftClone, rightClone, ((VariableNode) node).getVariable());
        }
        return null;
    }

    public void setFitness(double fit){
        fitness = fit;
    }

    public double getFitness(){
        return fitness;
    }

    public SyntaxTree crossover(SyntaxTree tree1, Random rand){
        Node copyThis = cloneTree(this.root);
        Node copyTree1= cloneTree(tree1.root);
        int sizeOfThis = numNodes(copyThis);
        int sizeOfTree1 = numNodes(copyTree1);

        if(sizeOfThis < 2 || sizeOfTree1 < 2){
            return new SyntaxTree(copyThis);
        }

        int indxThis = rand.nextInt(1, sizeOfThis);
        int indxTree1 = rand.nextInt(1, sizeOfThis);
        Node subtreeThis = getSubtree(indxThis, new int[]{0}, copyThis);
        Node subtreeTree1 = getSubtree(indxTree1, new int[]{0}, copyTree1);
    
        Node rt = replaceSubtree(indxThis, new int[]{0}, subtreeTree1, copyThis);
        return new SyntaxTree(rt);
    }

    private Node getSubtree(int target, int[] count, Node cur) {
        if (cur == null) return null;
        if (count[0] == target) return cur;
        count[0]++;
        Node left = getSubtree(target, count, cur.left);
        return (left != null) ? left : getSubtree(target, count, cur.right);
    }

    private Node replaceSubtree(int target, int[] count, Node replacement, Node cur) {
        if (cur == null){
            return null;
        }
        if (count[0] == target){
            return cloneTree(replacement);
        }
        count[0]++;
        cur.left = replaceSubtree(target, count, replacement, cur.left);
        cur.right = replaceSubtree(target, count, replacement, cur.right);
        return cur;
    }
}
