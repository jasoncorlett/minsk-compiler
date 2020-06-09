package minsk.codeanalysis.symbols;

public class VariableSymbol extends AbstractSymbol {
	
	private final boolean isReadOnly;
	private final TypeSymbol type;

	public VariableSymbol(String name, boolean isReadOnly, TypeSymbol type) {
		super(name);
		this.isReadOnly = isReadOnly;
		this.type = type;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public TypeSymbol getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s", getName(), type);
	}

	@Override
	public SymbolKind getKind() {
		return SymbolKind.Variable;
	}
}
