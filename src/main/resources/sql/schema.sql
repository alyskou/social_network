drop table users;
drop table friendships;
drop table news;

create table users (
    email      varchar(200) not null,
    password   varchar(200) not null,
    first_name varchar(200) not null,
    last_name  varchar(200) not null,
    sex varchar(200) not null,
    age int,
    interests varchar(200) not null,
    city varchar(200) not null,
    primary key (email)
);

create table friendships (
    from_email varchar(200) not null,
    to_email varchar(200) not null,
    primary key (from_email, to_email)
);

create table news (
    author_email varchar(200) not null,
    timestamp timestamp not null,
    text varchar(1000) not null,
    primary key (author_email, timestamp)
);
