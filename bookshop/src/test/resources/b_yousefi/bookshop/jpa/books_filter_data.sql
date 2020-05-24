INSERT INTO author
(id,
 birthday,
 full_name,
 description,
 picture_id)
VALUES (1,
        null,
        'author1',
        'author1 description',
        null);
---------------------------------------------------------------------
INSERT INTO author
(id,
 birthday,
 full_name,
 description,
 picture_id)
VALUES (2,
        null,
        'author2',
        'author2 description',
        null);
------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
INSERT INTO category
(id,
 parent_cat_id,
 name,
 description)
values (1,
        null,
        'category1',
        null);
---------------------------------------------------------------------
INSERT INTO category
(id,
 parent_cat_id,
 name,
 description)
values (2,
        1,
        'category2',
        'child of category1');
---------------------------------------------------------------------
INSERT INTO category
(id,
 parent_cat_id,
 name,
 description)
values (3,
        1,
        'category3',
        'child of category1');
---------------------------------------------------------------------
INSERT INTO category
(id,
 parent_cat_id,
 name,
 description)
values (4,
        2,
        'category4',
        'child of category2');
------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
INSERT INTO publication
(id,
 description,
 name,
 website)
VALUES (1,
        null,
        'publication1',
        null);
---------------------------------------------------------------------
INSERT INTO publication
(id,
 description,
 name,
 website)
VALUES (2,
        null,
        'publication2',
        null);
------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
INSERT INTO book
(id,
 isbn,
 name,
 published_day,
 summary,
 publication_id)
VALUES (1,
        '0192802380',
        'book1',
        null,
        null,
        1);
--------------------------------------------
INSERT INTO book_category
(book_id,
 category_id)
VALUES (1,
        2);
--------------------------------------------
INSERT INTO book_category
(book_id,
 category_id)
VALUES (1,
        3);
--------------------------------------------
INSERT INTO book_author
(book_id,
 author_id)
VALUES (1,
        1);
------------------------------------------------------------------------------------------------
INSERT INTO book
(id,
 isbn,
 name,
 published_day,
 summary,
 publication_id)
VALUES (2,
        '0292802380',
        'book2',
        null,
        null,
        1);
--------------------------------------------
INSERT INTO book_category
(book_id,
 category_id)
VALUES (2,
        4);
--------------------------------------------
INSERT INTO book_author
(book_id,
 author_id)
VALUES (2,
        2);
--------------------------------------------
INSERT INTO book_author
(book_id,
 author_id)
VALUES (2,
        1);
------------------------------------------------------------------------------------------------
INSERT INTO book
(id,
 isbn,
 name,
 published_day,
 summary,
 publication_id)
VALUES (3,
        '0292802380',
        'book3',
        null,
        null,
        2);
--------------------------------------------
INSERT INTO book_category
(book_id,
 category_id)
VALUES (3,
        3);
--------------------------------------------
INSERT INTO book_category
(book_id,
 category_id)
VALUES (3,
        1);
--------------------------------------------
INSERT INTO book_author
(book_id,
 author_id)
VALUES (3,
        2);
------------------------------------------------------------------------------------------------
INSERT INTO book
(id,
 isbn,
 name,
 published_day,
 summary,
 publication_id)
VALUES (4,
        '0292802380',
        'book4',
        null,
        null,
        2);
--------------------------------------------
INSERT INTO book_category
(book_id,
 category_id)
VALUES (4,
        2);
--------------------------------------------
INSERT INTO book_category
(book_id,
 category_id)
VALUES (4,
        3);
--------------------------------------------
INSERT INTO book_author
(book_id,
 author_id)
VALUES (4,
        1);
--------------------------------------------
INSERT INTO book_author
(book_id,
 author_id)
VALUES (4,
        2);

