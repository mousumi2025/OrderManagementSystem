# Order Management API

Spring Boot REST API with CRUD operations for the five tables of the Order Management System
(`customers`, `products`, `orders`, `order_items`, `payments`).

- Java 21, Spring Boot 3.5, Spring Data JPA, PostgreSQL
- The database schema is owned by [`../sql/deploy.sql`](../sql/deploy.sql). Run that first.
  The API never creates or alters tables; at startup Hibernate only **validates** that its mappings match.

## Configuration

All settings come from environment variables (defaults in [`application.yml`](src/main/resources/application.yml)):

| Variable      | Default                                                        |
|---------------|----------------------------------------------------------------|
| `DB_URL`      | `jdbc:postgresql://localhost:5432/CloudOrderManagementSystems` |
| `DB_USERNAME` | `postgres`                                                     |
| `DB_PASSWORD` | *(empty, must be set)*                                         |
| `PORT`        | `8080`                                                         |

Do not put real passwords in `application.yml`; it is committed to git.

## Build and run

Maven is optional; the included wrapper (`mvnw` / `mvnw.cmd`) downloads it.

```powershell
# Windows PowerShell
cd api
.\mvnw.cmd clean package
$env:DB_PASSWORD = "your-password"
java -jar target\order-management-api.jar
```

```bash
# Linux / macOS
cd api
./mvnw clean package
DB_PASSWORD=your-password java -jar target/order-management-api.jar
```

### Docker

```bash
docker build -t order-management-api ./api
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/CloudOrderManagementSystems \
  -e DB_PASSWORD=your-password \
  order-management-api
```

(`host.docker.internal` reaches a PostgreSQL running on the host machine.)

## Endpoints

| Resource    | Base path          | List filter        |
|-------------|--------------------|--------------------|
| Customers   | `/api/customers`   | —                  |
| Products    | `/api/products`    | —                  |
| Orders      | `/api/orders`      | `?customerId={id}` |
| Order items | `/api/order-items` | `?orderId={id}`    |
| Payments    | `/api/payments`    | `?orderId={id}`    |

Each resource supports:

| Method   | Path          | Result                       |
|----------|---------------|------------------------------|
| `GET`    | `/`           | 200, list (sorted by id)     |
| `GET`    | `/{id}`       | 200, or 404                  |
| `POST`   | `/`           | 201 with `Location` header   |
| `PUT`    | `/{id}`       | 200, replaces the record     |
| `DELETE` | `/{id}`       | 204                          |

Also available:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec: `http://localhost:8080/v3/api-docs`
- Health check: `http://localhost:8080/actuator/health`

### Request bodies

```jsonc
// POST /api/customers
{ "firstName": "Jane", "lastName": "Doe", "email": "jane@example.com", "phone": "555-0100" }

// POST /api/products          (quantityInStock defaults to 0)
{ "productName": "Widget", "description": "Blue", "price": 19.99, "quantityInStock": 10 }

// POST /api/orders            (orderDate defaults to now, status to "Pending", totalAmount to 0)
{ "customerId": 1, "status": "Pending", "totalAmount": 0 }

// POST /api/order-items       (unitPrice defaults to the product's current price)
{ "orderId": 1, "productId": 1, "quantity": 2 }

// POST /api/payments          (paymentDate defaults to now)
{ "orderId": 1, "amount": 39.98, "paymentStatus": "Paid", "paymentMethod": "Credit Card" }
```

Allowed values:
- order `status`: `Pending`, `Preparing`, `Completed`, `Cancelled`
- `paymentStatus`: `Pending`, `Paid`, `Failed`, `Refunded`
- `paymentMethod`: `Cash`, `Credit Card`, `Debit Card`, `Mobile Pay`

### Errors

Errors are returned as [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) problem details:

| Status | When                                                                                   |
|--------|----------------------------------------------------------------------------------------|
| 400    | Validation failure (with per-field `errors`), malformed JSON, non-numeric id           |
| 404    | Record not found, or a referenced customer / order / product does not exist            |
| 409    | Duplicate email / product name / (order, product) pair, or deleting a referenced record |

Deleting an order also deletes its order items (database `ON DELETE CASCADE`), but an order that
still has payments cannot be deleted until those payments are removed.
