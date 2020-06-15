package minsk.codeanalysis.binding;

public class BoundDoStatement extends BoundStatement {

    private BoundStatement body;
    private boolean continueWhen;
    private BoundExpression condition;

    public BoundDoStatement(BoundStatement body, boolean continueWhen, BoundExpression condition) {
        this.setBody(body);
        this.setContinueWhen(continueWhen);
        this.setCondition(condition);
    }

    public BoundStatement getBody() {
        return body;
    }

    public void setBody(BoundStatement body) {
        this.body = body;
    }

    public boolean isContinueWhen() {
        return continueWhen;
    }

    public void setContinueWhen(boolean continueWhen) {
        this.continueWhen = continueWhen;
    }

    public BoundExpression getCondition() {
        return condition;
    }

    public void setCondition(BoundExpression condition) {
        this.condition = condition;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.DoStatement;
    }

}
