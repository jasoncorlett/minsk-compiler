package minsk.codeanalysis.text;

public class TextLine {
	private final String text;
	private final int start;
	private final int end;
	private final int endOfLineWithBreak;

	public TextLine(String text, int start, int end, int endOfLineWithBreak) {
		this.text = text;
		this.start = start;
		this.end = end;
		this.endOfLineWithBreak = endOfLineWithBreak;
	}
	
	public String getText() {
		return text;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getEndOfLineBreak() {
		return endOfLineWithBreak;
	}
	
	public TextSpan getSpan() {
		return new TextSpan(start, end);
	}
	
	public TextSpan getSpanWithBreak() {
		return new TextSpan(start, endOfLineWithBreak);
	}
	
	public String substring(int start, int end) {
		return text.substring(start, end);
	}
	
	public String substring(TextSpan span) {
		return text.substring(span.getStart(), span.getEnd());
	}
}