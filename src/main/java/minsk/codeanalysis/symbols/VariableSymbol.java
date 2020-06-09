package minsk.codeanalysis.symbols;

public class VariableSymbol {
	
	private final String name;
	private final boolean isReadOnly;
	private final Class<?> type;

	public VariableSymbol(String name, boolean isReadOnly, Class<?> type) {
		this.name = name;
		this.isReadOnly = isReadOnly;
		this.type = type;
	}

	public String getName() {
		return name;
	}
	
	public boolean isReadOnly() {
		return isReadOnly;
	}

	public Class<?> getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s", name, type.getSimpleName());
	}
}
