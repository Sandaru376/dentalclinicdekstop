package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/** Shared visual styling for all clinic screens. */
public final class UITheme {
    public static final Color PRIMARY = new Color(14, 165, 233);
    public static final Color PRIMARY_DARK = new Color(2, 132, 199);
    public static final Color ACCENT = new Color(56, 189, 248);
    public static final Color BACKGROUND = new Color(15, 23, 42);
    public static final Color SURFACE = new Color(30, 41, 59);
    public static final Color SURFACE_ALT = new Color(23, 35, 58);
    public static final Color INPUT_SURFACE = new Color(248, 250, 252);
    public static final Color INPUT_TEXT = new Color(15, 23, 42);
    public static final Color TEXT = new Color(248, 250, 252);
    public static final Color MUTED = new Color(203, 213, 225);
    public static final Color BORDER = new Color(51, 65, 85);
    public static final Color DANGER = new Color(248, 113, 113);

    private static final Map<String, String> ICONS = new HashMap<>();
    static {
        ICONS.put("login", "login");
        ICONS.put("book appointment", "calendar");
        ICONS.put("add slot", "calendar");
        ICONS.put("add doctor", "plus");
        ICONS.put("add reception staff", "plus");
        ICONS.put("create reception account", "plus");
        ICONS.put("create doctor account", "plus");
        ICONS.put("add", "plus");
        ICONS.put("refresh", "refresh");
        ICONS.put("search", "search");
        ICONS.put("print", "print");
        ICONS.put("print bill / token", "print");
        ICONS.put("logout", "logout");
        ICONS.put("close", "close");
        ICONS.put("delete selected", "close");
    }

    private UITheme() { }

    public static ImageIcon resourceIcon(String resourcePath, int width, int height) {
        java.net.URL location = UITheme.class.getResource(resourcePath);
        if (location == null) return null;
        Image source = new ImageIcon(location).getImage();
        Image scaled = source.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public static void setWindowIcon(Window window, String resourcePath) {
        ImageIcon icon = resourceIcon(resourcePath, 32, 32);
        if (icon != null) window.setIconImage(icon.getImage());
    }

    public static int showConfirmDialog(Component parent, String message, String title) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        JDialog dialog = pane.createDialog(parent, title);
        style(dialog);
        dialog.setResizable(false);
        dialog.setVisible(true);
        Object selected = pane.getValue();
        dialog.dispose();
        return selected instanceof Integer ? (Integer) selected : JOptionPane.CLOSED_OPTION;
    }

    public static void showMessageDialog(Component parent, String message, String title, int messageType) {
        JOptionPane pane = new JOptionPane(message, messageType, JOptionPane.DEFAULT_OPTION);
        JDialog dialog = pane.createDialog(parent, title);
        style(dialog);
        dialog.setResizable(false);
        dialog.setVisible(true);
        dialog.dispose();
    }

    public static void install() {
        Font base = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("defaultFont", base);
        UIManager.put("Label.font", base);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Button.font", base.deriveFont(Font.BOLD));
        UIManager.put("Button.background", PRIMARY);
        UIManager.put("Button.foreground", TEXT);
        UIManager.put("TextField.font", base);
        UIManager.put("TextField.background", INPUT_SURFACE);
        UIManager.put("TextField.foreground", INPUT_TEXT);
        UIManager.put("PasswordField.font", base);
        UIManager.put("PasswordField.background", INPUT_SURFACE);
        UIManager.put("PasswordField.foreground", INPUT_TEXT);
        UIManager.put("ComboBox.font", base);
        UIManager.put("ComboBox.background", INPUT_SURFACE);
        UIManager.put("ComboBox.foreground", INPUT_TEXT);
        UIManager.put("Spinner.font", base);
        UIManager.put("Spinner.background", INPUT_SURFACE);
        UIManager.put("Spinner.foreground", INPUT_TEXT);
        UIManager.put("TextArea.background", INPUT_SURFACE);
        UIManager.put("TextArea.foreground", INPUT_TEXT);
        UIManager.put("Table.font", base);
        UIManager.put("TableHeader.font", base.deriveFont(Font.BOLD));
        UIManager.put("TabbedPane.font", base.deriveFont(Font.BOLD));
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", BACKGROUND);
        UIManager.put("OptionPane.messageForeground", TEXT);
        UIManager.put("Table.background", SURFACE);
        UIManager.put("Table.foreground", TEXT);
        UIManager.put("Table.selectionBackground", new Color(22, 78, 99));
        UIManager.put("Table.selectionForeground", TEXT);
        UIManager.put("TabbedPane.selected", SURFACE);
        UIManager.put("TabbedPane.background", BACKGROUND);
        UIManager.put("TabbedPane.foreground", TEXT);
        UIManager.put("ScrollPane.background", SURFACE);
        UIManager.put("Viewport.background", SURFACE);
        UIManager.put("ToolTip.background", SURFACE);
        UIManager.put("ToolTip.foreground", TEXT);
    }

