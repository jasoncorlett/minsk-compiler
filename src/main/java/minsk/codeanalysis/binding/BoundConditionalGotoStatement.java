package minsk.codeanalysis.binding;

import minsk.codeanalysis.Nested;

public class BoundConditionalGotoStatement extends BoundStatement {

	private final BoundLabel label;
	private final BoundExpression condition;
	private final boolean jumpWhen;
	
	public BoundConditionalGotoStatement(BoundLabel label, BoundExpression condition) {
		this(label, condition, true);
	}
	
	public BoundConditionalGotoStatement(BoundLabel label, BoundExpression condition, boolean jumpWhen) {
		this.label = label;
		this.condition = condition;
		this.jumpWhen = jumpWhen;
	}
	
	public BoundLabel getLabel() {
		return label;
	}

	@Nested
	public BoundExpression getCondition() {
		return condition;
	}

	public boolean getJumpWhen() {
		return jumpWhen;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.ConditionalGotoStatement;
	}
	
	@Override
	public String toString() {
		return "ConditionalGoto [ To: " + getLabel() + ", When: " + jumpWhen + " ]";
	}

}
