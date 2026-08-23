package test_case;

import db.DBConnection;

import java.sql.Connection;

/**
 * Quick manual sanity check: run this file (Right click -> Run File in NetBeans)
 * to confirm the app can reach your MySQL Workbench database before you
 * launch the full Swing app from Main.java.
 */
public class Test_case {
    public static void main(String[] args) {
        System.out.println("Testing MySQL connection...");
        try (Connection con = DBConnection.getConnection()) {
            System.out.println("SUCCESS: connected to " + con.getCatalog());
        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage());
            System.out.println("Check: is MySQL running, did you run sql/schema.sql, " +
                    "and does db/DBConnection.java have the right username/password?");
        }
    }
}
