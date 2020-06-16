package minsk.codeanalysis.binding;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import minsk.codeanalysis.symbols.AbstractSymbol;
import minsk.codeanalysis.symbols.FunctionSymbol;
import minsk.codeanalysis.symbols.VariableSymbol;

public class BoundScope {

	private final Map<String, AbstractSymbol> symbols = new HashMap<>();
	private final BoundScope parent;
	
	public BoundScope(BoundScope parent) {
		this.parent = parent;
	}

	private boolean declareSymbol(AbstractSymbol symbol) {
		if (symbols.containsKey(symbol.getName())) {
			return false;
		}

		symbols.put(symbol.getName(), symbol);
		return true;
	}

	private <T extends AbstractSymbol> T lookupSymbol(String name, Class<T> clazz) {
		if (symbols.containsKey(name)) {
			var s = symbols.get(name);
			if (clazz.isInstance(s)) {
				return clazz.cast(s);
			}
			return null;
		}

		if (parent != null) {
			return parent.lookupSymbol(name, clazz);
		}

		return null;
	}

	private <T extends AbstractSymbol> Collection<T> getDeclaredSymbols(Class<T> clazz) {
		return symbols.values().stream()
			.filter(clazz::isInstance)
			.map(clazz::cast)
			.collect(Collectors.toCollection(LinkedList::new));
	}
	
	public boolean declareVariable(VariableSymbol variable) {
		return declareSymbol(variable);
	}

	public VariableSymbol lookupVariable(String name) {
		return lookupSymbol(name, VariableSymbol.class);
	}
	
	public Collection<VariableSymbol> getDeclaredVariables() {
		return getDeclaredSymbols(VariableSymbol.class);
	}
		
	public boolean declareFunction(FunctionSymbol function) {
		return declareSymbol(function);
	}
	
	public FunctionSymbol lookupFunction(String name) {
		return lookupSymbol(name, FunctionSymbol.class);
	}
	
	public Collection<FunctionSymbol> getDeclaredFunctions() {
		return getDeclaredSymbols(FunctionSymbol.class);
	}
	
	public BoundScope getParent() {
		return parent;		
	}
	
}
