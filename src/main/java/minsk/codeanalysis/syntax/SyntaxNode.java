package minsk.codeanalysis.syntax;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import minsk.codeanalysis.text.TextSpan;

public interface SyntaxNode {
	public abstract SyntaxKind getKind();
	
	public default TextSpan getSpan() {
		var children = getChildren();
		
		if (children.isEmpty()) {
			return null;
		}
		
		var first = children.get(0).getSpan();
		var last = children.get(children.size() - 1).getSpan();
		
		return new TextSpan(first.getStart(), last.getEnd());
	}
	
	private static SyntaxNode invoke(SyntaxNode node, Method method) {
		try {
			return (SyntaxNode) method.invoke(node);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static boolean isChild(Method method) {
		return SyntaxNode.class.isAssignableFrom(method.getReturnType()) && method.getAnnotation(SyntaxChild.class) != null;
	}
	
	private static int getMethodOrder(Method method) {
		return method.getAnnotation(SyntaxChild.class).order();
	}
	
	public default List<SyntaxNode> getChildren() {
		return Arrays.stream(this.getClass().getDeclaredMethods())
				.filter(SyntaxNode::isChild)
				.sorted(Comparator.comparingInt(SyntaxNode::getMethodOrder))
				.map(m -> SyntaxNode.invoke(this, m))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}