package utilities;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Usage:
 *   JTable table = ...;
 *   TableAutoResizer.attach(table); // call once
 *   // Columns will auto-fit after any data/structure change.
 */
public final class TableAutoResizer {

    private static final int PADDING = 10;
    private static final int MIN_COL_WIDTH = 50;
    private static final int MAX_COL_WIDTH = 600; // safety cap; tweak if needed
    private static final int DEBOUNCE_MS = 120;

    private TableAutoResizer() {}

    public static void attach(JTable table) {
        new Installer(table); // self-registering helper
        resizeNow(table);     // initial fit
    }

    /** Manually trigger a resize if you ever need to. */
    public static void resizeNow(JTable table) {
        if (table.getTableHeader() == null) return;
        TableColumnModel colModel = table.getColumnModel();
        // Turn off JTable's built-in width sharing
        int oldMode = table.getAutoResizeMode();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int c = 0; c < table.getColumnCount(); c++) {
            TableColumn col = colModel.getColumn(c);
            int width = MIN_COL_WIDTH;

            // header width
            TableCellRenderer hdrRenderer = table.getTableHeader().getDefaultRenderer();
            Component hc = hdrRenderer.getTableCellRendererComponent(
                    table, col.getHeaderValue(), false, false, 0, c);
            width = Math.max(width, hc.getPreferredSize().width + PADDING);

            // cell widths
            for (int r = 0; r < table.getRowCount(); r++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(r, c);
                Component comp = table.prepareRenderer(cellRenderer, r, c);
                width = Math.max(width, comp.getPreferredSize().width + PADDING);
            }

            width = Math.min(width, MAX_COL_WIDTH);
            col.setPreferredWidth(width);
        }

        // keep OFF to allow horizontal scroll; restore if you prefer:
        // table.setAutoResizeMode(oldMode);
    }

    /** Internal listener bundle that reattaches on model swaps and debounces resizes. */
    private static final class Installer implements TableModelListener, PropertyChangeListener {
        private final JTable table;
        private Timer debounce;

        Installer(JTable table) {
            this.table = table;

            // listen to current model
            if (table.getModel() != null) {
                table.getModel().addTableModelListener(this);
            }

            // if table model is replaced later, re-attach
            table.addPropertyChangeListener(this);

            // if columns are added/removed (structure change), resize
            table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
                @Override public void columnAdded(TableColumnModelEvent e)  { schedule(); }
                @Override public void columnRemoved(TableColumnModelEvent e){ schedule(); }
                @Override public void columnMoved(TableColumnModelEvent e)  { schedule(); }
                @Override public void columnMarginChanged(ChangeEvent e)     { /* ignore */ }
                @Override public void columnSelectionChanged(ListSelectionEvent e) { /* ignore */ }
            });

            // when the table becomes displayable (e.g., first shown), do a pass
            table.addHierarchyListener(e -> {
                if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0 && table.isDisplayable()) {
                    schedule();
                }
            });
        }

        @Override
        public void tableChanged(TableModelEvent e) {
            // any data/structure change ⇒ resize (debounced)
            schedule();
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("model".equals(evt.getPropertyName())) {
                Object oldM = evt.getOldValue();
                Object newM = evt.getNewValue();
                if (oldM instanceof TableModel) {
                    ((TableModel) oldM).removeTableModelListener(this);
                }
                if (newM instanceof TableModel) {
                    ((TableModel) newM).addTableModelListener(this);
                }
                schedule();
            }
        }

        private void schedule() {
            if (debounce == null) {
                debounce = new Timer(DEBOUNCE_MS, e -> SwingUtilities.invokeLater(() -> resizeNow(table)));
                debounce.setRepeats(false);
            }
            debounce.restart();
        }
    }

    // --- Demo main (optional) ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Auto-resize JTable on data change");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Name","Occupation","Country"}, 0);
            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            TableAutoResizer.attach(table);

            // sample data + a button to mutate data to see auto-resize
            JButton add = new JButton("Add Random Row");
            add.addActionListener(e -> model.addRow(new Object[]{
                    model.getRowCount()+1,
                    "Alice " + System.nanoTime(),
                    "Senior Principal Software Engineer (Platform & Tooling, JVM/Swing)",
                    "United States of America"
            }));

            f.add(new JScrollPane(table), BorderLayout.CENTER);
            f.add(add, BorderLayout.SOUTH);
            f.setSize(800, 400);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
