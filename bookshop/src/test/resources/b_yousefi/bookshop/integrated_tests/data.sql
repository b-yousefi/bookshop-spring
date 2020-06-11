INSERT INTO user
(id,
 full_name,
 password,
 phone_number,
 username,
 picture_id,
 role)
VALUES (1,
        'behnaz yousefi',
        'admin',
        '989352229966',
        'admin',
        null,
        'ADMIN');
INSERT INTO user
(id,
 full_name,
 password,
 phone_number,
 username,
 picture_id,
 role)
VALUES (2,
        'bahareh yousefi',
        'user_test1',
        '989352229977',
        'user_test1',
        null,
        'ROLE_USER');
---------------------------------------------------------------------------------------------------------
INSERT INTO author
(id,
 birthday,
 description,
 full_name,
 picture_id)
VALUES (1,
        null,
        'Jane Austen was an English novelist known primarily for her six major novels',
        'Jane Austen',
        null);
---------------------------------------------------------------------------------------------------------
INSERT INTO category(id, parent_cat_id, name, description)
values (1, null, 'Fiction',
        'Fiction books contain a made-up story – a story that did not actually happen in real life.These stories are derived from the imagination and creativity of the authors and are not based on facts');
INSERT INTO category(id, parent_cat_id, name, description)
values (2, 1, 'Fairy Tale',
        'Fairy tale is usually a story for children that involves imaginary creatures and magical events.');
---------------------------------------------------------------------------------------------------------
INSERT INTO publication
(id,
 description,
 name,
 website)
VALUES (1,
        'Oxford University Press (OUP) is the largest university press in the world and the second oldest after Cambridge University Press',
        'oxford',
        'www.oxford.com');
---------------------------------------------------------------------------------------------------------
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

INSERT INTO book_category
(book_id,
 categories_id)
VALUES (1,
        2);
---------------------------------------------------------------------------------------------------------