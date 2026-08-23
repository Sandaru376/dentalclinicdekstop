import controller.LoginController;
import model.User;

/**
 * Simple hand-rolled tests (no JUnit jar required). Run LoginTestRunner's
 * main() method. Requires the DB to be set up with sql/schema.sql first,
 * since it logs in against the real default admin account.
 */
public class LoginControllerTest {

    private int passed = 0;
    private int failed = 0;

    public void runAll() {
        testValidAdminLogin();
        testWrongPassword();
        testUnknownUsername();
        System.out.println("\nResults: " + passed + " passed, " + failed + " failed");
    }

    private void testValidAdminLogin() {
        LoginController controller = new LoginController();
        User user = controller.login("admin", "admin123");
        check("Valid admin login should succeed", user != null && "ADMIN".equals(user.getRole()));
    }

    private void testWrongPassword() {
        LoginController controller = new LoginController();
        User user = controller.login("admin", "wrong-password");
        check("Wrong password should fail", user == null);
    }

    private void testUnknownUsername() {
        LoginController controller = new LoginController();
        User user = controller.login("no_such_user", "whatever");
        check("Unknown username should fail", user == null);
    }

    private void check(String description, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS - " + description);
        } else {
            failed++;
            System.out.println("FAIL - " + description);
        }
    }
}
