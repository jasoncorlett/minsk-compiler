package minsk.codeanalysis.syntax;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("java:S115") // Sonar rule about case of enum constants
public enum SyntaxKind {
	// Tokens
	BadToken, 
	EndOfFileToken,
	LiteralToken, 
	WhitespaceToken,
	@Fixed("~") @Unary(6) TildeToken,
	@Fixed("!") @Unary(6) BangToken,
	@Fixed("/") @Binary(5) SlashToken,
	@Fixed("*") @Binary(5) StarToken,
	@Fixed("%") @Binary(5) PercentToken,
	@Fixed("+") @Binary(4) @Unary(6) PlusToken,
	@Fixed("-") @Binary(4) @Unary(6) MinusToken,
	@Fixed("(") OpenParenthesisToken,
	@Fixed(")") CloseParenthesisToken,
	IdentifierToken,
	@Fixed("==") @Binary(3) EqualsEqualsToken,
	@Fixed("!=") @Binary(3) BangEqualsToken,
	@Fixed("<")  @Binary(3) LessToken,
	@Fixed("<=") @Binary(3) LessEqualsToken,
	@Fixed(">")  @Binary(3) GreaterToken,
	@Fixed(">=") @Binary(3) GreaterEqualsToken,
	@Fixed("&")  @Binary(2) AmpersandToken,
	@Fixed("&&") @Binary(2) AmpersandAmpersandToken,
	@Fixed("|")  @Binary(1) PipeToken,
	@Fixed("||") @Binary(1) PipePipeToken,
	@Fixed("^")  @Binary(1) CaretToken,
	@Fixed("=") EqualsToken,
	@Fixed("{") OpenBraceToken,
	@Fixed("}") CloseBraceToken,
	StringToken,
	@Fixed(",") CommaToken,

	// Keywords
	@Fixed("true") TrueKeyword,
	@Fixed("false") FalseKeyword,
	@Fixed("var") VarKeyword,
	@Fixed("let") LetKeyword,
	@Fixed("if") IfKeyword,
	@Fixed("else") ElseKeyword,
	@Fixed("for") ForKeyword,
	@Fixed("to") ToKeyword,
	@Fixed("while") WhileKeyword,

	// Statements
	BlockStatement,
	ExpressionStatement,
	AssignmentStatement,
	VariableDeclaration,
	IfStatement,
	WhileStatement,
	ForStatement,
	
	// Expressions
	UnaryExpression,
	BinaryExpression,
	LiteralExpression, 
	ParenthesizedExpression,
	NameExpression, 
	AssignmentExpression,
	CallExpression,

	// Special nodes
	CompilationUnit,
	ElseClause;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	private @interface Fixed {
		String value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	private @interface Binary {
		int value() default 0;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	private @interface Unary {
		int value() default 0;
	}
	
	private final String fixedText;
	private final boolean isKeyword;
	private final int binaryPrecedence;
	private final int unaryPrecedence;
	
	private SyntaxKind() {
		isKeyword = this.toString().endsWith("Keyword");
		
		try {
			var fixedAnnotation = this.getClass().getField(this.name()).getAnnotation(Fixed.class); 
			fixedText = fixedAnnotation == null ? null : fixedAnnotation.value();
		} 
		catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
		
		try {
			var unaryAnnotation = this.getClass().getField(this.name()).getAnnotation(Unary.class);
			unaryPrecedence = unaryAnnotation == null ? 0 : unaryAnnotation.value();
		}
		catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
		
		try {
			var binaryAnnotation = this.getClass().getField(this.name()).getAnnotation(Binary.class);
			binaryPrecedence = binaryAnnotation == null ? 0 : binaryAnnotation.value();
		} 
		catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean isKeyword() {
		return isKeyword;
	}
	
	public String getFixedText() {
		return fixedText;
	}
	
	public int getUnaryPrecedence() {
		return unaryPrecedence;
	}
	
	public int getBinaryPrecedence() {
		return binaryPrecedence;
	}
}
