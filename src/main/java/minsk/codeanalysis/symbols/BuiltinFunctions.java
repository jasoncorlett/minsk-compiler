package minsk.codeanalysis.symbols;

import static minsk.codeanalysis.symbols.ParameterSymbol.param;

import java.util.List;

public class BuiltinFunctions {

	public static final FunctionSymbol print = new FunctionSymbol("print", List.of(param("text", TypeSymbol.String)), TypeSymbol.Void);
	public static final FunctionSymbol input = new FunctionSymbol("input", List.of(), TypeSymbol.String);
	
	public static List<FunctionSymbol> getAll() {
		return List.of(print, input);
	}
	
	private BuiltinFunctions() {
	}

}
