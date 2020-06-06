package minsk.codeanalysis.binding;

import minsk.codeanalysis.LabelSymbol;
import minsk.codeanalysis.Nested;

public class BoundConditionalGotoStatement extends BoundStatement {

	private final LabelSymbol label;
	private final BoundExpression condition;
	private final boolean jumpIfFalse;
	
	public BoundConditionalGotoStatement(LabelSymbol label, BoundExpression condition) {
		this(label, condition, false);
	}
	
	public BoundConditionalGotoStatement(LabelSymbol label, BoundExpression condition, boolean jumpIfFalse) {
		this.label = label;
		this.condition = condition;
		this.jumpIfFalse = jumpIfFalse;
	}
	
	public LabelSymbol getLabel() {
		return label;
	}

	@Nested
	public BoundExpression getCondition() {
		return condition;
	}

	public boolean isJumpIfFalse() {
		return jumpIfFalse;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.ConditionalGotoStatement;
	}
	
	@Override
	public String toString() {
		return "ConditionalGoto [ To: " + getLabel() + ", When: " + !jumpIfFalse + " ]";
	}

}
