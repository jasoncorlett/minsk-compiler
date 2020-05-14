package minsk.codeanalysis.binding;

public abstract class BoundExpression extends BoundNode {
	abstract Class<? extends Object> getType();
}