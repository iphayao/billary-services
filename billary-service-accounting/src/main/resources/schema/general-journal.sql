create table general_journal (
    id              uuid default uuid_generate_v4() primary key,
    date            date not null,
    description     varchar(255) not null,
    type            varchar(255) not null,
    reference       uuid not null,
    document_id     varchar(20) not null
);

create table general_journal_debit  (
    id                  uuid default uuid_generate_v4() primary key,
    general_journal_id  uuid not null,
    code                varchar(20) not null,
    description         varchar(255) not null,
    amount              numeric not null
);

create table general_journal_credit (
    id                  uuid default uuid_generate_v4() primary key,
    general_journal_id  uuid not null,
    code                varchar(20) not null,
    description         varchar(255) not null,
    amount              numeric not null
);