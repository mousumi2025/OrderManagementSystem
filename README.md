# Order Management System

PostgreSQL database schema for a simple order management system.

## Schema

| Table         | Description                                        |
|---------------|----------------------------------------------------|
| `customers`   | Customer contact details (unique email)            |
| `products`    | Product catalog with price and stock quantity      |
| `orders`      | Orders placed by customers, with status and total  |
| `order_items` | Line items linking orders to products              |
| `payments`    | Payments recorded against orders                   |

```
customers 1──* orders 1──* order_items *──1 products
                  │
                  1──* payments
```

## Project Structure

```
OrderManagementSystem/
├── README.md
├── .gitignore
├── api/                                # Spring Boot REST API (CRUD) - see api/README.md
└── sql/
    ├── deploy.sql                      # Master script: runs everything in order
    ├── 00_drop_tables.sql              # Drops all tables (clean rebuild)
    ├── tables/
    │   ├── 01_customers.sql
    │   ├── 02_products.sql
    │   ├── 03_orders.sql
    │   ├── 04_order_items.sql
    │   └── 05_payments.sql
    └── indexes/
        └── 01_foreign_key_indexes.sql
```

Files are numbered so they run in dependency order (parent tables before child tables).

## Deploy

Requires PostgreSQL 10+ (uses identity columns) and `psql`.

```bash
createdb order_management
psql -U postgres -d order_management -f sql/deploy.sql
```

> **Warning:** `deploy.sql` drops and recreates all tables, so any existing data is lost.

## REST API

The [`api/`](api/) folder holds a separately deployable Spring Boot service with CRUD endpoints
for every table. See [api/README.md](api/README.md) for build, run and endpoint details.
