package minsk.codeanalysis.binding;

public record VariableSymbol(String name, boolean isReadOnly, Class<?> type) {
}