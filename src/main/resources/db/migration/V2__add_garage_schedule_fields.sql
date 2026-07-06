alter table garage
    add column open_hour varchar(5) null,
    add column close_hour varchar(5) null,
    add column duration_limit_minutes int null;