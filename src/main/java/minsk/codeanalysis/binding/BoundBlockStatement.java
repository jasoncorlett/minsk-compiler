package minsk.codeanalysis.binding;

import java.util.Arrays;
import java.util.List;

import minsk.codeanalysis.Nested;

public class BoundBlockStatement extends BoundStatement {
	private final List<BoundStatement> statements;

	public BoundBlockStatement(List<BoundStatement> statements) {
		this.statements = statements;
	}
	
	public static BoundBlockStatement of(BoundStatement ...statements) {
		return new BoundBlockStatement(Arrays.asList(statements));
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.BlockStatement;
	}

	@Nested
	public List<BoundStatement> getStatements() {
		return statements;
	}

}
