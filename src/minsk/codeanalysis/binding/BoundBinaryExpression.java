package minsk.codeanalysis.binding;

public final class BoundBinaryExpression extends BoundExpression {

	private final BoundExpression left;
	private final BoundBinaryOperator operaetor;
	private final BoundExpression right;

	public BoundBinaryExpression(BoundExpression left, BoundBinaryOperator operaetor, BoundExpression right) {
		this.left = left;
		this.operaetor = operaetor;
		this.right = right;
	}

	@Override
	Class<? extends Object> getType() {
		return getLeft().getType();
	}

	public BoundExpression getLeft() {
		return left;
	}

	public BoundExpression getRight() {
		return right;
	}
	


	public BoundBinaryOperator getOperaetor() {
		return operaetor;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.BinaryExpression;
	}

}