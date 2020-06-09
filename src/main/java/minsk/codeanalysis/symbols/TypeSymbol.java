package minsk.codeanalysis.symbols;

public class TypeSymbol extends AbstractSymbol {

	public static final TypeSymbol Bool = new TypeSymbol("bool");
	public static final TypeSymbol Int = new TypeSymbol("int");
	public static final TypeSymbol String = new TypeSymbol("string");
	
	private TypeSymbol(String name) {
		super(name);
	}

	@Override
	public SymbolKind getKind() {
		return SymbolKind.Type;
	}
}
