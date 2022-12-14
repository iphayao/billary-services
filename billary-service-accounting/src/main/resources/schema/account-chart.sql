create table account_chart_category (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    code                varchar(5) not null,
    description         varchar(255) not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255)
);

create table account_chart_group (
    id                  int generated always as identity primary key,
    name                varchar(255) not null,
    code                varchar(5) not null,
    description         varchar(255) not null,
    category_id         int not null,
    created_at          timestamp not null,
    created_by          varchar(255),
    updated_at          timestamp not null,
    updated_by          varchar(255),
    foreign key (category_id)   references account_chart_category(id)
);

create table account_chart (
    id                      uuid default uuid_generate_v4() primary key,
    code                    varchar(255) not null,
    name                    varchar(255) not null,
    category_id             int not null,
    group_id                int not null,
    company_id              uuid not null,
    created_at              timestamp,
    created_by              varchar(255),
    updated_at              timestamp,
    updated_by              varchar(255),
    foreign key (category_id)   references account_chart_category(id),
    foreign key (group_id)      references account_chart_group(id)
);

insert into account_chart_category (name, code, description, created_at, created_by, updated_at, updated_by)
values ('สินทรัพย์','1000', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_category (name, code, description, created_at, created_by, updated_at, updated_by)
values ('หนี้สิน', '2000', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_category (name, code, description, created_at, created_by, updated_at, updated_by)
values ('ส่วนของเจ้าของ / ทุน','3000',  '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_category (name, code, description, created_at, created_by, updated_at, updated_by)
values ('รายได้', '4000', '', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_category (name, code, description, created_at, created_by, updated_at, updated_by)
values ('ค่าใช้จ่าย', '5000', '', current_timestamp, 'dba', current_timestamp, 'dba');

insert into account_chart_group (name, code, description, category_id, created_at, created_by, updated_at, updated_by)
values ('สินทรัพย์หมุนเวียน','1000', '', '1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_group (name, code, description, category_id, created_at, created_by, updated_at, updated_by)
values ('สินทรัพย์ถาวร','1100', '', '1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_group (name, code, description, category_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเสื่อมราคาสะสม','1200', '', '1', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_group (name, code, description, category_id, created_at, created_by, updated_at, updated_by)
values ('หนี้สินระยะสั้น','2000', '', '2', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_group (name, code, description, category_id, created_at, created_by, updated_at, updated_by)
values ('หนี้สินหมุนเวียน','2100', '', '2', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_group (name, code, description, category_id, created_at, created_by, updated_at, updated_by)
values ('หนี้สินระยะยาว','2200', '', '2', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_group (name, code, description, category_id, created_at, created_by, updated_at, updated_by)
values ('ส่วนของเจ้าของ / ทุน','3000', '', '3', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_group (name, code, description, category_id, created_at, created_by, updated_at, updated_by)
values ('รายได้','4000', '', '4', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_group (name, code, description, category_id, created_at, created_by, updated_at, updated_by)
values ('ค่าใช้จ่าย','5000', '', '5', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart_group (name, code, description, category_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเสื่อมราคา','5100', '', '5', current_timestamp, 'dba', current_timestamp, 'dba');

insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เงินสด','1001', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เงินฝากธนาคาร - กสิกรไทย','1002', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เงินฝากธนาคาร - ไทยพาณิชย์','1003', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เงินฝากธนาคาร - ไทยพาณิชย์ (กระแสรายวัน)','1004', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ลูกหนี้การค้า','1005', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ลูกหนี้ค่าหุ้นสามัญ','1006', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('วัสดุสำนักงาน','1007', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('วัสดุสิ้นเปลือง','1008', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ลิขสิทธิ์','1009', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('สินค้าคงเหลือ','1010', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ภาษีซื้อ','1011', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ลูกหนี้กรมสรรพากร','1012', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าใช้จ่าย จ่ายล่วงหน้า','1013', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเช่า จ่ายล่วงหน้า','1014', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ดอกเบื้ยเช่าซื้อรอตัดรอบบัญชี','1015', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ภาษีซื้อยังไม่ถึงกำหนดชำระ','1016', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ภาษีซื้อไม่ขอคืน','1017', '1', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ที่ดิน','1100', '1', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('อาคาร','1101', '1', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('คเครื่องจักร','1102', '1', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('อุปกรณ์สำนักงาน','1103', '1', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเสื่อมราคาสะสม','1104', '1', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ภาษีหัก ณ ที่จ่าย','1105', '1', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('วัตถุดิบ','1106', '1', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ยานพาหนะ','1107', '1', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเสื่อมราคาสะสม-อุปกรณ์สำนักงาน','1201', '1', '3', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเสื่อมราคาสะสม-เครื่องจักร','1202', '1', '3', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเสื่อมราคาสะสม-ยานยนต์','1203', '1', '3', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เจ้าหนี้การค้า','2001', '2', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าใช้จ่ายค้างจ่าย','2002', '2', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('รายได้รับล่วงหน้า','2003', '2', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เจ้าหนี้เงินกู้ระยะสั้น','2004', '2', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ภาษีขาย','2101', '2', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เจ้าหนี้กรมสรรพากร','2102', '2', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ภาษีหัก ณ ที่จ่ายค้างจ่าย','2103', '2', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าตรวจสอบบัญชีค้างจ่าย','2104', '2', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เงินประกันสังคมค้างจ่าย','2105', '2', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เจ้าหนี้เงินกู้ระยะยาว','2201', '2', '3', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เจ้าหนี้เช่าซื้อ','2202', '2', '3', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ทุนหุ้นสามัญที่ชำระเแล้ว','3001', '3', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ทุนหุ้นสามัญ - ที่จองแล้ว','3002', '3', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ถอนใช้ส่วนตัว','3003', '3', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('กำไรสะสม','3004', '3', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เงินปันผลจ่าย','3005', '3', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ขายสินค้า','4001', '4', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('รับคืน','4002', '4', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ส่วนลดจ่าย','4003', '4', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ดอกเบี้ยรับ','4004', '4', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('รายได้เบ็ดเตล็ด','4005', '4', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ซื้อสินค้า','5001', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ซื้อสินค้า','5002', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าใช้จ่ายในการจัดตั้งบริษัท','5003', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเช่า','5004', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าน้ำ ค่าไฟ ค่าโทรศัพท์ ค่าส่วนกลาง','5005', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เงินเดือนพนักงาน','5006', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าธรรมเนียม','5007', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าบริการ','5008', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าอากรสแตมป์','5009', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ซื้อวัตถุดิบ','5010', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ดอกเบี้ยจ่าย','5011', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ดอกเบี้ยเช่าซื้อ','5012', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าโฆษณา','5013', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('วัสดุสิ้นเปลืองใช้ไป','5014', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเดินทาง ที่พัก','5015', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('เงินสมทบกองทุนประกันสังคม','5016', '5', '1', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเสื่อมราคา-อุปกรณ์สำนักงาน','5101', '5', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเสื่อมราคา-เครื่องจักร','5102', '5', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');
insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('ค่าเสื่อมราคา-ยานยนต์','5103', '5', '2', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');



insert into account_chart (name, code, category_id, group_id, company_id, created_at, created_by, updated_at, updated_by)
values ('','1203', '1', '3', 'ea569dd5-43ba-4d42-b7cd-e89d5a7c8390', current_timestamp, 'dba', current_timestamp, 'dba');






