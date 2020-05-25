package minsk.codeanalysis.syntax;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("java:S115")
public enum SyntaxKind {
	// Tokens
	BadToken, 
	EndOfFileToken,
	LiteralToken, 
	WhitespaceToken,
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
	@Fixed("&&") @Binary(2) AmpersandAmpersandToken,
	@Fixed("||") @Binary(1) PipePipeToken,
	@Fixed("=") EqualsToken,
	@Fixed("{") OpenBraceToken,
	@Fixed("}") CloseBraceToken,
	
	// Keywords
	@Fixed("true") TrueKeyword,
	@Fixed("false") FalseKeyword,
	@Fixed("var") VarKeyword,
	@Fixed("let") LetKeyword,
	
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
	
	public String getFixedText() {
		try {
			var fixed = this.getClass().getField(this.name()).getAnnotation(Fixed.class); 
			return fixed == null ? null : fixed.value();
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getUnaryPrecedence() {
		try {
			var unary = this.getClass().getField(this.name()).getAnnotation(Unary.class);
			return unary == null ? 0 : unary.value();
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getBinaryPrecedence() {
		try {
			var binary = this.getClass().getField(this.name()).getAnnotation(Binary.class);
			return binary == null ? 0 : binary.value();
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
