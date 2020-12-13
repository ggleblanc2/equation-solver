package com.ggl.testing;

import java.util.ArrayList;
import java.util.List;

public class EquationSolver {
	
	private static final String FORMAT = "%-30s";

	public static void main(String[] args) {
		EquationSolver es = new EquationSolver();
		String title = String.format(FORMAT, "Original Equation:");
		
		es.equationSolver(title, "121+432");
		es.equationSolver(title, "-121+432");
		es.equationSolver(title, "-121.212-432.234");
		es.equationSolver(title, "432/121");
		es.equationSolver(title, "1+2*3(30+4/2-(10+2))*2+1");
		es.equationSolver(title, "1+2*3(30+4/2-(-10+2))*2+1");
		es.equationSolver(title, "1+2*3(30+4/2-(10+2))*-2+1");
	}

	private void equationSolver(String title, String equation) {
		System.out.println(title + equation);
		List<String> parts = parseEquation(equation);
		processEquation(parts);
	}

	private List<String> parseEquation(String equation) {
		List<String> parts = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		String testCharacters = "+-*/()";
		char[] letters = equation.toCharArray();
		for (int i = 0; i < letters.length; i++) {
			if (letters[i] == ' ') {
				continue;
			}
			
			if (Character.isDigit(letters[i])) {
				builder.append(letters[i]);
				continue;
			}
			
			if (letters[i] == '.') {
				builder.append(letters[i]);
				continue;
			}
			
			if (contains(letters[i], testCharacters)) {
				addPart(parts, builder);
				parts.add(Character.toString(letters[i]));
			}
		}
		
		addPart(parts, builder);
		
		return parts;
	}

	private void addPart(List<String> parts, StringBuilder builder) {
		if (builder.length() > 0) {
			parts.add(builder.toString());
			builder.delete(0, builder.length());
		}
	}
	
	private boolean contains(char c, String text) {
		for (int i = 0; i < text.length(); i++) {
			char t = text.charAt(i);
			if (c == t) {
				return true;
			}
		}
		
		return false;
	}
	
	private void processEquation(List<String> parts) {
		String title = String.format(FORMAT, "Expanded Equation:");
		System.out.println(title + toString(parts));
		
		while (parts.size() > 1) {
			int endPos = findFirstCloseParenthesis(parts);
			int startPos = findMatchingOpenParenthesis(parts, endPos);
			int newEndPos = addMissingOperator(parts, startPos, endPos);
			if (newEndPos > endPos) {
				title = String.format(FORMAT, "Add Multiplication Operator:");
				System.out.println(title + toString(parts));
				endPos = newEndPos;
			}
			
			newEndPos = processUnaryMinus(parts, startPos, endPos);
			if (newEndPos < endPos) {
				title = String.format(FORMAT, "Unary Minus:");
				System.out.println(title + toString(parts));
				endPos = newEndPos;
			}
				
			if (startPos > 0) {
				endPos = processEquation(parts, startPos + 1, endPos - 1) + 1;
				removeParenthesis(parts, startPos, endPos);
				title = String.format(FORMAT, "Parenthesis Removed:");
				System.out.println(title + toString(parts));
			} else {
				processEquation(parts, startPos, endPos);
			}
		}
		
		System.out.println();
	}
	
	private int findFirstCloseParenthesis(List<String> parts) {
		for (int index = 0; index < parts.size(); index++) {
			if (parts.get(index).equals(")")) {
				return index;
			}
		}
		return parts.size() - 1;
	}
	
	private int findMatchingOpenParenthesis(List<String> parts, int endPos) {
		for (int index = endPos - 1; index >= 0; index--) {
			if (parts.get(index).equals("(")) {
				return index;
			}
		}
		return 0;
	}
	
	private int addMissingOperator(List<String> parts, int startPos, int endPos) {
		double test1 = valueOf(parts.get(startPos));
		for (int index = startPos + 1; index <= endPos; index++) {
			double test2 = valueOf(parts.get(index));
			if (test1 != Double.MIN_VALUE && test2 != Double.MIN_VALUE) {
				parts.add(index, "*");
				test1 = valueOf(parts.get(index));
				endPos++;
			} else {
				test1 = test2;
			}
		}
		
		return endPos;
	}
	
	private int processUnaryMinus(List<String> parts, int startPos, int endPos) {
		String string1 = parts.get(startPos);
		if (string1.equals("-")) {
			return makeValueNegative(parts, endPos, startPos);
		}
		
		double test1 = valueOf(string1);
		for (int index = startPos + 1; index <= endPos; index++) {
			String test2 = parts.get(index);
			if (test1 == Double.MIN_VALUE && test2.equals("-")) {
				return makeValueNegative(parts, endPos, index);
			} else {
				test1 = valueOf(test2);
			}
		}
		
		return endPos;
	}

	private int makeValueNegative(List<String> parts, int endPos, int index) {
		double value = - valueOf(parts.get(index + 1));
		parts.remove(index);
		parts.set(index, Double.toString(value));
		endPos--;
		return endPos;
	}
	
	private int processEquation(List<String> parts, int startPos, int endPos) {
		String[][] operators = new String[][] { { "*", "/", "+", "-" },
				{ "Multiplication:", "Division:", "Addition:", "Subtraction:" } };
	
		for (int opIndex = 0; opIndex < operators[0].length; opIndex++) {
			for (int index = startPos; index <= endPos; index++) {
				String operator = parts.get(index);
				if (operator.equals(operators[0][opIndex])) {
					double value1 = valueOf(parts.get(index - 1));
					double value2 = valueOf(parts.get(index + 1));
					double value = computeValue(value1, operator, value2);
					
					parts.set(index, Double.toString(value));
					parts.remove(index + 1);
					parts.remove(index - 1);
					
					index--;
					endPos -= 2;
					
					String title = String.format(FORMAT, operators[1][opIndex]);
					System.out.println(title + toString(parts));
				}
			}
		}
				
		return endPos;
	}
		
	private double computeValue(double value1, String operator, double value2) {
		double value = (operator.equals("*")) ? value1 * value2 : 0D;
		value = (operator.equals("/")) ? value1 / value2 : value;
		value = (operator.equals("+")) ? value1 + value2 : value;
		value = (operator.equals("-")) ? value1 - value2 : value;
		return value;
	}
	
	private void removeParenthesis(List<String> parts, int startPos, int endPos) {
		parts.remove(endPos);
		parts.remove(startPos);
	}
	
	private double valueOf(String part) {
		try {
			return Double.valueOf(part);
		} catch (NumberFormatException e) {
			return Double.MIN_VALUE;
		}
	}
	
	private String toString(List<String> parts) {
		StringBuilder builder = new StringBuilder();
		for (String s : parts) {
			builder.append(s).append(" ");
		}
		return builder.toString().trim();
	}
		
}
