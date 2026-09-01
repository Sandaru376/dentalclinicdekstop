# Sunrise Dental Clinic Management System

A Core Java desktop application for managing appointments at Sunrise Dental
Clinic. The system replaces paper-based appointment records with a secure,
user-friendly Java Swing application connected to a MySQL database.

## Main features

- Secure login for authorised staff
- Role-based access for administrators, reception staff, and doctors
- Create appointments with an automatically generated appointment number
- Select a doctor, treatment type, date, and time for each appointment
- Search for an appointment using its appointment number
- Approve or reject appointments and generate a token number
- Calculate the bill using the doctor consultation fee and treatment cost
- Display and print a bill/receipt
- Manage doctors, reception users, treatment types, prices, and doctor availability
- View daily appointment and monthly revenue reports
- Help section with instructions for reception staff

## Technologies used

- Java 11 or later
- Java Swing for the desktop user interface
- MySQL database
- JDBC / MySQL Connector/J for database connectivity
- NetBeans IDE and Ant project structure

## Design approach

The project uses a simple layered design:

- `view` — Java Swing forms and user interfaces
- `controller` — application rules and validation
- `dao` — database access using SQL and JDBC
- `model` — Java objects such as `User`, `Appointment`, `Schedule`, and `TreatmentType`
- `db` — MySQL connection configuration
- `test` — simple automated Core Java test runners

This separation keeps the user interface independent from database queries and
makes the system easier to maintain and test.

## User roles

| Role | Main permissions |
|---|---|
| ADMIN | Create doctor and reception accounts, manage treatment types and prices, change doctor availability, view reports. |
| RECEPTION | Book appointments, search appointments, check appointment status, and print bills/tokens. |
| DOCTOR | View assigned appointments, approve or reject appointments, manage profile details and time slots. |

## Setup and run

1. Open MySQL Workbench and run [`sql/schema.sql`](sql/schema.sql).
2. Add the MySQL Connector/J JAR in NetBeans: **Project Properties → Libraries → Compile → Add JAR/Folder**.
3. If required, update the MySQL username and password in `src/db/DBConnection.java`.
4. Open this folder as a project in NetBeans.
5. Right-click the project and choose **Run**.

The default administrator account is:

```text
Username: admin
Password: admin123
```

## Demonstration flow

1. Log in as the administrator and create a doctor and reception account.
2. Log in as reception and book an appointment for a patient.
3. Log in as the doctor and approve the appointment. The system generates a token number and calculates the bill total.
4. Log in as reception, search for the appointment, and print the bill/token.

## Testing

The project contains simple automated test runners written using Core Java.
They print each test case, expected result, actual result, and a PASS/FAIL
summary in the NetBeans Output window.

| File | Purpose | Database required? |
|---|---|---|
| `test/TestRunner.java` | Tests password handling, appointment-search validation, and schedule-time validation. | No |
| `test/LoginTestRunner.java` | Tests valid login, incorrect password, and unknown-user login. | Yes |
| `test_case/Test_case.java` | Checks that the application can connect to MySQL. | Yes |

To run a test, right-click the required `.java` file in NetBeans and choose
**Run File**, then view the bottom **Output** window.

## Project structure

```text
src/
  controller/   Application logic
  dao/          Database queries
  db/           Database connection
  model/        Application data objects
  view/         Java Swing interfaces
  util/         Utility classes
sql/            Database schema and sample data
test/           Automated test runners
test_case/      Database connection test
```
