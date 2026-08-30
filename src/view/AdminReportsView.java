package view;

import controller.ReportController;
import model.Appointment;
import model.DailyReportSummary;
import model.DailyRevenueRow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/** Administrative daily appointment and monthly billed-revenue reports. */
public class AdminReportsView extends JFrame {
    private final ReportController reportController = new ReportController();
    private final CalendarDatePicker dailyDatePicker = new CalendarDatePicker();
    private final JComboBox<String> monthCombo = new JComboBox<>();
    private final JSpinner yearSpinner = new JSpinner();
    private final JLabel totalLabel = summaryLabel("Total: 0");
    private final JLabel acceptedLabel = summaryLabel("Accepted: 0");
    private final JLabel pendingLabel = summaryLabel("Pending: 0");
    private final JLabel rejectedLabel = summaryLabel("Rejected: 0");
    private final JLabel monthlyAppointmentsLabel = summaryLabel("Accepted appointments: 0");
    private final JLabel monthlyRevenueLabel = summaryLabel("Monthly revenue: Rs. 0.00");
    private final DefaultTableModel dailyTableModel = readOnlyModel(
            "Appointment", "Time", "Patient", "Doctor", "Treatment", "Status", "Amount");
    private final DefaultTableModel revenueTableModel = readOnlyModel(
            "Date", "Accepted appointments", "Revenue");
    private final JTable dailyTable = new JTable(dailyTableModel);
    private final JTable monthlyTable = new JTable(revenueTableModel);

    public AdminReportsView() {
        setTitle("Admin Reports");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        buildInterface();
        UITheme.style(this);
        setMinimumSize(new Dimension(900, 560));
        setSize(1040, 650);
        setLocationRelativeTo(null);
        loadDailyReport();
        loadMonthlyRevenue();
    }

    private void buildInterface() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel title = new JLabel("Clinic Reports");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setForeground(UITheme.PRIMARY_DARK);
        JLabel subtitle = new JLabel("Review appointments and accepted appointment revenue");
        subtitle.setForeground(UITheme.MUTED);
        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(4));
        heading.add(subtitle);
        root.add(heading, BorderLayout.NORTH);
        JTabbedPane reportsTabs = new JTabbedPane();
        reportsTabs.addTab("Daily Appointments", createDailyPanel());
        reportsTabs.addTab("Monthly Revenue", createMonthlyPanel());
        root.add(reportsTabs, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel createDailyPanel() {
        JPanel panel = reportPanel();
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        JLabel dateLabel = new JLabel("Report date:");
        dateLabel.setFont(dateLabel.getFont().deriveFont(Font.BOLD));
        JButton loadButton = new JButton("Load Daily Report");
        loadButton.addActionListener(event -> loadDailyReport());
        filters.add(dateLabel);
        filters.add(dailyDatePicker);
        filters.add(loadButton);
        JPanel summaries = new JPanel(new GridLayout(1, 4, 12, 0));
        summaries.add(card(totalLabel, new Color(226, 238, 241)));
        summaries.add(card(acceptedLabel, new Color(215, 240, 226)));
        summaries.add(card(pendingLabel, new Color(255, 240, 204)));
        summaries.add(card(rejectedLabel, new Color(250, 221, 221)));
        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.add(filters, BorderLayout.NORTH);
        top.add(summaries, BorderLayout.CENTER);
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(dailyTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMonthlyPanel() {
        for (Month month : Month.values()) monthCombo.addItem(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        LocalDate today = LocalDate.now();
        monthCombo.setSelectedIndex(today.getMonthValue() - 1);
        yearSpinner.setModel(new SpinnerNumberModel(today.getYear(), 2020, 2100, 1));
        JPanel panel = reportPanel();
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        JButton loadButton = new JButton("Load Monthly Report");
        loadButton.addActionListener(event -> loadMonthlyRevenue());
        filters.add(new JLabel("Month:"));
        filters.add(monthCombo);
        filters.add(new JLabel("Year:"));
        filters.add(yearSpinner);
        filters.add(loadButton);
        JPanel summaries = new JPanel(new GridLayout(1, 2, 12, 0));
        summaries.add(card(monthlyAppointmentsLabel, new Color(226, 238, 241)));
        summaries.add(card(monthlyRevenueLabel, new Color(215, 240, 226)));
        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.add(filters, BorderLayout.NORTH);
        top.add(summaries, BorderLayout.CENTER);
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(monthlyTable), BorderLayout.CENTER);
        return panel;
    }

    private void loadDailyReport() {
        try {
            LocalDate date = dailyDatePicker.getSelectedDate();
            List<Appointment> appointments = reportController.dailyAppointments(date);
            DailyReportSummary summary = reportController.dailySummary(date);
            dailyTableModel.setRowCount(0);
            for (Appointment appointment : appointments) {
                dailyTableModel.addRow(new Object[]{appointment.getAppointmentNumber(),
                        appointment.getAppointmentTime(), appointment.getPatientName(), appointment.getDoctorName(),
                        appointment.getTreatmentTypeName(), appointment.getStatus(), money(appointment.getTotalCost())});
            }
            totalLabel.setText("Total: " + summary.getTotal());
            acceptedLabel.setText("Accepted: " + summary.getAccepted());
            pendingLabel.setText("Pending: " + summary.getPending());
            rejectedLabel.setText("Rejected: " + summary.getRejected());
        } catch (Exception exception) {
            showReportError(exception);
        }
    }

    private void loadMonthlyRevenue() {
        try {
            int month = monthCombo.getSelectedIndex() + 1;
            int year = (Integer) yearSpinner.getValue();
            List<DailyRevenueRow> rows = reportController.monthlyRevenue(year, month);
            revenueTableModel.setRowCount(0);
            int appointments = 0;
            double revenue = 0;
            for (DailyRevenueRow row : rows) {
                appointments += row.getAppointmentCount();
                revenue += row.getRevenue();
                revenueTableModel.addRow(new Object[]{row.getDate(), row.getAppointmentCount(), money(row.getRevenue())});
            }
            monthlyAppointmentsLabel.setText("Accepted appointments: " + appointments);
            monthlyRevenueLabel.setText("Monthly revenue: " + money(revenue));
        } catch (Exception exception) {
            showReportError(exception);
        }
    }

    private void showReportError(Exception exception) {
        String message = exception.getMessage();
        if (message == null || message.trim().isEmpty()) message = "The report could not be loaded.";
        JOptionPane.showMessageDialog(this, message, "Report Error", JOptionPane.ERROR_MESSAGE);
    }

    private static JPanel reportPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBorder(new EmptyBorder(16, 10, 10, 10));
        return panel;
    }

    private static JLabel summaryLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 17f));
        return label;
    }

    private static JPanel card(JLabel label, Color background) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(background);
        card.putClientProperty("ui.preserveBackground", Boolean.TRUE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(background.darker()), new EmptyBorder(18, 10, 18, 10)));
        card.add(label, BorderLayout.CENTER);
        return card;
    }

    private static DefaultTableModel readOnlyModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private static String money(double amount) { return String.format("Rs. %,.2f", amount); }
}
