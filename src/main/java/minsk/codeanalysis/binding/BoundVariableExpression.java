package minsk.codeanalysis.binding;

import minsk.codeanalysis.symbols.TypeSymbol;
import minsk.codeanalysis.symbols.VariableSymbol;

public class BoundVariableExpression extends BoundExpression {
	private final VariableSymbol variable;
	
	public BoundVariableExpression(VariableSymbol variable) {
		this.variable = variable;
	}

	public VariableSymbol getVariable() {
		return variable;
	}
	
	@Override
	public TypeSymbol getType() {
		return variable.getType();
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.VariableExpression;
	}
	
	@Override
	public String toString() {
		return "VariableExpression [ Name = " + getVariable().getName() + ", Type: " + getType() + " ]";
	}
}
