package minsk.codeanalysis.text;

import java.util.Objects;

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
	
	@Override
	public int hashCode() {
		return Objects.hash(end, start);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TextSpan other = (TextSpan) obj;
		return end == other.end && start == other.start;
	}
}
