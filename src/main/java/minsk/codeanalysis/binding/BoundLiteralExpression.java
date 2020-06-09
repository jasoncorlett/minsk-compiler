package minsk.codeanalysis.binding;

import minsk.codeanalysis.symbols.TypeSymbol;

public final class BoundLiteralExpression extends BoundExpression {
	private final Object value;

	public BoundLiteralExpression(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public final TypeSymbol getType() {
		if (getValue() instanceof Boolean) {
			return TypeSymbol.Bool;
		}
		else if (getValue() instanceof String) {
			return TypeSymbol.String;
		}
		else if (getValue() instanceof Integer) {
			return TypeSymbol.Int;
		}
		
		return null;
	}

	@Override
	public final BoundNodeKind getKind() {
		return BoundNodeKind.LiteralExpression;
	}
	
	@Override
	public String toString() {
		return "BoundLiteralExpression [ Value: " + value + ", Type: " + getType() + "]";
	}
}