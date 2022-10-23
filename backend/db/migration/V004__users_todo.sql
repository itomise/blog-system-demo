create table users_todo
(
    created_at      timestamp not null default current_timestamp,
    updated_at      timestamp not null default current_timestamp,
    users_id        uuid not null,
    todo_id         uuid not null,

    primary key (users_id, todo_id),
    foreign key (users_id) references users (id) on delete cascade,
    foreign key (todo_id) references todo (id) on delete cascade
);

grant all on users_todo to im;