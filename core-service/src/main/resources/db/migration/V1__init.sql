create table products
(
    id         bigserial primary key,
    title      varchar(255)  not null unique ,
    price      numeric(8, 2) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table categories
(
    id         bigserial primary key,
    title      varchar(255) not null unique,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table products_categories
(
    product_id  bigint not null references products (id),
    category_id bigint not null references categories (id),
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp,
    primary key (product_id, category_id)
);

insert into products (title, price)
values ('Product#1', 500),
       ('Product#2', 200),
       ('Product#3', 400),
       ('Product#4', 600),
       ('Product#5', 900),
       ('Product#6', 350),
       ('Product#7', 650),
       ('Product#8', 800),
       ('Product#9', 1000),
       ('Product#10', 430),
       ('Product#11', 320);

insert into categories (title)
values ('Category#1'),
       ('Category#2'),
       ('Category#3'),
       ('Category#4'),
       ('Category#5');

insert into products_categories (product_id, category_id)
values (1, 1),
       (2, 1),
       (3, 2),
       (4, 2),
       (5, 3),
       (6, 3),
       (7, 4),
       (8, 4),
       (9, 5),
       (10, 5),
       (11, 2),
       (1, 2),
       (2, 2),
       (3, 3),
       (4, 3),
       (5, 5);

create table orders
(
    id           bigserial primary key,
    username     varchar(255)  not null,
    total_price  numeric(8, 2) not null,
    address      varchar(255),
    phone        varchar(255),
    order_status varchar(20)   not null default 'CREATED',
    created_at   timestamp default current_timestamp,
    updated_at   timestamp default current_timestamp
);

create table order_items
(
    id                bigserial primary key,
    product_id        bigint        not null references products (id),
    order_id          bigint        not null references orders (id),
    quantity          int           not null,
    price_per_product numeric(8, 2) not null,
    price             numeric(8, 2) not null,
    created_at        timestamp default current_timestamp,
    updated_at        timestamp default current_timestamp
);