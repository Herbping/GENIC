package SMTAst;
import Ast.*;


public class QuantvarNode extends SMTASTNode{
	public QuantvarNode(String s, SortNode sn){
		mysymbol = s;
		mysort = sn;
	}
	
	private String mysymbol;
	private SortNode mysort;
	
	@Override
	public void print_this() {
		System.out.print("(" + mysymbol +" ");
		mysort.print_this();
		System.out.print(")");
	}

	@Override
	public String to_String_z3() {
		String result = "(" + mysymbol +" " + mysort.to_String_z3() + ")";
		return result;
	}	
	public TypeNode getType(){
		return mysort.getType();
	}
	public String getName(){
		return mysymbol;
	}
}
