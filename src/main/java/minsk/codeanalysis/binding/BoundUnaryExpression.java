package minsk.codeanalysis.binding;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.symbols.TypeSymbol;

public final class BoundUnaryExpression extends BoundExpression {
	private final BoundUnaryOperator operator;
	private final BoundExpression operand;

	public BoundUnaryExpression(BoundUnaryOperator operator, BoundExpression operand) {
		this.operator = operator;
		this.operand = operand;
	}

	@Nested
	public BoundUnaryOperator getOperator() {
		return operator;
	}

	@Nested
	public BoundExpression getOperand() {
		return operand;
	}

	@Override
	public TypeSymbol getType() {
		return operator.getResultType();
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.UnaryExpression;
	}
}