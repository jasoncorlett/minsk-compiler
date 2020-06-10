package minsk.codeanalysis.symbols;

import java.util.List;

public class FunctionSymbol extends AbstractSymbol {

	private final List<ParameterSymbol> parameters;
	
	public FunctionSymbol(String name, List<ParameterSymbol> parameters, TypeSymbol type) {
		super(name);
		this.parameters = parameters;
	}

	@Override
	public SymbolKind getKind() {
		return SymbolKind.Function;
	}

	public List<ParameterSymbol> getParameters() {
		return parameters;
	}

}
