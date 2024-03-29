CREATE TABLE IF NOT EXISTS users
(
    id_user bigint generated by default as identity primary key,
    name    varchar(200),
    email   varchar(200)
    --constraint user_name check (users.name IS NOT NULL AND users.name <> ''),
    --constraint user_email check (users.email IS NOT NULL AND users.email <> '')
);

CREATE TABLE IF NOT EXISTS categories
(
    id_category integer generated by default as identity primary key,
    name        varchar(200),
    constraint categories_name check (categories.name IS NOT NULL AND categories.name <> '')
);

CREATE TABLE IF NOT EXISTS locations
(
    id_location bigint generated by default as identity primary key,
    lat         float,
    lon         float
);

CREATE TABLE IF NOT EXISTS events
(
    id_event           bigint generated by default as identity primary key,
    annotation         varchar(2000),
    id_category        integer references categories (id_category), -- при удалении категории что с ивентом?
    description        varchar(7000),
    event_date         timestamp without time zone,
    created_on         timestamp without time zone,
    id_location        bigint references locations (id_location),
    paid               boolean,
    participant_limit  integer,
    request_moderation boolean,
    title              varchar(120),
    state              varchar,
    published_date     timestamp without time zone,
    id_initiator       bigint references users (id_user) on DELETE cascade,
    confirmed_requests integer
);

CREATE TABLE IF NOT EXISTS compilations
(
    id_compilation integer generated by default as identity primary key,
    pinned         boolean,
    title          varchar(200)
);

CREATE TABLE IF NOT EXISTS compilations_and_events
(
    id_compilation integer references compilations (id_compilation),
    id_event       bigint references events (id_event),
    primary key (id_compilation, id_event)
);

CREATE TABLE IF NOT EXISTS participation_requests
(
    id_participation_request bigint generated by default as identity primary key,
    id_requestor             bigint references users (id_user) on delete cascade,
    created                  timestamp without time zone,
    id_event                 bigint references events (id_event) on delete cascade,
    status                   character(50)
);

CREATE TABLE IF NOT EXISTS comments
(
    id_comment bigint generated by default as identity primary key,
    text       varchar(2000),
    id_user    bigint references users (id_user),
    id_event   bigint references events (id_event)
);
