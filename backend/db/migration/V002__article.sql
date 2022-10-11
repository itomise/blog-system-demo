create table article
(
    created_at      timestamptz not null default current_timestamp,
    updated_at      timestamptz not null default current_timestamp,
    id              uuid not null,
    type            varchar(20) not null primary key,
    content         text not null
);

grant all on article to im;
create index article_id on article (id);
