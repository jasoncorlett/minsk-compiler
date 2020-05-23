package minsk.codeanalysis.text;

// TODO: Reason about desired usage and clean up fields, keeping all for now
public class TextLine {
	private final String fullText;
	private final String text;
	private final int start;
	private final int end;
	private final int endOfLineWithBreak;
	private final String lineBreak;

	public TextLine(String fullText, int start, int end, int endOfLineWithBreak) {
		this.fullText = fullText;
		this.text = fullText.substring(start, end);
		this.start = start;
		this.end = end;
		this.endOfLineWithBreak = endOfLineWithBreak;
		this.lineBreak = fullText.substring(end, endOfLineWithBreak);
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

	public TextSpan getSpan() {
		return new TextSpan(start, end);
	}
	
	public String substring(int start, int end) {
		return text.substring(start, end);
	}
	
	public String substring(TextSpan span) {
		return text.substring(span.getStart(), span.getEnd());
	}
}