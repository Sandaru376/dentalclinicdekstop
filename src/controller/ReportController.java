/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ReportDAO;
import model.Appointment;
import model.DailyReportSummary;
import model.DailyRevenueRow;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class ReportController {
    private final ReportDAO reportDAO = new ReportDAO();

    public List<Appointment> dailyAppointments(LocalDate date) {
        return reportDAO.findAppointmentsByDate(Date.valueOf(date));
    }

    public DailyReportSummary dailySummary(LocalDate date) {
        return reportDAO.getDailySummary(Date.valueOf(date));
    }

    public List<DailyRevenueRow> monthlyRevenue(int year, int month) {
        YearMonth selected = YearMonth.of(year, month);
        return reportDAO.getMonthlyRevenue(Date.valueOf(selected.atDay(1)),
                Date.valueOf(selected.plusMonths(1).atDay(1)));
    }
}
