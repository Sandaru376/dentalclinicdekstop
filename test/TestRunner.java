import controller.AppointmentSearchController;
import controller.DoctorController;
import java.sql.Time;
import util.PasswordUtil;

/**
 * Simple Java-core test runner for validation and password functions.
 * It does not require MySQL. In NetBeans, right-click this file and Run File.
 */
public class TestRunner {

    static int totalTests = 0;
    static int passedTests = 0;

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("     AUTOMATED CORE FUNCTION TESTING");
        System.out.println("======================================");

        String passwordHash = PasswordUtil.hash("admin123");
        runTest("Password is hashed consistently",
                PasswordUtil.hash("admin123").equals(passwordHash), true);
        runTest("Correct password is accepted",
                PasswordUtil.matches("admin123", passwordHash), true);
        runTest("Wrong password is rejected",
                PasswordUtil.matches("wrong123", passwordHash), false);

        AppointmentSearchController searchController = new AppointmentSearchController();
        runTest("Empty appointment number is rejected",
                throwsMessage(() -> searchController.search(""),
                        "Please enter an appointment number."), true);
        runTest("Null appointment number is rejected",
                throwsMessage(() -> searchController.search(null),
                        "Please enter an appointment number."), true);

        DoctorController doctorController = new DoctorController();
        runTest("End time before start time is rejected",
                throwsMessage(() -> doctorController.addSlot(1, null,
                        Time.valueOf("10:00:00"), Time.valueOf("09:00:00")),
                        "End time must be after start time"), true);
        runTest("Equal start and end time is rejected",
                throwsMessage(() -> doctorController.addSlot(1, null,
                        Time.valueOf("10:00:00"), Time.valueOf("10:00:00")),
                        "End time must be after start time"), true);

        System.out.println();
        System.out.println("======================================");
        System.out.println("              SUMMARY");
        System.out.println("======================================");
        System.out.println("Total Tests  : " + totalTests);
        System.out.println("Passed Tests : " + passedTests);
        System.out.println("Failed Tests : " + (totalTests - passedTests));
        System.out.println("======================================");
    }

    public static void runTest(String testName, boolean actual, boolean expected) {
        totalTests++;
        System.out.println();
        System.out.println("Test Case: " + testName);
        System.out.println("Expected  : " + expected);
        System.out.println("Actual    : " + actual);

        if (actual == expected) {
            passedTests++;
            System.out.println("Result    : PASS");
        } else {
            System.out.println("Result    : FAIL");
        }
    }

    private static boolean throwsMessage(Runnable action, String expectedMessage) {
        try {
            action.run();
            return false;
        } catch (IllegalArgumentException exception) {
            return expectedMessage.equals(exception.getMessage());
        }
    }
}
