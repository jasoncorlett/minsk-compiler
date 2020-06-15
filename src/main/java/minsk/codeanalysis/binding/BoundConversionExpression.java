package minsk.codeanalysis.binding;

import minsk.codeanalysis.symbols.TypeSymbol;

public class BoundConversionExpression extends BoundExpression {

    private final TypeSymbol type;
    private final BoundExpression expression;

    public BoundConversionExpression(TypeSymbol type, BoundExpression expression) {
        this.type = type;
        this.expression = expression;
    }

    public BoundExpression getExpression() {
        return expression;
    }

    @Override
    public TypeSymbol getType() {
        return type;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.ConversionExpression;
    }

}
