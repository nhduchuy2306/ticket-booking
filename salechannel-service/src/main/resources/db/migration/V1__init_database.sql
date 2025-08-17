create table if not exists salechannel
(
    id               varchar(255)                                                   not null primary key,
    commission_rate  double                                                         null,
    description      varchar(255)                                                   null,
    channel_name     varchar(255)                                                   null,
    status           enum('ACTIVE', 'INACTIVE')                                     null,
    channel_type     enum('API_PARTNER', 'BOX_OFFICE', 'MOBILE_APP', 'TICKET_SHOP') null,
    organization_id  varchar(255)                                                   null,
    start_sale_at    datetime(6)                                                    null,
    end_sale_at      datetime(6)                                                    null,
    change_timestamp datetime(6)                                                    null,
    change_user      varchar(255)                                                   null,
    create_timestamp datetime(6)                                                    null,
    create_user      varchar(255)                                                   null
);

create table if not exists salechannelconfig
(
    id               varchar(255) not null primary key,
    organization_id  varchar(255) null,
    config_data      TEXT         null,
    config_key       varchar(255) null,
    sale_channel_id  varchar(255) not null,
    change_timestamp datetime(6)  null,
    change_user      varchar(255) null,
    create_timestamp datetime(6)  null,
    create_user      varchar(255) null,
    foreign key (sale_channel_id) references salechannel (id)
);

create table if not exists salechannelevent
(
    id               varchar(255) not null primary key,
    event_id         varchar(255) not null,
    sale_channel_id  varchar(255) not null,
    change_timestamp datetime(6)  null,
    change_user      varchar(255) null,
    create_timestamp datetime(6)  null,
    create_user      varchar(255) null,
    foreign key (sale_channel_id) references salechannel (id)
);

create table if not exists salechannelorder
(
    id               varchar(255) not null primary key,
    order_id         varchar(255) not null,
    revenue          double       not null,
    sale_channel_id  varchar(255) not null,
    change_timestamp datetime(6)  null,
    change_user      varchar(255) null,
    create_timestamp datetime(6)  null,
    create_user      varchar(255) null,
    foreign key (sale_channel_id) references salechannel (id)
);

