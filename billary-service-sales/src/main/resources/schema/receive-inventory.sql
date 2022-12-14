create table receive_inventory_status (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    code                varchar(5) not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table receive_inventory (
    id                      uuid default uuid_generate_v4() primary key,
    document_id             varchar(12) not null,
    reference               varchar(20) not null,
    contact_id              UUID not null,
    issued_date             date not null,
    delivery_date           date null,
    line_vat_type_id        int not null,
    status_id               int not null,
    sent_to_contact         bool null,
    attention_to            varchar(255) null,
    delivery_address        varchar(255) null,
    expect_arrive_date      date null,
    sub_total               numeric not null,
    total_discount          numeric not null,
    total_after_discount    numeric not null,
    exempt_vat_amount       numeric not null,
    vatable_amount          numeric not null,
    vat_amount              numeric not null,
    total                   numeric not null,
    created_at              timestamp,
    created_by              varchar(255),
    updated_at              timestamp,
    updated_by              varchar(255),
    foreign key (contact_id)            references contact(id),
    foreign key (line_vat_type_id)      references product_vat_type(id),
    foreign key (status_id)             references receive_inventory_status(id)
);

create table receive_inventory_line_item (
    id                      uuid default uuid_generate_v4() primary key,
    receive_inventory_id    uuid,
    item_order              int not null,
    item_product_id         uuid,
    description             varchar(255),
    quantity                int not null,
    unit_name               varchar(255) not null,
    vat_type_id             int not null,
    unit_price              numeric not null,
    total_amount            numeric not null,
    discount_amount         numeric not null,
    exempt_vat_amount       numeric not null,
    vatable_amount          numeric not null,
    vat_amount              numeric not null,
    line_amount             numeric not null,
    created_at              timestamp,
    created_by              varchar(255),
    updated_at              timestamp,
    updated_by              varchar(255),
    foreign key (receive_inventory_id)  references receive_inventory(id),
    foreign key (item_product_id)       references product(id),
    foreign key (vat_type_id)           references product_vat_type(id)
);

--drop table receive_inventory;
--drop table receive_inventory_line_item;

insert into receive_inventory_status (name, code, created_at, created_by, updated_at, updated_by) values ('DRAFT', '00001', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receive_inventory_status (name, code, created_at, created_by, updated_at, updated_by) values ('SUBMITTED', '00002', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receive_inventory_status (name, code, created_at, created_by, updated_at, updated_by) values ('AUTHORIZED', '00003', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receive_inventory_status (name, code, created_at, created_by, updated_at, updated_by) values ('DELETED', '00004', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receive_inventory_status (name, code, created_at, created_by, updated_at, updated_by) values ('VOIDED', '00005', current_timestamp, 'dba', current_timestamp, 'dba');
