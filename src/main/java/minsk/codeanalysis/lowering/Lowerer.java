package minsk.codeanalysis.lowering;

import java.util.List;

import minsk.codeanalysis.binding.BoundAssignmentExpression;
import minsk.codeanalysis.binding.BoundBinaryExpression;
import minsk.codeanalysis.binding.BoundBinaryOperator;
import minsk.codeanalysis.binding.BoundBlockStatement;
import minsk.codeanalysis.binding.BoundExpressionStatement;
import minsk.codeanalysis.binding.BoundForStatement;
import minsk.codeanalysis.binding.BoundLiteralExpression;
import minsk.codeanalysis.binding.BoundStatement;
import minsk.codeanalysis.binding.BoundTreeRewriter;
import minsk.codeanalysis.binding.BoundVariableDeclaration;
import minsk.codeanalysis.binding.BoundVariableExpression;
import minsk.codeanalysis.binding.BoundWhileStatement;
import minsk.codeanalysis.syntax.SyntaxKind;

public class Lowerer extends BoundTreeRewriter {

	private Lowerer() {
	}

	public static BoundStatement lower(BoundStatement statement) {
		var lowerer = new Lowerer();
		return lowerer.rewriteStatement(statement);
	}

	@Override
	protected BoundStatement rewriteForStatement(BoundForStatement node) {
		// for <var> = <lower> to <upper>
		//		<body>
		// ----
		// var <var> = <lower>
		// while <var> <= <upper>
		//		<body>
		//		<var> = <var> + 1
		//
		
		var variableDeclaration = new BoundVariableDeclaration(node.getVariable(), node.getLowerBound());
		var variableExpression  = new BoundVariableExpression(node.getVariable());
		
		var condition = new BoundBinaryExpression(
				variableExpression,
				BoundBinaryOperator.bind(SyntaxKind.LessEqualsToken, Integer.class, Integer.class),
				node.getUpperBound()
		);
		
		var increment = new BoundExpressionStatement(new BoundAssignmentExpression(
				node.getVariable(),
				new BoundBinaryExpression(
						variableExpression,
						BoundBinaryOperator.bind(SyntaxKind.PlusToken, Integer.class, Integer.class),
						new BoundLiteralExpression(1))));
		
		var body = new BoundBlockStatement(List.of(
				node.getBody(),
				increment
		));

		var whileStatement = new BoundWhileStatement(condition, body);
		
		return new BoundBlockStatement(List.of(
				(BoundStatement) variableDeclaration,
				whileStatement
		));
	}

}
