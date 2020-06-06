package minsk.codeanalysis.binding;

public abstract class BoundExpression extends BoundNode {
	public abstract Class<?> getType();
	
	@Override
	public String toString() {
		return super.toString() + " " + getType().getSimpleName();
	}
}