-- CREATE DATABASE coin_desk_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE SCHEMA IF NOT EXISTS coin_desk_db;
SET SCHEMA coin_desk_db;

create table if not exists currency_info
(
    id         bigint auto_increment
    primary key,
    updated_at datetime(3)  not null,
    chart_name varchar(255) null,
    disclaimer varchar(255) null
    );

create table if not exists country_time_info
(
    id               bigint auto_increment
    primary key,
    updated          datetime(6) null,
    updated_iso      datetime(6) null,
    updated_uk       datetime(6) null,
    currency_info_id bigint      not null,
    constraint UK_ej4i7oa08ph86n3aroee5w931
    unique (currency_info_id),
    constraint FK1sp117gea9mrvmxtyf8qx9ubt
    foreign key (currency_info_id) references currency_info (id)
    );

create table if not exists currency_detail
(
    id               bigint auto_increment
    primary key,
    code             varchar(255)   null,
    description      varchar(255)   null,
    rate             varchar(255)   null,
    rate_float       decimal(11, 4) null,
    symbol           varchar(255)   null,
    currency_info_id bigint         not null,
    constraint uk_info_id_price_detail_code
    unique (currency_info_id, code),
    constraint FK6blcoq6wfv5k6i34sko1m2u8y
    foreign key (currency_info_id) references currency_info (id)
    );