    public static void style(Window window) {
        styleComponent(window);
        if (window instanceof JFrame) ((JFrame) window).getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));
        if (window instanceof JDialog) ((JDialog) window).getRootPane().setBorder(new EmptyBorder(14, 14, 14, 14));
        if (window instanceof JDialog) {
            JRootPane root = ((JDialog) window).getRootPane();
            root.registerKeyboardAction(event -> window.dispose(),
                    KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                    JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
    }

    public static void stylePanel(JComponent component) {
        styleComponent(component);
    }

    private static void styleComponent(Component component) {
        if (component instanceof JPanel && !Boolean.TRUE.equals(
                ((JPanel) component).getClientProperty("ui.preserveBackground"))) {
            component.setBackground(BACKGROUND);
        }
        if (component instanceof JLabel && !Boolean.TRUE.equals(
                ((JLabel) component).getClientProperty("ui.preserveForeground"))) {
            ((JLabel) component).setForeground(TEXT);
        }
        if (component instanceof JCheckBox) {
            component.setBackground(BACKGROUND);
            component.setForeground(TEXT);
        }
        if (component instanceof JTextField || component instanceof JPasswordField || component instanceof JComboBox || component instanceof JSpinner) {
            if (component instanceof JComponent) ((JComponent) component).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(125, 211, 252)), new EmptyBorder(6, 8, 6, 8)));
            component.setBackground(INPUT_SURFACE);
            component.setForeground(INPUT_TEXT);
        }
        if (component instanceof JButton) styleButton((JButton) component);
        if (component instanceof JTextArea) {
            component.setBackground(INPUT_SURFACE);
            component.setForeground(INPUT_TEXT);
        }
        if (component instanceof JTable) styleTable((JTable) component);
        if (component instanceof JScrollPane) {
            component.setBackground(SURFACE);
            ((JScrollPane) component).getViewport().setBackground(SURFACE);
            ((JScrollPane) component).setBorder(BorderFactory.createLineBorder(BORDER));
        }
        if (component instanceof JTabbedPane) {
            JTabbedPane tabs = (JTabbedPane) component;
            tabs.setUI(new MedicalTabbedPaneUI());
            tabs.setBackground(SURFACE_ALT);
            tabs.setForeground(TEXT);
            tabs.setFont(tabs.getFont().deriveFont(Font.BOLD, 14f));
            tabs.setBorder(BorderFactory.createLineBorder(BORDER));
        }
        // Do not restyle the small look-and-feel arrow buttons inside these controls.
        if (component instanceof JComboBox) return;
        if (component instanceof JSpinner) {
            styleComponent(((JSpinner) component).getEditor());
            return;
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) styleComponent(child);
        }
    }

    private static void styleButton(JButton button) {
        String text = button.getText() == null ? "" : button.getText().toLowerCase();
        boolean secondary = text.contains("close") || text.contains("logout") || text.contains("delete") || text.contains("reject");
        String variant = (String) button.getClientProperty("ui.variant");
        boolean calendarDay = "calendar-day".equals(variant);
        boolean selectedDay = "calendar-selected".equals(variant);
        Color normal = selectedDay ? ACCENT : calendarDay ? SURFACE_ALT : secondary ? SURFACE : PRIMARY;
        Color hover = selectedDay ? ACCENT.brighter() : calendarDay ? BORDER : secondary ? SURFACE_ALT : PRIMARY_DARK;
        button.setUI(new BasicButtonUI());
        button.setBackground(normal);
        button.setForeground(secondary ? DANGER : TEXT);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setFont(button.getFont().deriveFont(Font.BOLD));
        if (!text.isEmpty()) {
            button.getAccessibleContext().setAccessibleName(button.getText());
            if (button.getMnemonic() == 0) button.setMnemonic(Character.toUpperCase(text.charAt(0)));
        }
        if (button.getToolTipText() == null && !text.isEmpty()) button.setToolTipText(text);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(secondary ? new Color(127, 54, 67) : calendarDay ? BORDER : PRIMARY_DARK),
                new EmptyBorder(7, 13, 7, 13)));
        String icon = ICONS.get(text);
        if (icon != null) {
            button.setIcon(new LineIcon(icon, secondary ? DANGER : TEXT));
            button.setIconTextGap(8);
        }
        if (!Boolean.TRUE.equals(button.getClientProperty("ui.hoverInstalled"))) {
            button.putClientProperty("ui.hoverInstalled", Boolean.TRUE);
            button.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent event) { if (button.isEnabled()) button.setBackground(hover); }
                @Override public void mouseExited(MouseEvent event) { button.setBackground(normal); }
            });
        }
    }

    public static void markInvalid(JComponent component, boolean invalid) {
        component.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(invalid ? DANGER : new Color(125, 211, 252), invalid ? 2 : 1),
                new EmptyBorder(6, 8, 6, 8)));
    }

    public static void addCancelFooter(JDialog dialog) {
        Container original = dialog.getContentPane();
        JPanel shell = new JPanel(new BorderLayout(0, 8));
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(event -> dialog.dispose());
        footer.setBorder(new EmptyBorder(4, 8, 4, 8));
        footer.add(cancel);
        shell.add(original, BorderLayout.CENTER);
        shell.add(footer, BorderLayout.SOUTH);
        dialog.setContentPane(shell);
    }

    public static void installEmptyState(JScrollPane scrollPane, JTable table, String message) {
        JPanel empty = new JPanel(new GridBagLayout());
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setForeground(MUTED);
        label.putClientProperty("ui.preserveForeground", Boolean.TRUE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 15f));
        empty.add(label);
        stylePanel(empty);
        Runnable update = () -> scrollPane.setViewportView(table.getRowCount() == 0 ? empty : table);
        table.getModel().addTableModelListener(event -> SwingUtilities.invokeLater(update));
        if (table.getRowSorter() != null) {
            table.getRowSorter().addRowSorterListener(event -> SwingUtilities.invokeLater(update));
        }
        update.run();
    }

    /** Stable tab rendering that does not allow the operating-system theme to hide labels. */
    private static final class MedicalTabbedPaneUI extends BasicTabbedPaneUI {
        @Override protected void installDefaults() {
            super.installDefaults();
            tabInsets = new Insets(10, 18, 10, 18);
            selectedTabPadInsets = new Insets(0, 0, 0, 0);
            tabAreaInsets = new Insets(4, 4, 0, 4);
            contentBorderInsets = new Insets(1, 1, 1, 1);
            lightHighlight = BORDER;
            shadow = BORDER;
            darkShadow = BORDER;
            focus = ACCENT;
        }

        @Override protected void paintTabArea(Graphics graphics, int placement, int selectedIndex) {
            graphics.setColor(SURFACE_ALT);
            graphics.fillRect(0, 0, tabPane.getWidth(),
                    calculateTabAreaHeight(placement, runCount, maxTabHeight));
            super.paintTabArea(graphics, placement, selectedIndex);
        }

        @Override protected void paintTabBackground(Graphics graphics, int placement, int index,
                int x, int y, int width, int height, boolean selected) {
            graphics.setColor(selected ? PRIMARY_DARK : SURFACE_ALT);
            graphics.fillRect(x, y, width, height);
        }

        @Override protected void paintTabBorder(Graphics graphics, int placement, int index,
                int x, int y, int width, int height, boolean selected) {
            graphics.setColor(selected ? ACCENT : BORDER);
            graphics.drawRect(x, y, width - 1, height - 1);
            if (selected) {
                graphics.fillRect(x + 1, y + height - 3, width - 2, 3);
            }
        }

        @Override protected void paintText(Graphics graphics, int placement, Font font,
                java.awt.FontMetrics metrics, int index, String title, Rectangle textRect, boolean selected) {
            graphics.setFont(font);
            graphics.setColor(selected ? Color.WHITE : MUTED);
            graphics.drawString(title, textRect.x, textRect.y + metrics.getAscent());
        }

        @Override protected void paintFocusIndicator(Graphics graphics, int placement, Rectangle[] rectangles,
                int index, Rectangle iconRect, Rectangle textRect, boolean selected) { }
    }

    private static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setBackground(SURFACE);
        table.setForeground(TEXT);
        table.setShowVerticalLines(false);
        table.setGridColor(BORDER);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(22, 78, 99));
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Object.class, new MedicalCellRenderer());
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 34));
        header.setBackground(PRIMARY_DARK);
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.LEFT);
                setOpaque(true);
                setBorder(new EmptyBorder(0, 8, 0, 8));
            }
            @Override public Component getTableCellRendererComponent(JTable source, Object value, boolean selected,
                    boolean focused, int row, int column) {
                super.getTableCellRendererComponent(source, value, selected, focused, row, column);
                setBackground(PRIMARY_DARK);
                setForeground(Color.WHITE);
                setFont(header.getFont());
                return this;
            }
        });
    }

    private static final class MedicalCellRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            String columnName = table.getColumnName(column).toLowerCase();
            String text = value == null ? "" : String.valueOf(value);
            setText(text);
            setBorder(new EmptyBorder(0, 8, 0, 8));
            setHorizontalAlignment(columnName.contains("amount") || columnName.contains("total") ||
                    columnName.contains("cost") || columnName.contains("fee") || columnName.contains("revenue")
                    ? SwingConstants.RIGHT : columnName.contains("time") || columnName.contains("status")
                    ? SwingConstants.CENTER : SwingConstants.LEFT);
            if (selected) {
                setBackground(new Color(22, 78, 99));
                setForeground(TEXT);
            } else if ("ACCEPTED".equalsIgnoreCase(text)) {
                setBackground(new Color(20, 83, 45));
                setForeground(new Color(187, 247, 208));
                setFont(getFont().deriveFont(Font.BOLD));
            } else if ("PENDING".equalsIgnoreCase(text)) {
                setBackground(new Color(91, 65, 18));
                setForeground(new Color(254, 240, 138));
                setFont(getFont().deriveFont(Font.BOLD));
            } else if ("REJECTED".equalsIgnoreCase(text)) {
                setBackground(new Color(92, 37, 50));
                setForeground(new Color(254, 202, 202));
                setFont(getFont().deriveFont(Font.BOLD));
            } else {
                setBackground(row % 2 == 0 ? SURFACE : SURFACE_ALT);
                setForeground(TEXT);
                setFont(table.getFont());
            }
            return this;
        }
    }

    private static final class LineIcon implements Icon {
        private final String type;
        private final Color color;
        LineIcon(String type, Color color) { this.type = type; this.color = color; }
        public int getIconWidth() { return 16; }
        public int getIconHeight() { return 16; }
        public void paintIcon(Component c, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setColor(color); g.setStroke(new BasicStroke(1.8f));
            if ("plus".equals(type)) { g.drawLine(x+8,y+3,x+8,y+13); g.drawLine(x+3,y+8,x+13,y+8); }
            else if ("calendar".equals(type)) { g.drawRoundRect(x+2,y+3,12,11,2,2); g.drawLine(x+2,y+7,x+14,y+7); g.drawLine(x+5,y+1,x+5,y+5); g.drawLine(x+11,y+1,x+11,y+5); }
            else if ("search".equals(type)) { g.drawOval(x+2,y+2,8,8); g.drawLine(x+9,y+9,x+14,y+14); }
            else if ("refresh".equals(type)) { g.drawArc(x+2,y+2,12,12,35,285); g.drawLine(x+12,y+2,x+14,y+6); g.drawLine(x+12,y+2,x+8,y+3); }
            else if ("print".equals(type)) { g.drawRect(x+4,y+1,8,5); g.drawRoundRect(x+2,y+5,12,7,2,2); g.drawRect(x+4,y+10,8,5); }
            else if ("close".equals(type)) { g.drawLine(x+3,y+3,x+13,y+13); g.drawLine(x+13,y+3,x+3,y+13); }
            else if ("logout".equals(type)) { g.drawRect(x+2,y+2,7,12); g.drawLine(x+7,y+8,x+14,y+8); g.drawLine(x+11,y+5,x+14,y+8); g.drawLine(x+11,y+11,x+14,y+8); }
            else { g.drawRect(x+2,y+3,8,11); g.drawLine(x+7,y+8,x+14,y+8); g.drawLine(x+11,y+5,x+14,y+8); g.drawLine(x+11,y+11,x+14,y+8); }
            g.dispose();
        }
    }
}
