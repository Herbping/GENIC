package injchecker;

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

public class ESFAConvertor {
	List<ProgNode> progs;
	String finalState;
	String initialState;
	Map<String, Integer> nameList;
	Context ctx;
	
	ESFAConvertor(CoderNode root, Context ctx){
		this.progs = root.getProgList();
		finalState = "F";
		initialState = "S";
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
		for(ProgNode node: this.progs){
			// the id and name of FROM node
			Integer from = getId(node.getName());
			String from_s = node.getName();
			
			List<TransitionNode> transList = node.getTransList();
			for(TransitionNode trans: transList){
				// the id and name of TO node
				Integer to = getId(trans.getOutput().getFunc().FuncName());
				String to_s = trans.getOutput().getFunc().FuncName();
				
				Integer lookahead = trans.getLookahead();
				// factory used to generate predicate
				Z3Factory factory = new Z3Factory(ctx);
				BoolExpr pred = (BoolExpr) factory.StringToExpr(trans.getPred());
				BoolExpr[] preds = new BoolExpr[lookahead];
				
				preds = toCartesianPreds(pred);
				
				// get id and name of way points
				String [] wayPoints_s = new String[lookahead - 1];
				Integer [] wayPoints = new Integer[lookahead - 1];
				for(int i = 0; i < wayPoints.length - 1; i++){
					wayPoints_s[i] = from_s + "_" + i + "_" + to_s;
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
				Z3BooleanAlgebra Z3ba = new Z3BooleanAlgebra(ctx);
				return SFA.MkSFA(transitions, getId(initialState), Arrays.asList(getId(finalState)), Z3ba);
			}
		}
		return null;
	}

	private BoolExpr[] toCartesianPreds(BoolExpr pred) {
		// TODO Auto-generated method stub
		return null;
	}
}