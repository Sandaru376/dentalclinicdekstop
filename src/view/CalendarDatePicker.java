package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/** A compact field that opens a familiar month calendar. */
public class CalendarDatePicker extends JPanel {
    private static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");
    private final JTextField display = new JTextField();
    private LocalDate selectedDate = LocalDate.now();

    public CalendarDatePicker() {
        setLayout(new BorderLayout(6, 0));
        setOpaque(false);
        display.setEditable(false);
        display.setFocusable(false);
        display.setText(DISPLAY.format(selectedDate));
        display.setToolTipText("Click the calendar button to choose a date");

        JButton calendarButton = new JButton("Choose date");
        calendarButton.setToolTipText("Open calendar");
        calendarButton.addActionListener(e -> showCalendar());
        add(display, BorderLayout.CENTER);
        add(calendarButton, BorderLayout.EAST);
        setPreferredSize(new Dimension(310, 36));
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    private void showCalendar() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Choose a date", Dialog.ModalityType.APPLICATION_MODAL);
        CalendarPanel calendar = new CalendarPanel(dialog, selectedDate);
        dialog.setContentPane(calendar);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private final class CalendarPanel extends JPanel {
        private final JDialog dialog;
        private YearMonth shownMonth;
        private final JLabel monthTitle = new JLabel("", SwingConstants.CENTER);
        private final JPanel days = new JPanel(new GridLayout(0, 7, 4, 4));

        CalendarPanel(JDialog dialog, LocalDate initial) {
            this.dialog = dialog;
            this.shownMonth = YearMonth.from(initial);
            setLayout(new BorderLayout(8, 8));
            setBorder(new EmptyBorder(12, 12, 12, 12));

            JButton previous = new JButton("<");
            JButton next = new JButton(">");
            previous.setToolTipText("Previous month");
            next.setToolTipText("Next month");
            previous.addActionListener(e -> { shownMonth = shownMonth.minusMonths(1); rebuildDays(); });
            next.addActionListener(e -> { shownMonth = shownMonth.plusMonths(1); rebuildDays(); });
            monthTitle.setFont(monthTitle.getFont().deriveFont(Font.BOLD, 17f));

            JPanel heading = new JPanel(new BorderLayout(8, 0));
            heading.add(previous, BorderLayout.WEST);
            heading.add(monthTitle, BorderLayout.CENTER);
            heading.add(next, BorderLayout.EAST);
            add(heading, BorderLayout.NORTH);
            add(days, BorderLayout.CENTER);
            rebuildDays();
        }

        private void rebuildDays() {
            days.removeAll();
            monthTitle.setText(shownMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
            String[] headings = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            for (String heading : headings) {
                JLabel label = new JLabel(heading, SwingConstants.CENTER);
                label.setFont(label.getFont().deriveFont(Font.BOLD));
                label.setForeground(UITheme.MUTED);
                days.add(label);
            }

            LocalDate first = shownMonth.atDay(1);
            int emptyCells = first.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            for (int i = 0; i < emptyCells; i++) days.add(new JLabel());

            for (int day = 1; day <= shownMonth.lengthOfMonth(); day++) {
                LocalDate value = shownMonth.atDay(day);
                JButton button = new JButton(String.valueOf(day));
                button.setMargin(new Insets(7, 9, 7, 9));
                button.setEnabled(!value.isBefore(LocalDate.now()));
                if (value.equals(selectedDate)) {
                    button.setBackground(UITheme.ACCENT);
                    button.setFont(button.getFont().deriveFont(Font.BOLD));
                }
                button.addActionListener(e -> {
                    selectedDate = value;
                    display.setText(DISPLAY.format(selectedDate));
                    dialog.dispose();
                });
                days.add(button);
            }
            days.revalidate();
            days.repaint();
            dialog.pack();
        }
    }
}
