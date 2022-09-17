package lexico;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import utils.TokenType;

public class Scanner {
	private int state;
	private int pos;
	private char[] contentBuffer;
	private String[] reservedWords= {"int", "float", "print", "if", "else"};
	private int pCounter = 0;
	private int nLine = 0;
	private int nColumn = 0;
	
	public Scanner(String filename) {
		try {
			String contentTxt = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
			this.contentBuffer = contentTxt.toCharArray();
			this.pos = 0;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Token nextToken() {
		Token tk;
		String content = "";
		this.state = 0;
		char currentChar;
		
		if(isEOF())
			return null;
		
		while(true) {
			currentChar = nextChar();
			countLineColumn(currentChar);
			
			switch(state) {
				case 0:
					if(isSpace(currentChar) && !isEOF()) {
						continue;
					}
					else if(isEOF())
						return null;
						
					
					else if (isSpecialCaractere(currentChar)) {
						content+=currentChar;
						state=1;
					}
					
					else if(isLetter(currentChar)) {
						content += currentChar;
						state = 1;
					}
					
					else if(isNumber(currentChar)) {
						content += currentChar;
						state= 2;
					}
					
					else if(isDot(currentChar)) {
						pCounter++;
						content += '0';
						content += currentChar;
						state = 2;
					}
					
					else if(isMathOperator(currentChar)) {
						content += currentChar;
						state = 3;
					}
					
					else if(isOperator(currentChar)) {
						content += currentChar;
						state = 4;
					}
					
					else if(isAssign(currentChar)) {
						content += currentChar;
						state = 5;
					}
					
					else if(isLeftPar(currentChar)) {
						content += currentChar;
						state = 6;
					}
					
					else if(isRightPar(currentChar)) {
						content += currentChar;
						state = 7;
					}
					
					else if(isComment(currentChar)) {
						do {
							currentChar = nextChar();
							countLineColumn(currentChar);
						}while(currentChar != '\n');
					}
						
					else
						throw new RuntimeException(error() + "Unregnized Symbol: " + currentChar);
					
					break;
			
			 /*Generate TokenType.IDENTIFIER*/
				case 1:
					
					//if(isSpace(currentChar))
						//continue;
					
					if(isLetter(currentChar) || isNumber(currentChar) || isSpecialCaractere(currentChar)) {
						content+=currentChar;
					}				
					
					else if(isInvalid(currentChar))
						throw new IllegalArgumentException(error() + "Lexical Error: Unrecognized Symbol: " + currentChar);
					
					else {
						if(!isEOF()) {
							back(currentChar);
						}
						
						for(String s : reservedWords) {
							if(s.equals(content)) {
								tk = new Token(TokenType.RESERVED_WORD, content);
								return tk;
							}
						}
						tk = new Token(TokenType.IDENTIFIER, content);
						return tk;
					}
					
					break;
				
					
					/*Generate TokenType.NUMBER */
				case 2:
					if(isNumber(currentChar)) {
						content += currentChar;
					}
					
					else if(isDot(currentChar)) {
						pCounter++;
						content+=currentChar;
					}
					
					else if(isAssign(currentChar)) {
						tk = new Token(TokenType.ASSIGN, Character.toString(currentChar));
						return tk;
					}
					
					else if(isOperator(currentChar) || isSpace(currentChar)){	
						
						if(!isEOF()) {
							back(currentChar);
						}
						
						if(content.contains(".")) {
							if(pCounter > 1 || content.endsWith("."))
								throw new RuntimeException(error() + "Malformed Number: " + content + currentChar);
							tk = new Token(TokenType.NUMBER_DECIMAL, content);
						}
						else {
							tk = new Token(TokenType.NUMBER_INTEGER, content);
						}
						
						pCounter = 0;
						return tk;
					}
					
					else {
						throw new RuntimeException(error() + "Malformed Number: " + content + currentChar);
					}
					
					break;			
			
					
					//Generate Math Operator
				case 3:
					
					if(isInvalid(currentChar)) {
						throw new IllegalArgumentException(error() + "BAD EXPRESSION: " + content);
					}
					
					else if(!isEOF())
						back(currentChar);
					
					tk = new Token(TokenType.MATH_OPERATOR, content);
					return tk;
					
					//throw new IllegalArgumentException("Lexical Error: Unrecognized Symbol: " + currentChar);			
				
				case 4:
					
					if(isInvalid(currentChar))
						throw new IllegalArgumentException(error() + "Invalid Operator");
					
					else if(!isEOF()) {
						if(currentChar == '=') {
							content += currentChar;
							break;
						}
						back(currentChar);
					}
					
					tk = new Token(TokenType.RELATIONAL_OPERATOR, content);
					return tk;
				
				
				case 5:
					if(isInvalid(currentChar))
						throw new IllegalArgumentException(error() + "Unknower Expression");
					
					else if(!isEOF()) {
						back(currentChar);
					}
					
					tk = new Token(TokenType.ASSIGN, content);
					return tk;
					
				//Left Parenthesis 	
				case 6:
					if(!isEOF()) {
					back(currentChar);
				}
					tk = new Token(TokenType.LEFT_PAR, content);
					return tk;
				
				//Right Parenthesis 
				case 7:
					if(!isEOF()) {
						back(currentChar);
					}
					tk = new Token(TokenType.RIGHT_PAR, content);
					return tk;
			}
		}
	}
	
	private boolean isDot(char c) {
		return c == '.';
	}
	
	private boolean isLeftPar(char c) {
		return c == '(';
	}
	
	private boolean isRightPar(char c) {
		return c == ')';
	}
	
	private boolean isComment(char c) {
		return c == '#';
	}

	public boolean isLetter(char c) {
		return c>='a' && c<='z' || c>='A' && c<='Z';
	}
	
	public boolean isSpecialCaractere(char c) {
		return c == '_';
	}
	
	public boolean isNumber(char c) {
		return c>='0' && c<='9';
	}
	
	public boolean isOperator(char c) {
		return c =='>' || c=='<' || c == '!';
	}
	
	public boolean isSpace(char c) {
		return c == ' ' || c == '\n' || c == '\t' || c == '\r';
	}
	
	public boolean isAssign(char c) {
		return c == '=';
	}
	
	public boolean isInvalid(char c) {
		return !isLetter(c) && !isNumber(c) && !isOperator(c) && !isSpace(c) && !isAssign(c) && !isEOF() && !isLeftPar(c) && !isRightPar(c) && !isDot(c);
	}
	
	public boolean isMathOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/';
	}
	
	private char nextChar() {
		if(isEOF())
			return '\0';
		return this.contentBuffer[pos++];
	}
	
	private String error() {
		return "Error on line " + nLine + ", column " + nColumn + ". ";
	}

	private boolean isEOF() {
		if(this.pos >= this.contentBuffer.length)
			return true;
		return false;
	}
	
	private void back(char currentChar) {
		if(currentChar != '\n') {
			nColumn--;
		}
		else{
			nLine--;
		}
		this.pos--;
	}	
	
	private void countLineColumn(char currentChar){
		if(currentChar == '\n') {
			nLine++;
			nColumn=0;
		}
		else {
			nColumn++;
		}
	}
}
