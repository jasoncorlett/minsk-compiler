package minsk.codeanalysis.text;

import java.util.ArrayList;
import java.util.List;

public class SourceText {

	public static SourceText from(String text) {
		return new SourceText(text);
	}
	
	private final String text;
	private final List<TextLine> lines;

	private SourceText(String text) {
		this.text = text;
		this.lines = parseLines(text);
	}
	
	public int getLineIndex(int position) {
		var lower = 0;
		var upper = lines.size() - 1;
		var index = 0;
		
		do {
			index = lower + (upper - lower) / 2;
			var line = lines.get(index);
			
			if (position > line.getEnd()) {
				lower = index + 1;
			} else if (position < line.getStart()) {
				upper = index - 1;
			} else {
				return index;
			}
		} while (true);
	}
	
	public TextLine getLineAt(int position) {
		return lines.get(getLineIndex(position));
	}
	
	public String getLineText(int lineNumber) {
		return lines.get(lineNumber).getText();
	}
	
	public TextLine getLine(int lineNumber) {
		return lines.get(lineNumber);
	}
	
	private static List<TextLine> parseLines(String text) {
		var result = new ArrayList<TextLine>();
		var position = 0;
		var lineStart = 0;
		
		while (position < text.length()) {
			var lineBreakWidth = getLineBreakWidth(text, position);
			
			// End of line
			if (lineBreakWidth > 0) {
				result.add(new TextLine(text, lineStart, position, position + lineBreakWidth));
				
				position += lineBreakWidth;
				lineStart = position;
			}
			else {
				position++;
			}
		}
		
		if (position > lineStart) {
			result.add(new TextLine(text, lineStart, position, position));
		}
		
		return result;
	}
	
	private static int getLineBreakWidth(String text, int position) {
		var current = text.charAt(position);
		var lookahead = position + 1 >= text.length() ? '\0' : text.charAt(position + 1);
		
		if (current == '\r' && lookahead == '\n')
			return 2;
		
		if (current == '\n' || current == '\r')
			return 1;
		
		return 0;
	}
	
	public String getText() {
		return text;
	}
	
	public String substring(int start, int end) {
		return text.substring(start, end);
	}

	public String substring(TextSpan span) {
		return text.substring(span.getStart(), span.getEnd());
	}

	public int length() {
		return text.length();
	}

	public char charAt(int position) {
		return text.charAt(position);
	}
}
