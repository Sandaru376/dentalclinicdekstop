import controller.LoginController;
import model.User;

/**
 * Simple Java-core automated test runner.
 * Run this file in NetBeans after MySQL is running and schema.sql is imported.
 */
public class LoginTestRunner {

    static int totalTests = 0;
    static int passedTests = 0;

    public static void main(String[] args) {
        LoginController controller = new LoginController();

        System.out.println("======================================");
        System.out.println("       AUTOMATED LOGIN TESTING");
        System.out.println("======================================");

        runTest("Valid Admin Login", controller.login("admin", "admin123"), true);
        runTest("Invalid Password", controller.login("admin", "wrong123"), false);
        runTest("Non-existing User", controller.login("unknown", "password123"), false);

        System.out.println();
        System.out.println("======================================");
        System.out.println("              SUMMARY");
        System.out.println("======================================");
        System.out.println("Total Tests  : " + totalTests);
        System.out.println("Passed Tests : " + passedTests);
        System.out.println("Failed Tests : " + (totalTests - passedTests));
        System.out.println("======================================");
    }

    public static void runTest(String testName, User user, boolean expectedLoginSuccess) {
        totalTests++;
        boolean actualLoginSuccess = user != null;

        System.out.println();
        System.out.println("Test Case: " + testName);
        System.out.println("Expected  : " + loginResult(expectedLoginSuccess));
        System.out.println("Actual    : " + loginResult(actualLoginSuccess));

        if (actualLoginSuccess == expectedLoginSuccess) {
            passedTests++;
            System.out.println("Result    : PASS");
        } else {
            System.out.println("Result    : FAIL");
        }
    }

    private static String loginResult(boolean successful) {
        return successful ? "LOGIN_SUCCESS" : "INVALID_CREDENTIALS";
    }
}
