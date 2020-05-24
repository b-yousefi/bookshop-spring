INSERT INTO book
(id,
 isbn,
 name,
 published_day,
 summary,
 publication_id)
VALUES (2,
        '0144556677',
        'Sense and Sensibility',
        '2017-06-15',
        'After the death of their father, three young girls find themselves in abject poverty.' ||
        ' The responsibility to provide for the family lands on the oldest sisters, who are also dealing with heartbreaks',
        1);
-----------------------------------------------------------------------------------------------
INSERT INTO book_category
(book_id,
 category_id)
VALUES (2,
        2);
-----------------------------------------------------------------------------------------------
INSERT INTO book_author
(book_id,
 author_id)
VALUES (2,
        1);