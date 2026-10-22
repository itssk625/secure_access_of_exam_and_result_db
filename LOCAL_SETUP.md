# Local database environment variables (Windows PowerShell)

Run these in the same PowerShell terminal used to start the Spring Boot application. Replace the password placeholder locally; do not commit it or paste it into source files.

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/exam_access_control"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "YOUR_LOCAL_POSTGRES_PASSWORD"
```

Then launch the application from that terminal using your normal Maven/IDE workflow. These values are scoped to that PowerShell session. Alternatively, configure the same environment variables in your IDE run configuration.

`application.properties` intentionally requires `DB_PASSWORD`. The `.env.example` file is documentation only; Spring Boot does not load `.env` automatically.
