create table journal_entry_status (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    code                varchar(5) not null,
    description         varchar(255) not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table journal_entry_payment_type (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    description         varchar(255) not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table vat_type (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    description         varchar(255),
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table journal_entry (
    id                          uuid default uuid_generate_v4() primary key,
    document_id                 varchar(15) not null,
    reference                   varchar(20) not null,
    contact_id                  uuid not null,
    issued_date                 date not null,
    line_vat_type_id            int not null,
    status_id                   int not null,
    vat_inclusive               bool not null,
    use_input_tax               bool not null,
    sub_total                   numeric not null,
    total_discount              numeric not null,
    total_after_discount        numeric not null,
    exempt_vat_amount           numeric not null,
    vatable_amount              numeric not null,
    vat_amount                  numeric not null,
    total                       numeric not null,
    payment_amount              numeric not null,
    remark                      text null,
    company_id                  uuid not null,
    created_at                  timestamp,
    created_by                  varchar(255),
    updated_at                  timestamp,
    updated_by                  varchar(255),
    foreign key (contact_id)                    references contact(id),
    foreign key (line_vat_type_id)              references vat_type(id),
    foreign key (status_id)                     references journal_entry_status(id),
    foreign key (company_id)                    references company(id)
);

create table journal_entry_payment (
    id                      uuid default uuid_generate_v4() primary key,
    journal_entry_id        uuid,
    paid_amount             numeric not null,
    payment_date            date not null,
    payment_type_id         int not null,
    remark                  varchar(255) null,
    created_at              timestamp,
    created_by              varchar(255),
    updated_at              timestamp,
    updated_by              varchar(255),
    foreign key (journal_entry_id)  references journal_entry(id),
    foreign key (payment_type_id)   references journal_entry_payment_type(id)
);

create table journal_entry_line_item (
    id                      uuid default uuid_generate_v4() primary key,
    journal_entry_id        uuid,
    debit_account_chart_id  uuid,
    credit_account_chart_id uuid,
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
    foreign key (journal_entry_id)          references journal_entry(id),
    foreign key (vat_type_id)               references vat_type(id),
    foreign key (debit_account_chart_id)    references account_chart(id),
    foreign key (credit_account_chart_id)   references account_chart(id)
);

--drop table invoice_line_item;
--drop table invoice;

insert into expense_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('DRAFT', '00001', 'สร้างแบบร่างแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
insert into expense_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('SUBMITTED', '00002', 'สร้างค่าใช้จ่ายแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
insert into expense_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('PAID', '00003', 'ชำระค่าใช้จ่ายแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
insert into expense_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('DELETED', '00004', 'ลบค่าใช้จ่ายแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
insert into expense_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('VOIDED', '00005', 'ยกเลิกค่าใช้จ่ายแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');

insert into expense_payment_type (name, description, created_at, created_by, updated_at, updated_by) values ('CASH', 'เงินสด', current_timestamp, 'dba', current_timestamp, 'dba');
insert into expense_payment_type (name, description, created_at, created_by, updated_at, updated_by) values ('BANK_ACCOUNT', 'บัญชีธนาคาร', current_timestamp, 'dba', current_timestamp, 'dba');
insert into expense_payment_type (name, description, created_at, created_by, updated_at, updated_by) values ('BANK_ACCOUNT_KBANK', 'บัญชีธนาคารกสิกร', current_timestamp, 'dba', current_timestamp, 'dba');
insert into expense_payment_type (name, description, created_at, created_by, updated_at, updated_by) values ('BANK_ACCOUNT_SCB', 'บัญชีธนาคารไทยพาณิชย์', current_timestamp, 'dba', current_timestamp, 'dba');
insert into expense_payment_type (name, description, created_at, created_by, updated_at, updated_by) values ('CREDIT', 'เครดิต', current_timestamp, 'dba', current_timestamp, 'dba');

insert into vat_type (name, description, created_at, created_by, updated_at, updated_by) values('ราคารวมภาษีมูลค่าเพิ่มแล้ว', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into vat_type (name, description, created_at, created_by, updated_at, updated_by) values('ราคาไม่รวมภาษีมูลค่าเพิ่ม', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into vat_type (name, description, created_at, created_by, updated_at, updated_by) values('ภาษีมูลค่าเพิ่ม 0%', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into vat_type (name, description, created_at, created_by, updated_at, updated_by) values('ยกเว้นภาษีมูลค่าเพิ่ม', '', current_timestamp, 'dba', current_timestamp, 'dba');

