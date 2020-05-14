package minsk.codeanalysis.binding;

public final class BoundUnaryExpression extends BoundExpression {
	private final BoundUnaryOperatorKind operatorKind;
	private final BoundExpression operand;

	public BoundUnaryExpression(BoundUnaryOperatorKind operatorKind, BoundExpression operand) {
		this.operatorKind = operatorKind;
		this.operand = operand;
	}

	public BoundUnaryOperatorKind getOperatorKind() {
		return operatorKind;
	}

	public BoundExpression getOperand() {
		return operand;
	}

	@Override
	Class<? extends Object> getType() {
		return operand.getType();
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.UnaryExpression;
	}
}