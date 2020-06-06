package minsk.codeanalysis.binding;

public enum BoundNodeKind {
	// Statements
	BlockStatement,
	IfStatement,
	ExpressionStatement,
	ForStatement,
	WhileStatement,
	VariableDeclaration,
	LabelStatement,
	GotoStatement,
	ConditionalGotoStatement,
	
	// Expressions
	UnaryExpression,
	LiteralExpression, 
	BinaryExpression, 
	VariableExpression, 
	AssignmentExpression,
}