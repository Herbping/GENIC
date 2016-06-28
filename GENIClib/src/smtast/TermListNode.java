package smtast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class TermListNode extends SMTASTNode{
	public TermListNode(){
		mylist = new LinkedList<TermNode>();
	}
	
	public TermListNode(TermNode t){
		mylist = new LinkedList<TermNode>();
		mylist.add(t);
	}
	
	public TermListNode(List<TermNode> l){
		mylist = new LinkedList<TermNode>();
		mylist.addAll(l);
	}	
	public boolean containVar(String vname){
		for(TermNode t: mylist){
			if(t.containVar(vname))
				return true;
		}
		return false;
	}
	
	public void add(TermNode t){
		mylist.addLast(t);
	}
	
	private LinkedList<TermNode> mylist;
	
	public List<TermNode> getList(){
		return mylist;
	}
	
	public List<String> getVarList(){
		List<String> result = new ArrayList();
		for(TermNode t: mylist)
			result.addAll(t.getVarList());
		return result;
	}
	
	public Set<String> getConstSet_Int(){
		Set<String> result = new HashSet<String>();
		for(TermNode t: mylist)
			result.addAll(t.getConstSet_Int());
		return result;
	}
	
	@Override
	public void print_this() {
		for(int i = 0; i < mylist.size(); i++){
			mylist.get(i).print_this();
		}		
	}
	@Override
	public String toString_z3() {
		String result = "";
		for(int i = 0; i < mylist.size(); i++){
			result += mylist.get(i).toString_z3();
		}		
		return result;
	}

	public List<TermNode> getArgs() {
		return mylist;
	}

	public Set< String> getConstSet_BV(Integer length) {
		Set<String> result = new HashSet<String>();
		for(TermNode t: mylist)
			result.addAll(t.getConstSet_BV(length));
		return result;
	}
}
