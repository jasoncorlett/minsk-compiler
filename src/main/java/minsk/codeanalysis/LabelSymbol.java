package minsk.codeanalysis;

public class LabelSymbol {
	private final String name;

	public LabelSymbol(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
