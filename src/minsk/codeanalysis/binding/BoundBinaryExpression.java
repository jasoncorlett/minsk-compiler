package minsk.codeanalysis.binding;

public final class BoundBinaryExpression extends BoundExpression {

	private final BoundExpression left;
	private final BoundBinaryOperatorKind operaetorKind;
	private final BoundExpression right;

	public BoundBinaryExpression(BoundExpression left, BoundBinaryOperatorKind operaetorKind,
			BoundExpression right) {
		this.left = left;
		this.operaetorKind = operaetorKind;
		this.right = right;
	}

	@Override
	Class<? extends Object> getType() {
		return getLeft().getType();
	}

	public BoundExpression getLeft() {
		return left;
	}

	public BoundBinaryOperatorKind getOperaetorKind() {
		return operaetorKind;
	}

	public BoundExpression getRight() {
		return right;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.BinaryExpression;
	}

}