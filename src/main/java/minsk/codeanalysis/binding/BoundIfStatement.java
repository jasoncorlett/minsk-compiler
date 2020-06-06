package minsk.codeanalysis.binding;

import minsk.codeanalysis.Nested;

public class BoundIfStatement extends BoundStatement {
	private final BoundExpression condition;
	private final BoundStatement thenStatement;
	private final BoundStatement elseClause;

	public BoundIfStatement(BoundExpression condition, BoundStatement thenStatement, BoundStatement elseClause) {
		this.condition = condition;
		this.thenStatement = thenStatement;
		this.elseClause = elseClause;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.IfStatement;
	}

	@Nested(1)
	public BoundExpression getCondition() {
		return condition;
	}

	@Nested(2)
	public BoundStatement getThenStatement() {
		return thenStatement;
	}

	@Nested(3)
	public BoundStatement getElseClause() {
		return elseClause;
	}
}
