package minsk.codeanalysis.symbols;

public class VariableSymbol extends AbstractSymbol {
	
	private final boolean isReadOnly;
	private final Class<?> type;

	public VariableSymbol(String name, boolean isReadOnly, Class<?> type) {
		super(name);
		this.isReadOnly = isReadOnly;
		this.type = type;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public Class<?> getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s", getName(), type.getSimpleName());
	}

	@Override
	public SymbolKind getKind() {
		return SymbolKind.Variable;
	}
}
