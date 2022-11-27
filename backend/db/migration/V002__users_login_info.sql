create table users_login_info
(
    created_at                      timestamp not null default current_timestamp,
    updated_at                      timestamp not null default current_timestamp,
    user_id                         uuid not null,
    email                           varchar(100) not null,
    password_hash                   varchar(255) not null,
    password_salt                   varchar(100) not null,
    hash_algorithm_id               int not null,
    email_validation_status         int not null,
    confirmation_token              varchar(100),
    confirmation_token_expires      timestamp,
    password_recovery_token         varchar(100),
    password_recovery_token_expires timestamp,

    primary key (user_id),
    unique (email),
    foreign key (user_id) references users (id) on delete cascade
);

grant all on users to im;
comment on column users_login_info.email is '認証用のEmail';