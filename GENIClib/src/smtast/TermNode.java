package smtast;



public class TermNode extends SMTASTNode{
	public TermNode(QVListNode qvl, TermNode t, Integer i){
		setChildtype(i);
		mychild = t;
		mylist = qvl;
		mysymbol = null;
	}
	public TermNode(String s){
		mysymbol = s;
		setChildtype(SYMBOL);
		mylist = null;
		mychild = null;
		symboltype = true;
	}
	public TermNode(NumConstNode cn){
		mychild = cn;
		setChildtype(CONSTANT);
		mylist = null;
		mysymbol = null;
	}
	public TermNode(String s, TermListNode l){
		mysymbol = s;
		mylist = null;
		setChildtype(LIST);
		mychild = l;
		symboltype = false;
	}
	
	public final static Integer FORALL = 0;
	public final static Integer EXISTS = 1;
	public final static Integer SYMBOL = 2;
	public final static Integer CONSTANT = 3;
	public final static Integer LIST = 4;
	public final static boolean FUNC = false;
	public final static boolean VAR = true;
	
	private boolean symboltype;
	private SMTASTNode mylist;
	private SMTASTNode mychild;
	private String mysymbol;
	private Integer childtype;
	@Override
	public void print_this() {
		//TODO
	}
	public String getSymbol(){
		return mysymbol;
	}
	public boolean isFuncSymbol(){
		return !symboltype;
	}
	public boolean isVarSymbol(){
		return symboltype;
	}
	public QVListNode getQVList(){
		return (QVListNode) mylist;
	}
	public SMTASTNode getChild(){
		return mychild;
	}
	public Integer getChildtype() {
		return childtype;
	}
	
	public void setChildtype(Integer childtype) {
		this.childtype = childtype;
	}
	
	@Override
	public String to_String_z3() {
		String result = "";
		switch(this.getChildtype()){
		case 0: result += "(forall (";
				result += mylist.to_String_z3();
				result += ") ";
				result += mychild.to_String_z3() + ") ";
				break;
		case 1: result += "(exists (";
				result += mylist.to_String_z3();
				result += ") ";
				result += mychild.to_String_z3() + ") ";
				break;
		case 2: result += mysymbol + " ";
				break;
		case 3: result += mychild.to_String_z3();
				break;
		case 4: result += "(" + mysymbol + " " + mylist.to_String_z3() + ") ";
				break;
		}
		return result;
	}
	
}