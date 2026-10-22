# Implementation Blueprint Alignment Audit

Baseline: uploaded `examaccesss(2).zip` and `Secure_Exam_Result_Access_Control_Implementation_Blueprint_Updated(4).docx`.

## Fixes applied in this pass

1. **Authentication identity endpoint matcher:** `/api/auth/me` is explicitly authenticated before the broader `/api/auth/**` public matcher.
2. **Student result privacy:** individual result retrieval now checks ownership and returns `404` for non-published results; the student result list filters to `PUBLISHED` results.
3. **Course ID validation:** exam create/update now check `CourseRepository.existsById(...)` and return `400 Bad Request` for a nonexistent course ID. The database foreign key remains a second line of integrity protection.
4. **Configuration hygiene:** removed hardcoded database credentials from `application.properties`, switched to `DB_URL`, `DB_USERNAME`, and required `DB_PASSWORD` environment variables, and disabled `spring.jpa.show-sql`.
5. **Repository consistency:** removed duplicate imports in `ResultRepository` and added the status-filtered derived query method.
6. **Packaging hygiene:** compiled `target/` artifacts from the incoming ZIP are excluded from the updated source package so stale classes and embedded configuration are not distributed.
7. **Result-ID enumeration resistance:** a student requesting another student's result now receives `404 Not Found`, matching the response for a nonexistent result, rather than confirming that the result ID exists.
8. **Update-exam error ordering:** the endpoint checks that the exam exists before validating the replacement course, so a nonexistent exam consistently returns `404` instead of being masked by a course validation error.

## Verification performed

- Static source inspection confirms the above changes are present.
- Static search confirms the prior literal database password is not present in source configuration.
- ZIP integrity and source inventory are checked after packaging.

## Verification not performed

- `bash mvnw test -q` was attempted, but the Maven Wrapper could not download the Maven distribution from Maven Central in this environment. Therefore the build and automated tests did not run.
- Live PostgreSQL integration tests and Postman tests were not run because the local database and credentials are not available here.
- The student published-result success case remains pending until a valid published result exists. Draft-result denial/list filtering can be tested with the current data.

## Important remaining gaps against the full blueprint

The uploaded project is a partial implementation, not the full blueprint. The following are not implemented in the uploaded source and were not invented as part of this corrective pass:

- Dedicated `AuthorizationService` / policy layer with role, resource, context, and workflow decisions.
- HOD department-scoped result review, approval and rejection.
- Exam Cell verification and publication workflow / state transitions.
- Marks creation, editing, submission APIs and their state/assignment rules.
- Controlled result correction request and approval workflow.
- Audit log entity/service/table integration and logging of sensitive/denied operations.
- HMAC result-integrity generation, verification, tamper detection, and audit events.
- Admin user/role management and audit-log APIs.
- Complete 14-table schema/entity/mapping reconciliation and schema migration strategy.
- HTTPS/TLS deployment configuration.
- CSRF protection for cookie-based browser sessions. The current configuration is stateless HTTP Basic; confirm the intended authentication mode before changing CSRF/session behavior.
- Full security negative tests and end-to-end test coverage.

## Policy items intentionally not guessed

- **Exam retrieval:** currently requires authentication globally. The blueprint lists `GET /api/exams/{id}` but does not define a dedicated `VIEW_EXAM` permission; no new permission name was invented.
- **Course reassignment:** update currently permits changing a course for a planned exam. The blueprint does not explicitly prohibit reassignment, so this was not blocked without a requirement.
- **Hibernate schema mode:** `ddl-auto=update` was retained because the real schema has not been reconciled end-to-end in this environment. Review it against the finalized schema before changing to `validate`.
- **Student courses:** current implementation derives courses from results, while the blueprint describes enrolled courses. The current database/code does not establish a separate enrollment mapping, so this remains a design/schema gap to resolve.

## Local run setup

Set these environment variables in the same terminal or IDE run configuration used to launch Spring Boot:

- `DB_URL` (optional; default is `jdbc:postgresql://localhost:5432/exam_access_control`)
- `DB_USERNAME` (optional; default is `postgres`)
- `DB_PASSWORD` (required; set your local PostgreSQL password)

`.env.example` is a reference only. Spring Boot does not automatically load a `.env` file unless the chosen IDE/tool is configured to do so.

## Follow-up corrective pass (2026-10-22)

Additional changes made against the original five-batch checklist:

1. **Test endpoint isolation:** `/api/test/me` and `/api/test/enter-marks` were moved to `DevTestController`, annotated with `@Profile("dev")`. They are not registered under the default profile. `/api/auth/me` remains the production current-user endpoint and is explicitly authenticated in `SecurityConfig`.
2. **Exam retrieval policy documented and explicit:** `GET /api/exams/{id}` now has an explicit `isAuthenticated()` method guard and a source comment. The approved permission list contains no `VIEW_EXAM`; the interim policy is therefore that any authenticated user can read exam metadata. No new permission was invented. The team should approve a narrower policy if required.
3. **API endpoint documentation:** see `API_ENDPOINTS.md` for the distinction between single-result retrieval and a student's published-results list, current exam API access policy, and student-course query limitation.
4. **Ignore rules checked:** `.gitignore` ignores `target/`, `.env`, `.env.*` while allowing `.env.example`, and local application config. This checks the current ignore file only; it does not audit Git history.

