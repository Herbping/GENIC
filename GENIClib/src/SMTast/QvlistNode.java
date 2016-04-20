package SMTast;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ast.*;


public class QvlistNode extends SMTASTNode{
	public QvlistNode(){
		mylist = new LinkedList<QuantvarNode>();
	}
	
	private LinkedList<QuantvarNode> mylist;
	
	public void add(QuantvarNode n){
		mylist.addLast(n);
	}
	@Override
	public void print_this() {
		for(int i = 0; i < mylist.size(); i++){
			mylist.get(i).print_this();
		}
	}
	@Override
	public String to_String_z3() {
		String result = "";
		for(int i = 0; i < mylist.size(); i++){
			result += mylist.get(i).to_String_z3();
		}
		return result;
	}
	public List<TypeNode> getIntype(){
		List<TypeNode> result = new ArrayList<TypeNode>();
		for(int i=0; i<mylist.size(); i++){
			result.add(mylist.get(i).getType());
		}
		return result;
	}	
	public List<String> getVarList(){
		List<String> result = new ArrayList<String>();
		for(int i=0; i<mylist.size(); i++){
			result.add(mylist.get(i).getName());
		}
		return result;
	}

	
}