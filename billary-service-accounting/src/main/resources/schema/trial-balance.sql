create table trial_balance (
    id                  uuid default uuid_generate_v4() primary key,
    year                numeric not null,
    month               numeric not null,
    debit_amount        numeric not null,
    credit_amount       numeric not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table trial_balance_entry  (
    id                  uuid default uuid_generate_v4() primary key,
    trial_balance_id    uuid not null,
    account_name        varchar(255) not null,
    account_code        varchar(10) not null,
    debit               numeric not null,
    credit              numeric not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);