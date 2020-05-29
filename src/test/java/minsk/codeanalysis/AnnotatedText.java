package minsk.codeanalysis;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import minsk.codeanalysis.text.TextSpan;

public class AnnotatedText {
	public static AnnotatedText parse(String text) {
		var spans = new LinkedList<TextSpan>();
		var startStack = new LinkedList<Integer>();
		var position = 0;
		var sb = new StringBuilder();
		
		for (var c : text.toCharArray()) {
			if (c == '[') {
				startStack.push(position);
			}
			else if (c == ']') {
				if (startStack.size() == 0) {
					throw new RuntimeException("Too many ']' in text '%s'".formatted(text));
				}
				
				var start = startStack.pop();
				spans.add(new TextSpan(start, position));
			}
			else {
				position++;
				sb.append(c);
			}
		}
		
		return new AnnotatedText(sb.toString(), spans);
	}

	
	private final List<TextSpan> spans;
	private final String text;
	
	public AnnotatedText(String text, List<TextSpan> spans) {
		this.text = text;
		this.spans = spans;
	}
	
	public List<TextSpan> getSpans() {
		return spans;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return "AnnotatedText[" + text + " " + spans +"]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(spans, text);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnnotatedText other = (AnnotatedText) obj;
		return Objects.equals(spans, other.spans) && Objects.equals(text, other.text);
	}
}
