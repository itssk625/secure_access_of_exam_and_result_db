-- Read-only diagnostics for the existing PostgreSQL schema.
-- Run against exam_access_control in pgAdmin/psql. Do not run DDL from this file.

-- 1) Tables, columns, SQL types, nullability, and defaults in the public schema.
SELECT table_name, column_name, data_type, udt_name, is_nullable, column_default
FROM information_schema.columns
WHERE table_schema = 'public'
ORDER BY table_name, ordinal_position;

-- 2) Primary keys, foreign keys, unique/check constraints.
SELECT tc.table_name, tc.constraint_name, tc.constraint_type,
       pg_get_constraintdef(c.oid) AS definition
FROM information_schema.table_constraints tc
JOIN pg_constraint c
  ON c.conname = tc.constraint_name
 AND c.connamespace = (SELECT oid FROM pg_namespace WHERE nspname = tc.constraint_schema)
JOIN information_schema.tables t
  ON t.table_schema = tc.constraint_schema
 AND t.table_name = tc.table_name
WHERE tc.constraint_schema = 'public'
ORDER BY tc.table_name, tc.constraint_type, tc.constraint_name;

-- 3) Identity/sequence ownership and defaults, especially marks.mark_id and exams.exam_id.
SELECT table_name, column_name, column_default, is_identity, identity_generation
FROM information_schema.columns
WHERE table_schema = 'public'
  AND (table_name, column_name) IN (('marks', 'mark_id'), ('exams', 'exam_id'))
ORDER BY table_name, column_name;

-- 4) Sequence inventory and current ownership dependencies.
SELECT schemaname, sequencename, data_type, start_value, min_value, max_value, increment_by
FROM pg_sequences
WHERE schemaname = 'public'
ORDER BY sequencename;

SELECT seq.relname AS sequence_name,
       tbl.relname AS table_name,
       att.attname AS column_name,
       dep.deptype
FROM pg_class seq
JOIN pg_namespace ns ON ns.oid = seq.relnamespace
JOIN pg_depend dep ON dep.objid = seq.oid AND dep.classid = 'pg_class'::regclass
JOIN pg_class tbl ON tbl.oid = dep.refobjid
JOIN pg_attribute att ON att.attrelid = tbl.oid AND att.attnum = dep.refobjsubid
WHERE ns.nspname = 'public' AND seq.relkind = 'S'
ORDER BY tbl.relname, att.attname;

-- 5) Existing role and permission catalogue; inspect values, do not insert guesses.
SELECT * FROM public.roles ORDER BY role_id;
SELECT * FROM public.permissions ORDER BY permission_id;
SELECT * FROM public.role_permissions ORDER BY role_id, permission_id;

-- 6) Current status values; observational only, do not modify statuses to satisfy tests.
SELECT status, COUNT(*) AS row_count FROM public.exams GROUP BY status ORDER BY status;
SELECT status, COUNT(*) AS row_count FROM public.results GROUP BY status ORDER BY status;
SELECT mark_status, COUNT(*) AS row_count FROM public.marks GROUP BY mark_status ORDER BY mark_status;
