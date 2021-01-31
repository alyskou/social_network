-- select * from users where first_name like 'fa%' and last_name like 'ca%';
-- explain format = json select * from users where first_name like 'fa%' and last_name like 'ca%';

-- create index users_first_last on users (first_name(5), last_name(5));
