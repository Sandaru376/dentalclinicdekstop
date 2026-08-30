package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/** Shared visual styling for all clinic screens. */
public final class UITheme {
    public static final Color PRIMARY = new Color(20, 125, 132);
    public static final Color PRIMARY_DARK = new Color(13, 92, 99);
    public static final Color ACCENT = new Color(45, 156, 219);
    public static final Color BACKGROUND = new Color(241, 247, 248);
    public static final Color SURFACE = Color.WHITE;
    public static final Color TEXT = new Color(31, 50, 56);
    public static final Color MUTED = new Color(101, 119, 124);
    public static final Color DANGER = new Color(190, 65, 65);

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

    public static void install() {
        Font base = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("defaultFont", base);
        UIManager.put("Label.font", base);
        UIManager.put("Button.font", base.deriveFont(Font.BOLD));
        UIManager.put("TextField.font", base);
        UIManager.put("PasswordField.font", base);
        UIManager.put("ComboBox.font", base);
        UIManager.put("Spinner.font", base);
        UIManager.put("Table.font", base);
        UIManager.put("TableHeader.font", base.deriveFont(Font.BOLD));
        UIManager.put("TabbedPane.font", base.deriveFont(Font.BOLD));
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", BACKGROUND);
        UIManager.put("Table.selectionBackground", new Color(205, 236, 238));
        UIManager.put("Table.selectionForeground", TEXT);
        UIManager.put("TabbedPane.selected", SURFACE);
    }

    public static void style(Window window) {
        styleComponent(window);
        if (window instanceof JFrame) ((JFrame) window).getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));
        if (window instanceof JDialog) ((JDialog) window).getRootPane().setBorder(new EmptyBorder(14, 14, 14, 14));
    }

    public static void stylePanel(JComponent component) {
        styleComponent(component);
    }

    private static void styleComponent(Component component) {
        if (component instanceof JPanel && !Boolean.TRUE.equals(
                ((JPanel) component).getClientProperty("ui.preserveBackground"))) {
            component.setBackground(BACKGROUND);
        }
        if (component instanceof JLabel) ((JLabel) component).setForeground(TEXT);
        if (component instanceof JTextField || component instanceof JPasswordField || component instanceof JComboBox || component instanceof JSpinner) {
            if (component instanceof JComponent) ((JComponent) component).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(191, 208, 211)), new EmptyBorder(6, 8, 6, 8)));
            component.setBackground(SURFACE);
            component.setForeground(TEXT);
        }
        if (component instanceof JButton) styleButton((JButton) component);
        if (component instanceof JTextArea) {
            component.setBackground(SURFACE);
            component.setForeground(TEXT);
        }
        if (component instanceof JTable) styleTable((JTable) component);
        if (component instanceof JTabbedPane) {
            ((JTabbedPane) component).setBackground(BACKGROUND);
            ((JTabbedPane) component).setForeground(TEXT);
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) styleComponent(child);
        }
    }

    private static void styleButton(JButton button) {
        String text = button.getText() == null ? "" : button.getText().toLowerCase();
        boolean secondary = text.contains("close") || text.contains("logout") || text.contains("delete") || text.contains("reject");
        button.setBackground(secondary ? SURFACE : PRIMARY);
        button.setForeground(secondary ? DANGER : Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(secondary ? new Color(225, 178, 178) : PRIMARY_DARK),
                new EmptyBorder(7, 13, 7, 13)));
        String icon = ICONS.get(text);
        if (icon != null) {
            button.setIcon(new LineIcon(icon, secondary ? DANGER : Color.BLACK));
            button.setIconTextGap(8);
        }
    }

    private static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(225, 234, 236));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(205, 236, 238));
        table.setSelectionForeground(TEXT);
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 34));
        header.setBackground(PRIMARY_DARK);
        header.setForeground(Color.BLACK);
        header.setFont(header.getFont().deriveFont(Font.BOLD));
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
