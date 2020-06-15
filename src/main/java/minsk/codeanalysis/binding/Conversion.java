package minsk.codeanalysis.binding;

import minsk.codeanalysis.symbols.TypeSymbol;

public class Conversion {
	public static final Conversion None = new Conversion(false, false, false);
	public static final Conversion Identity = new Conversion(true, true, true);
	public static final Conversion Implicit = new Conversion(true, false, true);
	public static final Conversion Explicit = new Conversion(true, false, false);
	
	private Conversion(boolean exists, boolean isIdentity, boolean isImplicit) {
		this.exists = exists;
		this.isIdentity = isIdentity;
		this.isImplicit = isImplicit;
	}
	
	private final boolean exists;
	private final boolean isIdentity;
	private final boolean isImplicit;
	
	public boolean isExists() {
		return exists;
	}

	public boolean isIdentity() {
		return isIdentity;
	}
	
	public boolean isImplicit() {
		return isImplicit;
	}
	
	public boolean isExplicit() {
		return exists && !isImplicit;
	}
	
	public static Conversion classify(TypeSymbol from, TypeSymbol to) {
		if (from.equals(to)) {
			return Identity;
		}
		
		
		if ((from.equals(TypeSymbol.Bool) || from.equals(TypeSymbol.Int)) 
				&& to.equals(TypeSymbol.String)) {
			return Explicit;
		}
		
		if (from.equals(TypeSymbol.String) && 
				(to.equals(TypeSymbol.Bool) || to.equals(TypeSymbol.Int))) {
			return Explicit;
		}
		
		
		return None;
	}
}
