package minsk.codeanalysis.binding;

import minsk.codeanalysis.LabelSymbol;

public class BoundGotoStatement extends BoundStatement {

	private final LabelSymbol label;
	
	public BoundGotoStatement(LabelSymbol label) {
		this.label = label;
	}

	public LabelSymbol getLabel() {
		return label;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.GotoStatement;
	}
	
	@Override
	public String toString() {
		return "Goto [ To: " + getLabel() + " ]";
	}

}
