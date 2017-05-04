package com.mooncrusher.jexpressions;
import java.io.*;

public class JexpressionsTest{
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		ExpressionHandler exp = new ExpressionHandler();
		while(true){
			String s = reader.readLine();
			String t = exp.parsePostfix(s);
			System.out.println(t);
			System.out.println(exp.computeExpression(t));
		}
	}
}
