
drop table if exists products cascade;
create table products (id bigserial, title varchar(255), description varchar(5000), price int, primary key(id));
insert into products
(title, description, price) values
('Cheese', 'Fresh cheese', 320),
('Milk', 'Fresh milk', 80),
('Apples', 'Fresh apples', 80),
('Bread', 'Fresh bread', 30);

drop table if exists categories cascade;
create table categories (id bigserial, title varchar(255), primary key(id));
insert into categories
(title) values
('Food'),
('Devices');

drop table if exists products_categories cascade;
create table products_categories (product_id bigint not null, category_id bigint not null, primary key(product_id, category_id),
foreign key (product_id) references products(id), foreign key (category_id) references categories(id));
insert into products_categories (product_id, category_id) values (1, 1), (2, 1), (3, 1), (4, 2);