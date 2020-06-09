package minsk.codeanalysis.binding;

import minsk.codeanalysis.symbols.TypeSymbol;

public abstract class BoundExpression extends BoundNode {
	public abstract TypeSymbol getType();
	
	@Override
	public String toString() {
		return super.toString() + " " + getType();
	}
}