-- Auteurs
INSERT INTO authors (name, nationality, birth_year) VALUES ('Victor Hugo',   'Française',   1802);
INSERT INTO authors (name, nationality, birth_year) VALUES ('George Orwell', 'Britannique', 1903);
INSERT INTO authors (name, nationality, birth_year) VALUES ('J.K. Rowling',  'Britannique', 1965);

-- Livres
INSERT INTO books (title, isbn, category, author_id, available) VALUES ('Les Misérables',              '978-2-07-040922-8',  'Fiction',                       1, TRUE);
INSERT INTO books (title, isbn, category, author_id, available) VALUES ('Notre-Dame de Paris',         '978-2-07-041769-8',  'Fiction',                       1, TRUE);
INSERT INTO books (title, isbn, category, author_id, available) VALUES ('1984',                        '978-0-14-103614-4',  'Dystopie',                      2, TRUE);
INSERT INTO books (title, isbn, category, author_id, available) VALUES ('Animal Farm',                 '978-0-14-103613-7',  'Satire',                        2, TRUE);
INSERT INTO books (title, isbn, category, author_id, available) VALUES ('Harry Potter à l''école des sorciers', '978-2-07-064302-8', 'Fantasy', 3, TRUE);

-- Membres
INSERT INTO members (name, email, phone) VALUES ('Alice Martin', 'alice.martin@email.com', '0612345678');
INSERT INTO members (name, email)        VALUES ('Bob Bernard',  'bob.bernard@email.com');
