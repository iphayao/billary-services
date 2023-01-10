create table company (
    id                          uuid default uuid_generate_v4() primary key,
    code                        varchar(10) not null,
    name                        varchar(255) not null,
    name_en                     varchar(255) not null,
    address                     varchar(255) not null,
    address_en                  varchar(255) not null,
    tax_id                      varchar(13) not null,
    phone                       varchar(12) not null,
    website                     varchar(128) not null,
    office                      varchar(64) not null,
    company_logo                bytea null,
    company_stamp               bytea null,
    company_authorized_sign     bytea null,
    company_line_qr             bytea null,
    created_at                  timestamp,
    created_by                  varchar(255),
    updated_at                  timestamp,
    updated_by                  varchar(255)
);

create table document_type (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    name_en             varchar(255) not null,
    prefix              varchar(10) not null,
    description         varchar(255) not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table document (
    id                              uuid default uuid_generate_v4() primary key,
    company_id                      uuid not null,
    document_type_id                int not null,
    start_number                    int not null,
    start_month                     int not null,
    start_year                      int not null,
    created_at                      timestamp,
    created_by                      varchar(255),
    updated_at                      timestamp,
    updated_by                      varchar(255),
    foreign key(company_id)         references company(id),
    foreign key(document_type_id)   references document_type(id)
);

create table document_serial (
    id                  uuid default uuid_generate_v4() primary key,
    document_id         uuid not null,
    current_number      int not null,
    current_month       int not null,
    current_year        int not null,
    created_at          timestamp,
    created_by          varchar(255),
    updated_at          timestamp,
    updated_by          varchar(255),
    foreign key(document_id)        references document(id)
);

--drop table company cascade;
--drop table document_type cascade;
--drop table document_serial cascade;

// d01cfe4c-3a82-4341-a099-aca87f5b17d1

insert into company (id, code, name, name_en, address, address_en, tax_id, phone, website, office, created_at, created_by, updated_at, updated_by, version)
values ('d01cfe4c-3a82-4341-a099-aca87f5b17d1', 'T000000001', 'บริษัท เรดดี้ เปเปอร์ จำกัด', 'Ready Paper Co., Ltd.', '', '', '0135563017426', '084-142-1676', 'www.readypaper.co.th', 'สำนักงานใหญ่', current_timestamp, 'dba', current_timestamp, 'dba', 0);


insert into document_type (name, name_en, prefix, description, created_at, created_by, updated_at, updated_by)
values ('ใบกำกับภาษี', 'invoice', 'INV', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into document_type (name, name_en, prefix, description, created_at, created_by, updated_at, updated_by)
values ('ใบเสร็จรับเงิน', 'receipt', 'REV', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into document_type (name, name_en, prefix, description, created_at, created_by, updated_at, updated_by)
values ('ใบเสนอราคา', 'quotation', 'QT', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into document_type (name, name_en, prefix, description, created_at, created_by, updated_at, updated_by)
values ('ใบวางบิล', 'billing-note', 'BL', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into document_type (name, name_en, prefix, description, created_at, created_by, updated_at, updated_by)
values ('ใบสั่งของ', 'purchase-order', 'PO', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into document_type (name, name_en, prefix, description, created_at, created_by, updated_at, updated_by)
values ('ใบส่งของ', 'receive inventory', 'RI', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into document_type (name, name_en, prefix, description, created_at, created_by, updated_at, updated_by)
values ('ใบค่าใช้จ่าย', 'expense', 'EX', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into document_type (name, name_en, prefix, description, created_at, created_by, updated_at, updated_by)
values ('บันทึกรายการ', 'journal-entry', 'JV', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into document_type (name, name_en, prefix, description, created_at, created_by, updated_at, updated_by)
values ('บันทึกบัญชี', 'journal-voucher', 'JV', '', current_timestamp, 'dba', current_timestamp, 'dba');
