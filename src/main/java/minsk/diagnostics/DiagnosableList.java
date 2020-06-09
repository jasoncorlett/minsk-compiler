package minsk.diagnostics;

import java.util.ArrayList;

public class DiagnosableList<T> extends ArrayList<T> implements Diagnosable {
    private static final long serialVersionUID = 3044983494789979984L;

    private final transient DiagnosticsCollection diagnostics;

    public DiagnosableList(Diagnosable source) {
        diagnostics = new DiagnosticsCollection();
        diagnostics.addFrom(source);
    }

    public DiagnosableList() {
        diagnostics = new DiagnosticsCollection();
    }

    @Override
    public DiagnosticsCollection getDiagnostics() {
        return diagnostics;
    }

}