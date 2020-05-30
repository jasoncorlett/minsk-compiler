package minsk.codeanalysis.binding;

public enum BoundNodeKind {
	// Statements
	BlockStatement,
	IfStatement,
	ExpressionStatement,
	VariableDeclaration,
	
	// Expressions
	UnaryExpression,
	LiteralExpression, 
	BinaryExpression, 
	VariableExpression, 
	AssignmentExpression, 
}