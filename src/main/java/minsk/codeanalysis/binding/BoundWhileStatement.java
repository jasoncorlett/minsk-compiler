package minsk.codeanalysis.binding;

import minsk.codeanalysis.Nested;

public class BoundWhileStatement extends BoundStatement {

	private final BoundExpression condition;
	private final BoundStatement body;
	
	public BoundWhileStatement(BoundExpression condition, BoundStatement body) {
		this.condition = condition;
		this.body = body;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.WhileStatement;
	}

	@Nested
	public BoundExpression getCondition() {
		return condition;
	}

	@Nested
	public BoundStatement getBody() {
		return body;
	}
}
