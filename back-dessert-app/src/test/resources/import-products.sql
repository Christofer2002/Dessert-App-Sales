-- Insertar productos en la tabla products
INSERT INTO products (id, name, flavour, duration_days, unit_price, description, image, category_id)
VALUES
    (1, 'Apple Pie', 'Apple', 5, 12.99, 'Classic apple pie with cinnamon', 'apple_pie.jpg', 1),
    (2, 'Blueberry Muffin', 'Blueberry', 4, 3.99, 'Fresh blueberry muffin', 'blueberry_muffin.jpg', 3),
    (3, 'Glazed Donut', 'Vanilla', 2, 2.50, 'Soft and sweet vanilla glazed donut', 'glazed_donut.jpg', 2),
    (4, 'Chocolate Muffin', 'Chocolate', 4, 3.99, 'Rich chocolate muffin', 'chocolate_muffin.jpg', 3),
    (5, 'Strawberry Donut', 'Strawberry', 2, 2.75, 'Delicious strawberry frosted donut', 'strawberry_donut.jpg', 2);
