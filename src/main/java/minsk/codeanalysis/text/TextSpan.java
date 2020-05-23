package minsk.codeanalysis.text;

public class TextSpan {
	private final int start;
	private final int end;

	public TextSpan(int start, int end) {
		this.start = start;
		this.end= end;
	}

	public int getStart() {
		return start;
	}

	public int getLength() {
		return end - start;
	}
	
	public int getEnd() {
		return end;
	}
	
	@Override
	public String toString() {
		return "(" + start + ", " + end + ")";
	}
}
