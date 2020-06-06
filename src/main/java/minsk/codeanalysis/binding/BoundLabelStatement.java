package minsk.codeanalysis.binding;

import minsk.codeanalysis.LabelSymbol;

public class BoundLabelStatement extends BoundStatement {

	private final LabelSymbol label;
	
	public BoundLabelStatement(LabelSymbol label) {
		this.label = label;
	}

	public LabelSymbol getLabel() {
		return label;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.LabelStatement;
	}
	
	@Override
	public String toString() {
		return "LabelStatement [ " + getLabel() + " ]";
	}

}
