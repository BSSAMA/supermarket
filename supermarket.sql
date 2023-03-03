-- 用户表
create table accounts
(
    aid      int auto_increment
        primary key,
    username varchar(25)            not null,
    password varchar(255)           not null,
    email    varchar(40)            not null,
    tel      char(11)               not null,
    integral float   default 0      null,
    enable   char(5) default 'true' not null,
    constraint email
        unique (email),
    constraint username
        unique (username)
);

create index accounts_tel_index
    on accounts (tel);

-- 商品表
create table commodity
(
    cid             char(13)      not null,
    name            varchar(20)   not null,
    brand           varchar(20)   not null,
    date_production date          null,
    expiration_date int           null,
    buying_price    float         not null,
    price           float         not null,
    type            varchar(10)   not null,
    location        varchar(10)   not null,
    number          int default 0 not null,
    batch_number    char(8)       not null,
    primary key (cid, batch_number)
);

create index commodity_batch_number_index
    on commodity (batch_number);

create index fk_shelves_name
    on commodity (location);

create index fk_type_name
    on commodity (type);

-- 供应商表
create table provider
(
    pid       int auto_increment
        primary key,
    name      varchar(50)  not null,
    address   varchar(255) not null,
    head_name varchar(6)   not null,
    tel       char(11)     not null
);

-- 进货表
create table buy_commodity
(
    bid             char(15) not null,
    cid             char(13) not null,
    batch_number    char(8)  not null,
    date_production date     null,
    expiration_date int      null,
    buying_price    float    not null,
    number          int      not null,
    sum_price       float    not null,
    pid             int      not null,
    primary key (bid, cid, batch_number),
    constraint buy_commodity_provider_pid_fk
        foreign key (pid) references provider (pid)
            on update cascade on delete cascade
);

create index fk_commodity_BatchNumber
    on buy_commodity (batch_number);

create index fk_commodity_id
    on buy_commodity (cid);

-- 销售表
create table sales
(
    sid          char(15) not null,
    cid          char(13) not null,
    batch_number char(8)  not null,
    number       int      not null,
    price        float    not null,
    sum_price    float    not null,
    tel          char(11) null,
    primary key (sid, cid, batch_number),
    constraint fk_sales_accounts_tel
        foreign key (tel) references accounts (tel)
            on update cascade on delete cascade
);

create index fk_sales_commodity_BatchNumber
    on sales (batch_number);

create index fk_sales_commodity_id
    on sales (cid);

-- 菜单表
create table sys_menu
(
    mid       int auto_increment
        primary key,
    path      varchar(50) not null,
    name      varchar(50) not null,
    name_zh   varchar(10) not null,
    icon_cls  varchar(30) null,
    component varchar(50) not null,
    parent_id int         null
);

-- 角色表
create table sys_role
(
    rid  int auto_increment
        primary key,
    role varchar(20) not null,
    name varchar(5)  not null,
    constraint name
        unique (name),
    constraint role
        unique (role)
);

-- 用户角色表
create table sys_accounts_role
(
    account_id int not null,
    role_id    int not null,
    primary key (account_id, role_id),
    constraint fk_account_id
        foreign key (account_id) references accounts (aid)
            on update cascade on delete cascade,
    constraint fk_accounts_role_id
        foreign key (role_id) references sys_role (rid)
            on update cascade on delete cascade
);

-- 菜单角色表
create table sys_menu_role
(
    menu_id int not null,
    role_id int not null,
    primary key (menu_id, role_id),
    constraint fk_menu_id
        foreign key (menu_id) references sys_menu (mid)
            on update cascade on delete cascade,
    constraint fk_menu_role_id
        foreign key (role_id) references sys_role (rid)
            on update cascade on delete cascade
);

-- 用户数据
INSERT INTO accounts (aid, username, password, email, tel, integral, enable) VALUES
(1, 'admin', '$2a$10$AHIUTfjX0wFL9WmUbREES.9Ik8P.J4GwVKgaUprormR3HvhNuBgMC', '876799682@qq.com', '13271579615', 55, 'true'),
(2, 'user', '$2a$10$4TVS41wrommkKYMqReX8DuuXiQwzXb8JYi36hviRqjhf7FvSeXtuO', '3154473513@qq.com', '17629550117', 10, 'true');

-- 角色表数据
INSERT INTO sys_role (rid, role, name) VALUES
(1, 'ROLE_admin', '超级管理员'),
(2, 'ROLE_user', '普通用户');

