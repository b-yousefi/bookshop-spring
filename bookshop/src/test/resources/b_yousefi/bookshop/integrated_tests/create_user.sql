INSERT INTO user
(id,
 first_name,
 last_name,
 email,
 password,
 phone_number,
 username,
 picture_id,
 role)
VALUES (1,
        'behnaz',
        'yousefi',
        'b.yousefi2911@gmail.com',
        'admin',
        '989352229966',
        'admin',
        null,
        'ROLE_ADMIN');
-----------------------------------------------
INSERT INTO order_table
(id,
 address_id,
 user_id)
VALUES (1,
        null,
        1);
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
INSERT INTO user
(id,
 first_name,
 last_name,
 email,
 password,
 phone_number,
 username,
 picture_id,
 role)
VALUES (2,
        'bahareh',
        'yousefi',
        'behyousefi@yahoo.com',
        'user_test1',
        '989352229977',
        'user_test1',
        null,
        'ROLE_USER');
-----------------------------------------------
INSERT INTO order_table
(id,
 address_id,
 user_id)
VALUES (2,
        null,
        2);
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
        '2020-05-19');
-----------------------------------------------