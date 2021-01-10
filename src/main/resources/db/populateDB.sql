DELETE
FROM user_roles;
DELETE FROM restaurant;
DELETE FROM dish;
DELETE FROM vote;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@mail.ru', '{noop}password'),
       ('Admin', 'admin@mail.ru', '{noop}password'),
       ('User2', 'user2@mail.ru', '{noop}password');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001),
       ('ROLE_USER', 100001),
       ('ROLE_USER', 100002);

INSERT INTO restaurant (name)
VALUES ('Restaurant1'),
       ('Restaurant2'),
       ('Restaurant3');

INSERT INTO dish (name, date, restaurant_id, price)
VALUES ('Dish1-Rest1', '2020-12-22', 100003, 100),
       ('Dish2-Rest1', '2020-12-22', 100003, 200),
       ('Dish3-Rest1', '2020-12-22', 100003, 300),
       ('Dish4-Rest2', CURRENT_DATE, 100004, 30),
       ('Dish5-Rest2', CURRENT_DATE, 100004, 200),
       ('Dish6-Rest3', CURRENT_DATE, 100005, 120),
       ('Dish7-Rest3', CURRENT_DATE, 100005, 10);

INSERT INTO vote (date, user_id, restaurant_id)
VALUES ('2020-12-22', 100000, 100004),
       ('2020-12-22', 100001, 100004),
       ('2020-12-23', 100000, 100003),
       ('2020-12-23', 100001, 100003),
       (now, 100000, 100003),
       (now, 100001, 100004);