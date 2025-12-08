package Principal;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BonosTableModel extends AbstractTableModel {

    private List<Bono> bonos;
    // Añadimos columna "Días"
    private final String[] columnNames = {"ID", "Nombre", "Fin Temporada", "Días Validez", "Precio (€)"};

    public BonosTableModel(List<Bono> bonos) {
        this.bonos = bonos;
    }

    @Override public int getRowCount() { return bonos.size(); }
    @Override public int getColumnCount() { return columnNames.length; }
    @Override public String getColumnName(int column) { return columnNames[column]; }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Integer.class;
            case 2: return java.util.Date.class; // Fin Temporada (31/12/2025)
            case 3: return Integer.class;        // Duración (30)
            case 4: return Double.class;
            default: return String.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Bono bono = bonos.get(rowIndex);
        switch (columnIndex) {
            case 0: return bono.getId();
            case 1: return bono.getNombre();
            case 2: return bono.getFechaExpiracion(); // La fecha fija (31/12)
            case 3: return bono.getDuracionDias();    // La duración (30)
            case 4: return bono.getPrecio();
            default: return null;
        }
    }

    @Override public boolean isCellEditable(int r, int c) { return false; }
    public Bono getBonoAt(int r) { return bonos.get(r); }
}