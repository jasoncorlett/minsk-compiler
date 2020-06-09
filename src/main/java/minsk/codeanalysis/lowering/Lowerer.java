package minsk.codeanalysis.lowering;

import java.util.Collections;
import java.util.LinkedList;

import minsk.codeanalysis.binding.BoundAssignmentExpression;
import minsk.codeanalysis.binding.BoundBinaryExpression;
import minsk.codeanalysis.binding.BoundBinaryOperator;
import minsk.codeanalysis.binding.BoundBlockStatement;
import minsk.codeanalysis.binding.BoundConditionalGotoStatement;
import minsk.codeanalysis.binding.BoundExpressionStatement;
import minsk.codeanalysis.binding.BoundForStatement;
import minsk.codeanalysis.binding.BoundGotoStatement;
import minsk.codeanalysis.binding.BoundIfStatement;
import minsk.codeanalysis.binding.BoundLabel;
import minsk.codeanalysis.binding.BoundLabelStatement;
import minsk.codeanalysis.binding.BoundLiteralExpression;
import minsk.codeanalysis.binding.BoundStatement;
import minsk.codeanalysis.binding.BoundTreeRewriter;
import minsk.codeanalysis.binding.BoundVariableDeclaration;
import minsk.codeanalysis.binding.BoundVariableExpression;
import minsk.codeanalysis.binding.BoundWhileStatement;
import minsk.codeanalysis.symbols.VariableSymbol;
import minsk.codeanalysis.syntax.SyntaxKind;

public class Lowerer extends BoundTreeRewriter {

	private int labelCount = 0;
	private int variableCounter = 0;

	private BoundLabel generateLabel() {
		return new BoundLabel("Label" + ++labelCount);
	}
	
	protected VariableSymbol generateVariable(boolean isReadOnly, Class<?> clazz) {
		var name = "var" + ++variableCounter;
		return new VariableSymbol(name, isReadOnly, clazz);
	}
	
	private Lowerer() {
	}

	public static BoundBlockStatement lower(BoundStatement statement) {
		var lowerer = new Lowerer();
		var result = lowerer.rewriteStatement(statement);
		return flatten(result);
	}
	
	private static BoundBlockStatement flatten(BoundStatement statement) {
		var result = new LinkedList<BoundStatement>();
		var stack = new LinkedList<BoundStatement>();
		stack.push(statement);
		
		while (!stack.isEmpty()) {
			var current = stack.pop();
			
			if (current instanceof BoundBlockStatement block) {
				var list = new LinkedList<>(block.getStatements());
				Collections.reverse(list);
				
				for (var s : list) {
					stack.push(s);
				}
			}
			else {
				result.add(current);
			}
		}
		
		return new BoundBlockStatement(result);
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
		
		var lowerBoundDeclaration = new BoundVariableDeclaration(node.getVariable(), node.getLowerBound());
		var lowerBoundExpression = new BoundVariableExpression(node.getVariable());
		
		var upperBound = generateVariable(true, Integer.class);
		var upperBoundDeclaration = new BoundVariableDeclaration(upperBound, node.getUpperBound());
		var upperBoundExpression = new BoundVariableExpression(upperBound);
		
		var condition = new BoundBinaryExpression(
				lowerBoundExpression,
				BoundBinaryOperator.bind(SyntaxKind.LessEqualsToken, Integer.class, Integer.class),
				upperBoundExpression
		);
		
		var increment = new BoundExpressionStatement(new BoundAssignmentExpression(
				node.getVariable(),
				new BoundBinaryExpression(
						lowerBoundExpression,
						BoundBinaryOperator.bind(SyntaxKind.PlusToken, Integer.class, Integer.class),
						new BoundLiteralExpression(1))));
		
		var body = BoundBlockStatement.of(
				node.getBody(),
				increment
		);

		var whileStatement = new BoundWhileStatement(condition, body);
		
		var	result= BoundBlockStatement.of(
				lowerBoundDeclaration,
				upperBoundDeclaration,
				whileStatement
		);
		
		return rewriteStatement(result);
	}
	
	@Override
	protected BoundStatement rewriteWhileStatement(BoundWhileStatement node) {
		// while <condition>
		//		<body>
		// --------------
		// goto check
		// continue:
		// <body>
		// check:
		// gotoTrue <condition> continue
		// end:
		
		var continueLabel = generateLabel();
		var checkLabel = generateLabel();
		
		var gotoCheck = new BoundGotoStatement(checkLabel);
		var continueLabelStatement = new BoundLabelStatement(continueLabel);
		var checkLabelStatement = new BoundLabelStatement(checkLabel);
		var gotoContinue = new BoundConditionalGotoStatement(continueLabel, node.getCondition());
		
		var result = BoundBlockStatement.of(
				gotoCheck,
				continueLabelStatement,
				node.getBody(),
				checkLabelStatement,
				gotoContinue
		);
		
		return rewriteStatement(result);
	}
	
	@Override
	protected BoundStatement rewriteIfStatement(BoundIfStatement node) {
		// if <condition>
		// 		<then>
		// ------
		// gotoFalse <condition> end
		// <then>
		// end:
		if (node.getElseClause() == null) {
			var endLabel = generateLabel();
			var endLabelStatement = new BoundLabelStatement(endLabel);
			var gotoEnd = new BoundConditionalGotoStatement(endLabel, node.getCondition(), false);

			return rewriteStatement(BoundBlockStatement.of(
					gotoEnd,
					node.getThenStatement(),
					endLabelStatement
			));
		}
		
		// if <condition>
		// 		<then>
		// else
		// 		<else>
		// -------
		//
		// gotoFalse <condition> else
		// <then>
		// goto end
		// else:
		// <else>
		// end:
		else {
			var elseLabel = generateLabel();
			var endLabel = generateLabel();
			
			var elseLabelStatement = new BoundLabelStatement(elseLabel);
			var gotoElse = new BoundConditionalGotoStatement(elseLabel, node.getCondition(), false);
			var gotoEnd = new BoundGotoStatement(endLabel);
			var endLabelStatement = new BoundLabelStatement(endLabel);
			
			var result = BoundBlockStatement.of(
					gotoElse,
					node.getThenStatement(),
					gotoEnd,
					elseLabelStatement,
					node.getElseClause(),
					endLabelStatement
			);
			
			return rewriteStatement(result);
		}
	}

}