-- 用户角色表数据
INSERT INTO sys_accounts_role (account_id, role_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(4, 2);

-- 菜单表数据
INSERT INTO sys_menu (mid, path, name, name_zh, icon_cls, component, parent_id) VALUES
(2, '/', 'Commodity', '商品', 'el-icon-goods', 'Index', 0),
(3, '/commodityinfo', 'CommodityInfo', '商品信息', null, 'commodity/Info', 2),
(4, '/commoditystorage', 'CommodityStorage', '商品进货', null, 'commodity/Storage', 2),
(5, '/', 'Provider', '供应商', 'el-icon-notebook-2', 'Index', 0),
(6, '/providerinfo', 'ProviderInfo', '供应商信息', null, 'provider/Info', 5),
(7, '/', 'User', '用户', 'el-icon-user', 'Index', 0),
(8, '/userinfo', 'UserInfo', '用户信息', null, 'user/Info', 7),
(9, '/', 'Cash', '收银', 'el-icon-coin', 'Index', 0),
(11, '/cashlist', 'CashList', '清单', null, 'cash/List', 9),
(12, '/buycommoditylist', 'BuyCommodityList', '进货清单', null, 'commodity/BuyCommodityList', 2),
(13, '/saleslist', 'SalesList', '销售清单', null, 'commodity/SalesList', 2);

-- 菜单角色表数据
INSERT INTO sys_menu_role (menu_id, role_id) VALUES
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(11, 1),
(12, 1),
(13, 1),
(2, 2),
(3, 2);

-- 商品表数据
INSERT INTO commodity (cid, name, brand, date_production, expiration_date, buying_price, price, type, location, number, batch_number) VALUES
('4901754141008', '榨菜', '乌江', '2021-09-12', 16, 1.7, 2, '厨房用品', 'D-1-2', 40, '20210912'),
('6901209201332', '优加纯牛奶（箱）', '光明', '2022-01-29', 6, 47, 50, '一般食品', 'A-1-2', 20, '20220129'),
('6903148156988', '汰渍无磷洗衣皂', '汰渍', '2021-11-26', 36, 13, 12.7, '生活用品', 'E-1-1', 11, '20211126'),
('6928804011142', '可口可乐', 'cocacola', '2021-08-20', 12, 1.7, 2, '一般食品', 'A-1-1', 21, '20210820'),
('6928804011142', '可口可乐', 'cocacola', '2021-08-22', 12, 1.7, 2, '一般食品', 'A-1-1', 20, '20210822'),
('6928804011142', '可口可乐', 'cocacola', '2022-02-02', 12, 1.7, 2, '一般食品', 'A-1-1', 13, '20220202'),
('6935498308793', '山楂树下', '冠芳', '2021-07-26', 12, 4.3, 4.5, '一般食品', 'A-1-1', 53, '20210726'),
('6937168846406', '晨光速干笔', '晨光', null, null, 3.6, 4, '文具用品', 'B-1-1', 12, '00000000'),
('6937962103019', '红烧牛肉面', '康师傅', '2022-01-22', 6, 3.8, 4, '一般食品', 'A-3-1', 30, '20220122'),
('6958069801998', '五香卤蛋', '东门外', '2021-11-20', 12, 1.6, 2, '一般食品', 'A-3-1', 32, '20211120'),
('testtesttest1', 'test', 'test', null, null, 2, 2.5, 'test', 'test', 20, '00000000'),
('testtesttest2', 'test2', 'test', null, null, 2, 2.5, 'test', 'test', 20, '00000000');

-- 供应商表数据
INSERT INTO provider (pid, name, address, head_name, tel) VALUES
(1, '小食品批发城', '文峰区德隆街与相州路交叉口小食品批发城', '张三', '13211523244'),
(2, '饮品批发市场', '文峰区文昌大道与平原路交叉口饮品批发市场', '李四', '15825256985'),
(3, '晨光文具', '文峰区文昌大道与彰德路交叉口红河批发市场', '王五', '17611584852');


-- 进货单数据
INSERT INTO buy_commodity (bid, cid, batch_number, date_production, expiration_date, buying_price, number, sum_price, pid) VALUES
('B20220425223905', '6928804010000', '00000000', null, null, 1.8, 20, 36, 1),
('B20220425223905', '6928804011142', '20220202', '2022-02-02', 12, 1.7, 5, 8.5, 1),
('B20220425223905', '6928804012222', '20220412', '2022-04-12', 13, 1.8, 20, 36, 1),
('B20220426152841', '6928804011142', '20220202', '2022-02-02', 12, 1.7, 10, 17, 1),
('B20220426152841', '6935498308793', '20210726', '2021-07-26', 12, 4.3, 10, 43, 1);

-- 销售表
INSERT INTO sales (sid, cid, batch_number, number, price, sum_price, tel) VALUES
('S20220418115701', '6935498308793', '20210726', 2, 4.5, 9, '13271579615'),
('S20220418164752', '6958069801998', '20211120', 5, 2, 10, '17629550117'),
('S20220425231502', '6928804010000', '00000000', 15, 2, 30, null),
('S20220425231502', '6928804011142', '20220202', 2, 2, 4, null),
('S20220426153024', '6928804011142', '20210820', 4, 2, 8, '13271579615'),
('S20220426153024', '6935498308793', '20210726', 2, 4.5, 9, '13271579615');


