create table receipt_status (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    code                varchar(5) not null,
    description         varchar(255) not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table receipt_payment_type (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    description         varchar(255) not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table receipt (
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
    foreign key (status_id)             references receipt_status(id),
    foreign key (company_id)            references company(id)
);

create table receipt_line_item (
    id                      uuid default uuid_generate_v4() primary key,
    receipt_id              uuid,
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
    foreign key (receipt_id)        references receipt(id),
    foreign key (item_product_id)   references product(id),
    foreign key (vat_type_id)       references product_vat_type(id)
);

create table receipt_payment (
    id                      uuid default uuid_generate_v4() primary key,
    receipt_id              uuid,
    paid_amount             numeric not null,
    payment_date            date not null,
    payment_type_id         int not null,
    remark                  varchar(255) null,
    created_at              timestamp,
    created_by              varchar(255),
    updated_at              timestamp,
    updated_by              varchar(255),
    foreign key (receipt_id)        references receipt(id),
    foreign key (payment_type_id)   references receipt_payment_type(id)
);

--drop table receipt_line_item;
--drop table receipt;

insert into receipt_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('DRAFT', '00001', '????????????????????????????????????????????????', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receipt_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('SUBMITTED', '00002', '?????????????????????????????????????????????????????????????????????', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receipt_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('AUTHORIZED', '00003', '????????????????????????????????????????????????', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receipt_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('PAID', '00004', '?????????????????????????????????????????????', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receipt_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('DELETED', '00005', '????????????????????????????????????????????????????????????', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receipt_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('VOIDED', '00006', '????????????????????????????????????????????????????????????????????????', current_timestamp, 'dba', current_timestamp, 'dba');

insert into receipt_payment_type (name, description, created_at, created_by, updated_at, updated_by) values ('CASH', '??????????????????', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receipt_payment_type (name, description, created_at, created_by, updated_at, updated_by) values ('BANK_ACCOUNT', '?????????????????????????????????', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receipt_payment_type (name, description, created_at, created_by, updated_at, updated_by) values ('BANK_ACCOUNT_KBANK', '????????????????????????????????????????????????', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receipt_payment_type (name, description, created_at, created_by, updated_at, updated_by) values ('BANK_ACCOUNT_SCB', '???????????????????????????????????????????????????????????????', current_timestamp, 'dba', current_timestamp, 'dba');
insert into receipt_payment_type (name, description, created_at, created_by, updated_at, updated_by) values ('CREDIT', '??????????????????', current_timestamp, 'dba', current_timestamp, 'dba');
