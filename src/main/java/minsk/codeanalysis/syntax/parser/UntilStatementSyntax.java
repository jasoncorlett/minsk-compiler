package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class UntilStatementSyntax extends StatementSyntax {

    private final SyntaxToken untilKeyword;
    private final ExpressionSyntax condition;
    private final StatementSyntax body;

    public UntilStatementSyntax(SyntaxToken untilKeyword, ExpressionSyntax condition, StatementSyntax body) {
        this.untilKeyword = untilKeyword;
        this.condition = condition;
        this.body = body;
    }

    public SyntaxToken getUntilKeyword() {
        return untilKeyword;
    }

    public ExpressionSyntax getCondition() {
        return condition;
    }

    public StatementSyntax getBody() {
        return body;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.UntilStatement;
    }

}
