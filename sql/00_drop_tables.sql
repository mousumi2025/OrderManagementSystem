-- =============================================================
-- Order Management System (PostgreSQL)
-- 00_drop_tables.sql : Drop all tables (clean rebuild)
-- WARNING: Destroys all data in these tables.
-- =============================================================

DROP TABLE IF EXISTS payments, order_items, orders, products, customers CASCADE;
