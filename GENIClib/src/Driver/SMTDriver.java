package Driver;

import java.io.ByteArrayInputStream;

import smtparser.parser;
import java_cup.runtime.Symbol;

public class SMTDriver {
	public static Symbol GetSMTRootFromString(String str) throws Exception{
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		Symbol root = new parser().ParsingStreamToAST(stream);
		return root;
	}
}
