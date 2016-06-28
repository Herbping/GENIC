package injchecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import theory.Z3Pred.Z3BooleanAlgebra;
import ast.*;
import automata.sfa.SFA;
import automata.sfa.SFAInputMove;
import automata.sfa.SFAMove;
import z3factory.Z3Factory;

import com.microsoft.z3.*;

public class SFAConvertor {
	List<ProgNode> progs;
	String finalState;
	String initialState;
	Map<String, Integer> nameList;
	Context ctx;
	Expr unit;
	
	public SFAConvertor(CoderNode root, Context ctx){
		this.progs = root.getProgList();
		finalState = "F";
		initialState = "F";
		nameList = new HashMap();
		this.ctx = ctx;
	}
	
	Integer getId(String s){
		if(nameList.get(s) != null)
			return nameList.get(s);
		else{
			nameList.put(s, nameList.size()+1);
			return nameList.size();
		}
	}
	
	public SFA<BoolExpr, Expr> Convert() throws Exception{
		Collection<SFAMove<BoolExpr, Expr>> transitions = new LinkedList<SFAMove<BoolExpr, Expr>>();
		Z3BooleanAlgebra Z3ba = new Z3BooleanAlgebra(ctx);
		
		
		unit = ctx.mkConst("x", toSort(progs.get(0).getInType().getType()));
		
		for(ProgNode node: this.progs){
			// the id and name of FROM node
			Integer from = getId(node.getName());
			String from_s = node.getName();
			List<TransitionNode> transList = node.getTransList();
			for(int j = 0; j < transList.size(); j++){
				TransitionNode trans = transList.get(j);
				// the id and name of TO node
				Integer to = getId(trans.getOutput().getFunc().FuncName());
				String to_s = trans.getOutput().getFunc().FuncName();
				
				//System.out.println(TransitionInjChecker.check(ctx, domain, varsName, outputFuncs, sort));
				
				Integer lookahead = trans.getLookahead();
				// factory used to generate predicate
				Z3Factory factory = new Z3Factory(ctx);
				BoolExpr pred = (BoolExpr) factory.StringToExpr(trans.getPred());
				//System.out.println(pred);
				BoolExpr[] preds = new BoolExpr[lookahead];
				List<String> varsname= trans.getVarList();
				List<Expr> varlist = new ArrayList<Expr>();
				
				//////////// Bad Code
				for(int i = 0; i < varsname.size(); i++){
					varlist.add(ctx.mkIntConst(varsname.get(i)));
				}
				////////////
				
				preds = toCartesianPreds(pred, varlist);
				// get id and name of way points
				String [] wayPoints_s = new String[lookahead - 1];
				Integer [] wayPoints = new Integer[lookahead - 1];
				for(int i = 0; i < wayPoints.length ; i++){
					wayPoints_s[i] = from_s + "_" + j + "_" + i + "_" + to_s;
					wayPoints[i] = getId(wayPoints_s[i]);
				}
				
				if(lookahead == 1)
					transitions.add(new SFAInputMove<BoolExpr, Expr>(from, to, preds[0]));
				else{
					transitions.add(new SFAInputMove<BoolExpr, Expr>(from, wayPoints[0], preds[0]));
					for(int i = 0; i < lookahead - 2; i++){
						transitions.add(new SFAInputMove<BoolExpr, Expr>(wayPoints[i], wayPoints[i+1], preds[i+1]));
					}
					transitions.add(new SFAInputMove<BoolExpr, Expr>(wayPoints[lookahead-2], to, preds[lookahead - 1]));
				}

				
			}
		}
		//System.out.println(SFA.MkSFA(transitions, getId(initialState), Arrays.asList(getId(finalState)), Z3ba));
		return SFA.MkSFA(transitions, getId(initialState), Arrays.asList(getId(finalState)), Z3ba);
	}

	private BoolExpr[] toCartesianPreds(BoolExpr pred, List<Expr> varlist) throws Exception {
		long start = System.currentTimeMillis();
		Integer size = varlist.size();
		BoolExpr[] preds = new BoolExpr[size];
		Model model = check(ctx, pred, Status.SATISFIABLE);
		for(int i = 0; i < size; i++){
			preds[i] = pred;
			for(int j = 0; j < size; j++){
				if(i != j)
					preds[i] = (BoolExpr) preds[i].substitute(varlist.get(j), model.getConstInterp(varlist.get(j)));
			}
			preds[i] = (BoolExpr) preds[i].simplify();
			preds[i] = (BoolExpr) preds[i].substitute(varlist.get(i), unit);
		}
		long end = System.currentTimeMillis();
		//System.out.println("Cartesian: " + (end-start));
		return preds;
	}
	@SuppressWarnings("unused")
	private boolean CartesianCheck(BoolExpr pred, List<Expr> varlist) throws Exception {
		BoolExpr cart = ctx.mkAnd(toCartesianPreds(pred, varlist));
		BoolExpr neq = ctx.mkNot(ctx.mkEq(cart, pred));
		
		return check(ctx, neq, Status.SATISFIABLE) == null;
	}
	
	public Sort toSort(Integer t){
		if(t == 0)
			return ctx.mkIntSort();
		return null;
	}
	
	 static Model check(Context ctx, BoolExpr f, Status sat) throws Exception
	    {
	        Solver s = ctx.mkSolver();
	        s.add(f);
	        if (s.check() != sat)
	        	throw new Exception();
	        if (sat == Status.SATISFIABLE)
	            return s.getModel();
	        else
	            return null;
	    }
	
}