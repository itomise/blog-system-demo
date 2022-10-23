create table users
(
    created_at      timestamp not null default current_timestamp,
    updated_at      timestamp not null default current_timestamp,
    id              uuid not null,
    name            varchar(255) not null,
    email           varchar(255) not null,

    primary key (id),
    unique (email)
);

grant all on users to im;
