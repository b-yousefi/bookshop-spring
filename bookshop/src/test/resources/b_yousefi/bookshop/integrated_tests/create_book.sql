INSERT INTO book
(id,
 isbn,
 name,
 published_day,
 summary,
 publication_id)
VALUES (1,
        '0192802380',
        'Pride And Prejudice',
        '2017-06-15',
        'Pride and Prejudice is a romantic novel of manners written by Jane Austen in 1813.',
        1);
-----------------------------------------------------------------------------------------------
INSERT INTO book_category
(book_id,
 category_id)
VALUES (1,
        2);
-----------------------------------------------------------------------------------------------
INSERT INTO book_author
(book_id,
 author_id)
VALUES (1,
        1);
-----------------------------------------------------------------------------------------------