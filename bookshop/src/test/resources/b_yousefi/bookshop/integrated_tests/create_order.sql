INSERT INTO order_table
(id,
 address_id,
 user_id)
VALUES (1,
        1,
        1);
-----------------------------------------------
INSERT INTO order_table
(id,
 address_id,
 user_id)
VALUES (2,
        2,
        2);
-----------------------------------------------
INSERT INTO order_table
(id,
 address_id,
 user_id)
VALUES (3,
        2,
        2);
-----------------------------------------------
INSERT INTO order_table
(id,
 address_id,
 user_id)
VALUES (4,
        2,
        2);
-----------------------------------------------
INSERT INTO order_status
(id,
 order_id,
 status,
 message,
 updated_at)
VALUES (1,
        1,
        0,
        'open order',
        '2020-05-19');
-----------------------------------------------
INSERT INTO order_status
(id,
 order_id,
 status,
 message,
 updated_at)
VALUES (2,
        2,
        0,
        'open order',
        '2020-04-19');
-----------------------------------------------
INSERT INTO order_status
(id,
 order_id,
 status,
 message,
 updated_at)
VALUES (3,
        3,
        0,
        'open order',
        '2020-04-23');
-----------------------------------------------
INSERT INTO order_status
(id,
 order_id,
 status,
 message,
 updated_at)
VALUES (4,
        4,
        0,
        'open order',
        '2020-05-01');