INSERT INTO `db_bookshop`.`book`
(`id`,
`isbn`,
`name`,
`published_day`,
`summary`,
`picture_id`,
`publication_id`,
`price`,
`quantity`)
VALUES
(1,
'9781853260001',
'Pride and Prejudice',
null,
'Love is in the air when five sisters discover that a wealthy and eligible bachelor is suddenly within reach. But it is his friend, the haughty Mr. Darcy, who becomes smitten. Unfortunately for him, the object of his affection is not so easily swayed.
One of the most popular characters in English literature, Elizabeth Bennet is intelligent, witty, well-spoken and ahead of her time. If the terrible rumors about Mr. Darcy are true, he doesn’t stand a chance. Yet not all gossip is to be believed when marriage, money, and reputations are on the line. Will Elizabeth and Mr. Darcy circumvent her haste, his ego, and society’s expectations to find love?',
7,
6,
45,
12);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
`author_id`)
VALUES
(1,
1);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
`category_id`)
VALUES
(1,
11);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book`
(`id`,
`isbn`,
`name`,
`published_day`,
`summary`,
`picture_id`,
`publication_id`,
`price`,
`quantity`)
VALUES
(2,
'9780141439662',
'Sense and Sensibility',
null,
'\'The more I know of the world, the more am I convinced that I shall never see a man whom I can really love. I require so much!\'
Marianne Dashwood wears her heart on her sleeve, and when she falls in love with the dashing but unsuitable John Willoughby she ignores her sister Elinor\'s warning that her impulsive behaviour leaves her open to gossip and innuendo. Meanwhile Elinor, always sensitive to social convention, is struggling to conceal her own romantic disappointment, even from those closest to her. Through their parallel experience of love—and its threatened loss—the sisters learn that sense must mix with sensibility if they are to find personal happiness in a society where status and money govern the rules of love.
This edition includes explanatory notes, textual variants between the first and second editions, and Tony Tanner\'s introduction to the original Penguin Classic edition.',
8,
1,
32,
7);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
`author_id`)
VALUES
(2,
1);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
`category_id`)
VALUES
(2,
11);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book`
(`id`,
`isbn`,
`name`,
`published_day`,
`summary`,
`picture_id`,
`publication_id`,
`price`,
`quantity`)
VALUES
(3,
'9780199535552',
'Persuasion',
null,
'Twenty-seven-year old Anne Elliot is Austen\'s most adult heroine. Eight years before the story proper begins, she is happily betrothed to a naval officer, Frederick Wentworth, but she precipitously breaks off the engagement when persuaded by her friend Lady Russell that such a match is unworthy. The breakup produces in Anne a deep and long-lasting regret. When later Wentworth returns from sea a rich and successful captain, he finds Anne\'s family on the brink of financial ruin and his own sister a tenant in Kellynch Hall, the Elliot estate. All the tension of the novel revolves around one question: Will Anne and Wentworth be reunited in their love?

Jane Austen once compared her writing to painting on a little bit of ivory, 2 inches square. Readers of Persuasion will discover that neither her skill for delicate, ironic observations on social custom, love, and marriage nor her ability to apply a sharp focus lens to English manners and morals has deserted her in her final finished work.',
9,
1,
45,
3);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
`author_id`)
VALUES
(3,
1);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
`category_id`)
VALUES
(3,
11);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book`
(`id`,
`isbn`,
`name`,
`published_day`,
`summary`,
`picture_id`,
`publication_id`,
`price`,
`quantity`)
VALUES
(4,
'9780199535521',
'Emma',
null,
'So speculate the friends and neighbours of Emma Woodhouse, the lovely, lively, wilful,and fallible heroine of Jane Austen\'s fourth published novel. Confident that she knows best, Emma schemes to find a suitable husband for her pliant friend Harriet, only to discover that she understands the feelings of others as little as she does her own heart. As Emma puzzles and blunders her way through the mysteries of her social world, Austen evokes for her readers a cast of unforgettable characters and a detailed portrait of a small town undergoing historical transition.
Written with matchless wit and irony, judged by many to be her finest novel, Emma has been adapted many times for film and television. This new edition shows how Austen brilliantly turns the everyday into the exceptional.',
10,
1,
67,
12);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
`author_id`)
VALUES
(4,
1);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
`category_id`)
VALUES
(4,
11);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book`
(`id`,
`isbn`,
`name`,
`published_day`,
`summary`,
`picture_id`,
`publication_id`,
`price`,
`quantity`)
VALUES
(5,
'9781853261589',
'The Little Prince',
null,
'‘The most beautiful things in the world cannot be seen or touched, they are felt with the heart.’
After crash-landing in the Sahara Desert, a pilot encounters a little prince who is visiting Earth from his own planet. Their strange and moving meeting illuminates for the aviator many of life\'s universal truths, as he comes to learn what it means to be human from a child who is not. Antoine de Saint-Exupéry\'s delightful The Little Prince has been translated into over 180 languages and sold over 80 million copies.',
11,
4,
55,
30);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
`author_id`)
VALUES
(5,
2);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
`category_id`)
VALUES
(5,
8);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book`
(`id`,
`isbn`,
`name`,
`published_day`,
`summary`,
`picture_id`,
`publication_id`,
`price`,
`quantity`)
VALUES
(6,
'9780451526342',
'Animal Farm',
null,
'A farm is taken over by its overworked, mistreated animals. With flaming idealism and stirring slogans, they set out to create a paradise of progress, justice, and equality. Thus the stage is set for one of the most telling satiric fables ever penned –a razor-edged fairy tale for grown-ups that records the evolution from revolution against tyranny to a totalitarianism just as terrible.
When Animal Farm was first published, Stalinist Russia was seen as its target. Today it is devastatingly clear that wherever and whenever freedom is attacked, under whatever banner, the cutting clarity and savage comedy of George Orwell\’s masterpiece have a meaning and message still ferociously fresh.',
12,
7,
23,
25);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
`author_id`)
VALUES
(6,
3);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
`category_id`)
VALUES
(6,
8);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book`
(`id`,
`isbn`,
`name`,
`published_day`,
`summary`,
`picture_id`,
`publication_id`,
`price`,
`quantity`)
VALUES
(7,
'9780451526342',
'Nineteen Eighty-Four',
null,
'Hidden away in the Record Department of the sprawling Ministry of Truth, Winston Smith skilfully rewrites the past to suit the needs of the Party. Yet he inwardly rebels against the totalitarian world he lives in, which demands absolute obedience and controls him through the all-seeing telescreens and the watchful eye of Big Brother, symbolic head of the Party. In his longing for truth and liberty, Smith begins a secret love affair with a fellow-worker Julia, but soon discovers the true price of freedom is betrayal.
George Orwell\'s dystopian masterpiece, Nineteen Eighty-Four is perhaps the most pervasively influential book of the twentieth century.',
13,
7,
34,
7);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
`author_id`)
VALUES
(7,
3);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
`category_id`)
VALUES
(7,
8);
/*------------------------------------------------------------------------*/
