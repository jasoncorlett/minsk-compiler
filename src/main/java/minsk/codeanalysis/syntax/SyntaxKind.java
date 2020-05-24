package minsk.codeanalysis.syntax;

public enum SyntaxKind {
	// Tokens
	BadToken, 
	EndOfFileToken,
	LiteralToken, 
	WhitespaceToken,
	PlusToken("+"),
	SlashToken("/"),
	StarToken("*"),
	MinusToken("-"),
	OpenParenthesisToken("("),
	CloseParenthesisToken(")"),
	IdentifierToken,
	BangToken("!"),
	AmpersandAmpersandToken("&&"),
	PipePipeToken("||"),
	EqualsEqualsToken("=="),
	BangEqualsToken("!="),
	EqualsToken("="),
	OpenBraceToken("{"),
	CloseBraceToken("}"),
	
	// Keywords
	TrueKeyword("true"), 
	FalseKeyword("false"),
	
	// Statements
	BlockStatement,
	ExpressionStatement,
	AssignmentStatement,
	
	// Expressions
	UnaryExpression,
	BinaryExpression,
	LiteralExpression, 
	ParenthesizedExpression,
	NameExpression, 
	AssignmentExpression,

	// Special nodes
	CompilationUnit;
	
	private final String fixedText;
	
	SyntaxKind() {
		this.fixedText = null;
	}
	
	SyntaxKind(String fixedText) {
		this.fixedText = fixedText;
	}

	public String getFixedText() {
		return fixedText;
	}
}
