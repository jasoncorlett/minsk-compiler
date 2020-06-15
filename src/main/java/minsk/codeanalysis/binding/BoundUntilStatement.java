package minsk.codeanalysis.binding;

public class BoundUntilStatement extends BoundStatement {

    private final BoundExpression condition;
    private final BoundStatement body;

    public BoundUntilStatement(BoundExpression condition, BoundStatement body) {
        this.condition = condition;
        this.body = body;
    }

    public BoundExpression getCondition() {
        return condition;
    }

    public BoundStatement getBody() {
        return body;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.UntilStatement;
    }

}
