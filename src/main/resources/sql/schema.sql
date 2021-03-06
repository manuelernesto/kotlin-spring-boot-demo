CREATE TABLE if not exists AUTHOR
(
    id    bigint primary key auto_increment,
    name  varchar(50) not null,
    birth varchar(20)
);