create table session
(
    created_at                      timestamp not null default current_timestamp,
    updated_at                      timestamp not null default current_timestamp,
    id                              varchar(50) not null,
    user_id                         uuid not null,
    expire_at                       timestamp not null,

    primary key (id),
    foreign key (user_id) references users (id) on delete cascade
);

grant all on session to im;

CREATE UNIQUE INDEX ON session (user_id);