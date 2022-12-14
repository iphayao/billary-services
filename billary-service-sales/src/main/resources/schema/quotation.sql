create table quotation_status (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    code                varchar(5) not null,
    description         varchar(255) not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table quotation (
    id                      uuid default uuid_generate_v4() primary key,
    document_id             varchar(15) not null,
    reference               varchar(20) not null,
    contact_id              uuid not null,
    issued_date             date not null,
    due_date                date null,
    credit_day              int null,
    line_vat_type_id        int not null,
    status_id               int not null,
    sale_name               varchar(255) null,
    sale_channel            varchar(255) null,
    sent_to_contact         bool null,
    expect_payment_date     date null,
    planned_payment_date    date null,
    sub_total               numeric not null,
    total_discount          numeric not null,
    total_after_discount    numeric not null,
    exempt_vat_amount       numeric not null,
    vatable_amount          numeric not null,
    vat_amount              numeric not null,
    total                   numeric not null,
    remark                  text null,
    company_id              uuid not null,
    created_at              timestamp,
    created_by              varchar(255),
    updated_at              timestamp,
    updated_by              varchar(255),
    foreign key (contact_id)            references contact(id),
    foreign key (line_vat_type_id)      references product_vat_type(id),
    foreign key (status_id)             references quotation_status(id),
    foreign key (company_id)            references company(id)
);

create table quotation_line_item (
    id                      uuid default uuid_generate_v4() primary key,
    quotation_id            uuid,
    item_product_id         uuid,
    item_order              int not null,
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
    foreign key (quotation_id)        references quotation(id),
    foreign key (item_product_id)   references product(id),
    foreign key (vat_type_id)       references product_vat_type(id)
);

--drop table quotation_line_item;
--drop table quotation;

insert into quotation_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('DRAFT', '00001', 'สร้างแบบร่างแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
insert into quotation_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('SUBMITTED', '00002', 'สร้างใบเสนอราคาแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
insert into quotation_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('AUTHORIZED', '00003', 'สร้างใบวางบิลแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
insert into quotation_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('DELETED', '00004', 'ลบใบเสนอราคาแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
insert into quotation_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('VOIDED', '00005', 'ยกเลิกเสนอราคาแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
