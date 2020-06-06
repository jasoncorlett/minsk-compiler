package minsk.codeanalysis.binding;

import minsk.codeanalysis.Nested;

public class BoundVariableDeclaration extends BoundStatement {
	private final VariableSymbol variable;
	private final BoundExpression initializer;

	public BoundVariableDeclaration(VariableSymbol variable, BoundExpression initializer) {
		this.variable = variable;
		this.initializer = initializer;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.VariableDeclaration;
	}

	@Nested
	public VariableSymbol getVariable() {
		return variable;
	}

	@Nested
	public BoundExpression getInitializer() {
		return initializer;
	}
}
