package minsk.codeanalysis.binding;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.symbols.TypeSymbol;

public final class BoundBinaryExpression extends BoundExpression {

	private final BoundExpression left;
	private final BoundBinaryOperator operator;
	private final BoundExpression right;

	public BoundBinaryExpression(BoundExpression left, BoundBinaryOperator operator, BoundExpression right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	@Override
	public TypeSymbol getType() {
		return getOperator().getResultType();
	}

	@Nested(1)
	public BoundExpression getLeft() {
		return left;
	}
	
	@Nested(2)
	public BoundBinaryOperator getOperator() {
		return operator;
	}

	@Nested(3)
	public BoundExpression getRight() {
		return right;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.BinaryExpression;
	}

}