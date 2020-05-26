package minsk.codeanalysis.binding;

public enum BoundNodeKind {
	// Statements
	BlockStatement, 
	ExpressionStatement,
	VariableDeclaration,
	
	// Expressions
	UnaryExpression,
	LiteralExpression, 
	BinaryExpression, 
	VariableExpression, 
	AssignmentExpression
}