Still deliberately unresolved because evidence/decisions are required:
- Live PostgreSQL schema/sequence/constraint verification and whether `ddl-auto` can safely change to `validate`.
- Resolved in the Batch 2 follow-up: reassignment is allowed only while an exam is `PLANNED`, and the target course must exist.
- Runtime tests/build/Postman regression tests; Maven Wrapper download was unavailable in the environment.
- Git-history secret scan and rotation confirmation.

## Follow-up verification pass (2026-10-22, continued)

Additional source-level changes:

1. Removed the blanket `.requestMatchers("/api/auth/**").permitAll()` rule. No public authentication endpoints are implemented in this HTTP Basic application, so all current endpoints now fall through to authentication unless a narrower rule is explicitly added later.
2. Made the schema mode configurable with `DDL_AUTO` while retaining `update` as the temporary default. After reviewing the output of `SCHEMA_VERIFICATION.sql` and reconciling mappings, set `DDL_AUTO=validate` for a non-mutating schema check.
3. Added `SCHEMA_VERIFICATION.sql` with read-only queries for columns/defaults, constraints, sequence ownership, roles/permissions and existing status values. Run it against the actual database before changing mappings or ID generation.
4. Added unit tests for `ExamRequest` bean validation and exam time ordering, including equal/reversed times and optional null times.
5. Updated API documentation with current status and mapping caveats.

Verification attempted again: `bash mvnw test -q` still fails before Maven starts because the wrapper cannot download Maven 3.10.0 from Maven Central in this environment. Therefore the new tests are added but not executed here. Live schema, Postman behavior, and Git-history secret scanning are still pending.


## Batch 1 focused verification pass (2026-10-22)

Scope: only the original Batch 1 configuration/immediate-bug checklist.

- **Database schema / `ddl-auto`:** no live database connection or schema output was available here. `DDL_AUTO` is configurable; default remains `update` until the read-only schema diagnostic is run and mappings reconciled. Do not set `validate` blindly.
- **`/api/auth/me`:** the broad public `/api/auth/**` matcher is removed. All current endpoints require authentication unless a narrower explicit rule is added. Added `SecurityConfigTests` asserting unauthenticated requests to `/api/auth/me` and `/api/test/me` receive `401`. These tests are written but have not executed because Maven cannot be downloaded.
- **Student unpublished results:** source checks confirm individual retrieval returns `404` for a non-published result and the list query filters to `PUBLISHED`. Runtime tests still require a running app/database and suitable records.
- **Database credentials:** source configuration reads `DB_PASSWORD` from the environment without a hardcoded default. Static scan of the packaged source found no previous literal credential. Git history and actual credential rotation cannot be checked from this ZIP.
- **SQL logging:** `spring.jpa.show-sql=false` is present. No guarantee is made about other logging configuration outside the supplied source.

### Batch 1 status

Applied in source: authentication matcher, published-result restrictions, environment-based DB password, disabled SQL statement printing, configurable schema mode.
Pending external verification: live schema reconciliation and `validate` compatibility, runtime `401` tests, result endpoint integration tests, Git history/secret exposure audit and credential rotation confirmation.


## Batch 2 focused pass (2026-10-22)

- Reviewed existing `POST /api/exams`, `PUT /api/exams/{id}`, and `GET /api/exams/{id}`. The read endpoint explicitly requires authentication; the current interim policy is any authenticated user because no approved `VIEW_EXAM` permission was found in the current source.
- Exam creation requires `CREATE_EXAM`; update requires `MANAGE_EXAM`. Both check that the course exists. This is not course-scoped authorization; the current model/controller does not establish a rule linking the acting user to the target course.
- The update endpoint only edits records whose existing status is exactly `PLANNED`. The confirmed policy allows `courseId` reassignment for planned exams only when the target course exists; regression tests were added for the allowed and rejected cases.
- Added validation coverage for missing required request fields and a `maxMarks` value below `0.01`. Existing tests cover positive course ID, nonblank name/type, positive max marks, optional times, and end-time equality/reversal. Tests have not executed because Maven cannot be downloaded in this environment.
- Updated `API_ENDPOINTS.md` to describe the observed status behavior and explicitly distinguish course existence validation from course authorization.
- No status values or permission names were invented or changed.


## Batch 2 follow-up — course reassignment policy

- **Confirmed policy:** a `PLANNED` exam may be reassigned to another course, but the target course must exist.
- The existing `updateExam` implementation already validates target-course existence and only permits edits while status is `PLANNED`; no production logic change was needed for this policy.
- Added `ExamUpdatePolicyTests` to cover reassignment to an existing course, rejection of a nonexistent target course, and rejection of reassignment when the exam is not `PLANNED`.
- These are source-level test additions only. Maven execution is still required to confirm compilation and test results.
