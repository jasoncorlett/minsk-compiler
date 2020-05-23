package minsk.codeanalysis.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class SourceTextTest {
	
	@ParameterizedTest
	@MethodSource
	public void SourceTextIncludesLastLine(String text, int expectedLineCount) {
		var source= SourceText.from(text);
		assertEquals(expectedLineCount, source.getLines().size());
	}
	
	public static Stream<Arguments> SourceTextIncludesLastLine() {
		return Stream.of(
				Arguments.of(".", 1),
				Arguments.of(".\r\n", 2),
				Arguments.of(".\r\n\r\n", 3)
		);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"\n", "\r", "\r\n"})
	public void SourceTextReturnsCorrectLineNumber(String lineDelimiter) {
		var offset = lineDelimiter.length();
		
		var source = SourceText.from(String.join(lineDelimiter,
				"a",
				"b",
				"c",
				""
		));
		
		assertEquals(0, source.getLineIndex(0));
		assertEquals(1, source.getLineIndex(1 + offset));
		assertEquals(2, source.getLineIndex(2 + offset * 2));
		assertEquals(3, source.getLineIndex(3 + offset * 3));
	}
}
