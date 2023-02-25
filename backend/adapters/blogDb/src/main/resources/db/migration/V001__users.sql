create table users
(
    created_at      timestamp not null default current_timestamp,
    updated_at      timestamp not null default current_timestamp,
    id              uuid not null,
    email           varchar(100) not null,
    name            varchar(50),

    primary key (id),
    unique (email)
);

grant all on users to im;