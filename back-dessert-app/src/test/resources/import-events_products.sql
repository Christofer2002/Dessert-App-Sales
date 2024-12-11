-- Relacionar productos con eventos en la tabla intermedia events_products
INSERT INTO events_products (id, event_id, product_id)
VALUES
    (1, 1, 1),  -- El evento de cumpleaños incluye el Apple Pie
    (2, 1, 2),  -- El evento de cumpleaños incluye el Blueberry Muffin
    (3, 2, 3),  -- El evento de bodas incluye el Glazed Donut
    (4, 3, 4),  -- El evento corporativo incluye el Chocolate Muffin
    (5, 3, 5);  -- El evento corporativo incluye el Strawberry Donut
