package minsk.codeanalysis.binding;

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
	public Class<?> getType() {
		return getOperator().getResultType();
	}

	public BoundExpression getLeft() {
		return left;
	}

	public BoundExpression getRight() {
		return right;
	}

	public BoundBinaryOperator getOperator() {
		return operator;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.BinaryExpression;
	}

}