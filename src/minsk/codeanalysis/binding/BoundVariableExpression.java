package minsk.codeanalysis.binding;

public class BoundVariableExpression extends BoundExpression {
	private final VariableSymbol variable;
	
	public BoundVariableExpression(VariableSymbol variable) {
		this.variable = variable;
	}

	public VariableSymbol getVariable() {
		return variable;
	}
	
	@Override
	Class<? extends Object> getType() {
		return variable.getType();
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.VariableExpression;
	}
}
