package minsk.codeanalysis.binding;

import minsk.codeanalysis.symbols.TypeSymbol;

public abstract class BoundExpression extends BoundNode {
	public abstract TypeSymbol getType();
	
	public boolean instanceOf(TypeSymbol type) {
		return getType().equals(type);
	}

	@Override
	public String toString() {
		return super.toString() + " " + getType();
	}
}