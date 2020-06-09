package minsk.codeanalysis.binding;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.symbols.VariableSymbol;

public class BoundForStatement extends BoundStatement {

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

	@Nested(1)
	public VariableSymbol getVariable() {
		return variable;
	}

	@Nested(2)
	public BoundExpression getLowerBound() {
		return lowerBound;
	}

	@Nested(3)
	public BoundExpression getUpperBound() {
		return upperBound;
	}

	@Nested(4)
	public BoundStatement getBody() {
		return body;
	}
}
