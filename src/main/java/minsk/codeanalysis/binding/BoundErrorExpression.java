package minsk.codeanalysis.binding;

import minsk.codeanalysis.symbols.TypeSymbol;

public class BoundErrorExpression extends BoundExpression {

	public BoundErrorExpression() {
	}

	@Override
	public TypeSymbol getType() {
		return TypeSymbol.Error;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.ErrorExpression;
	}
}
