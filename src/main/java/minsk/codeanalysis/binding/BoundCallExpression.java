package minsk.codeanalysis.binding;

import java.util.List;

import minsk.codeanalysis.symbols.FunctionSymbol;
import minsk.codeanalysis.symbols.TypeSymbol;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.parser.ExpressionSyntax;

public class BoundCallExpression extends BoundExpression {
    private final FunctionSymbol function;
    private final List<BoundExpression> arguments;

    public BoundCallExpression(FunctionSymbol function, List<BoundExpression> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public TypeSymbol getType() {
        return function.getType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.CallExpression;
    }

    public FunctionSymbol getFunction() {
        return function;
    }

    public List<BoundExpression> getArguments() {
        return arguments;
    }

}
