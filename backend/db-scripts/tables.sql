create table account
(
    username     varchar(20)
        constraint user_pk
            primary key,
    email        varchar(100)          not null,
    full_name    varchar(60)           not null,
    password     varchar(20)           not null,
    is_logged_in boolean default FALSE not null
);


create table ad
(
    ad_id              serial
        constraint ad_pk
            primary key,
    address            varchar(100) not null,
    pickup_date        date         not null,
    pickup_time_start  time         not null,
    pickup_time_end    time         not null,
    dropoff_date       date         not null,
    dropoff_time_start time         not null,
    dropoff_time_end   time         not null,
    weight_kg          float,
    zipcode            varchar(6)   not null,
    clothing_desc      text         not null,
    special_instr      text default '',
    bleach             boolean,
    iron               boolean,
    fold               boolean,
    phone_num          varchar(20)  not null,
    creator            varchar(20)  not null
        constraint ad_creator___fk
            references account
);

create table bid
(
    ad_id            integer                        not null
        constraint bid_ad___fk
            references ad,
    creator_username varchar(20)                    not null
        constraint bid_account___fk
            references account,
    bid_amount       FLOAT                          not null,
    date_created     timestamp default '2021-02-21' not null,
    CONSTRAINT bid_pk
        PRIMARY KEY (ad_id, creator_username)
);