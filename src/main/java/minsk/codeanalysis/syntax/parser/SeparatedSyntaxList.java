package minsk.codeanalysis.syntax.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import minsk.codeanalysis.syntax.SyntaxNode;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class SeparatedSyntaxList<E extends SyntaxNode> extends AbstractSeparatedSyntaxList implements Iterable<E> {

	private final List<SyntaxNode> nodesAndSeparators;

	public SeparatedSyntaxList(List<SyntaxNode> nodesAndSeparators) {
		this.nodesAndSeparators = nodesAndSeparators;
	}

	public int count() {
		return (nodesAndSeparators.size() + 1) / 2;
	}

	/**
	 * @see java.util.ArrayList.elementData
	 */
	@SuppressWarnings("unchecked")
	private E getImpl(int index) {
		return (E) nodesAndSeparators.get(index);
	}

	public E get(int index) {
		return getImpl(index * 2);
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
	public Iterator<E> iterator() {
		var result = new ArrayList<E>();
		
		for (var i = 0; i < count(); i++) {
			result.add(get(i));
		}

		return result.iterator();
	}
}
