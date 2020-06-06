package minsk.codeanalysis.binding;

import minsk.codeanalysis.LabelSymbol;
import minsk.codeanalysis.Nested;

public class BoundConditionalGotoStatement extends BoundStatement {

	private final LabelSymbol label;
	private final BoundExpression condition;
	private final boolean jumpWhen;
	
	public BoundConditionalGotoStatement(LabelSymbol label, BoundExpression condition) {
		this(label, condition, true);
	}
	
	public BoundConditionalGotoStatement(LabelSymbol label, BoundExpression condition, boolean jumpWhen) {
		this.label = label;
		this.condition = condition;
		this.jumpWhen = jumpWhen;
	}
	
	public LabelSymbol getLabel() {
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
