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

insert into link (url, talent_id) values ('google.com', 1);
insert into link (url, talent_id) values ('bing.com', 2);
insert into link (url, talent_id) values ('discord.com', 2);
insert into link (url, talent_id) values ('google.com', 3);
insert into link (url, talent_id) values ('twitch.tv', 4);
insert into link (url, talent_id) values ('youtube.com', 4);
insert into link (url, talent_id) values ('reddit.com', 5);
insert into link (url, talent_id) values ('twitter.com', 6);
insert into link (url, talent_id) values ('telegram.com', 7);
insert into link (url, talent_id) values ('spotify.com', 7);
insert into link (url, talent_id) values ('linkedin.com', 8);
insert into link (url, talent_id) values ('github.com', 9);
insert into link (url, talent_id) values ('dota2.com', 10);
insert into link (url, talent_id) values ('blizzard.com', 10);
insert into link (url, talent_id) values ('softserveinc.com', 11);