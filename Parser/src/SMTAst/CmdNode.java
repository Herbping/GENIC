package SMTAst;
import java.util.List;

import Ast.*;

public 
class CmdNode extends SMTASTNode{
	public CmdNode(SMTASTNode n, Integer type){
		mychild = n;
		childtype = type;
	}
	public final static Integer CMDDEF = 0;
	public final static Integer CMDTERM = 1;
	
	private SMTASTNode mychild;
	private int childtype;
	
	public void print_this() {
		mychild.print_this();
		System.out.println();
	}
	
	public Integer gettype(){
		return childtype;
	}
	public String getFuncName(){
		if(childtype == CMDTERM)
			return null;
		return ((CmddefNode)mychild).getFuncName();
	}
	@Override
	public String to_String_z3() {
		return mychild.to_String_z3();
	}
	public List<TypeNode> getIntype(){
		if(childtype == CMDTERM)
			return null;
		return ((CmddefNode)mychild).getIntype();
	}
	public List<String> getVarList(){
		if(childtype == CMDTERM)
			return null;
		return ((CmddefNode)mychild).getVarList();
	}
	public TypeNode getOuttype(){
		if(childtype == CMDTERM)
			return null;
		return ((CmddefNode)mychild).getOuttype();
	}
}