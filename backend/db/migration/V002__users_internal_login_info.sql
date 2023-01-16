create table users_internal_login_info
(
    created_at                      timestamp not null default current_timestamp,
    updated_at                      timestamp not null default current_timestamp,
    user_id                         uuid not null,
    password_hash                   varchar(255) not null,
    password_salt                   varchar(100) not null,
    hash_algorithm_id               int not null,

    primary key (user_id),
    foreign key (user_id) references users (id) on delete cascade
);

grant all on users_internal_login_info to im;