create table ledger (
    id                  uuid default uuid_generate_v4() primary key,
    code                varchar(10) not null,
    description         varchar(255) not null,
    year                numeric not null,
    month               numeric not null,
    sum_debit           numeric not null,
    sum_credit          numeric not null,
    diff_sum_debit_credit   numeric not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table ledger_debit  (
    id                  uuid default uuid_generate_v4() primary key,
    ledger_id           uuid not null,
    date                date not null,
    description         varchar(255) not null,
    amount              numeric not null,
    reference           uuid not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table ledger_credit  (
    id                  uuid default uuid_generate_v4() primary key,
    ledger_id           uuid not null,
    date                date not null,
    description         varchar(255) not null,
    amount              numeric not null,
    reference           uuid not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);