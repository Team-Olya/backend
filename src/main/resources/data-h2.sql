insert into kind (kind) values ('QA');
insert into kind (kind) values ('JS Developer');
insert into kind (kind) values ('Java Developer');
insert into kind (kind) values ('Project Manager');

insert into talent (name, surname, email, password, kind_id, experience, description, location)
values ('Olha', 'Shutylieva', 'os@mail.com', '$2a$12$twzKjMPahMiUZ/JumzboTOx9oqJnN4RX7B6hUoN7s5NajdLgygJr6', 4, 5,
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',
        'Ukraine');
insert into talent (name, surname, email, password, kind_id, experience, description, location)
values ('Alexey', 'Pedun', 'ap@mail.com', '$2a$12$twzKjMPahMiUZ/JumzboTOx9oqJnN4RX7B6hUoN7s5NajdLgygJr6', 1, 5,
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',
        'Ukraine');
insert into talent (name, surname, email, password, kind_id, experience, description, location)
values ('Ekaterina', 'Nikitenko', 'en@mail.com', '$2a$12$twzKjMPahMiUZ/JumzboTOx9oqJnN4RX7B6hUoN7s5NajdLgygJr6', 1, 5,
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',
        'Ukraine');
insert into talent (name, surname, email, password, kind_id, experience, description, location)
values ('Anastasiia', 'Mashchenko', 'am@mail.com', '$2a$12$twzKjMPahMiUZ/JumzboTOx9oqJnN4RX7B6hUoN7s5NajdLgygJr6', 1, 5,
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',
        'Ukraine');
insert into talent (name, surname, email, password, kind_id, experience, description, location)
values ('Max', 'Koropets', 'mk@mail.com', '$2a$12$twzKjMPahMiUZ/JumzboTOx9oqJnN4RX7B6hUoN7s5NajdLgygJr6', 1, 5,
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',
        'Ukraine');
insert into talent (name, surname, email, password, kind_id, experience, description, location)
values ('Bohdan', 'Rohozianskyi', 'br@mail.com', '$2a$12$twzKjMPahMiUZ/JumzboTOx9oqJnN4RX7B6hUoN7s5NajdLgygJr6', 2, 5,
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',
        'Ukraine');
insert into talent (name, surname, email, password, kind_id, experience, description, location)
values ('Maksym', 'Lavrovskyi', 'ml@mail.com', '$2a$12$twzKjMPahMiUZ/JumzboTOx9oqJnN4RX7B6hUoN7s5NajdLgygJr6', 2, 5,
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',
        'Ukraine');
insert into talent (name, surname, email, password, kind_id, experience, description, location)
values ('Dmytro', 'Kataiev', 'dk@mail.com', '$2a$12$twzKjMPahMiUZ/JumzboTOx9oqJnN4RX7B6hUoN7s5NajdLgygJr6', 2, 5,
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',
        'Ukraine');
insert into talent (name, surname, email, password, kind_id, experience, description, location)
values ('Vladyslav', 'Liubchyk', 'vl@mail.com', '$2a$12$twzKjMPahMiUZ/JumzboTOx9oqJnN4RX7B6hUoN7s5NajdLgygJr6', 3, 5,
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',
        'Ukraine');
insert into talent (name, surname, email, password, kind_id, experience, description, location)
values ('Yaroslava', 'Nechaieva', 'yn@mail.com', '$2a$12$twzKjMPahMiUZ/JumzboTOx9oqJnN4RX7B6hUoN7s5NajdLgygJr6', 3, 5,
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',
        'Ukraine');
insert into talent (name, surname, email, password, kind_id, experience, description, location)
values ('Sofiia', 'Kazantseva', 'sk@mail.com', '$2a$12$twzKjMPahMiUZ/JumzboTOx9oqJnN4RX7B6hUoN7s5NajdLgygJr6', 3, 5,
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',
        'Ukraine');

insert into link (url, talent_id) values ('https://google.com', 1);
insert into link (url, talent_id) values ('https://bing.com', 2);
insert into link (url, talent_id) values ('https://discord.com', 2);
insert into link (url, talent_id) values ('https://google.com', 3);
insert into link (url, talent_id) values ('https://twitch.tv', 4);
insert into link (url, talent_id) values ('https://youtube.com', 4);
insert into link (url, talent_id) values ('https://reddit.com', 5);
insert into link (url, talent_id) values ('https://twitter.com', 6);
insert into link (url, talent_id) values ('https://telegram.com', 7);
insert into link (url, talent_id) values ('https://spotify.com', 7);
insert into link (url, talent_id) values ('https://linkedin.com', 8);
insert into link (url, talent_id) values ('https://github.com', 9);
insert into link (url, talent_id) values ('https://dota2.com', 10);
insert into link (url, talent_id) values ('https://blizzard.com', 10);
insert into link (url, talent_id) values ('https://softserveinc.com', 11);

insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Java Spring Boot project', 'lil morty', 1, 'PUBLISHED');
insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'My c++ project', '<git hub link>', 1, 'DRAFT');
insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Java Hibernate project', '<git hub link>', 1, 'HIDDEN');

insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Java Spring Boot project', 'lil morty', 2, 'PUBLISHED');
insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'My c++ project', '<git hub link>', 2, 'DRAFT');
insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Java Hibernate project', '<git hub link>', 2, 'HIDDEN');

insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Dota 2 profile', 'https://store.steampowered.com', 3, 'PUBLISHED');
insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'My c++ project', '<git hub link>', 3, 'DRAFT');
insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Java Hibernate project', '<git hub link>', 3, 'HIDDEN');

insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Counter Strike profile', '<steam link>', 4, 'PUBLISHED');

insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'My web site', 'https://spotify.com', 5, 'DRAFT');
insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Java Hibernate project', '<git hub link>', 5, 'HIDDEN');

insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'My company web site', 'https://softserveinc.com', 6, 'DRAFT');
insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Java Hibernate project', '<git hub link>', 6, 'HIDDEN');

insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'My c++ project', '<git hub link>', 7, 'DRAFT');

insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'My Song', 'kalush - stefania', 8, 'DRAFT');

insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Counter Strike profile', '<steam link>', 9, 'PUBLISHED');
insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Counter Strike profile', '<steam link>', 9, 'HIDDEN');
insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Counter Strike profile', '<steam link>', 9, 'DRAFT');

insert into proof (publication_date, title, description, talent_id, status)
values (CURRENT_TIMESTAMP, 'Java Hibernate project', '<git hub link>', 10, 'HIDDEN');