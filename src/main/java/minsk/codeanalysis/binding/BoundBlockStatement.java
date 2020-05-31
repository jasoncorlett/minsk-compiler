package minsk.codeanalysis.binding;

import java.util.List;

public class BoundBlockStatement implements BoundStatement {
	private final List<BoundStatement> statements;

	public BoundBlockStatement(List<BoundStatement> statements) {
		this.statements = statements;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.BlockStatement;
	}

	public List<BoundStatement> getStatements() {
		return statements;
	}

}
