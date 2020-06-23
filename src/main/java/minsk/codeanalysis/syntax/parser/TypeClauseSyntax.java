package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class TypeClauseSyntax extends ExpressionSyntax {

    private final SyntaxToken colonToken;
    private final SyntaxToken identifierToken;

    public TypeClauseSyntax(SyntaxToken colonToken, SyntaxToken identifierToken) {
        this.colonToken = colonToken;
        this.identifierToken = identifierToken;
    }

    public SyntaxToken getIdentifierToken() {
        return identifierToken;
    }

    public SyntaxToken getColonToken() {
        return colonToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.TypeClause;
    }

}