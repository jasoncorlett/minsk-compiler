package minsk.codeanalysis.binding;

public class BoundForStatement implements BoundStatement {

	private final VariableSymbol variable;
	private final BoundExpression lowerBound;
	private final BoundExpression upperBound;
	private final BoundStatement body;

	public BoundForStatement(VariableSymbol variable, BoundExpression lowerBound, BoundExpression upperBound,
			BoundStatement body) {
				this.variable = variable;
				this.lowerBound = lowerBound;
				this.upperBound = upperBound;
				this.body = body;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.ForStatement;
	}

	public VariableSymbol getVariable() {
		return variable;
	}

	public BoundExpression getLowerBound() {
		return lowerBound;
	}

	public BoundExpression getUpperBound() {
		return upperBound;
	}

	public BoundStatement getBody() {
		return body;
	}
}
