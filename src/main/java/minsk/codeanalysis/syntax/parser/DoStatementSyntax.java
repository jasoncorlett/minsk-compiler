package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class DoStatementSyntax extends StatementSyntax {

    private final SyntaxToken doKeyword;
    private final StatementSyntax body;
    private final SyntaxToken loopKeyword;
    private final ExpressionSyntax condition;

    public DoStatementSyntax(SyntaxToken doKeyword, StatementSyntax body, SyntaxToken loopKeyword,
            ExpressionSyntax condition) {
        this.doKeyword = doKeyword;
        this.body = body;
        this.loopKeyword = loopKeyword;
        this.condition = condition;
    }

    public SyntaxToken getDoKeyword() {
        return doKeyword;
    }

    public StatementSyntax getBody() {
        return body;
    }

    public SyntaxToken getLoopKeyword() {
        return loopKeyword;
    }

    public ExpressionSyntax getCondition() {
        return condition;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.DoStatement;
    }

}
