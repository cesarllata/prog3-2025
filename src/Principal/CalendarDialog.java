package Principal;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Diálogo simple de calendario para seleccionar una fecha.
 */
public class CalendarDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private LocalDate selectedDate;
    private YearMonth currentMonth;
    private JPanel daysPanel;
    private JLabel monthLabel;

    public CalendarDialog(Frame parent) {
        super(parent, "Seleccionar fecha", true);
        currentMonth = YearMonth.now();
        initComponents();
        setSize(340, 320);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new BorderLayout());
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prev = new JButton("<");
        JButton next = new JButton(">");
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(monthLabel.getFont().deriveFont(Font.BOLD, 14f));
        nav.add(prev);
        nav.add(monthLabel);
        nav.add(next);
        top.add(nav, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);

        daysPanel = new JPanel(new GridLayout(0, 7));
        add(daysPanel, BorderLayout.CENTER);

        prev.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            rebuildCalendar();
        });
        next.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            rebuildCalendar();
        });

        rebuildCalendar();
    }

    private void rebuildCalendar() {
        daysPanel.removeAll();

        // Day names
        for (DayOfWeek dow : DayOfWeek.values()) {
            JLabel lbl = new JLabel(dow.getDisplayName(TextStyle.SHORT, new Locale("es")), SwingConstants.CENTER);
            lbl.setForeground(Color.DARK_GRAY);
            daysPanel.add(lbl);
        }

        YearMonth ym = currentMonth;
        monthLabel.setText(ym.getMonth().getDisplayName(TextStyle.FULL, new Locale("es")) + " " + ym.getYear());

        LocalDate firstOfMonth = ym.atDay(1);
        int firstDayIndex = firstOfMonth.getDayOfWeek().getValue(); // 1=Mon .. 7=Sun

        // We want week to start on Monday; firstDayIndex-1 empty slots
        int emptySlots = firstDayIndex - 1;
        for (int i = 0; i < emptySlots; i++) {
            daysPanel.add(new JLabel(""));
        }

        int daysInMonth = ym.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            final int d = day;
            JButton btn = new JButton(String.valueOf(day));
            btn.setMargin(new Insets(2, 2, 2, 2));
            btn.addActionListener(e -> {
                selectedDate = LocalDate.of(ym.getYear(), ym.getMonth(), d);
                dispose();
            });
            daysPanel.add(btn);
        }

        // Fill remaining cells to keep grid consistent
        int totalCells = emptySlots + daysInMonth;
        int remainder = totalCells % 7;
        if (remainder != 0) {
            int toAdd = 7 - remainder;
            for (int i = 0; i < toAdd; i++) daysPanel.add(new JLabel(""));
        }

        daysPanel.revalidate();
        daysPanel.repaint();
    }

    /**
     * Muestra el diálogo y devuelve la fecha seleccionada (o null si se canceló).
     */
    public LocalDate selectDate() {
        setVisible(true);
        return selectedDate;
    }
}
