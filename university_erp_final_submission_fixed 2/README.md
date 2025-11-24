
University ERP — Complete Project (following assignment)

How to run (quick):
1. Install MySQL and create user with privileges.
2. Copy src/main/resources/application.properties.example -> src/main/resources/application.properties and fill DB credentials.
3. From project root run: mvn -DskipTests package
4. Run: java -cp target/classes edu.univ.erp.ui.Main
   On first run the app will execute SQL schemas and seed data with bcrypt-hashed passwords.

Seeded accounts (created at first run):
- admin1 / adminpass
- inst1 / instpass
- stu1 / stupass
- stu2 / stupass2

SQL files are in the project/sql folder.



# UI Merge
The full multiple-window UI files have been merged into `src/main/java/edu/univ/erp/ui`.
See `docs/ui_pom_reference.xml` for the UI-only pom and `docs/images` for the included screenshot.
