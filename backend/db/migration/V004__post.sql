create table post
(
    created_at                      timestamp not null default current_timestamp,
    updated_at                      timestamp not null default current_timestamp,
    id                              uuid not null,
    title                           varchar(255) not null,
    content                         text not null,
    status                          smallint not null,
    published_at                    timestamp,

    primary key (id)
);

grant all on post to im;