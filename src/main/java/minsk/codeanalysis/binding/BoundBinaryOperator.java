package minsk.codeanalysis.binding;

import minsk.codeanalysis.syntax.SyntaxKind;

public final class BoundBinaryOperator {
	
	private static final BoundBinaryOperator[] operators = new BoundBinaryOperator[] {
			new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, Integer.class),
			new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Integer.class),
			new BoundBinaryOperator(SyntaxKind.StarToken, BoundBinaryOperatorKind.Multiplication, Integer.class),
			new BoundBinaryOperator(SyntaxKind.SlashToken, BoundBinaryOperatorKind.Division, Integer.class),
			new BoundBinaryOperator(SyntaxKind.PercentToken, BoundBinaryOperatorKind.Modulo, Integer.class),
			
			new BoundBinaryOperator(SyntaxKind.AmpersandAmpersandToken, BoundBinaryOperatorKind.LogicalAnd, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.PipePipeToken, BoundBinaryOperatorKind.LogicalOr, Boolean.class),
			
			new BoundBinaryOperator(SyntaxKind.EqualsEqualsToken, BoundBinaryOperatorKind.Equals, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, Boolean.class),
			
			new BoundBinaryOperator(SyntaxKind.EqualsEqualsToken, BoundBinaryOperatorKind.Equals, Integer.class, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, Integer.class, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.GreaterToken, BoundBinaryOperatorKind.Greater, Integer.class, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.GreaterEqualsToken, BoundBinaryOperatorKind.GreaterEquals, Integer.class, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.LessToken, BoundBinaryOperatorKind.Less, Integer.class, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.LessEqualsToken, BoundBinaryOperatorKind.LessEquals, Integer.class, Boolean.class),
	};
	
	public static BoundBinaryOperator bind(SyntaxKind syntaxKind, Class<?> leftType, Class<?> rightType) {
		for (var op : operators) {
			if (op.syntaxKind == syntaxKind && op.leftType == leftType && op.rightType == rightType) {
				return op;
			}
		}
		return null;
	}
	
	private final SyntaxKind syntaxKind;
	private final BoundBinaryOperatorKind kind;
	private final Class<?> leftType;
	private final Class<?> rightType;
	private final Class<?> resultType;
	
	private BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind kind, Class<?> type) {
		this(syntaxKind, kind, type, type, type);
	}
	
	private BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind kind, Class<?> operandType, Class<?> resultType) {
		this(syntaxKind, kind, operandType, operandType, resultType);
	}
	
	private BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind kind, Class<?> leftType, Class<?> rightType, Class<?> resultType) {
		this.syntaxKind = syntaxKind;
		this.kind = kind;
		this.leftType = leftType;
		this.rightType = rightType;
		this.resultType = resultType;
	}
	
	public SyntaxKind getSyntaxKind() {
		return syntaxKind;
	}

	public BoundBinaryOperatorKind getKind() {
		return kind;
	}

	public Class<?> getLeftType() {
		return leftType;
	}

	public Class<?> getRightType() {
		return rightType;
	}

	public Class<?> getResultType() {
		return resultType;
	}
	
}