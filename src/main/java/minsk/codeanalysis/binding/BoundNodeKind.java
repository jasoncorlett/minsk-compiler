package minsk.codeanalysis.binding;

public enum BoundNodeKind {
	// Statements
	BlockStatement,
	IfStatement,
	ExpressionStatement,
	ForStatement,
	WhileStatement,
	VariableDeclaration,
	
	// Expressions
	UnaryExpression,
	LiteralExpression, 
	BinaryExpression, 
	VariableExpression, 
	AssignmentExpression,
}