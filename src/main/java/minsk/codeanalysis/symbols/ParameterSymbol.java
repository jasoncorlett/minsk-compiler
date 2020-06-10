package minsk.codeanalysis.symbols;

public class ParameterSymbol extends VariableSymbol {

	public ParameterSymbol(String name, TypeSymbol type) {
		super(name, true, type);
	}
	
	@Override
	public SymbolKind getKind() {
		return SymbolKind.Parameter;
	}
	
	public static ParameterSymbol param(String name, TypeSymbol type) {
		return new ParameterSymbol(name, type);
	}

}
