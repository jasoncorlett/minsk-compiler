package minsk.codeanalysis.binding;

import minsk.codeanalysis.symbols.TypeSymbol;
import minsk.codeanalysis.syntax.SyntaxKind;

public final class BoundUnaryOperator {
	
	private final static BoundUnaryOperator[] operators = new BoundUnaryOperator[] {
			new BoundUnaryOperator(SyntaxKind.BangToken, BoundUnaryOperatorKind.LogicalNegation, TypeSymbol.Bool),
			
			new BoundUnaryOperator(SyntaxKind.PlusToken, BoundUnaryOperatorKind.Identity, TypeSymbol.Int),
			new BoundUnaryOperator(SyntaxKind.MinusToken, BoundUnaryOperatorKind.Negation, TypeSymbol.Int),
			new BoundUnaryOperator(SyntaxKind.TildeToken, BoundUnaryOperatorKind.OnesComplement, TypeSymbol.Int),
	};
	
	public static BoundUnaryOperator bind(SyntaxKind syntaxKind, TypeSymbol operandType) {
		for (var op : operators) {
			if (op.syntaxKind == syntaxKind && op.operandType == operandType) {
				return op;
			}
		}
		return null;
	}
	
	private final SyntaxKind syntaxKind;
	private final BoundUnaryOperatorKind kind;
	private final TypeSymbol operandType;
	private final TypeSymbol resultType;
	
	private BoundUnaryOperator(SyntaxKind syntaxKind, BoundUnaryOperatorKind kind, TypeSymbol operandType, TypeSymbol resultType) {
		this.syntaxKind = syntaxKind;
		this.kind = kind;
		this.operandType = operandType;
		this.resultType = resultType;
	}
	
	private BoundUnaryOperator(SyntaxKind syntaxKind, BoundUnaryOperatorKind kind, TypeSymbol operandType) {
		this(syntaxKind, kind, operandType, operandType);
	}

	public SyntaxKind getSyntaxKind() {
		return syntaxKind;
	}

	public BoundUnaryOperatorKind getKind() {
		return kind;
	}

	public TypeSymbol getOperandType() {
		return operandType;
	}

	public TypeSymbol getResultType() {
		return resultType;
	}
	
}