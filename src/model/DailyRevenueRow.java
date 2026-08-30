/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

public class DailyRevenueRow {
    private final Date date;
    private final int appointmentCount;
    private final double revenue;

    public DailyRevenueRow(Date date, int appointmentCount, double revenue) {
        this.date = date;
        this.appointmentCount = appointmentCount;
        this.revenue = revenue;
    }

    public Date getDate() { return date; }
    public int getAppointmentCount() { return appointmentCount; }
    public double getRevenue() { return revenue; }
}
