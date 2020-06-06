package minsk.codeanalysis.lowering;

import minsk.codeanalysis.binding.BoundStatement;
import minsk.codeanalysis.binding.BoundTreeRewriter;

public class Lowerer extends BoundTreeRewriter {

	private Lowerer() {
	}

	public static BoundStatement lower(BoundStatement statement) {
		var lowerer = new Lowerer();
		return lowerer.rewriteStatement(statement);
	}
	
}
