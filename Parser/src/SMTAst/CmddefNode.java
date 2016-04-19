package SMTAst;
import java.util.List;

import Ast.*;


public class CmddefNode extends SMTASTNode{
	public CmddefNode(String s, QvlistNode qvl, SortNode ast, TermNode t){
		funcname = s;
		funcsort = ast;
		mylist = qvl;
		functerm = t;
	}
	
	private String funcname;
	private SortNode funcsort;
	private TermNode functerm;
	private QvlistNode mylist;
	
	@Override
	public void print_this() {
		System.out.print("(define-fun " + funcname + " (");
		mylist.print_this();
		System.out.print(") ");
		funcsort.print_this();
		System.out.print(" ");
		functerm.print_this();
	}
	public List<TypeNode> getIntype(){
		return mylist.getIntype();
	}	
	public List<String> getVarList(){
		return mylist.getVarList();
	}	
	public TypeNode getOuttype(){
		return funcsort.getType();
	}
	String getFuncName(){
		return funcname;
	}

	@Override
	public String to_String_z3() {
		String result = "(define-fun " + funcname + " (";
		result += mylist.to_String_z3();
		result += ") ";
		result += funcsort.to_String_z3();
		result += " " + functerm.to_String_z3() + ")";
		return result;
	}
}