create table garage (
    id bigint not null auto_increment,
    sector varchar(255) not null,
    base_price decimal(19,2) not null,
    capacity int not null,
    primary key (id),
    unique key uk_garage_sector (sector)
);

create table spot (
    id bigint not null auto_increment,
    sector varchar(255) not null,
    latitude decimal(19,8) not null,
    longitude decimal(19,8) not null,
    status varchar(20) not null,
    primary key (id)
);

create table parking_session (
    id bigint not null auto_increment,
    plate varchar(255) not null,
    spot_id bigint not null,
    sector varchar(255) not null,
    entry_time datetime(6) not null,
    parked_time datetime(6) null,
    exit_time datetime(6) null,
    status varchar(20) not null,
    amount_paid decimal(19,2) not null,
    primary key (id)
);

create table revenue (
    id bigint not null auto_increment,
    date date not null,
    sector varchar(255) not null,
    amount decimal(19,2) not null,
    primary key (id),
    unique key uk_revenue_date_sector (date, sector)
);
