create table todo_status
(
    status      varchar(20) not null,

    primary key (status)
);

grant all on todo_status to im;

insert into todo_status
values ('NEW'), ('DOING'), ('DONE');