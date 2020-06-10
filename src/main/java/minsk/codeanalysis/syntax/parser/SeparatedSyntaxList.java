package minsk.codeanalysis.syntax.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import minsk.codeanalysis.syntax.SyntaxNode;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class SeparatedSyntaxList<T extends SyntaxNode> extends AbstractSeparatedSyntaxList implements Iterable<T> {

	private final List<SyntaxNode> nodesAndSeparators;
	
	public SeparatedSyntaxList(List<SyntaxNode> nodesAndSeparators) {
		this.nodesAndSeparators = nodesAndSeparators;
	}

	public int count() {
		return (nodesAndSeparators.size() + 1)/ 2;
	}

	public T get(int index) {
		var element = nodesAndSeparators.get(index * 2);
		
		if (element instanceof T t) {
			return t;
		}
		
		return null;
	}
	
	public SyntaxToken getSeparator(int index) {
		if (index == count() - 1)
			return null;
		
		return (SyntaxToken) nodesAndSeparators.get(index * 2 + 1);
	}

	@Override
	public List<SyntaxNode> getWithSeparators() {
		return nodesAndSeparators;
	}

	@Override
	public Iterator<T> iterator() {
		var result = new ArrayList<T>();
		for (int i = 0; i < count(); i++) {
			result.add(get(i));
		}
		return result.iterator();
	}
}
