package minsk.codeanalysis.binding;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import minsk.codeanalysis.symbols.VariableSymbol;

public class BoundScope {

	private final Map<String, VariableSymbol> variables = new HashMap<>();
	private final BoundScope parent;
	
	public BoundScope(BoundScope parent) {
		this.parent = parent;
	}
	
	public boolean declare(VariableSymbol variable) {
		if (variables.containsKey(variable.getName())) {
			return false;
		}
		
		variables.put(variable.getName(), variable);
		return true;
	}
	
	public VariableSymbol lookup(String name) {
		if (variables.containsKey(name)) {
			return variables.get(name);
		}
		
		if (parent != null) {
			return parent.lookup(name);
		}
		
		return null;
	}
	
	public Collection<VariableSymbol> getDeclaredVariables() {
		return variables.values();
	}
	
	public BoundScope getParent() {
		return parent;		
	}
	
}
