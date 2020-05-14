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
		var expression = parseTerm();
		var endOfFileToken = match(SyntaxKind.EndOfFileToken);
		
		return new SyntaxTree(diagnostics, expression, endOfFileToken);
	}
	
	public ExpressionSyntax parseTerm() {
		var left = parseFactor();
		
		while (current().kind == SyntaxKind.PlusToken ||
				current().kind == SyntaxKind.MinusToken) {
			
			var operatorToken = nextToken();
			var right = parseFactor();
			
			left = new BinaryExpressionSyntax(left, operatorToken, right);
		}
		
		return left;
	}
	
	public ExpressionSyntax parseFactor() {
		var left = parsePrimaryExpression();
		
		while (current().kind == SyntaxKind.StarToken ||
				current().kind == SyntaxKind.SlashToken) {
			
			var operatorToken = nextToken();
			var right = parsePrimaryExpression();
			
			left = new BinaryExpressionSyntax(left, operatorToken, right);
		}
		
		return left;
	}
	
	private ExpressionSyntax parsePrimaryExpression() {
		if (current().kind == SyntaxKind.OpenParenthesisToken) {
			var left = nextToken();
			var expression = parseTerm();
			var right = match(SyntaxKind.CloseParenthesisToken);
			return new ParenthesizedExpressionSyntax(left, expression, right);
		}
		
		var numberToken = match(SyntaxKind.NumberToken);
		return new LiteralExpressionSyntax(numberToken);
	}
}