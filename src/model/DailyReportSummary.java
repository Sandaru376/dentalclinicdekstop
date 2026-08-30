/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class DailyReportSummary {
    private final int total;
    private final int accepted;
    private final int pending;
    private final int rejected;

    public DailyReportSummary(int total, int accepted, int pending, int rejected) {
        this.total = total;
        this.accepted = accepted;
        this.pending = pending;
        this.rejected = rejected;
    }

    public int getTotal() { return total; }
    public int getAccepted() { return accepted; }
    public int getPending() { return pending; }
    public int getRejected() { return rejected; }
}
