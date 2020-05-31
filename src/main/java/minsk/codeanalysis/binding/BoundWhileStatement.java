package minsk.codeanalysis.binding;

public class BoundWhileStatement implements BoundStatement {

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

	public BoundExpression getCondition() {
		return condition;
	}

	public BoundStatement getBody() {
		return body;
	}
}
