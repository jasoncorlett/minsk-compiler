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
	DoStatement,
	UntilStatement,

	// Expressions
	UnaryExpression,
	LiteralExpression, 
	BinaryExpression, 
	VariableExpression, 
	AssignmentExpression,
	ErrorExpression,
	CallExpression,
}