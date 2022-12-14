create table product_type (
    id                  int generated always as identity primary key, 
    name                varchar(255) not null,
    description         varchar(255), 
    created_at          timestamp not null,
    created_by          varchar(255), 
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table product_unit (
    id                  int generated always as identity primary key, 
    name                varchar(255) not null,
    description         varchar(255), 
    created_at          timestamp not null,
    created_by          varchar(255), 
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table product_vat_type (
    id                  int generated always as identity primary key, 
    name                varchar(255) not null,
    description         varchar(255), 
    created_at          timestamp not null,
    created_by          varchar(255), 
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table product_inventory_type (
    id                  int generated always as identity primary key, 
    name                varchar(255) not null,
    description         varchar(255), 
    created_at          timestamp not null,
    created_by          varchar(255), 
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table product_category (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    description         varchar(255),
    company_id          uuid not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255),
    foreign key (company_id)    references company(id)
);

create table product (
	id                  uuid default uuid_generate_v4() primary key,
	code                varchar(10) not null,
	name                varchar(255) not null,
	description         varchar(255) not null,
	price               numeric not null,
	category_id         int not null,
	type_id             int not null,
	unit_id             int not null,
	vat_type_id         int not null,
	inventory_type_id   int not null,
	company_id          uuid not null,
    created_at          timestamp, 
    created_by          varchar(255), 
    updated_at          timestamp, 
    updated_by          varchar(255),
	foreign key (type_id)           references product_type(id), 
	foreign key (category_id)       references product_category(id), 
	foreign key (unit_id)           references product_unit(id), 
	foreign key (vat_type_id)       references product_vat_type(id), 
	foreign key (inventory_type_id) references product_inventory_type(id),
	foreign key (company_id)        references company(id)
);

--drop table product;
--drop table product_type;
--drop table product_category;
--drop table product_unit;
--drop table product_vat_type;
--drop table product_inventory_type;

insert into product_type (name, description, created_at, created_by, updated_at, updated_by) values ('สินค้าเพื่อขาย', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_type (name, description, created_at, created_by, updated_at, updated_by) values ('สืนค้าเพื่อผลิต', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_type (name, description, created_at, created_by, updated_at, updated_by) values ('อื่นๆ', '', current_timestamp, 'dba', current_timestamp, 'dba');

insert into product_unit (name, description, created_at, created_by, updated_at, updated_by) values ('ม้วน', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_unit (name, description, created_at, created_by, updated_at, updated_by) values ('ครั้ง', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_unit (name, description, created_at, created_by, updated_at, updated_by) values ('กล่อง', '', current_timestamp, 'dba', current_timestamp, 'dba');

insert into product_inventory_type (name, description, created_at, created_by, updated_at, updated_by) values ('สินค้าไม่นับสต๊อก', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_inventory_type (name, description, created_at, created_by, updated_at, updated_by) values ('สินค้านับสต๊อก', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_inventory_type (name, description, created_at, created_by, updated_at, updated_by) values ('บริการ', '', current_timestamp, 'dba', current_timestamp, 'dba');

insert into product_vat_type (name, description, created_at, created_by, updated_at, updated_by) values('ราคารวมภาษีมูลค่าเพิ่มแล้ว', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_vat_type (name, description, created_at, created_by, updated_at, updated_by) values('ราคาไม่รวมภาษีมูลค่าเพิ่ม', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_vat_type (name, description, created_at, created_by, updated_at, updated_by) values('ภาษีมูลค่าเพิ่ม 0%', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_vat_type (name, description, created_at, created_by, updated_at, updated_by) values('ยกเว้นภาษีมูลค่าเพิ่ม', '', current_timestamp, 'dba', current_timestamp, 'dba');

insert into product_category (name, description, company_id, created_at, created_by, updated_at, updated_by) values ('58 แกรม', '', 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_category (name, description, company_id, created_at, created_by, updated_at, updated_by) values ('65 แกรม', '', 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_category (name, description, company_id, created_at, created_by, updated_at, updated_by) values ('ค่าบริการ', '', 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product_category (name, description, company_id, created_at, created_by, updated_at, updated_by) values ('ส่วนลด', '', 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');

insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065110075', 'ReadyRolls กระดาษความร้อน 110x75 มม.', '65 แกรม', 50, 2, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065100038', 'ReadyRolls กระดาษความร้อน 110x38 มม.', '65 แกรม', 30, 2, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065057030', 'ReadyRolls กระดาษความร้อน 57x30 มม.', '58 แกรม', 6, 2, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1058057030', 'ReadyRolls กระดาษความร้อน 57x30 มม.', '65 แกรม', 6, 1, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065057038', 'ReadyRolls กระดาษความร้อน 57x38 มม.', '58 แกรม', 12, 2, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1058057038', 'ReadyRolls กระดาษความร้อน 57x38 มม.', '65 แกรม', 12, 1, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065057040', 'ReadyRolls กระดาษความร้อน 57x40 มม.', '58 แกรม', 12, 1, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1058057040', 'ReadyRolls กระดาษความร้อน 57x40 มม.', '65 แกรม', 12, 1, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065057045', 'ReadyRolls กระดาษความร้อน 57x45 มม.', '58 แกรม', 14, 2, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1058057045', 'ReadyRolls กระดาษความร้อน 57x45 มม.', '65 แกรม', 14, 1, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065057048', 'ReadyRolls กระดาษความร้อน 57x48 มม.', '58 แกรม', 15, 1, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065057048', 'ReadyRolls กระดาษความร้อน 57x48 มม.', '65 แกรม', 15, 2, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065057050', 'ReadyRolls กระดาษความร้อน 57x50 มม.', '58 แกรม', 15, 2, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1058057050', 'ReadyRolls กระดาษความร้อน 57x50 มม.', '65 แกรม', 15, 1, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065057080', 'ReadyRolls กระดาษความร้อน 57x80 มม.', '65 แกรม', 35, 2, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065080050', 'ReadyRolls กระดาษความร้อน 80x50 มม.', '65 แกรม', 20, 2, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1065080080', 'ReadyRolls กระดาษความร้อน 80x80 มม.', '58 แกรม', 35, 2, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('1058080080', 'ReadyRolls กระดาษความร้อน 80x80 มม.', '65 แกรม', 40, 1, 1, 1, 1, 2, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('2000000001', 'ค่าจัดส่งสินค้า', '', 0, 3, 2, 2, 1, 3, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('3000000001', 'ค่าธรรมเนียม', '', 0, 3, 2, 2, 1, 3, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into product (code, name, description, price, category_id, type_id, unit_id, vat_type_id, inventory_type_id, company_id, created_at, created_by, updated_at, updated_by)
values('4000000001', 'ส่วนลดอื่นๆ', '', 0, 4, 2, 2, 1, 3, 'd01cfe4c-3a82-4341-a099-aca87f5b17d1', current_timestamp, 'dba', current_timestamp, 'dba');

