# Existing PostgreSQL schema verification

Do not change `Marks.markId` generation, rename columns, or switch Hibernate to `validate` until the existing database has been inspected.

1. Open pgAdmin Query Tool for `exam_access_control` (or connect with `psql`).
2. Run `SCHEMA_VERIFICATION.sql` and save the results for tables/columns, constraints, sequence defaults, role/permission mappings, and status values.
3. Compare those results with the `@Table`, `@Column`, `@Id`, `@GeneratedValue`, and relationship annotations in `src/main/java/.../entity`.
4. In particular, confirm whether `marks.mark_id` has a database default or sequence and whether `exams.exam_id` uses `exams_exam_id_seq`.
5. Only after the mappings match the existing schema, set `DDL_AUTO=validate` in the same shell/IDE environment and start the app. If validation fails, record the exact mismatch; do not switch back to `update` just to hide it.

PowerShell example for a validation-only startup:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/exam_access_control"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "YOUR_LOCAL_POSTGRES_PASSWORD"
$env:DDL_AUTO = "validate"
.\mvnw.cmd test
```

On Windows PowerShell, use `.[0m\mvnw.cmd test` instead of `.\mvnw.cmd test` if the wrapper is executable in your environment. Unset `DDL_AUTO` or set it back to `update` only if you deliberately choose to keep the current temporary development behavior after reviewing the schema.
