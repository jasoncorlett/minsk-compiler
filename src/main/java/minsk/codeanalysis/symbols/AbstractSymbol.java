package minsk.codeanalysis.symbols;

public abstract class AbstractSymbol {
	private final String name;

	protected AbstractSymbol(String name) {
		this.name = name;
	}
	
	public abstract SymbolKind getKind();

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
