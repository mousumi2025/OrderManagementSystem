-- =============================================================
-- Order Management System (PostgreSQL)
-- deploy.sql : Master script - rebuilds the full schema.
--
-- Usage (from the repository root):
--   psql -U <user> -d <database> -f sql/deploy.sql
-- =============================================================

\set ON_ERROR_STOP on

BEGIN;

\echo '>> Dropping existing tables...'
\ir 00_drop_tables.sql

\echo '>> Creating tables...'
\ir tables/01_customers.sql
\ir tables/02_products.sql
\ir tables/03_orders.sql
\ir tables/04_order_items.sql
\ir tables/05_payments.sql

\echo '>> Creating indexes...'
\ir indexes/01_foreign_key_indexes.sql

COMMIT;

\echo '>> Deployment complete.'
