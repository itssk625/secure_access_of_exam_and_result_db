# Existing API behavior (corrective audit)

This document describes endpoints present in the current backend source. It is not a claim that the complete project blueprint is implemented.

## Authentication

- `GET /api/auth/me`: current authenticated user's username and authorities. Authentication is required.
- `GET /api/test/me`: development diagnostic endpoint only; registered only when the Spring `dev` profile is active.
- `GET /api/test/enter-marks`: development diagnostic endpoint only; requires `ENTER_MARKS` and is registered only when the Spring `dev` profile is active.

The application currently uses stateless HTTP Basic authentication. There are no implemented public authentication endpoints, so the security configuration does not permit the `/api/auth/**` namespace wholesale. The test endpoints are not registered under the default profile.

## Exam endpoints

- `POST /api/exams`: requires `CREATE_EXAM`; validates the request, exam times and existence of the referenced course; sets initial status to `PLANNED` server-side.
- `PUT /api/exams/{id}`: requires `MANAGE_EXAM`; only permits editing exams whose current status is `PLANNED`; validates the replacement course exists. Changing `courseId` is allowed while the existing exam status is `PLANNED`, provided the target course exists. Reassignment is rejected for exams in any other status. Regression tests cover both cases and reject nonexistent target courses.
- `GET /api/exams/{id}`: requires authentication. The approved permission catalogue has no `VIEW_EXAM` permission, so the current interim policy is that any authenticated user can read exam metadata. This policy should be confirmed by the project team rather than silently adding a permission.

Exam creation/update require `endTime` to be after `startTime` when both are supplied. The times are optional individually in the current DTO; this means the API does not require a schedule to be supplied. Request validation also requires a positive course ID, nonblank exam name/type, and `maxMarks` of at least `0.01`. The controller trims exam name/type before persistence.

Current exam status behavior evidenced by source: creation assigns `PLANNED`; updates are allowed only when the existing status equals `PLANNED` (case-sensitive comparison); the update endpoint does not itself change the status. The source does not establish the full set of statuses present in PostgreSQL, so no broader status lifecycle is claimed here.

Authorization limitation: `CREATE_EXAM` and `MANAGE_EXAM` are method-level permissions, and course existence is checked. The current source does not implement an additional resource-level rule tying those permissions to a specific course. Do not treat course existence as course authorization.

## Student result endpoints

- `GET /api/results/{resultId}`: retrieves one result, only for the authenticated student's own record and only when its status is `PUBLISHED`. A missing, another student's, or unpublished result returns `404`.
- `GET /api/students/{id}/results`: returns a list of the authenticated student's `PUBLISHED` results. A different student's ID is forbidden.
- `GET /api/students/{id}`: returns the authenticated student's own profile only.
- `GET /api/students/{id}/courses`: returns distinct courses derived from the student's existing result records. It is not an authoritative enrollment list because no enrollment source has been verified in the current schema.

## Database and status notes
- PostgreSQL inspection confirmed `exams.max_marks` and `marks.mark_status`
  exist and are non-nullable. The `exams_exam_id_seq` and
  `marks_mark_id_seq` defaults were also confirmed.
- `Exam.examId` uses the matching sequence. `Marks.markId` does not yet
  declare a JPA-generated ID strategy; its insertion behavior remains
  unverified.
- Full entity-to-schema reconciliation has not been completed.
## Exam permission policy

- `POST /api/exams` requires `CREATE_EXAM`, currently granted to
  `EXAM_CELL`. Authorized users may create exams for any existing course.
- `PUT /api/exams/{id}` requires `MANAGE_EXAM`, currently granted to
  `EXAM_CELL`. Only exams with status `PLANNED` may be edited.
  Reassignment is allowed only to an existing course.
- Faculty do not currently have `CREATE_EXAM` or `MANAGE_EXAM`.
  Faculty exam-management access is not enabled until explicitly approved.

  ## Verification status

- The user reports that the local Maven test suite passed.
- The earlier audit environment could not download Maven, so its tests
  were not executed there.
- Live application/Postman integration testing and Git-history secret
  scanning remain unverified.