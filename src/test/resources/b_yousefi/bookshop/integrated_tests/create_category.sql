INSERT INTO category
(id,
 parent_cat_id,
 name,
 description)
values (1,
        null,
        'Fiction',
        'Fiction books contain a made-up story – a story that did not actually happen in real life.These stories are derived from the imagination and creativity of the authors and are not based on facts');
INSERT INTO category
(id,
 parent_cat_id,
 name,
 description)
values (2,
        1,
        'Fairy Tale',
        'Fairy tale is usually a story for children that involves imaginary creatures and magical events.');