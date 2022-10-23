create table todo
(
    created_at      timestamp not null default current_timestamp,
    updated_at      timestamp not null default current_timestamp,
    id              uuid not null,
    status          varchar(20) not null,
    value           varchar(1000) not null,

    primary key (id),
    foreign key (status) references todo_status (status) on update cascade
);

grant all on todo to im;
