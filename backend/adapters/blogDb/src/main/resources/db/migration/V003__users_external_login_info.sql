create table users_external_login_info
(
    created_at                      timestamp not null default current_timestamp,
    updated_at                      timestamp not null default current_timestamp,
    user_id                         uuid not null,
    external_service_type           smallint not null,

    primary key (user_id),
    foreign key (user_id) references users (id) on delete cascade
);

grant all on users_external_login_info to im;