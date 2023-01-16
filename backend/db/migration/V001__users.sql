create table users
(
    created_at      timestamp not null default current_timestamp,
    updated_at      timestamp not null default current_timestamp,
    id              uuid not null,
    name            varchar(50) not null,
    email           varchar(100) not null,

    primary key (id),
    unique (email)
);

grant all on users to im;
comment on column users.email is '通知等の用途に使うためのEmail';