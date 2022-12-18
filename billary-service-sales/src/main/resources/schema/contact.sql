create table contact_type (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    description         varchar(255),
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table business_type (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    description         varchar(255),
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table contact_new (
    id                  uuid default uuid_generate_v4() primary key,
    name                varchar(255) not null,
    address             varchar(255),
    zip_code            varchar(6),
    tax_id              varchar(13),
    office              varchar(255),
    person              varchar(255),
    email               varchar(255),
    phone               varchar(15),
    contact_type_id     int not null,
    business_type_id    int,
    company_id          UUID not null,
    created_at          timestamp,
    created_by          varchar(255),
    updated_at          timestamp,
    updated_by          varchar(255),
    foreign key (contact_type_id)   references contact_type(id),
    foreign key (business_type_id)  references business_type(id),
    foreign key (company_id)        references company(id)
);

insert into contact_type (name, description, created_at, created_by, updated_at, updated_by) values ('ผู้ซื้อ', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into contact_type (name, description, created_at, created_by, updated_at, updated_by) values ('ผู้จัดจำหน่าย', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into contact_type (name, description, created_at, created_by, updated_at, updated_by) values ('ผู้ซื้อ/ผู้จัดจำหน่าย', '', current_timestamp, 'dba', current_timestamp, 'dba');

insert into business_type (name, description, created_at, created_by, updated_at, updated_by) values ('นิติบุคคล', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into business_type (name, description, created_at, created_by, updated_at, updated_by) values ('บุคคลธรรมดา', '', current_timestamp, 'dba', current_timestamp, 'dba');
