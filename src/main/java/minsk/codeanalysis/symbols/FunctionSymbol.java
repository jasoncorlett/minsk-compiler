package minsk.codeanalysis.symbols;

import java.util.List;

public class FunctionSymbol extends AbstractSymbol {

	private final List<ParameterSymbol> arguments;
	private final TypeSymbol type;

	public FunctionSymbol(String name, List<ParameterSymbol> arguments, TypeSymbol type) {
		super(name);
		this.arguments = arguments;
		this.type = type;
	}

	@Override
	public SymbolKind getKind() {
		return SymbolKind.Function;
	}

	public List<ParameterSymbol> getArguments() {
		return arguments;
	}

	public TypeSymbol getType() {
		return type;
	}
}
