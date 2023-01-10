create table journal_voucher_status (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    code                varchar(5) not null,
    description         varchar(255) not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table journal_voucher (
    id                          uuid default uuid_generate_v4() primary key,
    document_id                 varchar(15) not null,
    reference                   varchar(20) not null,
    contact_id                  uuid not null,
    issued_date                 date not null,
    status_id                   int not null,
    description                 varchar(255) null,
    total_debit_amount          numeric not null,
    total_credit_amount         numeric not null,
    remark                      text null,
    company_id                  uuid not null,
    created_at                  timestamp,
    created_by                  varchar(255),
    updated_at                  timestamp,
    updated_by                  varchar(255),
    foreign key (contact_id)                    references contact(id),
    foreign key (status_id)                     references journal_entry_new_status(id),
    foreign key (company_id)                    references company(id)
);

create table journal_voucher_line_item(
    id                      uuid default uuid_generate_v4() primary key,
    journal_voucher_id      uuid,
    account_chart_id        uuid,
    item_order              int not null,
    debit_amount            numeric not null,
    credit_amount           numeric not null,
    created_at              timestamp,
    created_by              varchar(255),
    updated_at              timestamp,
    updated_by              varchar(255),
    foreign key (journal_voucher_id)        references journal_voucher(id),
    foreign key (account_chart_id)          references account_chart(id)
);

insert into journal_voucher_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('DRAFT', '00001', 'สร้างแบบร่างแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
insert into journal_voucher_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('SUBMITTED', '00002', 'บันทึกบัญชีแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
insert into journal_voucher_status (name, code, description, created_at, created_by, updated_at, updated_by) values ('DELETED', '00003', 'ลบบันทึกบัญชีแล้ว', current_timestamp, 'dba', current_timestamp, 'dba');
