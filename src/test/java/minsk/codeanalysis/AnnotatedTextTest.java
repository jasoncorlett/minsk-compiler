package minsk.codeanalysis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import minsk.codeanalysis.text.TextSpan;

class AnnotatedTextTest {

	@Test
	void TestOneCharacter() {
		var text = "[a]";
		
		var expected = new AnnotatedText("a",
				List.of(new TextSpan(0, 1)));
		
		var actualSpans = AnnotatedText.parse(text);

		assertEquals(expected, actualSpans);
	}
	
	@Test
	void TestMultipleSpans() {
		var text = "a[b]cd[ef]gh";
		var expected = new AnnotatedText("abcdefgh", List.of(new TextSpan(1,2), new TextSpan(4,6)));
		var actual = AnnotatedText.parse(text);

		assertEquals(expected, actual);
	}
	
	@Test
	void TestMultipleLines() {
		var text = """
				[a] bb [ccc] dddd
				eeeee [ffffff] ggggggg [hhhhhhhh]
				""";
		
		var expected = new AnnotatedText("""
				a bb ccc dddd
				eeeee ffffff ggggggg hhhhhhhh
				""", 
				List.of(new TextSpan(0,1), new TextSpan(5,8),
				new TextSpan(20, 26), new TextSpan(35, 43)));

		var actual = AnnotatedText.parse(text);

		assertEquals(expected, actual);
	}
}
