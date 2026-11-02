# Expense Tracker (Spring Boot + MySQL)

Features: add / edit / delete expenses, monthly filter, monthly total, category-wise totals.

## Run locally
1. Install Java 17, Maven, MySQL.
2. Edit DB username/password in `src/main/resources/application.properties` (default: root / root).
   The database `expense_db` is created automatically.
3. Run:  `mvn spring-boot:run`
4. Open http://localhost:8080

## Deploy (Railway example)
1. Push this project to a GitHub repo.
2. railway.app -> New Project -> Deploy from GitHub repo (it detects the Dockerfile).
3. In the same project: New -> Database -> MySQL.
4. In your app service -> Variables, add:
   DB_URL       = jdbc:mysql://<MYSQLHOST>:<MYSQLPORT>/<MYSQLDATABASE>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   DB_USERNAME  = <MYSQLUSER>
   DB_PASSWORD  = <MYSQLPASSWORD>
   (copy the values from the MySQL service's Variables tab)
5. Settings -> Networking -> Generate Domain. Open the URL.
