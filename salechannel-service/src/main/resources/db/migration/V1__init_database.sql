create table if not exists CHANELSALES
(
    id                 varchar(255) not null primary key,
    change_timestamp   datetime(6)  null,
    change_user        varchar(255) null,
    create_timestamp   datetime(6)  null,
    create_user        varchar(255) null,
    channel_id         varchar(255) not null,
    commission_earned  double       null,
    event_id           varchar(255) not null,
    sale_date          datetime(6)  null,
    total_revenue      double       null,
    total_tickets_sold int          null
);

create table if not exists CHANNELPRICING
(
    id                varchar(255) not null
        primary key,
    change_timestamp  datetime(6)  null,
    change_user       varchar(255) null,
    create_timestamp  datetime(6)  null,
    create_user       varchar(255) null,
    channel_id        varchar(255) not null,
    is_active         bit          null,
    markup_percentage double       null,
    special_price     double       null,
    ticket_type_id    varchar(255) not null,
    valid_from        datetime(6)  null,
    valid_until       datetime(6)  null
);

create table if not exists EVENTINFO
(
    id               varchar(255)                                                                           not null primary key,
    change_timestamp datetime(6)                                                                            null,
    change_user      varchar(255)                                                                           null,
    create_timestamp datetime(6)                                                                            null,
    create_user      varchar(255)                                                                           null,
    description      varchar(255)                                                                           null,
    door_close_time  datetime(6)                                                                            null,
    door_open_time   datetime(6)                                                                            null,
    end_time         datetime(6)                                                                            null,
    name             varchar(255)                                                                           null,
    organizer        varchar(255)                                                                           null,
    start_time       datetime(6)                                                                            null,
    status           enum ('CANCELLED', 'COMPLETED', 'DRAFT', 'PENDING_APPROVAL', 'POSTPONED', 'PUBLISHED') null,
    venue            varchar(255)                                                                           null
);

create table if not exists SALECHANNEL
(
    id               varchar(255)                                                    not null primary key,
    change_timestamp datetime(6)                                                     null,
    change_user      varchar(255)                                                    null,
    create_timestamp datetime(6)                                                     null,
    create_user      varchar(255)                                                    null,
    channel_name     varchar(255)                                                    null,
    channel_type     enum ('API_PARTNER', 'BOX_OFFICE', 'MOBILE_APP', 'TICKET_SHOP') null,
    commission_rate  double                                                          null,
    description      varchar(255)                                                    null,
    is_active        bit                                                             null
);

create table if not exists BOXOFFICES
(
    id               varchar(255) not null primary key,
    change_timestamp datetime(6)  null,
    change_user      varchar(255) null,
    create_timestamp datetime(6)  null,
    create_user      varchar(255) null,
    location_address varchar(255) null,
    location_name    varchar(255) null,
    opening_hours    varchar(255) null,
    sale_channel_id  varchar(255) not null unique,
    foreign key (sale_channel_id) references salechannel (id)
);

create table if not exists CHANNELEVENT
(
    channel_id varchar(255) not null,
    event_id   varchar(255) not null,
    foreign key (event_id) references eventinfo (id),
    foreign key (channel_id) references salechannel (id)
);

create table if not exists EVENTSALECHANNEL
(
    id               varchar(255) not null primary key,
    change_timestamp datetime(6)  null,
    change_user      varchar(255) null,
    create_timestamp datetime(6)  null,
    create_user      varchar(255) null,
    custom_url       varchar(255) null,
    event_id         bigint       null,
    is_published     bit          null,
    sale_channel_id  varchar(255) null,
    foreign key (sale_channel_id) references salechannel (id)
);

create table if not exists TICKETSHOP
(
    id                   varchar(255) not null
        primary key,
    change_timestamp     datetime(6)  null,
    change_user          varchar(255) null,
    create_timestamp     datetime(6)  null,
    create_user          varchar(255) null,
    allow_guest_checkout bit          null,
    banner_url           varchar(255) null,
    contact_email        varchar(255) null,
    contact_phone        varchar(255) null,
    custom_domain        varchar(255) null,
    facebook_url         varchar(255) null,
    instagram_url        varchar(255) null,
    logo_url             varchar(255) null,
    meta_description     varchar(500) null,
    meta_title           varchar(255) null,
    organizer_id         varchar(255) not null,
    primary_color        varchar(255) null,
    privacy_url          varchar(255) null,
    require_account      bit          null,
    secondary_color      varchar(255) null,
    show_sold_out_events bit          null,
    subdomain            varchar(255) not null unique,
    terms_url            varchar(255) null,
    twitter_url          varchar(255) null,
    sale_channel_id      varchar(255) not null unique,
    foreign key (sale_channel_id) references salechannel (id)
);

create table if not exists TICKETSHOPTEMPLATES
(
    id               varchar(255)  not null primary key,
    change_timestamp datetime(6)   null,
    change_user      varchar(255)  null,
    create_timestamp datetime(6)   null,
    create_user      varchar(255)  null,
    description      varchar(1000) null,
    event_card_style varchar(255)  null,
    footer_style     varchar(255)  null,
    header_style     varchar(255)  null,
    is_default       bit           null,
    name             varchar(255)  not null,
    primary_color    varchar(255)  null,
    secondary_color  varchar(255)  null,
    thumbnail        varchar(255)  null
);