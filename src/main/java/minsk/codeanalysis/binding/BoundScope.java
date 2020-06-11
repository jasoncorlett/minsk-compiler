package minsk.codeanalysis.binding;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import minsk.codeanalysis.symbols.FunctionSymbol;
import minsk.codeanalysis.symbols.VariableSymbol;

public class BoundScope {

	private final Map<String, VariableSymbol> variables = new HashMap<>();
	private final Map<String, FunctionSymbol> functions = new HashMap<>();
	private final BoundScope parent;
	
	public BoundScope(BoundScope parent) {
		this.parent = parent;
	}
	
	public boolean declareVariable(VariableSymbol variable) {
		if (variables.containsKey(variable.getName())) {
			return false;
		}
		
		variables.put(variable.getName(), variable);
		return true;
	}
	
	public VariableSymbol lookupVariable(String name) {
		if (variables.containsKey(name)) {
			return variables.get(name);
		}
		
		if (parent != null) {
			return parent.lookupVariable(name);
		}
		
		return null;
	}
	
	public Collection<VariableSymbol> getDeclaredVariables() {
		return variables.values();
	}
		
	public boolean declareFunction(FunctionSymbol function) {
		if (functions.containsKey(function.getName())) {
			return false;
		}
		
		functions.put(function.getName(), function);
		return true;
	}
	
	public FunctionSymbol lookupFunction(String name) {
		if (functions.containsKey(name)) {
			return functions.get(name);
		}
		
		if (parent != null) {
			return parent.lookupFunction(name);
		}
		
		return null;
	}
	
	public Collection<FunctionSymbol> getDeclaredFunctions() {
		return functions.values();
	}
	
	public BoundScope getParent() {
		return parent;		
	}
	
}
