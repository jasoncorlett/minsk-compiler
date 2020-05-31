package minsk.codeanalysis.syntax;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	
	private Stream<SyntaxNode> invokeChildMethod(Method method) {
		try {
			var result = method.invoke(this);
			
			if (result instanceof SyntaxNode) {
				return Stream.of((SyntaxNode)result);
			}
			else if (result instanceof List) {
				return ((List<?>) result).stream().filter(SyntaxNode.class::isInstance).map(SyntaxNode.class::cast);
			}
			else {
				return Stream.empty();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static boolean isChild(Method method) {
		return method.getAnnotation(SyntaxChild.class) != null;
	}
	
	private static int getChildMethodOrder(Method method) {
		return method.getAnnotation(SyntaxChild.class).order();
	}
	
	public default List<SyntaxNode> getChildren() {
		return Arrays.stream(this.getClass().getDeclaredMethods())
				.filter(SyntaxNode::isChild)
				.sorted(Comparator.comparingInt(SyntaxNode::getChildMethodOrder))
				.flatMap(this::invokeChildMethod)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}