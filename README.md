# Dental Clinic Desktop (NetBeans / Java Swing / MySQL)

Converted from the Spring Boot REST version into a plain Java Swing desktop
app matching your assignment's folder structure (`controller/dao/db/model/view/test_case`).

## Roles (no patient login — reception books for walk-ins)
- **ADMIN** — creates doctor & reception login accounts, manages treatment types/prices, can toggle doctor availability.
- **RECEPTION** — books appointments for walk-in patients, tracks status, prints the bill/token once a doctor approves.
- **DOCTOR** — sees the appointments reception booked for them, approves (auto-generates the token number) or rejects, manages their own available time slots.

## Setup
1. **Database**: Open MySQL Workbench, run `sql/schema.sql`. It creates the
   `dental_clinic` database, all tables, a default admin login
   (`admin` / `admin123`), and some starter treatment types.
2. **MySQL driver jar**: Download `mysql-connector-j` (e.g. from
   dev.mysql.com or Maven Central — I couldn't bundle it here due to network
   restrictions in this environment). In NetBeans: right-click the project →
   Properties → Libraries → Compile tab → Add JAR/Folder → select the jar.
3. **DB credentials**: Edit `src/db/DBConnection.java` if your MySQL
   Workbench root password isn't `1234`.
4. **Open in NetBeans**: File → Open Project → select this folder. NetBeans
   will generate `nbproject/build-impl.xml` automatically the first time.
5. **Run**: Right-click the project → Run (runs `Main.java` → opens the
   Login screen).
6. **Sanity check the DB connection first** (optional): right-click
   `test_case/Test_case.java` → Run File.

## Login screens / drag-and-drop note
Every screen in `src/view/` is a `JFrame`/`JDialog` built in Java code
(`GridBagLayout`/`GridLayout`/`BorderLayout`), not a paired `.form` file —
that's the only piece I couldn't safely hand-generate, since NetBeans's
`.form` XML has to exactly match what its GUI builder expects and I have no
way to verify that without the actual IDE. Everything still compiles and
runs as-is. If you specifically need the drag-and-drop **Design** tab to work
for a screen:
1. In NetBeans: File → New File → Swing GUI Forms → JFrame Form.
2. Drag on the same components listed in that view's class comment.
3. Copy the business-logic lines (everything inside the button's
   `ActionListener`/`actionPerformed`) from my `.java` file into the
   generated one.
The `controller/`, `dao/`, `db/`, and `model/` layers don't change either way.

## Typical flow to demo
1. Log in as `admin` / `admin123` → add a doctor and a reception account (Doctor tab / Reception tab).
2. Log out, log in as the reception account → "Book Appointment" for a walk-in patient.
3. Log in as the doctor account → "My Appointments" → Approve (this generates the token).
4. Log back in as reception → select that appointment → "Print Bill / Token".
