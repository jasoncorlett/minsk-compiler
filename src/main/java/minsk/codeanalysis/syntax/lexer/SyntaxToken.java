package minsk.codeanalysis.syntax.lexer;

import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.SyntaxNode;
import minsk.codeanalysis.text.TextSpan;

public class SyntaxToken extends SyntaxNode {
	private final int position;
	private final String text;
	private final Object value;
	private final SyntaxKind kind;
	
	public SyntaxToken(SyntaxKind kind, int position, String text, Object value) {
		this.kind = kind;
		this.position = position;
		this.text = text;
		this.value = value;
	}
	
	public boolean isMissing() {
		return text == null;
	}

	public int getPosition() {
		return position;
	}

	public String getText() {
		return text;
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public TextSpan getSpan() {
		var length = (text != null) ? text.length() : 0;
		return new TextSpan(position, position + length);
	}

	@Override
	public SyntaxKind getKind() {
		return kind;
	}
	
	@Override
	public String toString() {
		var builder = new StringBuilder();
		builder.append(getKind());
		
		if (this.getKind() == SyntaxKind.IdentifierToken) {
			builder.append(" ");
			builder.append(this.getText());
		}
		
		if (this.getValue() != null) {
			builder.append(" ");
			builder.append(this.getValue());
		}
		
		return builder.toString();
	}
}