package minsk.codeanalysis.binding;

public class BoundLabelStatement extends BoundStatement {

	private final BoundLabel label;
	
	public BoundLabelStatement(BoundLabel label) {
		this.label = label;
	}

	public BoundLabel getLabel() {
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
