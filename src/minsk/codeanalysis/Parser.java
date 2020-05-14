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
			
			if (token.getKind() == SyntaxKind.WhitespaceToken || token.getKind() == SyntaxKind.BadToken) {
				continue;
			}
			
			tokens.add(token);
		} while (token.getKind() != SyntaxKind.EndOfFileToken);
		
		diagnostics.addAll(lexer.getDiagnostics());
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
		if (current().getKind() == kind) {
			return nextToken();
		}
		
		diagnostics.add("ERROR: Unexpected token: " + current().getKind() + " expected " + kind);
		return new SyntaxToken(kind, current().getPosition(), null, null);
	}
	
	public SyntaxTree parse() {
		var expression = parseExpression();
		var endOfFileToken = match(SyntaxKind.EndOfFileToken);
		
		return new SyntaxTree(diagnostics, expression, endOfFileToken);
	}

	
	private ExpressionSyntax parseExpression() {
		return parseExpression(0);
	}
	
	private ExpressionSyntax parseExpression(int parentPrecedence) {
		ExpressionSyntax left;
		
		var unaryOperatorPrecedence = SyntaxFacts.getUnaryOperatorPrecedence(current().getKind());
		
		if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence) {
			var operatorToken = nextToken();
			var operand = parseExpression(unaryOperatorPrecedence);
			left = new UnaryExpressionSyntax(operatorToken, operand);
		} else {
			left = parsePrimaryExpression();
		}
		
		while (true) {
			var precedence = SyntaxFacts.getBinaryOperatorPrecedence(current().getKind());
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
		if (current().getKind() == SyntaxKind.OpenParenthesisToken) {
			var left = nextToken();
			var expression = parseExpression();
			var right = match(SyntaxKind.CloseParenthesisToken);
			return new ParenthesizedExpressionSyntax(left, expression, right);
		}
		
		var numberToken = match(SyntaxKind.NumberToken);
		return new LiteralExpressionSyntax(numberToken);
	}
}