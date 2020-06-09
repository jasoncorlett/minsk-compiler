package minsk.codeanalysis.binding;

import minsk.codeanalysis.TreeNode;
import minsk.codeanalysis.symbols.TypeSymbol;
import minsk.codeanalysis.syntax.SyntaxKind;

public final class BoundBinaryOperator extends TreeNode {
	
	private static final BoundBinaryOperator[] operators = new BoundBinaryOperator[] {
			new BoundBinaryOperator(SyntaxKind.PipeToken, BoundBinaryOperatorKind.BitwiseOr, TypeSymbol.Int),
			new BoundBinaryOperator(SyntaxKind.AmpersandToken, BoundBinaryOperatorKind.BitwiseAnd, TypeSymbol.Int),
			new BoundBinaryOperator(SyntaxKind.CaretToken, BoundBinaryOperatorKind.BitwiseXor, TypeSymbol.Int),
			new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, TypeSymbol.Int),
			new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, TypeSymbol.Int),
			new BoundBinaryOperator(SyntaxKind.StarToken, BoundBinaryOperatorKind.Multiplication, TypeSymbol.Int),
			new BoundBinaryOperator(SyntaxKind.SlashToken, BoundBinaryOperatorKind.Division, TypeSymbol.Int),
			new BoundBinaryOperator(SyntaxKind.PercentToken, BoundBinaryOperatorKind.Modulo, TypeSymbol.Int),
			
			new BoundBinaryOperator(SyntaxKind.AmpersandToken, BoundBinaryOperatorKind.BitwiseAnd, TypeSymbol.Bool),
			new BoundBinaryOperator(SyntaxKind.PipeToken, BoundBinaryOperatorKind.BitwiseOr, TypeSymbol.Bool),
			new BoundBinaryOperator(SyntaxKind.CaretToken, BoundBinaryOperatorKind.BitwiseXor, TypeSymbol.Bool),
			new BoundBinaryOperator(SyntaxKind.AmpersandAmpersandToken, BoundBinaryOperatorKind.LogicalAnd, TypeSymbol.Bool),
			new BoundBinaryOperator(SyntaxKind.PipePipeToken, BoundBinaryOperatorKind.LogicalOr, TypeSymbol.Bool),
			
			new BoundBinaryOperator(SyntaxKind.EqualsEqualsToken, BoundBinaryOperatorKind.Equals, TypeSymbol.Bool),
			new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, TypeSymbol.Bool),
			
			new BoundBinaryOperator(SyntaxKind.EqualsEqualsToken, BoundBinaryOperatorKind.Equals, TypeSymbol.Int, TypeSymbol.Bool),
			new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, TypeSymbol.Int, TypeSymbol.Bool),
			new BoundBinaryOperator(SyntaxKind.GreaterToken, BoundBinaryOperatorKind.Greater, TypeSymbol.Int, TypeSymbol.Bool),
			new BoundBinaryOperator(SyntaxKind.GreaterEqualsToken, BoundBinaryOperatorKind.GreaterEquals, TypeSymbol.Int, TypeSymbol.Bool),
			new BoundBinaryOperator(SyntaxKind.LessToken, BoundBinaryOperatorKind.Less, TypeSymbol.Int, TypeSymbol.Bool),
			new BoundBinaryOperator(SyntaxKind.LessEqualsToken, BoundBinaryOperatorKind.LessEquals, TypeSymbol.Int, TypeSymbol.Bool),
			
			new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, TypeSymbol.String),
			new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, TypeSymbol.String, TypeSymbol.Int, TypeSymbol.String),
			new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, TypeSymbol.String, TypeSymbol.Bool, TypeSymbol.String),
			
			new BoundBinaryOperator(SyntaxKind.EqualsEqualsToken, BoundBinaryOperatorKind.Equals, TypeSymbol.String, TypeSymbol.Bool),
			new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, TypeSymbol.String, TypeSymbol.Bool),
			
	};
	
	public static BoundBinaryOperator bind(SyntaxKind syntaxKind, TypeSymbol leftType, TypeSymbol rightType) {
		for (var op : operators) {
			if (op.syntaxKind == syntaxKind && op.leftType == leftType && op.rightType == rightType) {
				return op;
			}
		}
		return null;
	}
	
	private final SyntaxKind syntaxKind;
	private final BoundBinaryOperatorKind kind;
	private final TypeSymbol leftType;
	private final TypeSymbol rightType;
	private final TypeSymbol resultType;
	
	private BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind kind, TypeSymbol type) {
		this(syntaxKind, kind, type, type, type);
	}
	
	private BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind kind, TypeSymbol operandType, TypeSymbol resultType) {
		this(syntaxKind, kind, operandType, operandType, resultType);
	}
	
	private BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind kind, TypeSymbol leftType, TypeSymbol rightType, TypeSymbol resultType) {
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

	public TypeSymbol getLeftType() {
		return leftType;
	}

	public TypeSymbol getRightType() {
		return rightType;
	}

	public TypeSymbol getResultType() {
		return resultType;
	}
	
	@Override
	public String toString() {
		return "BoundBinaryOperator [ " + syntaxKind.getFixedText() + " ]";
	}
	
}