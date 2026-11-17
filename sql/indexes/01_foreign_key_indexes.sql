-- =============================================================
-- Indexes on foreign keys
-- =============================================================

CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_items_order     ON order_items(order_id);
CREATE INDEX idx_items_product   ON order_items(product_id);
CREATE INDEX idx_payments_order  ON payments(order_id);
