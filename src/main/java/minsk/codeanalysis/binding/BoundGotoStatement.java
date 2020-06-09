package minsk.codeanalysis.binding;

public class BoundGotoStatement extends BoundStatement {

	private final BoundLabel label;
	
	public BoundGotoStatement(BoundLabel label) {
		this.label = label;
	}

	public BoundLabel getLabel() {
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
