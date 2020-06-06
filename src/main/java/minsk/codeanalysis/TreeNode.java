package minsk.codeanalysis;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TreeNode {
	private Stream<TreeNode> invokeChildMethod(Method method) {
		try {
			var result = method.invoke(this);
			
			if (TreeNode.class.isInstance(result)) {
				return Stream.of(TreeNode.class.cast(result));
			}
			else if (result instanceof List<TreeNode> list) {
				return list.stream(); // .filter(TreeNode.class::isInstance).map(TreeNode.class::cast);
			}
			else {
				return Stream.empty();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static boolean isChild(Method method) {
		return method.getAnnotation(Nested.class) != null;
	}
	
	private static int getChildMethodOrder(Method method) {
		return method.getAnnotation(Nested.class).value();
	}
	
	public List<TreeNode> getChildren() {
		return Arrays.stream(this.getClass().getDeclaredMethods())
				.filter(TreeNode::isChild)
				.sorted(Comparator.comparingInt(TreeNode::getChildMethodOrder))
				.flatMap(this::invokeChildMethod)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	
//	public <T extends TreeNode> List<T> getChildren(Class<T> clazz) {
//		return Arrays.stream(this.getClass().getDeclaredMethods())
//				.filter(TreeNode::isChild)
//				.sorted(Comparator.comparingInt(TreeNode::getChildMethodOrder))
//				.flatMap(this::invokeChildMethod)
//				.filter(Objects::nonNull)
//				.filter(clazz::isInstance)
//				.map(clazz::cast)
//				.collect(Collectors.toList());
//	}
}
