package minsk.codeanalysis.binding;

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

	public BoundExpression getCondition() {
		return condition;
	}

	public BoundStatement getThenStatement() {
		return thenStatement;
	}

	public BoundStatement getElseStatement() {
		return elseClause;
	}
}
