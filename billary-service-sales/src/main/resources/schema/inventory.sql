create table inventory_location (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    code                varchar(5) not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table inventory_movement_type (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    code                varchar(5) not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table inventory (
    id                  uuid default uuid_generate_v4() primary key,
    product_id          uuid not null,
    location_id         int not null,
    current_qty         int not null,
    current_price       numeric not null,
    initial_qty         int not null,
    initial_price       numeric not null,
    created_at          timestamp,
    created_by          varchar(255),
    updated_at          timestamp,
    updated_by          varchar(255),
    foreign key (product_id)    references product(id),
    foreign key (location_id)   references inventory_location(id)
);

create table inventory_movement (
    id                  uuid default uuid_generate_v4() primary key,
    reference           varchar(255),
    movement_type_id    int not null,
    inventory_id        uuid not null,
    date                timestamp,
    quantity            int not null,
    remaining           int not null,
    unit_price          numeric not null,
    total_amount        numeric not null,
    created_at          timestamp,
    created_by          varchar(255),
    updated_at          timestamp,
    updated_by          varchar(255),
    foreign key (inventory_id)      references inventory(id),
    foreign key (movement_type_id)  references inventory_movement_type(id)
);

--create table stock_history (
--    id                  int generated by default as identity primary key,
--    product_id          int not null,
--    location_id         int not null,
--    current_qty         int not null,
--    current_price       numeric not null,
--    initial_qty         int not null,
--    initial_price       numeric not null,
--    created_at          timestamp,
--    created_by          varchar(255),
--    updated_at          timestamp,
--    updated_by          varchar(255),
--    version             int
--);

--drop table stock_movement;
--drop table stock;
--drop table stock_movement_type;
--drop table stock_location;
--drop table stock_history;

insert into inventory_location (name, code, created_at, created_by, updated_at, updated_by)
values('????????????????????????????????????', '00000' , current_timestamp, 'dba', current_timestamp, 'dba');
insert into inventory_location (name, code, created_at, created_by, updated_at, updated_by)
values('???????????????????????????????????????', '00001' , current_timestamp, 'dba', current_timestamp, 'dba');

insert into inventory_movement_type (name, code, created_at, created_by, updated_at, updated_by)
values('?????????', '00001' , current_timestamp, 'dba', current_timestamp, 'dba');
insert into inventory_movement_type (name, code, created_at, created_by, updated_at, updated_by)
values('????????????', '00002' , current_timestamp, 'dba', current_timestamp, 'dba');