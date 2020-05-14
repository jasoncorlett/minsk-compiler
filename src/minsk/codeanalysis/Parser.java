package minsk.codeanalysis;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	public List<String> diagnostics = new ArrayList<String>();
	public final List<SyntaxToken> tokens;
	private int position = 0;
	
	public Parser(String text) {
		tokens = new ArrayList<SyntaxToken>();
		var lexer = new Lexer(text);

		SyntaxToken token;
		do  {
			token = lexer.nextToken();
			
			if (token.kind == SyntaxKind.WhitespaceToken || token.kind == SyntaxKind.BadToken) {
				continue;
			}
			
			tokens.add(token);
		} while (token.kind != SyntaxKind.EndOfFileToken);
		
		diagnostics.addAll(lexer.diagnostics);
	}
	
	private SyntaxToken nextToken() {
		var current = current();
		position++;
		return current;
	}
	
	private SyntaxToken peek(int offset) {
		var index = position + offset;
		if (index >= tokens.size()) {
			return tokens.get(tokens.size() - 1);
		}
		else {
			return tokens.get(index);
		}
	}
	
	private SyntaxToken current() {
		return peek(0);
	}
	
	private SyntaxToken match(SyntaxKind kind) {
		if (current().kind == kind) {
			return nextToken();
		}
		
		diagnostics.add("ERROR: Unexpected token: " + current().kind + " expected " + kind);
		return new SyntaxToken(kind, current().position, null, null);
	}
	
	public SyntaxTree parse() {
		var expression = parseExpression();
		var endOfFileToken = match(SyntaxKind.EndOfFileToken);
		
		return new SyntaxTree(diagnostics, expression, endOfFileToken);
	}
	
	private static int getBinaryOperatorPrecedence(SyntaxKind kind) {
		switch (kind) {
		case StarToken:
		case SlashToken:
			return 2;
			
		case PlusToken:
		case MinusToken:
			return 1;
			
		default:
			return 0; // Not a binary operator
		}
	}
	
	private ExpressionSyntax parseExpression() {
		return parseExpression(0);
	}
	
	private ExpressionSyntax parseExpression(int parentPrecedence) {
		var left = parsePrimaryExpression();
		
		while (true) {
			var precedence = getBinaryOperatorPrecedence(current().kind);
			if (precedence == 0 || precedence <= parentPrecedence) {
				break;
			}
			
			var operatorToken = nextToken();
			var right = parseExpression(precedence);
			
			left = new BinaryExpressionSyntax(left, operatorToken, right);
		}
		
		return left;
	}
	
	private ExpressionSyntax parsePrimaryExpression() {
		if (current().kind == SyntaxKind.OpenParenthesisToken) {
			var left = nextToken();
			var expression = parseExpression();
			var right = match(SyntaxKind.CloseParenthesisToken);
			return new ParenthesizedExpressionSyntax(left, expression, right);
		}
		
		var numberToken = match(SyntaxKind.NumberToken);
		return new LiteralExpressionSyntax(numberToken);
	}
}