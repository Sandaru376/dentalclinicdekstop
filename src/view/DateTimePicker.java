package view;

import javax.swing.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

/** Native Swing date/time input that avoids fragile format typing. */
public final class DateTimePicker {
    private DateTimePicker() { }

    public static CalendarDatePicker datePicker() {
        return new CalendarDatePicker();
    }

    public static JComboBox<LocalTime> timePicker(int selectedHour, int selectedMinute) {
        JComboBox<LocalTime> picker = new JComboBox<>();
        for (int hour = 7; hour <= 20; hour++) {
            for (int minute = 0; minute < 60; minute += 15) picker.addItem(LocalTime.of(hour, minute));
        }
        picker.setRenderer(new DefaultListCellRenderer() {
            private final java.time.format.DateTimeFormatter format = java.time.format.DateTimeFormatter.ofPattern("hh:mm a");
            @Override public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean selected, boolean focus) {
                return super.getListCellRendererComponent(list,
                        value instanceof LocalTime ? ((LocalTime) value).format(format) : value, index, selected, focus);
            }
        });
        picker.setSelectedItem(LocalTime.of(selectedHour, selectedMinute));
        picker.setToolTipText("Choose an available time");
        return picker;
    }

    public static Date sqlDate(CalendarDatePicker picker) {
        return Date.valueOf(picker.getSelectedDate());
    }

    public static Time sqlTime(JComboBox<LocalTime> picker) {
        LocalTime local = (LocalTime) picker.getSelectedItem();
        if (local == null) throw new IllegalArgumentException("Please choose a time.");
        return Time.valueOf(local);
    }
}
