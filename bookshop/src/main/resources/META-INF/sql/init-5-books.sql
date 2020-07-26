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
(8,
 '9780553381689',
 'A Song of Ice and Fire (Book 1)',
 '2002-05-28',
 'Winter is coming. Such is the stern motto of House Stark, the northernmost of the fiefdoms that owe allegiance to King Robert Baratheon in far-off King’s Landing. There Eddard Stark of Winterfell rules in Robert’s name. There his family dwells in peace and comfort: his proud wife, Catelyn; his sons Robb, Brandon, and Rickon; his daughters Sansa and Arya; and his bastard son, Jon Snow. Far to the north, behind the towering Wall, lie savage Wildings and worse—unnatural things relegated to myth during the centuries-long summer, but proving all too real and all too deadly in the turning of the season.
Yet a more immediate threat lurks to the south, where Jon Arryn, the Hand of the King, has died under mysterious circumstances. Now Robert is riding north to Winterfell, bringing his queen, the lovely but cold Cersei, his son, the cruel, vainglorious Prince Joffrey, and the queen’s brothers Jaime and Tyrion of the powerful and wealthy House Lannister—the first a swordsman without equal, the second a dwarf whose stunted stature belies a brilliant mind. All are heading for Winterfell and a fateful encounter that will change the course of kingdoms.
Meanwhile, across the Narrow Sea, Prince Viserys, heir of the fallen House Targaryen, which once ruled all of Westeros, schemes to reclaim the throne with an army of barbarian Dothraki—whose loyalty he will purchase in the only coin left to him: his beautiful yet innocent sister, Daenerys.',
 14,
8,
 9.39,
 150);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(8,
4);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(8,
3);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(8,
 10);
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
(9,
 '9780345535412',
 'A Song of Ice and Fire (Book 2)',
 '2012-06-03',
 'In this thrilling sequel to A Game of Thrones, George R. R. Martin has created a work of unsurpassed vision, power, and imagination. A Clash of Kings transports us to a world of revelry and revenge, wizardry and warfare unlike any we have ever experienced.
 A comet the color of blood and flame cuts across the sky. And from the ancient citadel of Dragonstone to the forbidding shores of Winterfell, chaos reigns. Six factions struggle for control of a divided land and the Iron Throne of the Seven Kingdoms, preparing to stake their claims through tempest, turmoil, and war. It is a tale in which brother plots against brother and the dead rise to walk in the night. Here a princess masquerades as an orphan boy; a knight of the mind prepares a poison for a treacherous sorceress; and wild men descend from the Mountains of the Moon to ravage the countryside. Against a backdrop of incest and fratricide, alchemy and murder, victory may go to the men and women possessed of the coldest steel . . . and the coldest hearts. For when kings clash, the whole land trembles.',
 15,
 8,
 11.97,
 130);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(9,
 4);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(9,
 3);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(9,
 10);
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
(10,
 '9780553381702',
 'A Song of Ice and Fire (Book 3)',
 '2002-05-28',
 'Of the five contenders for power, one is dead, another in disfavor, and still the wars rage as violently as ever, as alliances are made and broken. Joffrey, of House Lannister, sits on the Iron Throne, the uneasy ruler of the land of the Seven Kingdoms. His most bitter rival, Lord Stannis, stands defeated and disgraced, the victim of the jealous sorceress who holds him in her evil thrall. But young Robb, of House Stark, still rules the North from the fortress of Riverrun. Robb plots against his despised Lannister enemies, even as they hold his sister hostage at King’s Landing, the seat of the Iron Throne. Meanwhile, making her way across a blood-drenched continent is the exiled queen, Daenerys, mistress of the only three dragons still left in the world. . . .
But as opposing forces maneuver for the final titanic showdown, an army of barbaric wildlings arrives from the outermost line of civilization. In their vanguard is a horde of mythical Others—a supernatural army of the living dead whose animated corpses are unstoppable. As the future of the land hangs in the balance, no one will rest until the Seven Kingdoms have exploded in a veritable storm of swords. . . .',
 16,
 8,
 9.29,
 166);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(10,
 4);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(10,
 3);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(10,
 10);
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
(11,
 '9780553801507',
 'A Song of Ice and Fire (Book 4)',
 '2005-08-10',
 'It seems too good to be true. After centuries of bitter strife and fatal treachery, the seven powers dividing the land have decimated one another into an uneasy truce. Or so it appears. . . . With the death of the monstrous King Joffrey, Cersei is ruling as regent in King’s Landing. Robb Stark’s demise has broken the back of the Northern rebels, and his siblings are scattered throughout the kingdom like seeds on barren soil. Few legitimate claims to the once desperately sought Iron Throne still exist—or they are held in hands too weak or too distant to wield them effectively. The war, which raged out of control for so long, has burned itself out.
But as in the aftermath of any climactic struggle, it is not long before the survivors, outlaws, renegades, and carrion eaters start to gather, picking over the bones of the dead and fighting for the spoils of the soon-to-be dead. Now in the Seven Kingdoms, as the human crows assemble over a banquet of ashes, daring new plots and dangerous new alliances are formed, while surprising faces—some familiar, others only just appearing—are seen emerging from an ominous twilight of past struggles and chaos to take up the challenges ahead.
It is a time when the wise and the ambitious, the deceitful and the strong will acquire the skills, the power, and the magic to survive the stark and terrible times that lie before them. It is a time for nobles and commoners, soldiers and sorcerers, assassins and sages to come together and stake their fortunes . . . and their lives. For at a feast for crows, many are the guests—but only a few are the survivors.',
 17,
 8,
 11.56,
 166);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(11,
 4);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(11,
 3);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(11,
 10);
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
(12,
 '9780553385953',
 'A Song of Ice and Fire (Book 5)',
 '2013-10-29',
 'In the aftermath of a colossal battle, the future of the Seven Kingdoms hangs in the balance—beset by newly emerging threats from every direction. In the east, Daenerys Targaryen, the last scion of House Targaryen, rules with her three dragons as queen of a city built on dust and death. But Daenerys has thousands of enemies, and many have set out to find her. As they gather, one young man embarks upon his own quest for the queen, with an entirely different goal in mind.
Fleeing from Westeros with a price on his head, Tyrion Lannister, too, is making his way to Daenerys. But his newest allies in this quest are not the rag-tag band they seem, and at their heart lies one who could undo Daenerys’s claim to Westeros forever.
Meanwhile, to the north lies the mammoth Wall of ice and stone—a structure only as strong as those guarding it. There, Jon Snow, 998th Lord Commander of the Night’s Watch, will face his greatest challenge. For he has powerful foes not only within the Watch but also beyond, in the land of the creatures of ice.
From all corners, bitter conflicts reignite, intimate betrayals are perpetrated, and a grand cast of outlaws and priests, soldiers and skinchangers, nobles and slaves, will face seemingly insurmountable obstacles. Some will fail, others will grow in the strength of darkness. But in a time of rising restlessness, the tides of destiny and politics will lead inevitably to the greatest dance of all.',
 18,
 8,
 15.19,
 166);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(12,
 4);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(12,
 3);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(12,
 10);
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
(13,
 '9780439708180',
 'Harry Potter and the Sorcerer''s Stone',
 null,
 'Harry Potter has no idea how famous he is. That''s because he''s being raised by his miserable aunt and uncle who are terrified Harry will learn that he''s really a wizard, just as his parents were. But everything changes when Harry is summoned to attend an infamous school for wizards, and he begins to discover some clues about his illustrious birthright. From the surprising way he is greeted by a lovable giant, to the unique curriculum and colorful faculty at his unusual school, Harry finds himself drawn deep inside a mystical world he never knew existed and closer to his own noble destiny.',
 19,
 9,
 6.23,
 290);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(13,
 5);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(13,
 10);
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
(14,
 '9780439064873',
 'Harry Potter and the Chamber of Secrets (Book 2)',
 null,
 'The Dursleys were so mean that hideous that summer that all Harry Potter wanted was to get back to the Hogwarts School for Witchcraft and Wizardry. But just as he''s packing his bags, Harry receives a warning from a strange, impish creature named Dobby who says that if Harry Potter returns to Hogwarts, disaster will strike.
And strike it does. For in Harry''s second year at Hogwarts, fresh torments and horrors arise, including an outrageously stuck-up new professor, Gilderoy Lockheart, a spirit named Moaning Myrtle who haunts the girls'' bathroom, and the unwanted attentions of Ron Weasley''s younger sister, Ginny.
But each of these seem minor annoyances when the real trouble begins, and someone--or something--starts turning Hogwarts students to stone. Could it be Draco Malfoy, a more poisonous rival than ever? Could it possibly be Hagrid, whose mysterious past is finally told? Or could it be the one everyone at Hogwarts most suspects...Harry Potter himself?',
 20,
 9,
 6.59,
 290);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(14,
 5);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(14,
 10);
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
(15,
 '9780545582933',
 'Harry Potter and the Prisoner of Azkaban (Book 3)',
 null,
 'This special edition of Harry Potter and the Prisoner of Azkaban has a gorgeous new cover illustration by Kazu Kibuishi. Inside is the full text of the original novel, with decorations by Mary GrandPré.
For twelve long years, the dread fortress of Azkaban held an infamous prisoner named Sirius Black. Convicted of killing thirteen people with a single curse, he was said to be the heir apparent to the Dark Lord, Voldemort.
Now he has escaped, leaving only two clues as to where he might be headed: Harry Potter’s defeat of You-Know-Who was Black’s downfall as well. And the Azkaban guards heard Black muttering in his sleep, “He’s at Hogwarts… he’s at Hogwarts.”
Harry Potter isn’t safe, not even within the walls of his magical school, surrounded by his friends. Because on top of it all, there may be a traitor in their midst.?',
 21,
 9,
12.99,
 290);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(15,
 5);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(15,
 10);
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
(16,
 '9780439139601',
 'Harry Potter And The Goblet Of Fire (Book 4)',
 null,
 'The paperback edition of the legendary, record-breaking, best-selling fourth Harry Potter novel.
Harry Potter is midway through his training as a wizard and his coming of age. Harry wants to get away from the pernicious Dursleys and go to the International Quidditch Cup. He wants to find out about the mysterious event that''s supposed to take place at Hogwarts this year, an event involving two other rival schools of magic, and a competition that hasn''t happened for a hundred years. He wants to be a normal, fourteen-year-old wizard. But unfortunately for Harry Potter, he''s not normal - even by wizarding standards. And in his case, different can be deadly.',
 22,
 9,
 7.79,
 290);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(16,
 5);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(16,
 10);
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
(17,
 '9780545582971',
 'Harry Potter and the Order of the Phoenix (Book 5)',
 null,
 'Here are just a few things on Harry’s mind:
A Defense Against the Dark Arts teacher with a personality like poisoned honey
A venomous, disgruntled house-elf
Ron as keeper of the Gryffindor Quidditch team
The looming terror of the end-of-term Ordinary Wizarding Level exams
…and of course, the growing threat of He-Who-Must-Not-Be-Named. In the richest installment yet of J. K. Rowling’s seven-part story, Harry Potter is faced with the unreliability of the very government of the magical world and the impotence of the authorities at Hogwarts.
Despite this (or perhaps because of it), he finds depth and strength in his friends, beyond what even he knew; boundless loyalty; and unbearable sacrifice.
Though thick runs the plot (as well as the spine), readers will race through these pages and leave Hogwarts, like Harry, wishing only for the next train back.',
 23,
 9,
 14.99,
 290);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(17,
 5);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(17,
 10);
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
(18,
 '9780545582995',
 'Harry Potter and the Half-Blood Prince (Book 6)',
 null,
 'This special edition of Harry Potter and the Half-Blood Prince has a gorgeous new cover illustration by Kazu Kibuishi. Inside is the full text of the original novel, with decorations by Mary GrandPré.
The war against Voldemort is not going well; even Muggle governments are noticing. Ron scans the obituary pages of the Daily Prophet, looking for familiar names. Dumbledore is absent from Hogwarts for long stretches of time, and the Order of the Phoenix has already suffered losses.
And yet…
As in all wars, life goes on. Sixth-year students learn to Apparate — and lose a few eyebrows in the process. The Weasley twins expand their business. Teenagers flirt and fight and fall in love. Classes are never straightforward, though Harry receives some extraordinary help from the mysterious Half-Blood Prince.
So it’s the home front that takes center stage in the multilayered sixth installment of the story of Harry Potter. Here at Hogwarts, Harry will search for the full and complex story of the boy who became Lord Voldemort — and thereby find what may be his only vulnerability.',
 24,
 9,
 14.99,
 290);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(18,
 5);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(18,
 10);
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
(19,
 '9780545139700',
 'Harry Potter and the Deathly Hallows (Book 7)',
 null,
 'Readers beware. The brilliant, breathtaking conclusion to J.K. Rowling''s spellbinding series is not for the faint of heart--such revelations, battles, and betrayals await in Harry Potter and the Deathly Hallows that no fan will make it to the end unscathed. Luckily, Rowling has prepped loyal readers for the end of her series by doling out increasingly dark and dangerous tales of magic and mystery, shot through with lessons about honor and contempt, love and loss, and right and wrong. Fear not, you will find no spoilers in our review--to tell the plot would ruin the journey, and Harry Potter and the Deathly Hallows is an odyssey the likes of which Rowling''s fans have not yet seen, and are not likely to forget. But we would be remiss if we did not offer one small suggestion before you embark on your final adventure with Harry--bring plenty of tissues.
The heart of Book 7 is a hero''s mission--not just in Harry''s quest for the Horcruxes, but in his journey from boy to man--and Harry faces more danger than that found in all six books combined, from the direct threat of the Death Eaters and you-know-who, to the subtle perils of losing faith in himself. Attentive readers would do well to remember Dumbledore''s warning about making the choice between "what is right and what is easy," and know that Rowling applies the same difficult principle to the conclusion of her series. While fans will find the answers to hotly speculated questions about Dumbledore, Snape, and you-know-who, it is a testament to Rowling''s skill as a storyteller that even the most astute and careful reader will be taken by surprise.
A spectacular finish to a phenomenal series, Harry Potter and the Deathly Hallows is a bittersweet read for fans. The journey is hard, filled with events both tragic and triumphant, the battlefield littered with the bodies of the dearest and despised, but the final chapter is as brilliant and blinding as a phoenix''s flame, and fans and skeptics alike will emerge from the confines of the story with full but heavy hearts, giddy and grateful for the experience. --Daphne Durham',
 25,
 9,
 9,
 290);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(19,
 5);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(19,
 10);
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
(20,
 '9781501134517',
 'The Course of Love: A Novel',
 null,
 '“An engrossing tale [that] provides plenty of food for thought” (People, Best New Books pick), this playful, wise, and profoundly moving second novel from the internationally bestselling author of How Proust Can Change Your Life tracks the beautifully complicated arc of a romantic partnership.
We all know the headiness and excitement of the early days of love. But what comes after? In Edinburgh, a couple, Rabih and Kirsten, fall in love. They get married, they have children—but no long-term relationship is as simple as “happily ever after.” The Course of Love explores what happens after the birth of love, what it takes to maintain, and what happens to our original ideals under the pressures of an average existence. We see, along with Rabih and Kirsten, the first flush of infatuation, the effortlessness of falling into romantic love, and the course of life thereafter. Interwoven with their story and its challenges is an overlay of philosophy—an annotation and a guide to what we are reading. As The New York Times says, “The Course of Love is a return to the form that made Mr. de Botton’s name in the mid-1990s….love is the subject best suited to his obsessive aphorizing, and in this novel he again shows off his ability to pin our hopes, methods, and insecurities to the page.”
This is a Romantic novel in the true sense, one interested in exploring how love can survive and thrive in the long term. The result is a sensory experience—fictional, philosophical, psychological—that urges us to identify deeply with these characters and to reflect on his and her own experiences in love. Fresh, visceral, and utterly compelling, The Course of Love is a provocative and life-affirming novel for everyone who believes in love. “There’s no writer alive like de Botton, and his latest ambitious undertaking is as enlightening and humanizing as his previous works” (Chicago Tribune).',
 26,
 10,
 10.79,
 290);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_author`
(`book_id`,
 `author_id`)
VALUES
(20,
 6);
/*------------------------------------------------------------------------*/
INSERT INTO `db_bookshop`.`book_category`
(`book_id`,
 `category_id`)
VALUES
(20,
 6);

