package com.mooncrusher.jexpressions;
import java.util.*;

public class ExpressionHandler{
	private static final String[] FUNCTIONS = { "abs", "acos", "arg", "asin",
	"atan",	"conj", "cos", "cosh", "exp", "imag", "log", "neg", "pow", "real",
	"sin", "sinh", "sqrt", "tan", "tanh" };
	private static final String OPERATORS = "+-*/^|%&=";
	private static final String[] CONSTANTS = {"pi", "e"};

	private static boolean isNumber(String token){
		try {Double.parseDouble(token);}
		catch(Exception xxx){return false;}
		return true;
	}
	private static boolean isConstant(String token){
		for(String iter : CONSTANTS){
			if (iter.equals(token)){
				return true;
			}
		}
		return false;
	}
	private static boolean isOperator(String token){
		return OPERATORS.contains(token);
	}
	private static boolean isFunction(String token){
		for(String iter : FUNCTIONS){
			if (iter.equals(token)){
				return true;
			}
		}
		return false;
	}
	private static boolean isSeparator(String token){
		return token.equals(",");
	}
	private static boolean isUnaryMinus(String token){
		return token.equals("±");
	}
	private static boolean isOpenBracket(String token){
		return token.equals("(");
	}
	private static boolean isCloseBracket(String token){
		return token.equals(")");
	}
	private static boolean isLowerPriority(String op1, String op2){
		switch (op1){
			case "|":
			case "&":
				return !(op2.equals("|") || op2.equals("&"));
			case "+":
			case "-":
				return (op2.equals("*") || op2.equals("/") || op2.equals("%") || op2.equals("^"));
			case "*":
			case "/":
			case "%":
				return op2.equals("^");
			case "^":
				return true;
			default:
				return false; //(never happen)
		}
	}
	private static double constantValue(String token){
		switch(token){
			case "pi":
				return Math.PI;
			case "e":
				return Math.E;
		}
		return 0; //shouldnt happen
	}
	private static double operate(String op, String aa, String bb){
		double a = Double.parseDouble(aa);
		double b = Double.parseDouble(bb);
		switch (op){
			case "+":
				return a+b;
			case "-":
				return a-b;
			case "*":
				return a*b;
			case "/":
				return a/b;
			case "^":
				return Math.pow(a,b);
			case "|":
				if (a==1||b==1){
					return 1;
				}
				return 0;
			case "%":
				return a%b;
			case "&":
				if (a==1&&b==1){
					return 1;
				}
				return 0;
			case "=":
				if (a==b){
					return 1;
				}
				return 0;
		}
		return 0;//shouldnt happen
	}

	public String parsePostfix(String infix){
		infix = infix.replace(" ", "").replace("(-", "(0-").replace(",-", ",0-");
		if (infix.charAt(0)=='-'){
			infix = "0" + infix;
		}

		StringTokenizer tokens = new StringTokenizer(infix, OPERATORS + "()"+ ","+ "±", true);
		StringBuffer postfix = new StringBuffer("");
		Stack<String> operators = new Stack<>();

		while(tokens.hasMoreTokens()){
			String token = tokens.nextToken();

			if (isNumber(token)||isConstant(token)){
				postfix.append(token+ " ");
				continue;
			}
			if (isFunction(token)){
				operators.push(token);
				continue;
			}
			if (isSeparator(token)){
				while(!operators.peek().equals("(")){
					postfix.append(operators.pop()+ " ");
				}
				continue;
			}
			if (isOperator(token)){
				while(!operators.empty() && isOperator(operators.peek()) &&
				isLowerPriority(token, operators.peek())){
					postfix.append(operators.pop()+ " ");
				}
				operators.push(token);
				continue;
			}
			if (isOpenBracket(token)){
				operators.push(token);
				continue;
			}
			if (isCloseBracket(token)){
				while(!operators.empty() && !isOpenBracket(operators.peek())){
					postfix.append(operators.pop()+ " ");
				}
				operators.pop();
				if (isFunction(operators.peek())){
					postfix.append(operators.pop()+ " ");
				}
				continue;
			}
		}
		while(!operators.empty()){
			postfix.append(operators.pop());
		}
		return postfix.toString();
	}

	public double computeExpression(String postfix){
		StringTokenizer tokens = new StringTokenizer(postfix, " ");
		Stack<String> operands = new Stack<>();

		while(tokens.hasMoreTokens()){
			String token = tokens.nextToken();
			if (isNumber(token)){
				operands.push(token);
				continue;
			}
			if (isConstant(token)){
				operands.push(constantValue(token) + "");
				continue;
			}
			if (isOperator(token)||isFunction(token)){
				String a = operands.pop();
				String b = operands.pop();
				operands.push(operate(token, a, b)+ "");
			}
		}
		return Double.parseDouble(operands.pop());
	}
}
