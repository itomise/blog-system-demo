create table users
(
    created_at      timestamptz not null default current_timestamp,
    updated_at      timestamptz not null default current_timestamp,
    id              varchar(255) not null primary key,
    name            varchar(255) not null,
    email           varchar(255) not null
);

grant all on users to im;
create index users_id on users (id);
