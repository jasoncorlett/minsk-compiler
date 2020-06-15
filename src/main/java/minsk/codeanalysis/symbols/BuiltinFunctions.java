package minsk.codeanalysis.symbols;

import static minsk.codeanalysis.symbols.ParameterSymbol.param;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuiltinFunctions {

	public static final FunctionSymbol print = new FunctionSymbol("print", List.of(param("text", TypeSymbol.String)), TypeSymbol.Void);
	public static final FunctionSymbol input = new FunctionSymbol("input", List.of(), TypeSymbol.String);
	public static final FunctionSymbol rnd = new FunctionSymbol("rnd", List.of(param("max", TypeSymbol.Int)), TypeSymbol.Int);
	
	public static List<FunctionSymbol> getAll() {
		return Arrays.stream(BuiltinFunctions.class.getDeclaredFields())
			.filter(f -> Modifier.isStatic(f.getModifiers()))
			.filter(f -> f.getType().isAssignableFrom(FunctionSymbol.class))
			.map(BuiltinFunctions::getFieldSymbol)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}
	
	private static FunctionSymbol getFieldSymbol(Field f) {
		try {
			return (FunctionSymbol) f.get(null);
		}
		catch (Exception e) {
		}
		
		return null;
	}
	
	private BuiltinFunctions() {
	}

}
