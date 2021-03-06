package ast;

import java.util.List;



public class TransitionNode extends ASTnode{
	public TransitionNode(List<String> p, PredNode predstr, OutputNode o){
		myPattern = p;
		myPred = predstr;
		myOutput = o;
	}
	private List<String> myPattern;
	private PredNode myPred;
	private OutputNode myOutput;
	
	public List<String> getPattern(){
		return myPattern;
	}
	
	public List<String> getVarList(){
		return myPattern.subList(0, myPattern.size()-1);
	}
	
	public OutputNode getOutput(){
		return myOutput;
	}
	public List<OutputFuncNode> getOutputFuncList(){
		return myOutput.getOutputFuncList();
	}
	public PredNode getPredNode(){
		return myPred;
	}
	public Integer getLookahead(){
		return myPattern.size()-1;
	}
	public String getPred(){
		return myPred.getPred();
	}
	@Override
	public void print_this() {
		System.out.print(myPattern.get(0));
		for(String s: myPattern){
			if(s != myPattern.get(0))
			System.out.print("::"+s);
		}
		System.out.print(" when ");
		myPred.print_this();
		System.out.print("; ->");
		myOutput.print_this();
		
	}
}