drop table if exists mes_define_field;

drop table if exists mes_define_field_value;

/*==============================================================*/
/* Table: mes_define_field                                      */
/*==============================================================*/
create table mes_define_field
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '自定义字段id',
   table_code           varchar(50) not null comment '数据库表名',
   field_name           varchar(50) comment '字段名',
   field_desc           varchar(50) comment '字段描述',
   field_type           varchar(10) comment '1,int;2,char;',
   scope                int comment '字段约束',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint NOT NULL DEFAULT '0'  comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_table_code_field_name (tenant_id, table_code, field_name)
);

alter table mes_define_field comment 'mes_define_field/自定义字段';

/*==============================================================*/
/* Table: mes_define_field_value                                */
/*==============================================================*/
create table mes_define_field_value
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '自定义字段值id',
   table_code           varchar(50) not null comment '数据库表名',
   field_id             varchar(50) comment '字段id',
   main_id              varchar(50) comment '主表数据行id',
   data_value           varchar(50) comment '值',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint NOT NULL DEFAULT '0'  comment '删除标识',
   primary key (id),
   unique key AK_uk_tenant_id_table_code_main_id_field_id (tenant_id, table_code, field_id, main_id)
);

alter table mes_define_field_value comment 'mes_define_field_value/自定义字段值存储';


drop table if exists mes_factory;

/*==============================================================*/
/* Table: mes_factory                                           */
/*==============================================================*/
create table mes_factory
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '运营后台管理基础上，工厂id即租户id',
   company              varchar(50) comment '所属企业',
   factory_code         varchar(50) not null comment '工厂编码',
   factory_name         varchar(50) not null comment '工厂名称',
   tenant_type          varchar(10) comment '0，商用；1，测试；2，渠道；3，内部使用；租户类型可以考虑用单独表。',
   cloud_environment    varchar(10) comment '0,本地私有云；1，阿里云；2，微软云；3，华为云；这个在工厂表里不体现。',
   contract             varchar(50) comment '合同编码',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   start_date           date comment '合同开始日期',
   end_date             date comment '合同到期日期',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_factory_code (tenant_id, factory_code)
);

alter table mes_factory comment 'mes_factory/工厂';


drop table if exists mes_work_line;

drop table if exists mes_workstation;

drop table if exists mes_workstation_employee;

drop table if exists mes_work_line_employee;
/*==============================================================*/
/* Table: mes_work_line_employee                                */
/*==============================================================*/
create table mes_work_line_employee
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '班组人员id',
   work_line_id         varchar(50) comment '班组id',
   employee_id          varchar(50),
   employee_name        varchar(50) not null comment '员工名称',
   employee_code        varchar(50) comment '员工编码',
   position_name        varchar(50) comment '岗位id',
   position_id          varchar(50) comment '岗位id',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_work_line_employee (tenant_id, work_line_id, employee_id)
);

alter table mes_work_line_employee comment '产线关联人员';

drop table if exists mes_work_shop;

/*==============================================================*/
/* Table: mes_work_shop                                         */
/*==============================================================*/
create table mes_work_shop
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '车间id',
   shop_code            varchar(50) not null comment '车间编码',
   shop_name            varchar(50) not null comment '车间名称',
   qrcode               varchar(50) comment '二维码',
   up_area              varchar(50) comment '上级区域',
   up_area_name         varchar(50) comment '上级区域',
   duty_person          varchar(50) comment '负责人',
   duty_person_name     varchar(50) comment '负责人',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   attach               varchar(1000) comment '附件',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key AK_uk_tenant_id_shop_code (tenant_id, shop_code)
);

alter table mes_work_shop comment 'mes_work_shop/车间';

drop table if exists mes_work_shop_employee;

/*==============================================================*/
/* Table: mes_work_shop_employee                                */
/*==============================================================*/
create table mes_work_shop_employee
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '班组人员id',
   work_shop_id         varchar(50) comment '车间id',
   employee_id          varchar(50),
   employee_name        varchar(50) not null comment '员工名称',
   employee_code        varchar(50) comment '员工编码',
   position_id          varchar(50) comment '岗位id',
   position_name        varchar(50) comment '岗位id',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_work_shop_employee (tenant_id, work_shop_id, employee_id)
);

alter table mes_work_shop_employee comment '车间关联人员';

/*==============================================================*/
/* Table: mes_workline                                          */
/*==============================================================*/
create table mes_work_line
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '产线id',
   line_code            varchar(50) not null comment '产线编码',
   line_name            varchar(50) not null comment '产线名称',
   qrcode               varchar(50) comment '二维码',
   up_area              varchar(50) comment '上级区域',
   up_area_name         varchar(50) comment '上级区域',
   duty_person          varchar(50) comment '负责人',
   duty_person_name     varchar(50) comment '负责人',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   attach               varchar(1000) comment '附件',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_line_code (tenant_id, line_code)
);

alter table mes_work_line comment 'mes_work_line/产线';

/*==============================================================*/
/* Table: mes_workstation                                       */
/*==============================================================*/
create table mes_workstation
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工位id',
   station_code         varchar(50) not null comment '工位编码',
   station_name         varchar(50) not null comment '工位名称',
   qrcode               varchar(50) comment '二维码',
   up_area_type         varchar(3) comment '1，产线；2，车间；',
   up_area_name         varchar(50) comment '上级区域',
   up_area              varchar(50) comment '上级区域',
   duty_person          varchar(50) comment '负责人',
   duty_person_name     varchar(50) comment '负责人',
   is_multistation      varchar(3) comment '1,是；0，否；',
   equipment            varchar(200) comment '报工设备',
   attach               varchar(1000) comment '附件',
   note                 varchar(256) comment '备注',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_station_code (tenant_id, station_code)
);

alter table mes_workstation comment '工位';

/*==============================================================*/
/* Table: mes_workstation_employee                              */
/*==============================================================*/
create table mes_workstation_employee
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '班组人员id',
   workstation_id       varchar(50) comment '班组id',
   employee_id          varchar(50),
   employee_name        varchar(50) not null comment '员工名称',
   employee_code        varchar(50) comment '员工编码',
   position_name        varchar(50) comment '岗位id',
   position_id          varchar(50) comment '岗位id',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_mes_work_station_employee (tenant_id, workstation_id, employee_id)
);

alter table mes_workstation_employee comment '工位关联人员';

drop table if exists mes_customer;

drop table if exists mes_shift;

drop table if exists mes_supplier;

drop table if exists mes_team_employee;

drop table if exists mes_team;

/*==============================================================*/
/* Table: mes_customer                                          */
/*==============================================================*/
create table mes_customer
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '客户id',
   customer_code        varchar(50) not null comment '客户编码',
   customer_name        varchar(50) not null comment '客户名称',
   adress               varchar(50) comment '联系地址',
   contact              varchar(50) comment '联系人',
   phone                varchar(50) comment '联系电话',
   email                varchar(50) comment '邮箱',
   attach               varchar(1000) comment '附件',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_customer_code (tenant_id, customer_code)
);

alter table mes_customer comment 'mes_customer/客户';

/*==============================================================*/
/* Table: mes_shift                                             */
/*==============================================================*/
create table mes_shift
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '班次id',
   shift_name           varchar(50) not null comment '班次',
   shift_start_time     varchar(50) not null comment '班次开始时间',
   shift_end_time       varchar(50) not null comment '班次结束时间',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_shift_name (tenant_id, shift_name)
);

alter table mes_shift comment 'mes_shift/班次';

/*==============================================================*/
/* Table: mes_suppliers                                         */
/*==============================================================*/
create table mes_supplier
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '供应商id',
   supplier_code        varchar(50) not null comment '供应商编码',
   supplier_name        varchar(50) not null comment '供应商名称',
   adress               varchar(50) comment '联系地址',
   contact              varchar(50) comment '联系人',
   phone                varchar(50) comment '联系电话',
   email                varchar(50) comment '邮箱',
   attach               varchar(1000) comment '附件',
   status               varchar(50) default '1' comment '状态：1，启用，0，停用；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_supplier_code (tenant_id, supplier_code)
);

alter table mes_supplier comment 'mes_supplier/供应商';

/*==============================================================*/
/* Table: mes_teamemployee                                      */
/*==============================================================*/
create table mes_team_employee
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '班组人员id',
   team_id              varchar(50) comment '班组id',
   employee_id          varchar(50) comment '用户ID',
   employee_name        varchar(50) not null comment '员工名称',
   employee_code        varchar(50) comment '员工编码',
   position_id          varchar(50) comment '岗位id',
   position_name        varchar(50) comment '岗位id',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   is_leader            varchar(10) comment '1，是组长；0，非组长；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_team_id_employee_code (tenant_id, team_id, employee_code)
);

alter table mes_team_employee comment 'mes_team_employee/班组人员';

/*==============================================================*/
/* Table: mes_teams                                             */
/*==============================================================*/
create table mes_team
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '班组id',
   team_name            varchar(50) not null comment '班组名称',
   team_code            varchar(50) comment '班组编码',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_team_code (tenant_id, team_code)
);

alter table mes_team comment 'mes_team/班组';

drop table if exists mes_unit;

/*==============================================================*/
/* Table: mes_unit                                              */
/*==============================================================*/
create table mes_unit
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '单位id',
   unit_name            varchar(50) not null comment '单位名称',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_mes_unit (tenant_id, unit_name)
);

alter table mes_unit comment 'mes_unit/计量单位';


drop table if exists mes_work_calendar;

/*==============================================================*/
/* Table: mes_calendar                                          */
/*==============================================================*/
create table mes_work_calendar
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '班组id',
   type                 varchar(10) comment '1,生产日历；2，设备日历；3，质量日历；',
   workdate             date not null comment '班组名称',
   date_type            varchar(10) comment '工作日；休息日；法定节假日；',
   shift_id             varchar(50),
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_work_calendar comment '工作日历';


drop table if exists mes_ng_item_type;

/*==============================================================*/
/* Table: mes_ng_item_type                                      */
/*==============================================================*/
create table mes_ng_item_type
(
   tenant_id            varchar(50) comment '租户id',
   id                   varchar(50) not null comment '不良类型id',
   ng_type_name         varchar(50) not null comment '不良类型名称',
   note                 varchar(256) comment '备注',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   key uk_tenant_id_ng_type_name (tenant_id, note)
);

alter table mes_ng_item_type comment '不良类型';


drop table if exists mes_ng_item;

/*==============================================================*/
/* Table: mes_ng_item                                           */
/*==============================================================*/
create table mes_ng_item
(
   tenant_id            varchar(50) comment '租户id',
   id                   varchar(50) not null comment '不良项id',
   ng_code              varchar(50) not null comment '不良项编码',
   ng_name              varchar(50) not null comment '不良项名称',
   ng_type_id           varchar(50) comment '不良类型',
   note                 varchar(256) comment '备注',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_ng_code (tenant_id, ng_code)
);

alter table mes_ng_item comment '不良项';

drop table if exists mes_qc_item_type;

/*==============================================================*/
/* Table: mes_qc_item_type                                      */
/*==============================================================*/
create table mes_qc_item_type
(
   tenant_id            varchar(50) comment '租户id',
   id                   varchar(50) not null comment '不良类型id',
   qc_type_name         varchar(50) not null comment '不良类型名称',
   note                 varchar(256) comment '备注',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   key uk_tenant_id_qc_type_name (tenant_id, qc_type_name)
);

alter table mes_qc_item_type comment '质检项分类';


drop table if exists mes_qc_item;

/*==============================================================*/
/* Table: mes_qc_item                                           */
/*==============================================================*/
create table mes_qc_item
(
   tenant_id            varchar(50) comment '租户id',
   id                   varchar(50) not null comment '不良项id',
   qc_type_id           varchar(50) comment '不良类型',
   qc_item_code         varchar(50) not null comment '不良项编码',
   qc_item_name         varchar(50) not null comment '不良项名称',
   note                 varchar(256) comment '备注',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_ng_code (tenant_id, qc_item_code)
);

alter table mes_qc_item comment '质检项';

drop table if exists mes_qc_method;

/*==============================================================*/
/* Table: mes_qc_method                                         */
/*==============================================================*/
create table mes_qc_method
(
   tenant_id            varchar(50) comment '租户id',
   id                   varchar(50) not null comment '质检方案id',
   method_name          varchar(50) not null comment '质检方案名称',
   is_auto              varchar(10) comment '触发方式:1,自动触发；0，非自动触发；',
   qc_type              varchar(10) comment '质检类型:1,入厂检；2，出厂检；3，首检；4，生产检，5，巡检，6，普通检',
   qc_way               varchar(10) comment '质检方式:1、全检，2、比例抽检；3，固定抽检，4、自定义抽检',
   qc_qty               decimal(18,6) comment '质检数量',
   record_way           varchar(10) comment '记录方式:1、单体记录，2、质检项记录；3，次品项记录',
   is_waste             varchar(10) comment '1，不报废；0，报废；',
   judge_type           varchar(10) comment '判定维度:1、判定单次样本；2、判定整个批次',
   note                 varchar(256) comment '特别提示',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_mes_qc_method (tenant_id, method_name)
);

alter table mes_qc_method comment '质检方案主表';


drop table if exists mes_qc_method_detail;

/*==============================================================*/
/* Table: mes_qc_method_detail                                  */
/*==============================================================*/
create table mes_qc_method_detail
(
   tenant_id            varchar(50) comment '租户id',
   id                   varchar(50) not null comment '质检方案物料id',
   mehtod_id            varchar(50) not null comment '质检方案id',
   item_id              varchar(50) comment '质检项id',
   item_name            varchar(50) comment '质检项名称',
   qc_standard          varchar(10) comment '质检项标准',
   min_value            decimal(18,6) comment '最小值',
   max_value            decimal(18,6) comment '最大值',
   equal_value          decimal(18,6) comment '等于值',
   standard_value       decimal(18,6) comment '标准值',
   up_deviation        decimal(18,6) comment '上偏差',
   down_deviation      decimal(18,6) comment '下偏差',
   value_unit           varchar(50) comment '单位',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_mes_qc_method_detail (tenant_id, mehtod_id, item_id)
);

alter table mes_qc_method_detail comment '质检方案关联质检项';


drop table if exists mes_qc_method_item;

/*==============================================================*/
/* Table: mes_qc_method_item                                    */
/*==============================================================*/
create table mes_qc_method_item
(
   tenant_id            varchar(50) comment '租户id',
   id                   varchar(50) not null comment '质检方案物料id',
   mehtod_id            varchar(50) not null comment '质检方案id',
   item_id              varchar(50) comment '物料id',
   item_name            varchar(50) comment '物料名称',
   item_code            varchar(50) comment '物料编码',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   key uk_qc_method_item (tenant_id, mehtod_id, item_id)
);

alter table mes_qc_method_item comment '质检方案关联物料';


drop table if exists mes_item_type;

/*==============================================================*/
/* Table: mes_item_type                                         */
/*==============================================================*/
create table mes_item_type
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '类型id',
   type_code            varchar(50) not null comment '物料类型编码',
   type_name            varchar(50) not null comment '物料类型名称',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_type_code (tenant_id, type_code)
);

alter table mes_item_type comment 'mes_item_type/物料类型';

drop table if exists mes_item;

/*==============================================================*/
/* Table: mes_item                                              */
/*==============================================================*/
create table mes_item
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '物料id',
   item_code            varchar(50) not null comment '物料编码',
   item_name            varchar(50) not null comment '物料名称',
   item_description     varchar(200) not null comment '物料名称',
   item_type_id         varchar(50) comment '物料类型',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   main_unit            varchar(50) comment '主单位',
   main_accuracy        int,
   feed_unit            varchar(50) comment '投产单位',
   output_unit          varchar(50) comment '产出单位',
   spec                 varchar(50) comment '规格',
   is_batch             varchar(10) comment '启动批次管理：1，是；0，否；',
   is_qrcode            varchar(10) comment '启动二维码1，是；0，否；',
   label_rule           varchar(50) comment '标签规则',
   attach               varchar(1000) comment '附件',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_item_code (tenant_id, item_code)
);

alter table mes_item comment 'mes_item/物料定义';


drop table if exists mes_item_quality_employee;

/*==============================================================*/
/* Table: mes_item_quality_employee                             */
/*==============================================================*/
create table mes_item_quality_employee
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '物料质检人员id',
   item_id              varchar(50) comment '物料id',
   employee_id          varchar(50) comment '质检人员id',
   employee_name        varchar(50) comment '质检人员id',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   note                 varchar(256) comment '备注',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_item_quality_employee comment '物料关联质检人员';


drop table if exists mes_item_unit;

/*==============================================================*/
/* Table: mes_item_unit                                         */
/*==============================================================*/
create table mes_item_unit
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '物料单位id',
   item_id              varchar(50) comment '物料id',
   main_unit_qty        decimal(18,6) comment '主单位数量',
   main_unit            varchar(50) not null comment '主单位名称',
   convert_qty          decimal(18,6) comment '转换单位数量',
   convert_unit         varchar(50) comment '转换单位名称',
   accuracy             int comment '转换单位精度',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   seq                  int not null comment '序号',
   primary key (id)
);

alter table mes_item_unit comment 'mes_item_unit/物料转换单位';


drop table if exists mes_item_quality;

/*==============================================================*/
/* Table: mes_item_quality                                      */
/*==============================================================*/
create table mes_item_quality
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '物料质量id',
   item_id              varchar(50) comment '物料id',
   quality_id           varchar(50) comment '质检方案id',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   note                 varchar(256) comment '备注',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_item_id_quality_id (tenant_id, item_id, quality_id)
);

alter table mes_item_quality comment '物料关联质检方案';


drop table if exists mes_item_stock;

/*==============================================================*/
/* Table: mes_item_stock                                        */
/*==============================================================*/
create table mes_item_stock
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '库存id',
   item_id              varchar(50) comment '物料id',
   is_fifo              varchar(10) comment '启动先进先出：1，是；0，否；',
   in_unit              varchar(50) comment '入库单位，选单位表里的单位',
   valid_time           int comment '存储有效期，时间段',
   valid_unit           varchar(10) comment '1，分钟；2，小时；3，天；4，月；5，年',
   warn_time            int comment '预警提前期',
   warn_unit            varchar(10) comment '1，分钟；2，小时；3，天；4，月；5，年',
   warehouse            varchar(50) comment '存储仓库',
   safety_stock         decimal(18,6) comment '安全库存',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   note                 varchar(256) comment '备注',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_item_id_quality_id (tenant_id, item_id)
);

alter table mes_item_stock comment '物料关联库存';

drop table if exists mes_item_supplier;

/*==============================================================*/
/* Table: mes_item_supplier                                     */
/*==============================================================*/
create table mes_item_supplier
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '物料供应商id',
   item_id              varchar(50) comment '物料id',
   supplier_id          varchar(50) comment '供应商id',
   supplier_code          varchar(50) comment '供应商编码',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   note                 varchar(256) comment '备注',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_item_id_quality_id (tenant_id, item_id, supplier_id)
);

alter table mes_item_supplier comment '物料关联供应商';


drop table if exists mes_item_bom;

/*==============================================================*/
/* Table: mes_item_bom                                          */
/*==============================================================*/
create table mes_item_bom
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment 'bomid',
   item_id              varchar(50) not null comment '产品编码',
   item_code            varchar(50) not null comment '产品编码',
   item_name            varchar(50) not null comment '产品编码',
   spec                 varchar(50) comment '规格描述',
   version              varchar(50) comment '版本',
   qty                  decimal(18,6) comment '数量',
   unit                 varchar(50) not null comment '单位',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_mes_bom (tenant_id, item_id, version)
);

alter table mes_item_bom comment 'mes_item_bom/物料BOM';


drop table if exists mes_item_bom_detail;

/*==============================================================*/
/* Table: mes_item_bom_detail                                   */
/*==============================================================*/
create table mes_item_bom_detail
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment 'bom明细id',
   bom_id               varchar(50) comment 'bomid',
   seq                  int comment '行号',
   item_id              varchar(50) not null comment '物料id',
   item_name            varchar(50) not null comment '物料id',
   item_code            varchar(50) not null comment '物料id',
   qty                  decimal(18,6) comment '数量',
   unit                 varchar(50) not null comment '单位',
   loss_rate            decimal(18,6) comment '耗损率',
   is_control           varchar(10) comment '投料管控:1,管控；0，不管控；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_bom_id_item_id (tenant_id, bom_id, item_id)
);

alter table mes_item_bom_detail comment 'mes_item_bom_detail/物料BOM明细表';


drop table if exists mes_item_bom_substitute;

/*==============================================================*/
/* Table: mes_item_bom_substitute                               */
/*==============================================================*/
create table mes_item_bom_substitute
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment 'bom替代料id',
   bom_id               varchar(50) comment 'bomid',
   item_id              varchar(50) not null comment '被替代料id',
   subtitute_item_id    varchar(50) comment '替代料id',
   let_subtitute_qty    decimal(18,6) not null comment '被替代料数量',
   let_subtitute_unit   varchar(50) comment '被替代料单位',
   subtitute_qty        decimal(18,6) not null comment '替代料数量',
   subtitute_unit       varchar(50) comment '替代料单位',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_item_bom_substitute comment 'mes_item_bom_substitute/物料BOM替代料';


drop table if exists mes_machine_maintain_policy;

/*==============================================================*/
/* Table: mes_machine_maintain_policy                           */
/*==============================================================*/
create table mes_machine_maintain_policy
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '策略组id',
   policy_type          varchar(10) not null  comment '1、设备；2、工装备件；',
   data_name            varchar(50) not null  comment '策略组名称',
   ref_qty              int  comment '引用数量：指其他地方引用该策略组的所有累计数量。',
   note                 varchar(256)  comment '备注',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_maintain_policy (tenant_id, policy_type, data_name)
);

alter table mes_machine_maintain_policy comment '维保策略组定义';


drop table if exists mes_machine_stop_reason;

/*==============================================================*/
/* Table: mes_machine_stop_reason                               */
/*==============================================================*/
create table mes_machine_stop_reason
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '停机原因id',
   stop_reason          varchar(50) not null  comment '停机原因',
   stop_code            varchar(50)  comment '停机原因编码',
   reason_type          varchar(10) not null  comment '1、计划性停机；2、非计划性停机',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_stop_reason (tenant_id, stop_code)
);

alter table mes_machine_stop_reason comment '停机原因';


drop table if exists mes_machine_label;

/*==============================================================*/
/* Table: mes_machine_label                                     */
/*==============================================================*/
create table mes_machine_label
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '设备标签id',
   label_type           varchar(10)  comment '1、设备分类 2、设备等级',
   machine_label_name   varchar(50) not null  comment '标签名称',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_label (tenant_id, label_type, machine_label_name)
);

alter table mes_machine_label comment '设备标注';

drop table if exists mes_process;

/*==============================================================*/
/* Table: mes_process                                           */
/*==============================================================*/
create table mes_process
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工序id',
   process_code         varchar(50) not null comment '工序编码',
   process_name         varchar(50) not null comment '工序名称',
   surpass_rate         tinyint comment '允许超产比例',
   description          varchar(200) comment '工艺描述',
   template_id          varchar(50) comment '填报模板',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   attach               varchar(1000) comment '附件',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_process_code (tenant_id, process_code)
);

alter table mes_process comment 'mes_process/工序';


drop table if exists mes_process_station;

/*==============================================================*/
/* Table: mes_process_station                                   */
/*==============================================================*/
create table mes_process_station
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工序关联工位id',
   process_id           varchar(50) comment '工序id',
   station_id           varchar(50) comment '工位id',
   station_code         varchar(50) comment '工位编码',
   station_name         varchar(50) comment '工位名称',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_process_station (process_id, station_id, tenant_id)
);

alter table mes_process_station comment 'mes_process_station/工序关联工位';


drop table if exists mes_process_qc_method;

/*==============================================================*/
/* Table: mes_process_qc_method                                 */
/*==============================================================*/
create table mes_process_qc_method
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工序关联工位id',
   process_id           varchar(50) comment '工序id',
   qc_method_id         varchar(50) comment '质检方案id',
   qc_method_name       varchar(50) comment '质检方案名称',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_mes_qc_method (tenant_id, process_id, qc_method_id)
);

alter table mes_process_qc_method comment '工序关联质检';

drop table if exists mes_process_ng_item;

/*==============================================================*/
/* Table: mes_process_ng_item                                   */
/*==============================================================*/
create table mes_process_ng_item
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工序关联工位id',
   process_id           varchar(50) comment '工序id',
   ng_item_type_name    varchar(50) comment '不良项分类名称',
   ng_item_type_id      varchar(50) comment '不良项分类Id',
   ng_item_name         varchar(50) comment '不良项名称',
   ng_item_id           varchar(50) comment '不良项id',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_mes_process_ng_item (tenant_id, process_id, ng_item_type_id, ng_item_id)
);

alter table mes_process_ng_item comment 'mes_process_ng_item/工序关联不良项';




drop table if exists mes_machine_manufacturer;

/*==============================================================*/
/* Table: mes_machine_manufacturer                              */
/*==============================================================*/
create table mes_machine_manufacturer
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '设备制造商id',
   manufacturer_name    varchar(50) not null  comment '制造商名称',
   simple_name          varchar(50)  comment '制造商简称',
   adress               varchar(50)  comment '联系地址',
   contact              varchar(50)  comment '联系人',
   phone                varchar(50)  comment '联系电话',
   email                varchar(50)  comment '邮箱',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_manufacturer (tenant_id, manufacturer_name)
);

alter table mes_machine_manufacturer comment '设备制造商';


drop table if exists mes_machine_dataconfig;

/*==============================================================*/
/* Table: mes_machine_dataconfig                                */
/*==============================================================*/
create table mes_machine_dataconfig
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '读数配置id',
   data_type            varchar(10) not null  comment '1、生产数据；2、工艺数据；3、设备数据4、质量数据；',
   data_name            varchar(50) not null  comment '读数名称',
   data_unit            varchar(50)  comment '读数单位',
   data_adress          varchar(50)  comment '读数地址',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key mes_machine_dataconfig (tenant_id, data_type, data_name)
);

alter table mes_machine_dataconfig comment '读数配置';


drop table if exists mes_route;

/*==============================================================*/
/* Table: mes_route                                             */
/*==============================================================*/
create table mes_route
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工艺路线id',
   route_code           varchar(50) not null comment '工艺路线编码',
   route_name           varchar(50) not null comment '工艺路线名称',
   active_date          datetime comment '生效日期',
   unable_date          datetime comment '失效日期',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_route_code (tenant_id, route_code)
);

alter table mes_route comment 'mes_route/工艺路线主表';

drop table if exists mes_route_line;

/*==============================================================*/
/* Table: mes_route_line                                        */
/*==============================================================*/
create table mes_route_line
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工艺路线明细表id',
   route_id             varchar(50) comment '工艺id',
   seq                  int not null comment '序号',
   process_id           varchar(50) comment '工序id',
   process_code         varchar(50) comment '工序编码',
   process_name         varchar(50) comment '工序名称',
   prior_code           varchar(50) comment '上道工序编码；如果该工序为首道工序，prior_code填充first',
   next_code            varchar(50) comment '下道工序编码。如果该工序为最后一道工序，next_code字段填充end。用这个两个字段目的是为了更好寻找到最后一道工序与第一道工序。',
   link_type            varchar(10) comment '1,前续开始后续可开始2，前续结束后续可开始',
   in_unit              varchar(50) comment '投产单位',
   out_unit             varchar(50) comment '产出单位',
   in_qty               decimal(18,6) comment '投产数量',
   out_qty              decimal(18,6) comment '产出数量',
   machine_qty          tinyint comment '设备数量',
   wait_time            decimal(18,6) comment '等待时长',
   setup_time           decimal(18,6) comment '准备时长',
   setup_time_unit      varchar(50) comment '1，分钟；2，小时；3，天',
   process_time         decimal(18,6) comment '加工时长',
   mactime_time         decimal(18,6) comment '设备台时',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_route_line comment 'mes_route_line/工艺路线明细表';


drop table if exists mes_route_line_station;

/*==============================================================*/
/* Table: mes_route_line_station                                */
/*==============================================================*/
create table mes_route_line_station
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工序关联工位id',
   route_id             varchar(50) comment '工艺id',
   route_line_id        varchar(50) comment '路线id',
   station_id           varchar(50) comment '工位id',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_route_line_station comment '工序关联工位';


drop table if exists mes_route_line_method;

/*==============================================================*/
/* Table: mes_route_line_method                                 */
/*==============================================================*/
create table mes_route_line_method
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工序关联工位id',
   route_id             varchar(50) comment '工艺id',
   route_line_id        varchar(50) comment '路线id',
   qc_method_id         varchar(50) comment '质检方案id',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_route_line_method comment '工序关联质检方案';



drop table if exists mes_machine_type_task_employee;

/*==============================================================*/
/* Table: mes_machine_type_task_employee                        */
/*==============================================================*/
create table mes_machine_type_task_employee
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '任务人员id',
   policy_id            varchar(50)  comment '任务id',
   employee_id          varchar(50)  comment '员工id',
   employee_name        varchar(50) not null  comment '员工名称',
   employee_code        varchar(50)  comment '员工编码',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key mes_machine_type_task_employee (tenant_id, policy_id, employee_id)
);

alter table mes_machine_type_task_employee comment '策略关联人员';

drop table if exists mes_machine_type;

/*==============================================================*/
/* Table: mes_machine_type                                      */
/*==============================================================*/
create table mes_machine_type
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '设备类型id',
   type_name            varchar(50) not null  comment '设备类型名称',
   is_bind              varchar(10) default '0'  comment '工装绑定:1,绑定；0、不绑定；',
   label_id             varchar(50)  comment '关联mes_machine_label表id',
   is_clean             varchar(10) default '0'  comment '是否开启清洁:1,开启；0、不开启；',
   clean_valid          int  comment '',
   valid_unit           varchar(50)  comment '1,天；2，小时；',
   is_scan              varchar(10) default '0'  comment '1、需要，0，不需要',
   is_confirm           varchar(10) default '0'  comment '1,需要；0，不需要',
   template_id          varchar(50)  comment '模板id',
   ahead_type           varchar(10)  comment '1、提前1小时，2、提前8小时；3、提前1天；4、提前1周；5、不提醒；',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_manufacturer (tenant_id, type_name)
);

alter table mes_machine_type comment '设备类型';

drop table if exists mes_machine_type_policy;

/*==============================================================*/
/* Table: mes_machine_type_policy                               */
/*==============================================================*/
create table mes_machine_type_policy
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '设备类型策略id',
   machine_type_id      varchar(50)  comment '设备类型id',
   policy_group_id      varchar(50)  comment '策略组id',
   policy_name          varchar(50)  comment '策略名称',
   policy_description   varchar(256)  comment '策略描述',
   start_time           datetime  comment '策略开始时间',
   end_time             datetime  comment '策略结束时间',
   policy_type          varchar(10)  comment '1、点检；2、保养；',
   policy_rule          varchar(10)  comment '1、固定周期 2、浮动周期 3、累计用度 4、固定用度 5、手工创建',
   rule_name            varchar(50)  comment '用度名',
   qty                  int  comment '',
   unit                 varchar(10)  comment '1、分钟；2、小时；3、日；4、周；5、月',
   task_name            varchar(50)  comment '任务名称',
   task_description     varchar(256)  comment '任务描述',
   qty2                 int  comment '计划工时',
   unit2                varchar(10)  comment '1、分钟；2、小时；',
   template_id          varchar(50)  comment '模板id',
   is_scan              varchar(10) default '0'  comment '1、需要，0，不需要',
   is_confirm           varchar(10) default '0'  comment '1,需要；0，不需要',
   attach               varchar(1000)  comment '附件',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_type_policy (tenant_id, machine_type_id, policy_group_id, policy_name)
);

alter table mes_machine_type_policy comment '设备类型关联策略组';

drop table if exists mes_machine_type_data;

/*==============================================================*/
/* Table: mes_machine_type_data                                 */
/*==============================================================*/
create table mes_machine_type_data
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '设备类型读数id',
   machine_type_id      varchar(50)  comment '设备类型id',
   data_id              varchar(10) default '0'  comment '读数id',
   data_name            varchar(50)  comment '读数名称',
   unit                 varchar(50)  comment '单位',
   data_adress          varchar(50)  comment '读数地址',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   key uk_mes_machine_type_data (tenant_id, machine_type_id, data_id)
);

alter table mes_machine_type_data comment '设备类型关联读数';



drop table if exists mes_ware_house;

/*==============================================================*/
/* Table: mes_ware_house                                        */
/*==============================================================*/
create table mes_ware_house
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '车间id',
   type                 varchar(10)  comment '1、仓库；2、线边仓',
   work_shop_id         varchar(50)  comment '车间id:如果选中线边仓，线边仓是需要关联一个车间的。',
   house_code           varchar(50) not null  comment '仓库编码',
   house_name           varchar(50) not null  comment '仓库名称',
   qrcode               varchar(50)  comment '二维码',
   is_qcconrol          varchar(10)  comment '1,是；0，否；',
   qc_status            varchar(50)  comment ' 由字段“质量管理”触发   1、合格；2、不合格；3、待检  （这三个选项支持多选）

             多选保存为jason格式',
   is_capacity          varchar(10)  comment '1，是；0，否；',
   capacity_item        varchar(50)  comment ' 由库容管理触发  1，最大库存检查；2、最小库存检查；3、安全库存检查（支持多选）保存为jason',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   attach               varchar(1000)  comment '附件',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_ware_house (tenant_id, type, house_code)
);

alter table mes_ware_house comment '仓库';


drop table if exists mes_ware_storage;

create table mes_ware_storage
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '仓位id',
   storage_type        varchar(10)  comment '1、一级仓位；2二级仓位；',
   storage_code        varchar(50) not null  comment '仓位编码',
   storage_name        varchar(50) not null  comment '仓位名称',
   qrcode               varchar(50)  comment '二维码',
   is_qccontrol         varchar(10)  comment '1,与上级一致；2，单独启用；',
   qc_status            varchar(50)  comment ' 由字段“质量管理”触发   1、合格；2、不合格；3、待检  （这三个选项支持多选）

             多选保存为jason格式',
   up_storage_type      varchar(10)  comment '1、仓库；2、线边仓3，一级仓位；',
   up_storage_code      varchar(50)  comment '上级位置编码',
   up_storage_name      varchar(50)  comment '上级位置名称',
   up_storage_id        varchar(50)  comment '上级位置id',
   attach               varchar(1000)  comment '附件',
   note                 varchar(256)  comment '备注',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；   修改停用和启用时都要校验下级是否全部停用或启用',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_ware_storage comment '仓位';




drop table if exists mes_product;

/*==============================================================*/
/* Table: mes_product                                           */
/*==============================================================*/
create table mes_product
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '产品id',
   item_bom_id               varchar(50)  comment '物料清单BOMid',
   route_id             varchar(50) comment '工艺路线id',
   item_id              varchar(50) not null comment '物料ID',
   item_code            varchar(50) not null comment '物料编码',
   item_name            varchar(50) not null comment '物料名称',
   spec                 varchar(50) comment '规格描述',
   version              varchar(50) not null comment '版本',
   qty                  decimal(18,6) not null comment '数量',
   unit                 varchar(50) not null comment '单位',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   key uk_mes_product (tenant_id, item_bom_id, route_id, item_id, version)
);

alter table mes_product comment 'mes_product/产品';

drop table if exists mes_product_line;

/*==============================================================*/
/* Table: mes_product_line                                      */
/*==============================================================*/
create table mes_product_line
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '产品路线明细表id',
   product_id           varchar(50) comment '产品id',
   route_id             varchar(50) comment '工艺路线id',
   seq                  int not null comment '序号',
   process_id           varchar(50) comment '工序id',
   process_code         varchar(50) comment '工序编码',
   process_name         varchar(50) comment '工序名称',
   prior_code           varchar(50) comment '上道工序编码',
   next_code            varchar(50) comment '下道工序编码',
   link_type            varchar(10) comment '1,前续开始后续可开始2，前续结束后续可开始',
   in_unit              varchar(50) comment '投产单位',
   out_unit             varchar(50) comment '产出单位',
   in_qty               decimal(18,6) comment '投产数量',
   out_qty              decimal(18,6) comment '产出数量',
   machine_qty          tinyint comment '设备数量',
   wait_time            decimal(18,6) comment '等待时长',
   setup_time           decimal(18,6) comment '准备时长',
   setup_time_unit      varchar(50) comment '1，分钟；2，小时；3，天',
   process_time         decimal(18,6) comment '加工时长',
   machine_time         decimal(18,6) comment '设备台时',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_product_line comment '产品工艺路线明细';


drop table if exists mes_product_bom;

/*==============================================================*/
/* Table: mes_product_bom                                       */
/*==============================================================*/
create table mes_product_bom
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment 'bom明细id',
   product_id           varchar(50) comment '产品id',
   product_line_id      varchar(50) comment '产品工艺路线明细id',
   item_id              varchar(50) not null comment '物料id',
   item_code            varchar(50) not null comment '物料编码',
   item_name            varchar(50) not null comment '物料名称',
   qty                  decimal(18,6) comment '数量',
   unit                 varchar(50) not null comment '单位',
   loss_rate            decimal(18,6) comment '耗损率',
   is_control           varchar(10) comment '投料管控',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_product_bom comment 'mes_product_bom/产品BOM';

drop table if exists mes_product_route_station;

/*==============================================================*/
/* Table: mes_product_route_station                             */
/*==============================================================*/
create table mes_product_route_station
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工序关联工位id',
   product_id           varchar(50) comment '产品id',
   product_line_id      varchar(50) comment '产品LineId',
   station_id           varchar(50) comment '工位id',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_product_route_station comment '产品路线工序工位';

drop table if exists mes_product_route_method;

/*==============================================================*/
/* Table: mes_product_route_method                              */
/*==============================================================*/
create table mes_product_route_method
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工序关联工位id',
   product_id           varchar(50) comment '产品id',
   product_line_id      varchar(50) comment '产线Lineid',
   qc_method_id         varchar(50) comment '质检方案id',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_product_route_method comment '产品路线工序质检方案';



drop table if exists mes_product_bom_substitute;

/*==============================================================*/
/* Table: mes_product_bom_substitute                            */
/*==============================================================*/
create table mes_product_bom_substitute
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment 'bom替代料id',
   product_id           varchar(50) comment '产品id',
   product_bom_id       varchar(50) comment 'bomid',
   item_id              varchar(50) not null comment '被替代料id',
   subtitute_item_id    varchar(50) not null comment '替代料id',
   let_subtitute_qty    decimal(18,6) not null comment '被替代料数量',
   let_subtitute_unit   varchar(50) not null comment '被替代料单位',
   subtitute_qty        decimal(18,6) not null comment '替代料数量',
   subtitute_unit       varchar(50) comment '替代料单位',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_product_bom_substitute comment '产品BOM替代料';


drop table if exists mes_machine_fault_reason;

/*==============================================================*/
/* Table: mes_machine_fault_reason                              */
/*==============================================================*/
create table mes_machine_fault_reason
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '故障现象id',
   fault_reason         varchar(50) not null  comment '故障现象',
   fault_code           varchar(50)  comment '故障原因代码',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_stop_reason (tenant_id, fault_code)
);

alter table mes_machine_fault_reason comment '故障原因';


drop table if exists mes_machine_fault_appearance;

/*==============================================================*/
/* Table: mes_machine_fault_appearance                          */
/*==============================================================*/
create table mes_machine_fault_appearance
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '故障现象id',
   fault_appearance     varchar(50) not null  comment '故障现象',
   fault_code           varchar(50)  comment '故障现象编码',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   key uk_mes_machine_fault_appearance (tenant_id, fault_code)
);

alter table mes_machine_fault_appearance comment '故障现象';


drop table if exists mes_machine;

/*==============================================================*/
/* Table: mes_machine                                           */
/*==============================================================*/
create table mes_machine
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '设备id',
   machine_name         varchar(50) not null  comment '设备名称',
   machine_code         varchar(50) not null  comment '设备编码',
   machine_picture      varchar(1000)  comment '设备图片',
   machine_type_id      varchar(50)  comment '设备类型id',
   label_id             varchar(50)  comment '设备分类',
   qr_code              varchar(50)  comment '电子标签',
   level_id             varchar(50)  comment '设备等级',
   work_shop_id         varchar(50)  comment '车间',
   manufacturer_id      varchar(50)  comment '制造商',
   model                varchar(50)  comment '型号',
   serial_num           varchar(50)  comment '序列号',
   exit_date            date  comment '供应商出厂日期',
   enter_date           date  comment '进厂日期',
   first_turn_on_date   date  comment '首次启用日期',
   status               varchar(50) default '0'  comment '状态 ：1，启用，0停用；',
   description          varchar(256)  comment '规格描述',
   attach               varchar(1000)  comment '附件',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine (tenant_id, machine_code)
);

alter table mes_machine comment '设备管理';


drop table if exists mes_work_order;

/*==============================================================*/
/* Table: mes_work_order                                        */
/*==============================================================*/
create table mes_work_order
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工单id',
   order_no             varchar(50) not null comment '工单号',
   order_layer          tinyint default 1 comment '1，2，3，4，5；分别代表第1，2，3，4，5层；创建的工单默认为1，即父工单。',
   product_type         varchar(10) not null comment '1、面向库存；2、面向订单',
   sale_order_id        varchar(50) comment '销售订单号',
   order_type           varchar(10) not null comment '1、正常工单2、返工工单3、委外工单、4、打样工单',
   batch_type           varchar(10) comment '1、手动生成；2、按规则生成；',
   batch_no             varchar(50) comment '生产批号',
   item_id              varchar(50) comment '产品ID',
   item_code            varchar(50) not null comment '产品编码',
   item_name            varchar(50) comment '产品名称',
   spec                 varchar(50) comment '产品规格',
   plan_qty             decimal(18,6) comment '计划数量',
   completed_qty        decimal(18,6) comment '完成数量',
   unit                 varchar(50) comment '单位',
  level                tinyint comment '等级',
   pmc                  varchar(500) comment '以jason形式存储计划员对应的 id 及姓名；',
   director             varchar(500) comment '以jason形式存储生产主管对应的 id 及姓名；',
   plan_start_time      datetime comment '计划开始时间',
   plan_end_time        datetime comment '计划结束时间',
   real_start_time      datetime comment '实际开始时间',
   real_end_time        datetime comment '实际结束时间',
   workflow_type        varchar(10) comment '1、工艺路线；2、工艺路线+物料清单；3、产品BOM',
   bom_id               varchar(50) comment '引用的物料清单id',
   route_id             varchar(50) comment '工艺路线id',
   product_id           varchar(50) comment '产品bomid',
   flow_id              varchar(50) comment '审批流id，关联审批流表。',
   audit_status         varchar(50) comment '审批状态',
   attach               varchar(1000) comment '附件',
   status               varchar(50) default '1' comment '1、计划2、已排程3、已下发4、开工、5、完工6、取消',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_tenant_id_order_no (tenant_id, order_no)
);

alter table mes_work_order comment 'mes_work_order/工单';

drop table if exists mes_work_order_line;

/*==============================================================*/
/* Table: mes_work_order_line                                   */
/*==============================================================*/
create table mes_work_order_line
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工单工艺路线id',
   order_id             varchar(50) comment '工单id',
   route_id             varchar(50) comment '工艺路线id',
   seq                  int not null comment '序号',
   process_id           varchar(50) comment '工序id',
   process_code         varchar(50) comment '工序编码',
   process_name         varchar(50) comment '工序名称',
   prior_code           varchar(50) comment '上道工序编码',
   next_code            varchar(50) comment '下道工序编码',
   link_type            varchar(10) comment '1,前续开始后续可开始2，前续结束后续可开始',
   in_unit              varchar(50) comment '投产单位',
   out_unit             varchar(50) comment '产出单位',
   in_qty               decimal(18,6) comment '投产数量',
   out_qty              decimal(18,6) comment '产出数量',
   machine_qty          tinyint comment '设备数量',
   wait_time            decimal(18,6) comment '等待时长',
   setup_time           decimal(18,6) comment '准备时长',
   setup_time_unit      varchar(50) comment '1，分钟；2，小时；3，天',
   process_time         decimal(18,6) comment '加工时长',
   machine_time         decimal(18,6) comment '设备台时',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_work_order_line comment '缓存一份工艺路线进工单，避免因基础工艺路线修改问题';


drop table if exists mes_work_order_bom;

/*==============================================================*/
/* Table: mes_work_order_bom                                    */
/*==============================================================*/
create table mes_work_order_bom
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工单生产bomid',
   order_id             varchar(50) comment '工单id',
   product_id           varchar(50) comment '产品bomid',
   product_line_id      varchar(50) comment '产品工艺路线明细id',
   seq                  int comment '工序序号',
   process_id           varchar(50) comment '工序id',
   process_code         varchar(50) comment '工序编码',
   process_name         varchar(50) comment '工序名称',
   item_id              varchar(50) comment '物料id',
   item_code            varchar(50) not null comment '物料编码',
   item_name            varchar(50) not null comment '物料名称',
   qty                  decimal(18,6) comment '数量',
   unit                 varchar(50) comment '单位',
   loss_rate            decimal(18,6) comment '耗损率',
   is_control           varchar(10) comment '投料管控',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   Column_22            char(10),
   primary key (id)
);

alter table mes_work_order_bom comment '工单产品BOM';


drop table if exists mes_work_order_item_bom;

/*==============================================================*/
/* Table: mes_work_order_item_bom                               */
/*==============================================================*/
create table mes_work_order_item_bom
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工单物料id',
   order_id             varchar(50) comment '工单id',
   bom_id               varchar(50) comment '引用的物料清单id',
   item_id              varchar(50) not null comment '物料id',
   item_name            varchar(50) comment '物料名称',
   item_code            varchar(50) comment '物料编码',
   qty                  decimal(18,6) comment '数量',
   unit                 varchar(50) comment '单位',
   loss_rate            decimal(18,6) comment '耗损率',
   is_control           varchar(10) comment '投料管控:1,管控；0，不管控；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_work_order_item_bom comment '缓存一份物料清单进工单';


ALTER TABLE `mes_item_bom_detail` 
ADD COLUMN `unit_name` VARCHAR(50) NULL COMMENT '单位名称' AFTER `unit`;


ALTER TABLE `mes_item_bom_substitute` 
ADD COLUMN `let_subtitute_unit_name` VARCHAR(50) NULL COMMENT '被替代料单位名称' AFTER `let_subtitute_unit`,
ADD COLUMN `subtitute_unit_name` VARCHAR(50) NULL COMMENT '替代料单位名称' AFTER `subtitute_unit`;


ALTER TABLE `mes_product_bom` 
ADD COLUMN `unit_name` VARCHAR(45) NULL COMMENT '单位名称' AFTER `unit`;


ALTER TABLE `mes_product_bom_substitute` 
ADD COLUMN `let_subtitute_unit_name` VARCHAR(45) NULL COMMENT '被替代料单位' AFTER `let_subtitute_unit`,
ADD COLUMN `subtitute_unit_name` VARCHAR(45) NULL COMMENT '替代料单位名称' AFTER `subtitute_unit`;


ALTER TABLE `mes_work_order_bom` 
ADD COLUMN `unit_name` VARCHAR(50) NULL COMMENT '单位名称' AFTER `unit`;


ALTER TABLE `mes_work_order_item_bom` 
ADD COLUMN `unit_name` VARCHAR(50) NULL COMMENT '单位名称' AFTER `unit`;



ALTER TABLE `mes_work_order_bom` 
ADD COLUMN `total_qty` DECIMAL(18,6) NULL COMMENT '本工序总用量' AFTER `qty`;

DROP TABLE IF EXISTS mes_item_cell;
/*==============================================================*/
/* Table: mes_item_cell                                         */
/*==============================================================*/
CREATE TABLE mes_item_cell
(
   tenant_id            VARCHAR(50) COMMENT '租户ID',
   id                   VARCHAR(50) NOT NULL COMMENT '物料单元id',
   qrcode               VARCHAR(50) COMMENT '标签码',
   father_qrcode        VARCHAR(50) COMMENT '父标签码',
   batch                VARCHAR(50) COMMENT '入库批号/生产批号',
   seq_num              VARCHAR(50) COMMENT '序列号:用于相同批次，产出需要区分不同序列使用',
   item_id              VARCHAR(50) COMMENT '物料名称',
   item_code            VARCHAR(50) COMMENT '物料编码',
   item_name            VARCHAR(50) COMMENT '物料名称',
   spec                 VARCHAR(50) COMMENT '规格',
   qty                  DECIMAL(18,6) COMMENT '数量',
   unit_id              VARCHAR(50) COMMENT '单位，以生成时进入表中时第一次业务单位存储。',
   unit_name            VARCHAR(50) COMMENT '单位名称，以生成时进入表中时第一次业务单位存储。',
   storage_id           VARCHAR(50) COMMENT '对于在仓库中的才需要位置；原材料发到线边仓，也有个线边仓的位置。在制品不用位置表示，填入process。出库未入库的无位置状态1、有仓位编码的写入编码id;2、出库无位置的写入null;3、在制品写入process,最后一道工序也写入null标记生产完，与出库类似；',
   storage_code         VARCHAR(50) COMMENT '仓位编码',
   storage_name         VARCHAR(50) COMMENT '仓位名称',
   area_code            VARCHAR(50) COMMENT '位置一级仓位编码',
   area_name            VARCHAR(50) COMMENT '位置一级仓位名称',
   house_code           VARCHAR(50) COMMENT '仓库编码',
   house_name           VARCHAR(50) COMMENT '仓库名称',
   hope_in_storage_code VARCHAR(50) COMMENT '期望入库位置编码',
   hope_in_storage_name VARCHAR(50) COMMENT '期望入库位置名称',
   position_status      VARCHAR(10) COMMENT '1、仓储中2、转运中;3、在制',
   qrcode_status        VARCHAR(10) COMMENT '1、厂内；2、已发货；3、已投产；4、已置空；5、已退料；6、已报废；',
   qc_status            VARCHAR(10) COMMENT '质量状态：1、合格；2、待检；3、不合格。',
   business_status      VARCHAR(10) COMMENT '1、质检中；2、盘点中；3、无业务；4、生产中；5、生产完；',
   item_level           VARCHAR(10) COMMENT '1,一等品；2、二等品；3、三等品。作为可以灵活配置的数据字典。',
   sale_order_id        VARCHAR(50) COMMENT '销售id',
   sale_order_no        VARCHAR(50) COMMENT '销售订单号',
   order_id             VARCHAR(50) COMMENT '工单id',
   order_no             VARCHAR(50) COMMENT '工单号',
   supplier_id          VARCHAR(50) COMMENT '供应商id',
   supplier_name        VARCHAR(50) COMMENT '供应商名称',
   supplier_batch       VARCHAR(50) COMMENT '供应商批号',
   customer_id          VARCHAR(50) COMMENT '客户id',
   customer_name        VARCHAR(50) COMMENT '客户名称',
   customer_batch       VARCHAR(50) COMMENT '客户批号',
   produce_date         DATE COMMENT '生产日期',
   valid_date           DATE COMMENT '有效期',
   from_process         VARCHAR(50) COMMENT '已处理工序',
   now_process          VARCHAR(50) COMMENT '如果下道处理工序为end，那这个码就完成了所有工序。是一个完工品。预留以后使用',
   item_type            VARCHAR(10) COMMENT '1、库存品；2、在制品；2、完工未入库品',
   manage_way           VARCHAR(10) COMMENT '管理方式：1，标签管理；2，批次管理；3、无码管理。',
   qc_time              DATETIME COMMENT '上次质检时间',
   inventory_check_time DATETIME COMMENT '上次盘点时间',
   attach               VARCHAR(1000) COMMENT '附件',
   note                 VARCHAR(256) COMMENT '备注',
   create_by            VARCHAR(50) COMMENT '创建人',
   create_time          DATETIME COMMENT '创建时间',
   update_by            VARCHAR(50) COMMENT '修改人',
   update_time          DATETIME COMMENT '修改时间',
   is_deleted           TINYINT DEFAULT 0 COMMENT '删除标识',
   PRIMARY KEY (id)
);
ALTER TABLE mes_item_cell COMMENT '物料单元包括无码，批次，标签码三种类型。';


drop table if exists mes_machine_log;

/*==============================================================*/
/* Table: mes_machine_log                                       */
/*==============================================================*/
create table mes_machine_log
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '设备日志id',
   machine_id           varchar(50)  comment '设备id',
   oprate_type          varchar(10) default '0'  comment '记录关键动作的操作；',
   description          varchar(256)  comment '描述',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_machine_log comment '设备日志';




drop table if exists mes_machine_policy;

/*==============================================================*/
/* Table: mes_machine_policy                                    */
/*==============================================================*/
create table mes_machine_policy
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '设备策略id',
   machine_id           varchar(50)  comment '设备id',
   policy_group_id      varchar(50)  comment '策略组id',
   policy_name          varchar(50)  comment '策略名称',
   policy_description   varchar(256)  comment '策略描述',
   start_time           datetime  comment '策略开始时间',
   end_time             datetime  comment '策略结束时间',
   excute_base_time     datetime  comment '执行基准时间',
   last_excute_time     datetime  comment '上次执行时间',
   policy_type          varchar(10)  comment '1、点检；2、保养；',
   policy_rule          varchar(10)  comment '1、固定周期 2、浮动周期 3、累计用度 4、固定用度 5、手工创建',
   rule_name            varchar(50)  comment '用度名',
   qty                  int  comment '',
   unit                 varchar(10)  comment '1、分钟；2、小时；3、日；4、周；5、月',
   task_name            varchar(50)  comment '任务名称',
   task_description     varchar(256)  comment '任务描述',
   plan_time            int  comment '计划工时',
   time_unit            varchar(10)  comment '1、分钟；2、小时；',
   template_id          varchar(50)  comment '模板id',
   is_scan              varchar(10) default '0'  comment '1、需要，0，不需要',
   is_confirm           varchar(10) default '0'  comment '1,需要；0，不需要',
   attach               varchar(1000)  comment '附件',
   status               varchar(50) default '0'  comment '状态 ：1，启用，0停用；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_type_policy (tenant_id, machine_id, policy_group_id, policy_name)
);

alter table mes_machine_policy comment '设备关联策略';


drop table if exists mes_machine_repair_task_employee;

/*==============================================================*/
/* Table: mes_machine_repair_task_employee                      */
/*==============================================================*/
create table mes_machine_repair_task_employee
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '派工单人员id',
   repair_task_id       varchar(50) not null  comment '任务单id',
   employee_id          varchar(50)  comment '员工id',
   employee_code        varchar(50)  comment '员工编码',
   employee_name        varchar(50)  comment '员工名称',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_machine_repair_task_employee comment '维修任务计划执行人';


drop table if exists mes_machine_stop_time;

/*==============================================================*/
/* Table: mes_machine_stop_time                                 */
/*==============================================================*/
create table mes_machine_stop_time
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '设备停机id',
   machine_id           varchar(50)  comment '设备id',
   stop_type            varchar(10)  comment '1、计划性停机；2、非计划性停机；',
   stop_reason_id       varchar(50) default '0'  comment '停机原因',
   start_time           datetime  comment '停机开始时间',
   end_time             datetime  comment '停机结束时间',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   key uk_mes_machine_type_data (tenant_id, machine_id, stop_reason_id)
);

alter table mes_machine_stop_time comment '设备关联停机时间';


drop table if exists mes_machine_data;

/*==============================================================*/
/* Table: mes_machine_data                                      */
/*==============================================================*/
create table mes_machine_data
(
    tenant_id            varchar(50)  comment '租户ID',
    id                   varchar(50) not null  comment '设备读数id',
    machine_id           varchar(50)  comment '设备id',
    data_id              varchar(50) default '0'  comment '读数id',
    data_name            varchar(50)  comment '读数名称',
    unit                 varchar(50)  comment '单位',
    data_adress          varchar(50)  comment '读数地址',
    create_by            varchar(50)  comment '创建人',
    create_time          datetime  comment '创建时间',
    update_by            varchar(50)  comment '修改人',
    update_time          datetime  comment '修改时间',
    is_deleted           tinyint default 0  comment '删除标识',
    primary key (id),
    key uk_mes_machine_type_data (tenant_id, machine_id, data_id)
);

alter table mes_machine_data comment '设备类型关联读数';


drop table if exists mes_machine_maintenance_task;

/*==============================================================*/
/* Table: mes_machine_maintenance_task                          */
/*==============================================================*/
create table mes_machine_maintenance_task
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '维修任务id',
   maintenance_type     varchar(10)  comment '1,点检；2、保养；',
   task_code            varchar(50) not null  comment '维保任务号',
   task_title           varchar(50) not null  comment '维保任务标题',
   policy_id            varchar(50)  comment '',
   policy_code          varchar(50)  comment '策略编码',
   policy_name          varchar(256)  comment '策略名称',
   maintain_machine_id  varchar(50)  comment '维修目标',
   plan_start_time      datetime  comment '计划开始时间',
   plan_end_time        datetime  comment '计划结束时间',
   real_start_time      datetime  comment '实际开始时间',
   real_end_time        datetime  comment '实际结束时间',
   real_excuter         varchar(50)  comment '',
   attach               varchar(1000)  comment '附件',
   status               varchar(50) default '1'  comment '状态 ：1、未开始；2、执行中；3、已暂停；4、已结束；5、已取消；6、验收；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_type_policy (tenant_id)
);

alter table mes_machine_maintenance_task comment '维保任务';


drop table if exists mes_machine_maintenance_task_employee;

/*==============================================================*/
/* Table: mes_machine_maintenance_task_employee                 */
/*==============================================================*/
create table mes_machine_maintenance_task_employee
(
    tenant_id            varchar(50)  comment '租户ID',
    id                   varchar(50) not null  comment '派工单人员id',
    repair_task_id       varchar(50) not null  comment '任务单id',
    employee_id          varchar(50)  comment '员工id',
    employee_code        varchar(50)  comment '员工编码',
    employee_name        varchar(50)  comment '员工名称',
    create_by            varchar(50)  comment '创建人',
    create_time          datetime  comment '创建时间',
    update_by            varchar(50)  comment '修改人',
    update_time          datetime  comment '修改时间',
    is_deleted           tinyint default 0  comment '删除标识',
    primary key (id)
);

alter table mes_machine_maintenance_task_employee comment '维保任务计划执行人';




/*==============================================================*/
/* Table: mes_work_process_task                                 */
/*==============================================================*/
create table mes_work_process_task
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工序任务id',
   order_id             varchar(50) comment '工单id',
   order_no             varchar(50) not null comment '工单号',
   batch_no             varchar(50) comment '生产批次',
   item_id              varchar(50) comment '成品物料id',
   item_code            varchar(50) not null comment '成品物料编码',
   item_name            varchar(50) comment '成品物料名称',
   spec                 varchar(50) comment '成品物料规格',
   process_id           varchar(50) comment '工序id',
   process_name         varchar(50) comment '工序',
   process_code         varchar(50) comment '工序编码',
   seq                  int comment '工序',
   prior_code           varchar(50) comment '上道工序编码',
   next_code            varchar(50) comment '下道工序编码',
   link_type            varchar(10) comment '1,前续开始后续可开始2，前续结束后续可开始',
   plan_qty             decimal(18,6) comment '计划数量',
   scheduled_qty        decimal(18,6) comment '排程数量，排程后回写；',
   publish_qty          decimal(18,6) comment '下发后回写；',
   completed_qty        decimal(18,6) comment '完成数量，报工后回写。',
   unit                 varchar(50) comment '单位',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_work_process_task comment 'mes_work_process_task/工单工序任务';

drop table if exists mes_work_plan_task;

/*==============================================================*/
/* Table: mes_work_plan_task                                    */
/*==============================================================*/
create table mes_work_plan_task
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '生产任务id',
   task_code            varchar(50) comment '生产任务单号',
   task_type            varchar(50) default '2' comment '1、任务排他2、任务共接',
   process_task_id      varchar(50) comment '关联工单工序任务id',
   order_id             varchar(50) comment '工单id',
   order_no             varchar(50) comment '工单号',
   batch_no             varchar(50) comment '批次号',
   process_id           varchar(50) comment '工序id',
   process_code         varchar(50) comment '工序编码',
   process_name         varchar(50) comment '工序名称',
   item_id              varchar(50) comment '关联txorg',
   item_code            varchar(50) comment '产品编码',
   item_name            varchar(50) comment '产品名称',
   station_id           varchar(50) comment '工位id',
   station_code         varchar(50) comment '工位编码',
   station_name         varchar(50) comment '工位名称',
   plan_qty             decimal(18,6) comment '排程数量',
   unit                 varchar(50) comment '单位',
   user_type            varchar(10) comment '1、指定给班组（用户组）2、指定给人员（用户）',
   team_id              varchar(50) comment '指定班组ID，单选，在主表中体现。',
   plan_type            varchar(10) comment '1,排时间；2，排班次；',
   plan_date            date  comment '计划日期',
   shift_id             varchar(50) comment '班次',
   plan_start_time      datetime comment '计划开始时间',
   plan_time            decimal(18,6)  comment '计划时长',
   time_unit            varchar(50) comment '单位:1,分；2，小时；3，分；',
   plan_end_time        datetime comment '计划完工时间',
   real_start_time      datetime comment '实际开始时间',
   real_end_time        datetime comment '实际完工时间',
   total_qty            decimal(18,6) comment '实际产量',
   lock_status          varchar(50) default '0' comment '1、锁定；0、释放；',
   plan_status          varchar(50) default '1' comment '1、已排程；2、已下发；',
   exe_status           varchar(10) comment '1、未开始；2、生产；3、暂停；4、结束；5、取消；',
   is_stock_check             varchar(10) default '0' comment '库存校验1、 是；0，否；',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_work_plan_task comment '派工单生产任务';

drop table if exists mes_work_plan_task_employee;

/*==============================================================*/
/* Table: mes_work_plan_task_employee                           */
/*==============================================================*/
create table mes_work_plan_task_employee
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '派工单人员id',
   task_id              varchar(50) not null comment '任务单id',
   employee_id          varchar(50) comment '员工id',
   employee_code        varchar(50)  comment '员工编码',
   employee_name        varchar(50)  comment '员工名称',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_work_plan_task_employee comment '派工计划生产任务关联计划执行人员';



drop table if exists mes_machine_task_log;

/*==============================================================*/
/* Table: mes_machine_task_log                                  */
/*==============================================================*/
create table mes_machine_task_log
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '任务日志id',
   task_type            varchar(10) default '0'  comment '1、维修任务；2、点检任务；3、保养任务；',
   task_id              varchar(50)  comment '任务id',
   oprate_type          varchar(10) default '0'  comment '1、创建任务；2、领取任务；3、暂停任务；4、恢复任务；5、结束任务；6、取消任务；',
   description          varchar(256)  comment '描述',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_machine_task_log comment '设备任务日志';



drop table if exists mes_machine_repair_task;

/*==============================================================*/
/* Table: mes_machine_repair_task                               */
/*==============================================================*/
create table mes_machine_repair_task
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '维修任务id',
   task_name            varchar(50)  comment '任务名称',
   task_code            varchar(50)  comment '任务编码',
   task_description     varchar(256)  comment '任务描述',
   repair_machine_id    varchar(50)  comment '维修目标',
   fault_appearance     varchar(50)  comment '故障现象',
   fault_reason         varchar(50)  comment '',
   end_time             datetime  comment '截止时间',
   is_scan              varchar(10) default '0'  comment '1、需要，0，不需要',
   template_id          varchar(50)  comment '模板id',
   is_confirm           varchar(10) default '0'  comment '1,需要；0，不需要',
   ahead_type           varchar(10)  comment '1、提前1小时，2、提前8小时；3、提前1天；4、提前1周；5、不提醒；',
   real_excuter         varchar(256)  comment '实际执行人',
   excute_start_time    datetime  comment '',
   excute_end_time      datetime  comment '',
   acceptor             varchar(50)  comment '验收人',
   attach               varchar(1000)  comment '附件',
   note                 varchar(256)  comment '备注',
   status               varchar(50) default '1'  comment '状态 ：1、未开始；2、执行中；3、已暂停；4、已结束；5、已取消；6、验收；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_type_policy (tenant_id)
);

alter table mes_machine_repair_task comment '维修任务';

ALTER TABLE `mes_work_plan_task` 
ADD COLUMN `distrubute_time` DATETIME NULL COMMENT '下发时间' AFTER `real_end_time`;

drop table if exists mes_work_produce_task;

/*==============================================================*/
/* Table: mes_work_produce_task                                 */
/*==============================================================*/
create table mes_work_produce_task
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '执行生产任务id',
   task_code            varchar(50) comment '执行生产任务编码：拷贝计划生产任务编码赋值',
   plan_task_id         varchar(50) comment '计划生产任务id',
   order_id             varchar(50) comment '工单id',
   order_no             varchar(50) comment '工单号',
   batch_no             varchar(50) comment '批次号',
   process_id           varchar(50) comment '工序id',
   process_code         varchar(50) comment '工序编码',
   process_name         varchar(50) comment '工序名称',
   item_id              varchar(50) comment '成品物料id',
   item_code            varchar(50) comment '成品物料编码',
   item_name            varchar(50) comment '成品物料名称',
   station_id           varchar(50) comment '执行工位id',
   station_code         varchar(50) comment '执行工位编码',
   station_name         varchar(50) comment '执行工位名称',
   plan_qty             decimal(18,6) comment '排程数量',
   unit                 varchar(50) comment '单位',
   plan_date            date comment '计划日期',
   shift_id             varchar(50) comment '班次',
   team_id              varchar(50) comment '执行班组id',
   plan_start_time      datetime comment '计划开始时间',
   plan_end_time        datetime comment '计划结束时间',
   real_start_time      datetime comment '实际开始时间',
   real_end_time        datetime comment '实际结束时间',
   total_qty            decimal(18,6) comment '总产量',
   good_qty             decimal(18,6) comment '报工数量',
   bad_qty              decimal(18,6) comment '报工不合格数量',
   exe_status           varchar(10) comment '1、未开始；2、生产；3、暂停；4、结束；5、取消；',
   is_sop               varchar(10) default '0'  comment '1、是；0、否。默认否',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_work_produce_task comment '执行生产任务';


drop table if exists mes_work_produce_task_employee;

/*==============================================================*/
/* Table: mes_work_produce_task_employee                        */
/*==============================================================*/
create table mes_work_produce_task_employee
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '执行生产任务人员id',
   excute_task_id       varchar(50) not null comment '执行生产任务id',
   employee_id          varchar(50) comment '员工id',
   employee_code        varchar(50)  comment '员工编码',
   employee_name        varchar(50)  comment '员工名称',
   using_type              varchar(50) comment '1、指派领取；2、自己领取，3、交接任务',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_work_produce_task_employee comment '执行生产任务关联计划执行人员';

ALTER TABLE `mes_work_plan_task` 
ADD COLUMN `seq` INT NULL COMMENT '工序序号' AFTER `process_name`;

ALTER TABLE `mes_work_produce_task` 
ADD COLUMN `seq` INT NULL COMMENT '工序序号' AFTER `process_name`;

drop table if exists mes_work_produce_material_record;

/*==============================================================*/
/* Table: mes_work_produce_material_record                      */
/*==============================================================*/
create table mes_work_produce_material_record
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '投料id',
   produce_task_id      varchar(50) not null comment '执行生产任务id',
   feed_type            varchar(10) comment '1、投料；2、撤料；',
   item_id              varchar(50) comment '物料id',
   item_code            varchar(50) comment '物料编码',
   item_name            varchar(50) comment '物料名称',
   batch_no             varchar(50) comment '批号',
   qrcode               varchar(50) comment '二维码',
   back_qrcode          varchar(50) comment '撤回二维码',
   item_cell_state_id   varchar(50) comment '物料单元id',
   feed_qty             decimal(18,6) comment '投料数量',
   remain_qty           decimal(18,6) comment '投后余量',
   used_qty             decimal(18,6) comment '扣料数量',
   feed_storage_id      varchar(50) comment '投料位置',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_work_produce_material_record comment '投料记录';





drop table if exists mes_ware_feeding_storage_relate_area;

/*==============================================================*/
/* Table: mes_ware_feeding_storage_relate_area                  */
/*==============================================================*/
create table mes_ware_feeding_storage_relate_area
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '关联仓位id',
   area_id              varchar(50)  comment '区域id',
   feeding_storage      varchar(50)  comment '投料仓位',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_ware_feeding_storage_relate_area comment '投料仓关联区域';


drop table if exists mes_ware_finished_storage_relate_area;

/*==============================================================*/
/* Table: mes_ware_finished_storage_relate_area                 */
/*==============================================================*/
create table mes_ware_finished_storage_relate_area
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '关联仓位id',
   area_id              varchar(50)  comment '区域id',
   finished_storage     varchar(50)  comment '完工仓位',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_ware_finished_storage_relate_area comment '完工仓关联区域';



drop table if exists mes_work_produce_record;

/*==============================================================*/
/* Table: mes_work_produce_record                               */
/*==============================================================*/
create table mes_work_produce_record
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '报工id',
   produce_task_id      varchar(50) comment '如果为空，则采取的是流转卡的生产方式；否则是生产任务的形式。',
   order_id             varchar(50) comment '工单id',
   order_no             varchar(50) comment '工单号',
   report_type          varchar(10) comment '1自动报工 2手工报工3有生产任务的报工4无生产任务，流转卡形式报工',
   process_id           varchar(50) comment '工序id',
   process_code         varchar(50) comment '工序编码',
   process_name         varchar(50) comment '工序名称',
   item_id              varchar(50) comment '产品id',
   item_code            varchar(50) comment '产品编码',
   item_name            varchar(50) comment '产品编码',
   item_cell_state_id   varchar(50) comment '物料单元id',
   qrcode               varchar(50) comment '对某个租户内，唯一存在',
   batch_no             varchar(50) comment '生产批号',
   customer_batch       varchar(50) comment '客户批号',
   sequence_code        varchar(50) comment '序列码可以用来区分相同批次不同箱数',
   submit_qty           decimal(18,6) comment '报工数量',
   unit                 varchar(50) comment '单位',
   qc_status            varchar(50) comment '1、合格；2、待检；3、不合格；',
   produce_date         date comment '生产日期',
   owner_date           date comment '归属日期，某些工厂，例如夜班时间2020.10.8 0：00：00到6：00：00,虽然日期上是10.8日，但是他们产量归属于10.7日，所以增加这个字段便于扩展。',
   start_time           datetime comment '开始时间',
   end_time             datetime comment '结束时间',
   product_time         decimal(18,6) comment '生产用时',
   time_unit            varchar(10),
   employee_id          varchar(50) comment '员工IDs',
   employee_code        varchar(50) comment '员工编码',
   employee_name        varchar(50) comment '员工名称',
   team_id              varchar(50) comment '班组id',
   station_id           varchar(50) comment '工位id',
   station_code         varchar(50) comment '工位编码',
   station_name         varchar(50) comment '工位名称',
   attach               varchar(1000) comment '附件',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_work_produce_record comment '产出记录';


drop table if exists mes_report_template_control;

create table mes_report_template_control
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '设备报告模板控件id',
   report_template_id   varchar(50) not null  comment '设备报告模板id',
   control_type         varchar(10)  comment '1、分组；2、文本；3、单选框；4、检查项；5、任务项；6、读数；7、照片；8、时间。',
   control_name         varchar(50) not null  comment '控件名称',
   father_control_id    varchar(50)  comment '设备报告模板父控件id：只支持一级，即分组下不可以再放分组;对于没放在分组下的控件，此字段为0；否则写分组控件id。',
   seq                  int  comment '排版序号',
   is_required_field    varchar(10) default '0'  comment '0,否；1，是。',
   remind_type          varchar(10) default '0'  comment '1、普通提示；2、警示提示。',
   remind_word          varchar(50)  comment '提示语',
   is_multiple_row      varchar(10) default '0'  comment '针对文本框可设置：  0，否；1，是。',
   option_value         varchar(50)  comment '选项值,以逗号隔开，供单选框使用。',
   is_exception         varchar(10) default '0'  comment '针检查项可设置：  0，关闭；1，开启。',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);
alter table mes_report_template_control comment '报告模板控件';

drop table if exists mes_report_template;

create table mes_report_template
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '设备报告模板id',
   template_name varchar(50) not null  comment '设备模板名称',
   template_type        varchar(10)  comment '1、生产报告模板；2、设备报告模板；',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_report_template comment '报告模板';

ALTER TABLE `mes_work_order_item_bom` 
ADD COLUMN `total_qty` DECIMAL(18,6) NULL COMMENT '本物料工单总用量' AFTER `qty`;

ALTER TABLE `mes_work_produce_material_record` 
ADD COLUMN `qc_status` VARCHAR(10) NULL COMMENT '投料质量状态' AFTER `item_cell_state_id`;



drop table if exists mes_machine_task_report;

create table mes_task_report
(
    tenant_id            varchar(50)  comment '租户ID',
    id                   varchar(50) not null  comment '任务报告id',
    task_type            varchar(10)  comment '1,点检任务；2、保养任务；3、维修任务；4、生产任务',
    task_id              varchar(50) not null  comment '任务号',
    template_id          varchar(50)  comment '模板id',
    report_context       text not null  comment '以jason形式存储：控件id,控件名称，控件值。',
    create_by            varchar(50)  comment '创建人',
    create_time          datetime  comment '创建时间',
    update_by            varchar(50)  comment '修改人',
    update_time          datetime  comment '修改时间',
    is_deleted           tinyint default 0  comment '删除标识',
    primary key (id),
);

alter table mes_task_report comment '设备任务报告';

ALTER TABLE `mes_machine_maintenance_task`
ADD COLUMN `acceptor` VARCHAR(50) NULL COMMENT '验收人' AFTER `real_end_time`;

alter table mes_machine_repair_task drop column end_time;

alter table mes_machine_repair_task
	add plan_start_time datetime null comment '计划开始时间' after fault_reason;

alter table mes_machine_repair_task
	add plan_end_time datetime null comment '计划结束时间' after plan_start_time;

alter table mes_machine_type_data
	add data_type varchar(10) null comment '读数类型' after data_id;

ALTER TABLE `mes_work_produce_material_record` 
ADD COLUMN `unit` VARCHAR(50) NULL AFTER `feed_storage_id`;

CREATE TABLE mes_item_take_delivery_record
(
    tenant_id      varchar(50)    DEFAULT NULL COMMENT '租户id',
    id             varchar(50) NOT NULL COMMENT '收货记录id',
    receive_type   varchar(10)    DEFAULT NULL COMMENT '收货类型：1、按采购订单收；2、普通收货；3、按收货单收货；3、退货接收',
    item_cell_id   varchar(50)    DEFAULT NULL COMMENT '物料单元id',
    qrcode         varchar(50)    DEFAULT NULL COMMENT '标签码',
    batch          varchar(50)    DEFAULT NULL COMMENT '批号',
    item_id        varchar(50)    DEFAULT NULL COMMENT '物料id',
    item_code      varchar(50)    DEFAULT NULL COMMENT '物料编码',
    item_name      varchar(50)    DEFAULT NULL COMMENT '物料名称',
    qty            decimal(18, 6) DEFAULT NULL COMMENT '数量',
    unit_id        varchar(50)    DEFAULT NULL COMMENT '单位',
    unit_name      varchar(50)    DEFAULT NULL COMMENT '单位名称',
    storage_code   varchar(50)    DEFAULT NULL COMMENT '仓位编码',
    storage_name   varchar(50)    DEFAULT NULL COMMENT '仓位名称',
    produce_date   timestamp NULL DEFAULT NULL COMMENT '生产日期',
    valid_date     timestamp NULL DEFAULT NULL COMMENT '有效期',
    supplier_id    varchar(50)    DEFAULT NULL COMMENT '供应商id',
    supplier_code  varchar(50)    DEFAULT NULL COMMENT '供应商编码',
    supplier_name  varchar(50)    DEFAULT NULL COMMENT '供应商名称',
    supplier_batch varchar(50)    DEFAULT NULL COMMENT '供应商批次',
    quality_status varchar(10)    DEFAULT NULL COMMENT '质量状态',
    reciept_id     varchar(50)    DEFAULT NULL COMMENT '收货单据',
    reciept_code   varchar(50)    DEFAULT NULL COMMENT '收货单据编码',
    attach         varchar(1000)  DEFAULT NULL COMMENT '附件',
    note           varchar(256)   DEFAULT NULL COMMENT '备注',
    create_by      varchar(50)    DEFAULT NULL COMMENT '创建人',
    create_time    datetime       DEFAULT NULL COMMENT '创建时间',
    update_by      varchar(50)    DEFAULT NULL COMMENT '修改人',
    update_time    datetime       DEFAULT NULL COMMENT '修改时间',
    is_deleted     tinyint        DEFAULT '0' COMMENT '删除标识',
    PRIMARY KEY (id)
);

alter table mes_item_transfer_record modify qty decimal(18,6) null comment '数量';

alter table mes_item_transfer_record modify produce_date datetime null comment '生产日期';

alter table mes_item_transfer_record modify valid_date datetime null comment '有效期';

alter table mes_item_transfer_record
	add manage_way varchar(50) null comment '管理方式：1，标签管理；2，批次管理；3、无码管理。' after quality_status;

drop table if exists mes_sale_order;

/*==============================================================*/
/* Table: mes_sale_orde                                         */
/*==============================================================*/
create table mes_sale_order
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '销售订单id',
   sale_order_no        varchar(50) comment '销售订单号',
   external_sale_order_no varchar(50) comment '外部单据号',
   customer_id         varchar(50) comment '客户id',
   customer_name       varchar(50) comment '客户名称',
   note                 varchar(256) comment '备注',
   status               varchar(10) comment '1、新建；2、完成；3、结束',
   flow_id              varchar(50) comment '审批流id，关联审批流表。',
   audit_status         varchar(50) comment '审批状态',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_sale_order comment '销售订单';


drop table if exists mes_sale_order_line;

/*==============================================================*/
/* Table: mes_sale_order_line                                   */
/*==============================================================*/
create table mes_sale_order_line
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '采购清单物料行id',
   sale_order_id        varchar(50) comment '销售订单id',
   sale_order_no        varchar(50) comment '销售订单',
   line_number          int comment '行号',
   item_id              varchar(50) comment '物料id',
   item_code            varchar(50) comment '物料编码',
   item_name            varchar(50) comment '物料名称',
   spec                 varchar(50) comment '物料规格',
   sale_order_qty       decimal(18,6) comment '下单数量',
   sale_order_complete_qty decimal(18,6) comment '交付数量',
   return_qty           decimal(18,6) comment '退货数量',
   plan_qty             decimal(18,6) comment '计划数量',
   complete_qty         decimal(18,6) comment '已完成数量',
   unit                 varchar(50) comment '单位',
   sale_order_main_qty  decimal(18,6) comment '主单位下单数量',
   sale_order_complete_main_qty decimal(18,6) comment '主单位交付数量',
   return_main_qty      decimal(18,6) comment '主单位退货数量',
   plan_main_qty        decimal(18,6) comment '主单位计划数量',
   complete_main_qty    decimal(18,6) comment '主单位已完成数量',
   main_unit            varchar(50) comment '主单位',
   required_date        date comment '需求时间',
   status               varchar(10) comment '1、新建；2、完成；3、结束',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_sale_order_line comment '销售订单物料行';


drop table if exists mes_work_order_relate_sale;

/*==============================================================*/
/* Table: mes_work_order_relate_sale                            */
/*==============================================================*/
create table mes_work_order_relate_sale
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '工单关联销售订单id',
   order_id             varchar(50) comment '工单id',
   order_no             varchar(50) comment '工单',
   sale_order_id        varchar(50) comment '销售订单id',
   sale_order_no        varchar(50) comment '销售订单',
   sale_order_line_id   varchar(50) comment '物料行id',
   line_number          int comment '行号',
   item_id              varchar(50) comment '物料id',
   item_code            varchar(50) comment '物料编码',
   item_name            varchar(50) comment '物料名称',
   spec                 varchar(50) comment '物料规格',
   plan_qty             decimal(18,6) comment '计划数量',
   unit                 varchar(50) comment '单位',
   required_date        date comment '需求时间',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);


alter table mes_work_order_relate_sale comment '工单关联销售订单物料行';



ALTER TABLE `mes_work_calendar` 
ADD COLUMN `station_id` VARCHAR(50) NULL DEFAULT NULL COMMENT '工位id' AFTER `shift_id`,
ADD COLUMN `station_code` VARCHAR(50) NULL DEFAULT NULL COMMENT '工位编码' AFTER `station_id`,
ADD COLUMN `station_name` VARCHAR(50) NULL DEFAULT NULL COMMENT '工位名称' AFTER `station_code`;

drop table if exists mes_schedule_standard_production_volume;

/*==============================================================*/
/* Table: mes_schedule_standard_production_volume               */
/*==============================================================*/
create table mes_schedule_standard_production_volume
(
   tenant_id            varchar(50) not null comment '租户ID',
   id                   varchar(50) not null comment '标准产能主键id',
   standard_code        varchar(50) comment '编码',
   standard_type        varchar(50) comment '1、工序；2、工艺路线；3、生产BOM。',
   product_bom_id       varchar(50) comment '产品BOM',
   route_id             varchar(50) comment '工艺路线',
   process_extend_id    varchar(50) comment '工序扩展ID',
   process_id           varchar(50) not null comment '工序id',
   process_code         varchar(50) not null comment '工序编码',
   process_name         varchar(50) comment '工序名称',
   process_seq          int comment '工序序号',
   station_id           varchar(50) comment '工位id',
   station_code         varchar(50) comment '工位编码',
   station_name         varchar(50) comment '工位名称',
   item_id              varchar(50) comment '产出物料id',
   item_code            varchar(50) comment '产出物料编码',
   item_name            varchar(50) comment '产出物料名称',
   volume_type          varchar(50) comment '1、产能；2、生产节拍。',
   time_unit            varchar(50) comment '0、秒；1、分；2、小时；3、天；',
   time_period          decimal(18,6) comment '时长',
   qty                  decimal(18,6) comment '数量',
   unit                 varchar(50) comment '单位',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_mes_schedule_standard_production_volume (tenant_id, standard_type, product_bom_id, route_id, process_id, station_id, item_id)
);

alter table mes_schedule_standard_production_volume comment '标准产能';


drop table if exists mes_schedule_prepare_time;

/*==============================================================*/
/* Table: mes_schedule_prepare_time                             */
/*==============================================================*/
create table mes_schedule_prepare_time
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '动态准备时间id',
   prepare_code         varchar(50) not null comment '编码',
   station_id           varchar(50) comment '工位id',
   station_code         varchar(50) comment '工位编码',
   station_name         varchar(50) comment '工位名称',
   source_item_id       varchar(50) comment '更换前产出物料id',
   source_item_code     varchar(50) comment '更换前产出物料编码',
   source_item_name     varchar(50) comment '更换前产出物料名称',
   destination_item_id  varchar(50) comment '更换后产出物料',
   destination_item_code varchar(50) comment '更换后产出物料编码',
   destination_item_name varchar(50) comment '更换后产出物料名称',
   prepare_duration     decimal(18,6) comment '动态准备时长',
   unit_name            varchar(50) comment '1、分钟；2、小时。',
   status               varchar(50) default '1' comment '状态 ：1，启用，0停用；',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id),
   unique key uk_mes_schedule_prepare_time (tenant_id, station_id, source_item_id, destination_item_id)
);

alter table mes_schedule_prepare_time comment '动态准备时间';

drop table if exists mes_schedule_auto_rule_configure;

/*==============================================================*/
/* Table: mes_schedule_auto_rule_configure                      */
/*==============================================================*/
create table mes_schedule_auto_rule_configure
(
   tenant_id            varchar(50) comment '租户ID',
   id                   varchar(50) not null comment '自动排程规则id',
   order_rule           varchar(200) comment '三个枚举含义：按工单优先级降序；按工单计划时间升序；按工单计划结束时间升序。',
   process_rule         varchar(50) default '1' comment '1、常规方案；2、最小换型时间',
   station_rule         varchar(50) default '1' comment '1、N型排程；2、Z型排程',
   split_rule           varchar(50) default '3' comment '1、按班次拆分；2、按设置数量拆分；3、不拆分；',
   dispatch_rule        varchar(50) default '1' comment '1、正向分派；2、逆向分派。',
   reserve1             varchar(50) comment '预留规则1',
   reserve2             varchar(50) comment '预留规则2',
   reserve3             varchar(50) comment '预留规则3',
   reserve4             varchar(50) comment '预留规则4',
   reserve5             varchar(50) comment '预留规则5',
   reserve6             varchar(50) comment '预留规则6',
   note                 varchar(256) comment '备注',
   create_by            varchar(50) comment '创建人',
   create_time          datetime comment '创建时间',
   update_by            varchar(50) comment '修改人',
   update_time          datetime comment '修改时间',
   is_deleted           tinyint default 0 comment '删除标识',
   primary key (id)
);

alter table mes_schedule_auto_rule_configure comment '自动排程规则设置';



drop table if exists mes_spare_parts;

/*==============================================================*/
/* Table: mes_spare_parts                                       */
/*==============================================================*/
create table mes_spare_parts
(
    tenant_id            varchar(50)  comment '租户ID',
    id                   varchar(50) not null  comment '备件id',
    type_id              varchar(50)  comment '备件类型',
    picture              varchar(50)  comment '图片',
    spare_parts_code     varchar(50) not null  comment '备件编码',
    spare_parts_name     varchar(50) not null  comment '备件名称',
    spare_parts_description varchar(200) not null  comment '备件描述',
    model                varchar(50)  comment '型号',
    qty                  decimal(18,6)  comment '数量',
    unit                 varchar(50)  comment '主单位',
    attach               varchar(1000)  comment '附件',
    note                 varchar(256)  comment '备注',
    create_by            varchar(50)  comment '创建人',
    create_time          datetime  comment '创建时间',
    update_by            varchar(50)  comment '修改人',
    update_time          datetime  comment '修改时间',
    is_deleted           tinyint default 0  comment '删除标识',
    primary key (id),
    unique key uk_spare_parts (tenant_id, spare_parts_code, model)
);

alter table mes_spare_parts comment '备件定义';



drop table if exists mes_spare_parts_type;

/*==============================================================*/
/* Table: mes_spare_parts_type                                  */
/*==============================================================*/
create table mes_spare_parts_type
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '备品备件类型id',
   type_code            varchar(50) not null  comment '备品备件类型编码',
   type_name            varchar(50) not null  comment '备品备件类型名称',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_spare_parts_type (tenant_id, type_code)
);

alter table mes_spare_parts_type comment '备件类型';


drop table if exists mes_spare_parts_house;

/*==============================================================*/
/* Table: mes_spare_parts_house                                 */
/*==============================================================*/
create table mes_spare_parts_house
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '车间id',
   type                 varchar(10)  comment '1、仓库；2、线边仓',
   work_shop_id         varchar(50)  comment '车间id:如果选中线边仓，线边仓是需要关联一个车间的。',
   house_code           varchar(50) not null  comment '仓库编码',
   house_name           varchar(50) not null  comment '仓库名称',
   qrcode               varchar(50)  comment '二维码',
   is_qcconrol          varchar(10)  comment '1,是；0，否；',
   qc_status            varchar(50)  comment ' 由字段“质量管理”触发   1、合格；2、不合格；3、待检  （这三个选项支持多选）多选保存为jason格式',
   is_capacity          varchar(10)  comment '1，是；0，否；',
   capacity_item        varchar(50)  comment ' 由库容管理触发  1，最大库存检查；2、最小库存检查；3、安全库存检查（支持多选）保存为jason',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   attach               varchar(1000)  comment '附件',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_ware_house (tenant_id, type, house_code)
);

alter table mes_spare_parts_house comment '备件库';



drop table if exists mes_spare_parts_storage;

/*==============================================================*/
/* Table: mes_spare_parts_storage                               */
/*==============================================================*/
create table mes_spare_parts_storage
(
    tenant_id            varchar(50)  comment '租户ID',
    id                   varchar(50) not null  comment '仓位id',
    storage_type         varchar(10)  comment '1、一级仓位；2二级仓位；',
    storage_code         varchar(50) not null  comment '仓位编码',
    storage_name         varchar(50) not null  comment '仓位名称',
    qrcode               varchar(50)  comment '二维码',
    is_qccontrol         varchar(10)  comment '1,与上级一致；2，单独启用；',
    qc_status            varchar(50)  comment ' 由字段“质量管理”触发   1、合格；2、不合格；3、待检  （这三个选项支持多选）多选保存为jason格式',
    up_storage_type      varchar(10)  comment '1、仓库；2、线边仓3，一级仓位；',
    up_storage_code      varchar(50)  comment '上级位置编码',
    up_storage_name      varchar(50)  comment '上级位置名称',
    up_storage_id        varchar(50)  comment '上级位置id',
    attach               varchar(1000)  comment '附件',
    note                 varchar(256)  comment '备注',
    status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；   修改停用和启用时都要校验下级是否全部停用或启用',
    create_by            varchar(50)  comment '创建人',
    create_time          datetime  comment '创建时间',
    update_by            varchar(50)  comment '修改人',
    update_time          datetime  comment '修改时间',
    is_deleted           tinyint default 0  comment '删除标识',
    primary key (id)
);

alter table mes_spare_parts_storage comment '备件库位';



drop table if exists mes_spare_parts_receipt_head;

/*==============================================================*/
/* Table: mes_spare_parts_receipt_head                          */
/*==============================================================*/
create table mes_spare_parts_receipt_head
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '入库单表头id',
   receipt_code         varchar(50)  comment '入库单单号',
   receipt_type         varchar(50)  comment '1、采购入库；2、退回入库；',
   in_storage_code      varchar(50)  comment '库位编码',
   in_storage_name      varchar(50)  comment '库位名称',
   in_date              date  comment '入库日期',
   employee             varchar(50)  comment '入库人员',
   note                 varchar(256)  comment '备注',
   status               varchar(10)  comment '1、新建；2、完成；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_spare_parts_receipt_head comment '备件入表头';


drop table if exists mes_spare_parts_receipt_line;

/*==============================================================*/
/* Table: mes_spare_parts_receipt_line                          */
/*==============================================================*/
create table mes_spare_parts_receipt_line
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '入库备件行id',
   receipt_head_id      varchar(50)  comment '入库单表头id',
   spare_parts_id       varchar(50)  comment '备件id',
   spare_parts_code     varchar(50)  comment '备件编码',
   spare_parts_name     varchar(50)  comment '备件名称',
   model                varchar(50)  comment '型号',
   qty                  decimal(18,6) comment '数量',
   unit                 varchar(50)  comment '单位',
   in_storage_id        varchar(50)  comment '相应的备件库库位',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_spare_parts_receipt_line comment '备件接收入明细';


drop table if exists mes_spare_parts_send_head;

/*==============================================================*/
/* Table: mes_spare_parts_send_head                             */
/*==============================================================*/
create table mes_spare_parts_send_head
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '出库单表头id',
   send_code            varchar(50)  comment '出库单单号',
   send_type            varchar(50)  comment '1、维修领用；2、保养领用；',
   out_storage_code     varchar(50)  comment '库位编码',
   out_storage_name     varchar(50)  comment '库位名称',
   out_date             date  comment '出库日期',
   employee             varchar(50)  comment '出库人员',
   note                 varchar(256)  comment '备注',
   status               varchar(10)  comment '1、新建；2、完成。',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_spare_parts_send_head comment '备件出库表头';


drop table if exists mes_spare_parts_send_line;

/*==============================================================*/
/* Table: mes_spare_parts_send_line                             */
/*==============================================================*/
create table mes_spare_parts_send_line
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '入库备件行id',
   send_head_id         varchar(50)  comment '入库单表头id',
   spare_parts_id       varchar(50)  comment '备件id',
   spare_parts_code     varchar(50)  comment '备件编码',
   spare_parts_name     varchar(50)  comment '备件名称',
   model                varchar(50)  comment '型号',
   qty                  decimal(18,6) comment '数量',
   unit                 varchar(50)  comment '单位',
   out_storage_id       varchar(50)  comment '相应的备件库库位',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_spare_parts_send_line comment '备件出库明细';



drop table if exists mes_qc_task_employee;

/*==============================================================*/
/* Table: mes_qc_task_employee                                  */
/*==============================================================*/
create table mes_qc_task_employee
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '执行生产任务人员id',
   excute_task_id       varchar(50) not null  comment '执行生产任务id',
   employee_id          varchar(50)  comment '员工id',
   employee_code        varchar(50) not null  comment '员工编码',
   employee_name        varchar(50) not null  comment '员工名称',
   using_type           varchar(50) default '0'  comment '1,指派领取；2，自己领取；3、交接。',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key mes_qc_task_employee (tenant_id, excute_task_id, employee_id)
);

alter table mes_qc_task_employee comment '质检任务关联执行人员';




drop table if exists mes_qc_task;

/*==============================================================*/
/* Table: mes_qc_task                                           */
/*==============================================================*/
create table mes_qc_task
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '质检任务id',
   qc_type              varchar(10)  comment '质检类型:1,入厂检；2，出厂检；3，首检；4，生产检，5，巡检，6，普通检',
   task_code            varchar(50)  comment '质检任务编码',
   raw_task_code        varchar(50)  comment '原始任务编码,对于复检的质检任务才有',
   method_id            varchar(50) not null  comment '质检方案id',
   method_name          varchar(50)  comment '质检方案名称',
   item_id              varchar(50)  comment '质检物料id',
   item_code            varchar(50)  comment '质检物料编码',
   item_name            varchar(50)  comment '质检物料名称',
   exe_status           varchar(10)  comment '1、未开始；2、生产；3、暂停；4、结束；5、取消；',
   qc_result            varchar(10)  comment '质检结果：1、合格；2、不合格；',
   location_id          varchar(50)  comment '位置id：当做生产检，首检，巡检时存工位id；入厂检，出厂检存仓位id,通用检根据情况存id。',
   location_name        varchar(50)  comment '位置名称：当做生产检，首检，巡检时存工位名称；入厂检，出厂检存仓位名称,通用检根据情况存名称。',
   location_code        varchar(50)  comment '位置编码：当做生产检，首检，巡检时存工位编码；入厂检，出厂检存仓位编码,通用检根据情况存编码。',
   order_id             varchar(50)  comment '工单id',
   order_no             varchar(50)  comment '工单号',
   batch_no             varchar(50)  comment '批次号',
   process_id           varchar(50)  comment '工序id',
   process_code         varchar(50)  comment '工序编码',
   process_name         varchar(50)  comment '工序名称',
   seq                  int  comment '工序号',
   plan_date            date  comment '计划日期',
   shift_id             varchar(50)  comment '班次',
   team_id              varchar(50)  comment '班组',
   total_qty            decimal(18,6)  comment '',
   sample_qty           decimal(18,6)  comment '',
   plan_start_time      datetime  comment '计划开始时间',
   plan_end_time        datetime  comment '计划结束时间',
   real_start_time      datetime  comment '实际开始时间',
   real_end_time        datetime  comment '实际结束时间',
   note                 varchar(256) comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_uk_mes_qc_task (tenant_id, task_code)
);

alter table mes_qc_task comment '质检任务';


drop table if exists mes_qc_task_report;

/*==============================================================*/
/* Table: mes_qc_task_report                                    */
/*==============================================================*/
create table mes_qc_task_report
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '质检任务报告id',
   qc_task_id           varchar(50)  comment '质检任务id',
   good_qty             int  comment '合格数量',
   bad_qty              int  comment '不合格数量',
   good_rate            decimal(18,6)  comment '合格率',
   qc_result            varchar(10)  comment '是否合格，指本次质检总体判断是否合格。1、合格；0，不合格。',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_qc_task_report comment '质检任务报告';


drop table if exists mes_qc_state_ajust_record;

/*==============================================================*/
/* Table: mes_qc_state_ajust_record                             */
/*==============================================================*/
create table mes_qc_state_ajust_record
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '调整记录id',
   item_cell_id         varchar(50)  comment '物料单元id',
   qrcode               varchar(50)  comment '标签码',
   batch                varchar(50)  comment '入库批号/生产批号',
   item_id              varchar(50)  comment '物料id',
   item_code            varchar(50)  comment '物料编码',
   item_name            varchar(50)  comment '物料名称',
   spec                 varchar(50)  comment '规格',
   qty                  decimal(18,6)  comment '数量',
   unit_id              varchar(50)  comment '单位，以生成时进入表中时第一次业务单位存储。',
   unit_name            varchar(50)  comment '单位名称，以生成时进入表中时第一次业务单位存储。',
   storage_id           varchar(50)  comment '仓位id',
   storage_code         varchar(50)  comment '仓位编码',
   storage_name         varchar(50)  comment '仓位名称',
   area_code            varchar(50)  comment '位置一级仓位编码',
   area_name            varchar(50)  comment '位置一级仓位名称',
   house_code           varchar(50)  comment '位置仓库编码',
   house_name           varchar(50)  comment '位置仓库名称',
   qc_status_before     varchar(10)  comment '调整前质量状态',
   qc_status_after      varchar(10)  comment '调整后质量状态',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_qc_state_ajust_record comment '质量调整记录';


drop table if exists mes_qc_task_item_standard;

/*==============================================================*/
/* Table: mes_qc_task_item_standard                             */
/*==============================================================*/
create table mes_qc_task_item_standard
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '质检标准id',
   qc_task_id           varchar(50)  comment '质检任务id',
   item_id              varchar(50)  comment '质检项id',
   item_name            varchar(50)  comment '质检项名称',
   qc_standard          varchar(10)  comment '1,>;2,<;3、=；4、>=;5、<=;6、区间；7、人工判断；8、手工输入；9、允差；',
   min_value            decimal(18,6)  comment '最小值',
   max_value            decimal(18,6)  comment '最大值',
   equal_value          decimal(18,6)  comment '等于值',
   standard_value       decimal(18,6)  comment '标准值',
   up_deviation        decimal(18,6)  comment '上偏差',
   down_deviation      decimal(18,6)  comment '下偏差',
   value_unit           varchar(50)  comment '单位',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_uk_mes_qc_task_item_standard (tenant_id, item_id, id)
);

alter table mes_qc_task_item_standard comment '质检任务关联质检标准';

drop table if exists mes_qc_task_relate_sample;

/*==============================================================*/
/* Table: mes_qc_task_relate_sample                             */
/*==============================================================*/
create table mes_qc_task_relate_sample
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '整体物料id',
   qc_task_id           varchar(50)  comment '质检任务id',
   qrcode               varchar(50)  comment '标签码',
   batch                varchar(50)  comment '入库批号/生产批号',
   item_id              varchar(50)  comment '物料id',
   item_code            varchar(50)  comment '物料编码',
   item_name            varchar(50)  comment '物料名称',
   qty                  decimal(18,6)  comment '数量',
   unit                 varchar(50)  comment '单位',
   unit_name            varchar(50)  comment '单位名称，以生成时进入表中时第一次业务单位存储。',
   storage_id           varchar(50)  comment '对于在仓库中的才需要位置；原材料发到线边仓，也有个线边仓的位置。在制品不用位置表示，填入process。出库未入库的无位置状态

             1、有仓位编码的写入编码id;2、出库无位置的写入null;3、在制品写入process,最后一道工序也写入null标记生产完，与出库类似；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_qc_task_relate_sample comment '质检任务关联物料样本';

drop table if exists mes_qc_task_relate_total;

/*==============================================================*/
/* Table: mes_qc_task_relate_total                              */
/*==============================================================*/
create table mes_qc_task_relate_total
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '整体物料id',
   qc_task_id           varchar(50)  comment '质检任务id',
   qrcode               varchar(50)  comment '标签码',
   batch                varchar(50)  comment '入库批号/生产批号',
   item_id              varchar(50)  comment '物料id',
   item_code            varchar(50)  comment '物料编码',
   item_name            varchar(50)  comment '物料名称',
   qty                  decimal(18,6)  comment '数量',
   unit                 varchar(50)  comment '单位',
   unit_name            varchar(50)  comment '单位名称，以生成时进入表中时第一次业务单位存储。',
   storage_id           varchar(50)  comment '对于在仓库中的才需要位置；原材料发到线边仓，也有个线边仓的位置。在制品不用位置表示，填入process。出库未入库的无位置状态

             1、有仓位编码的写入编码id;2、出库无位置的写入null;3、在制品写入process,最后一道工序也写入null标记生产完，与出库类似；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_qc_task_relate_total comment '质检任务关联物料整体';


drop table if exists mes_qc_task_report_item_value;

/*==============================================================*/
/* Table: mes_qc_task_report_item_value                         */
/*==============================================================*/
create table mes_qc_task_report_item_value
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '质检报告记录值id',
   qc_task_id           varchar(50)  comment '质检任务id',
   qc_task_report_id    varchar(50)  comment '质检报告id',
   qc_task_standard_id  varchar(50)  comment '质检标准id',
   item_id              varchar(50)  comment '质检项id',
   sample_seq           int  comment '样本序号',
   qc_check_value       varchar(50)  comment '质检值：质检值会根据质检标准所选的类型，填入文字，数字或是否。所以将该字段定义成了varchar。需要在代码中比较大小时可以根据质检项标准将其转换为相应的数字。',
   is_ok                varchar(10)  comment '是否合格',
   deduction            decimal(18,6)  comment '扣减',
   attach               varchar(1000)  comment '附件',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_qc_task_report_item_value (qc_task_id, item_id, tenant_id)
);

alter table mes_qc_task_report_item_value comment '质检报告记录值';


alter table mes_qc_task_employee modify employee_code varchar(50) null comment '员工编码';

alter table mes_qc_task
	add location_type varchar(10) null comment '1、仓位；2、工位。' after qc_result;

alter table mes_qc_task
    add related_receipt_type varchar(50) null comment '关联单据类型' after raw_task_code;

alter table mes_qc_task
    add related_receipt_id varchar(50) null comment '关联单据id' after related_receipt_type;

alter table mes_qc_task
    add related_receipt_code varchar(50) null comment '关联单据编码' after related_receipt_id;


ALTER TABLE `ils_uflo_file`
    ADD COLUMN `process_name` VARCHAR(200) NULL COMMENT '流程名称' AFTER `content`;

alter table mes_item_transfer_record
	add spec varchar(50) null comment '规格' after item_name;




-- imes_dev.mes_item_delivery_goods_record definition

CREATE TABLE mes_item_delivery_goods_record
(
    tenant_id         varchar(50)    DEFAULT NULL COMMENT '租户ID',
    id                varchar(50) NOT NULL COMMENT '收货记录id',
    item_cell_id      varchar(50)    DEFAULT NULL COMMENT '物料单元id',
    deliver_type      varchar(10)    DEFAULT NULL COMMENT '发货类型：1、按销售订单发货；2、普通发货；3、退料发货',
    qrcode            varchar(50)    DEFAULT NULL COMMENT '标签码',
    father_qrcode     varchar(50)    DEFAULT NULL COMMENT '父标签码',
    batch             varchar(50)    DEFAULT NULL COMMENT '批号',
    item_id           varchar(50)    DEFAULT NULL COMMENT '物料id',
    item_code         varchar(50)    DEFAULT NULL COMMENT '物料编码',
    item_name         varchar(50)    DEFAULT NULL COMMENT '物料名称',
    qty               decimal(18, 6) DEFAULT NULL COMMENT '数量',
    unit_id           varchar(50)    DEFAULT NULL COMMENT '单位',
    unit_name         varchar(50)    DEFAULT NULL COMMENT '单位名称',
    item_level        varchar(50)    DEFAULT NULL COMMENT '等级：1,一等品；2、二等品；3、三等品',
    work_order_code   varchar(50)    DEFAULT NULL COMMENT '工单号',
    quality_status    varchar(10)    DEFAULT NULL COMMENT '质量状态',
    storage_code      varchar(50)    DEFAULT NULL COMMENT '发货仓位编码',
    storage_name      varchar(50)    DEFAULT NULL COMMENT '发货仓位名称',
    produce_date      date           DEFAULT NULL COMMENT '生产日期',
    valid_date        date           DEFAULT NULL COMMENT '有效期',
    deliver_bill_code varchar(50)    DEFAULT NULL COMMENT '发货单据',
    customer_id       varchar(50)    DEFAULT NULL COMMENT '客户id',
    customer_name     varchar(50)    DEFAULT NULL COMMENT '客户名称',
    note              varchar(256)   DEFAULT NULL COMMENT '备注',
    create_by         varchar(50)    DEFAULT NULL COMMENT '创建人',
    create_time       datetime       DEFAULT NULL COMMENT '创建时间',
    update_by         varchar(50)    DEFAULT NULL COMMENT '修改人',
    update_time       datetime       DEFAULT NULL COMMENT '修改时间',
    is_deleted        tinyint        DEFAULT '0' COMMENT '删除标识',
    PRIMARY KEY (id)
);





/*==============================================================*/
/* Table: mes_sop_template                                      */
/*==============================================================*/
create table mes_sop_template
(
    tenant_id            varchar(50)  comment '租户ID',
    id                   varchar(50) not null  comment 'SOP模板id',
    template_code        varchar(50)  comment 'SOP模板编号',
    template_name        varchar(50) not null  comment 'SOP模板名称',
    template_type        varchar(10)  comment '1、产品BOM；2、工艺路线；',
    entity_code          varchar(50)  comment '1、对于实体类型为产品BOM，对应产品BOM编码；2、对于实体类型为工艺路线对应工艺路线编码；',
    entity_name          varchar(50)  comment '1、对于实体类型为产品BOM，对应产品BOM名称；2、对于实体类型为工艺路线对应工艺路线名称；',
    process_code         varchar(50)  comment '模板实体工序编码',
    process_name         varchar(50)  comment '模板实体工序名称',
    version              varchar(50)  comment '版本',
    status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
    note                 varchar(256)  comment '备注',
    create_by            varchar(50)  comment '创建人',
    create_time          datetime  comment '创建时间',
    update_by            varchar(50)  comment '修改人',
    update_time          datetime  comment '修改时间',
    is_deleted           tinyint default 0  comment '删除标识',
    primary key (id),
    unique key uk_mes_sop_template (tenant_id, template_type, entity_code, process_code, version)
);

alter table mes_sop_template comment 'SOP模板表头';



create table mes_sop_template_step
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '模板步骤id',
   template_id          varchar(50) not null  comment '关联SOP模板id',
   step_seq             int  comment '顺序为1，2，3，4，5。。。',
   step_name            varchar(50)  comment '步骤名称',
   step_display_name    varchar(50)  comment '步骤显示名称',
   first_step           varchar(10)  comment '1、是；0，否。',
   last_step            varchar(10)  comment '1、是；0，否。',
   execute_authority    varchar(10)  comment '1、用户；2、角色。',
   executer             varchar(50)  comment '如果权限为角色，即对应角色编码；如果权限为用户，即对应用户。',
   next_step_id         varchar(50)  comment '后续步骤id,当最后一个步骤时，该值为end。',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_sop_template_step (tenant_id, template_id, step_name)
);

alter table mes_sop_template_step comment 'SOP模板步骤';


/*==============================================================*/
/* Table: mes_sop_template_control                              */
/*==============================================================*/
create table mes_sop_template_control
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '模板步骤控件id',
   template_step_id     varchar(50) not null  comment '模板步骤id',
   control_type         varchar(10)  comment '1、标签入库；2、库存入库；3、投料；4、产出；5、质检；6、标签出库；7、库存出库、8、报告模板。
             如果是入库，投料，产出，质检，出库控件，控件值为空，操作记录分别记录到对应操作表中。这些对应的每个操作表中都要有一个字段对应标准作业任务步骤记录值中的id。',
   control_logic        varchar(50)  comment '对于入库控件：1、管控物料清单物料与入库位置；2、不管控。
             对于出库控件：1、管控产出物料与出库位置；2、不管控。
             对于投料控件：1、管控某次单独投入的物料；2、不管控。
             对于产出控件：无需管控，由结束按钮控制；
             对于质检控件：质检任务产生可以根据上个步骤点完成时触发产生一个质检任务；
             报告模板控件：报告模板控件不需要控制逻辑。',
   entity_item          varchar(50)  comment '当使用的是投料控件时，实体对应物料编码，其他控件该字段为空。',
   control_name         varchar(50) not null  comment '控件名称',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (),
   unique key uk_mes_sop_template_control (tenant_id, template_step_id, control_name)
);

alter table mes_sop_template_control comment 'SOP模板步骤对应控件';



/*==============================================================*/
/* Table: mes_sop_control                                       */
/*==============================================================*/
create table mes_sop_control
(
    tenant_id            varchar(50)  comment '租户ID',
    id                   varchar(50) not null  comment '模板步骤控件id',
    sop_step_id          varchar(50) not null  comment '模板步骤id',
    related_task_id      varchar(50)  comment '关联任务号',
    control_type         varchar(10)  comment '1、标签入库；2、库存入库；3、投料；4、产出；5、质检；6、标签出库；7、库存出库、8、报告模板。
             如果是入库，投料，产出，质检，出库控件，控件值为空，操作记录分别记录到对应操作表中。这些对应的每个操作表中都要有一个字段对应标准作业任务步骤记录值中的id。',
    control_logic        varchar(50)  comment '对于入库控件：1、管控物料清单物料与入库位置；2、不管控。
             对于出库控件：1、管控产出物料与出库位置；2、不管控。
             对于投料控件：1、管控某次单独投入的物料；2、不管控。
             对于产出控件：无需管控，由结束按钮控制；
             对于质检控件：质检任务产生可以根据上个步骤点完成时触发产生一个质检任务；
             报告模板控件：报告模板控件不需要控制逻辑。',
    entity_item          varchar(50)  comment '当使用的是投料控件时，实体对应物料编码，其他控件该字段为空。',
    control_name         varchar(50) not null  comment '任务步骤控件名称',
    note                 varchar(256)  comment '备注',
    create_by            varchar(50)  comment '创建人',
    create_time          datetime  comment '创建时间',
    update_by            varchar(50)  comment '修改人',
    update_time          datetime  comment '修改时间',
    is_deleted           tinyint default 0  comment '删除标识',
    primary key (id),
    unique key uk_mes_sop_control (tenant_id, sop_step_id, control_name)
);

alter table mes_sop_control comment '标准作业任务步骤控件';



/*==============================================================*/
/* Table: mes_sop_step                                          */
/*==============================================================*/
create table mes_sop_step
(
    tenant_id            varchar(50)  comment '租户ID',
    id                   varchar(50) not null  comment '任务步骤id',
    related_task_type    varchar(10)  comment '1、生产任务；2、质检任务；3、维保任务。',
    related_task_id      varchar(50)  comment '关联任务id：可以是生产任务；质检任务；维保任务。',
    template_id          varchar(50) not null  comment '关联SOP模板id',
    step_seq             int  comment '同一层，同一父步骤下步骤顺序从大到小排列，顺序为1，2，3，4，5。。。',
    step_name            varchar(50)  comment '步骤/步骤组名称',
    step_display_name    varchar(50)  comment '步骤/步骤组显示名称',
    first_step           varchar(10)  comment '1、是；0，否。',
    last_step            varchar(10)  comment '1、是；0，否。',
    execute_authority    varchar(10)  comment '1、用户；2、角色。',
    executer             varchar(50)  comment '如果权限为角色，即对应角色id；如果权限为用户，即对应用户id。',
    next_step_id        varchar(50)  comment '后续步骤id',
    step_status          varchar(10)  comment '1、未执行；2、执行中；3、完成。',
    note                 varchar(256)  comment '备注',
    create_by            varchar(50)  comment '创建人',
    create_time          datetime  comment '创建时间',
    update_by            varchar(50)  comment '修改人',
    update_time          datetime  comment '修改时间',
    is_deleted           tinyint default 0  comment '删除标识',
    primary key (id),
    unique key uk_mes_sop_step (tenant_id, related_task_id, step_name)
);

alter table mes_sop_step comment '标准作业任务步骤';


alter table mes_sop_template add entity_line_id varchar(50) null comment '模板实体对应实体明细id' after entity_name;


alter table mes_sop_template_control
	add control_seq int null comment '控件顺序' after template_step_id;

alter table mes_sop_control
	add control_seq int null comment '控件顺序' after related_task_id;

alter table mes_work_produce_material_record
	add sop_step_id varchar(50) null comment '关联标准作业步骤id' after unit;

alter table mes_work_produce_material_record
	add sop_control_id varchar(50) null comment '关联标准作业控件id' after sop_step_id;

alter table mes_work_produce_record
    add sop_step_id varchar(50) null comment '关联标准作业步骤id' after station_name;

alter table mes_work_produce_record
    add sop_control_id varchar(50) null comment '关联标准作业控件id' after sop_step_id;

alter table ils_biz_config modify config_value VARCHAR(500);
alter table ils_biz_config modify config_desc VARCHAR(500);

alter table sys_user
    add lock_status int null comment '锁定状态(1-正常,2-锁定)' after rel_tenant_ids;
alter table sys_user
    add lock_time datetime null comment '锁定时间' after lock_status;
alter table sys_user
    add err_num int default 0 comment '错误次数' after lock_time;
<<<<<<< HEAD
alter table SYS_USER
    add err_login_time date null comment '第一次错误登录时间' after err_num;

    alter table mes_qc_task
    add sop_step_id varchar(50) null comment '关联标准作业步骤id' after audit_status;

alter table mes_qc_task
    add sop_control_id varchar(50) null comment '关联标准作业控件id' after sop_step_id;




alter table mes_item_transfer_record
    add sop_step_id varchar(50) null comment '关联标准作业步骤id' after quality_status;

alter table mes_item_transfer_record
    add sop_control_id varchar(50) null comment '关联标准作业控件id' after sop_step_id;

alter table mes_item_transfer_record
    add task_id varchar(50) null comment '关联任务id' after sop_control_id;

alter table sys_user
    add err_login_time date null comment '第一次错误登录时间' after err_num;

alter table mes_sop_template_control
	add entity_item_name varchar(50) null comment '实体名称' after entity_item;

alter table mes_sop_template_control
	add control_logic_name varchar(50) null comment '逻辑名称' after control_logic;

drop table if exists mes_work_order_line_station;
create table mes_work_order_line_station
(
    tenant_id          varchar(50)  default null comment '租户id',
    id                 varchar(50) not null comment '工序关联工位id',
    related_type       varchar(50)  default null comment '工单可能对应：1、工艺路线；2、生产bom。',
    work_order_line_id varchar(50)  default null comment '工单工艺路线id',
    seq                int         not null comment '序号',
    process_id         varchar(50)  default null comment '工序id',
    process_code       varchar(50)  default null comment '工序编码',
    process_name       varchar(50)  default null comment '工序名称',
    station_name       varchar(50)  default null comment '工位名称',
    station_id         varchar(50)  default null comment '工位id',
    note               varchar(256) default null comment '备注',
    create_by          varchar(50)  default null comment '创建人',
    create_time        datetime     default null comment '创建时间',
    update_by          varchar(50)  default null comment '修改人',
    update_time        datetime     default null comment '修改时间',
    is_deleted         tinyint      default '0' comment '删除标识',
    primary key (id),
    unique key uk_work_order_line_station (tenant_id, work_order_line_id, process_id,station_id)
);

alter table mes_work_order_line_station comment '工单工艺对应工位';

drop table if exists mes_work_order_line_method;
create table mes_work_order_line_method
(
    tenant_id          varchar(50)  default null comment '租户id',
    id                 varchar(50) not null comment '工单工艺质检方案id',
    related_type       varchar(50)  default null comment '工单可能对应：1、工艺路线；2、生产bom。',
    work_order_line_id varchar(50)  default null comment '工单工艺路线id',
    seq                int         not null comment '序号',
    process_id         varchar(50)  default null comment '工序id',
    process_code       varchar(50)  default null comment '工序编码',
    process_name       varchar(50)  default null comment '工序名称',
    qc_method_name     varchar(50)  default null comment '质检方案名称',
    qc_method_id       varchar(50)  default null comment '质检方案id',
    qc_type            varchar(50)  default null comment '质检类型',
    note               varchar(256) default null comment '备注',
    create_by          varchar(50)  default null comment '创建人',
    create_time        datetime     default null comment '创建时间',
    update_by          varchar(50)  default null comment '修改人',
    update_time        datetime     default null comment '修改时间',
    is_deleted         tinyint      default '0' comment '删除标识',
    primary key (id),
    unique key uk_mes_work_order_line_method (tenant_id, work_order_line_id, process_id,qc_method_id)
);

alter table mes_work_order_line_method comment '工单工艺质检方案';

drop table if exists mes_work_order_line_station;
create table mes_work_order_line_station
(
    tenant_id          varchar(50)  default null comment '租户id',
    id                 varchar(50) not null comment '工序关联工位id',
    related_type       varchar(50)  default null comment '工单可能对应：1、工艺路线；2、生产bom。',
    work_order_line_id varchar(50)  default null comment '工单工艺路线id',
    seq                int         not null comment '序号',
    process_id         varchar(50)  default null comment '工序id',
    process_code       varchar(50)  default null comment '工序编码',
    process_name       varchar(50)  default null comment '工序名称',
    station_name       varchar(50)  default null comment '工位名称',
    station_id         varchar(50)  default null comment '工位id',
    note               varchar(256) default null comment '备注',
    create_by          varchar(50)  default null comment '创建人',
    create_time        datetime     default null comment '创建时间',
    update_by          varchar(50)  default null comment '修改人',
    update_time        datetime     default null comment '修改时间',
    is_deleted         tinyint      default '0' comment '删除标识',
    primary key (id),
    unique key uk_work_order_line_station (tenant_id, work_order_line_id, process_id,station_id)
);

alter table mes_work_order_line_station comment '工单工艺对应工位';

drop table if exists mes_work_order_line_method;
create table mes_work_order_line_method
(
    tenant_id          varchar(50)  default null comment '租户id',
    id                 varchar(50) not null comment '工单工艺质检方案id',
    related_type       varchar(50)  default null comment '工单可能对应：1、工艺路线；2、生产bom。',
    work_order_line_id varchar(50)  default null comment '工单工艺路线id',
    seq                int         not null comment '序号',
    process_id         varchar(50)  default null comment '工序id',
    process_code       varchar(50)  default null comment '工序编码',
    process_name       varchar(50)  default null comment '工序名称',
    qc_method_name     varchar(50)  default null comment '质检方案名称',
    qc_method_id       varchar(50)  default null comment '质检方案id',
    qc_type            varchar(50)  default null comment '质检类型',
    note               varchar(256) default null comment '备注',
    create_by          varchar(50)  default null comment '创建人',
    create_time        datetime     default null comment '创建时间',
    update_by          varchar(50)  default null comment '修改人',
    update_time        datetime     default null comment '修改时间',
    is_deleted         tinyint      default '0' comment '删除标识',
    primary key (id),
    unique key uk_mes_work_order_line_method (tenant_id, work_order_line_id, process_id,qc_method_id)
);

alter table mes_work_order_line_method comment '工单工艺质检方案';

drop table if exists mes_work_order_line_station;
create table mes_work_order_line_station
(
    tenant_id          varchar(50)  default null comment '租户id',
    id                 varchar(50) not null comment '工序关联工位id',
    related_type       varchar(50)  default null comment '工单可能对应：1、工艺路线；2、生产bom。',
    work_order_line_id varchar(50)  default null comment '工单工艺路线id',
    seq                int         not null comment '序号',
    process_id         varchar(50)  default null comment '工序id',
    process_code       varchar(50)  default null comment '工序编码',
    process_name       varchar(50)  default null comment '工序名称',
    station_name       varchar(50)  default null comment '工位名称',
    station_id         varchar(50)  default null comment '工位id',
    note               varchar(256) default null comment '备注',
    create_by          varchar(50)  default null comment '创建人',
    create_time        datetime     default null comment '创建时间',
    update_by          varchar(50)  default null comment '修改人',
    update_time        datetime     default null comment '修改时间',
    is_deleted         tinyint      default '0' comment '删除标识',
    primary key (id),
    unique key uk_work_order_line_station (tenant_id, work_order_line_id, process_id,station_id)
);

alter table mes_work_order_line_station comment '工单工艺对应工位';

drop table if exists mes_work_order_line_method;
create table mes_work_order_line_method
(
    tenant_id          varchar(50)  default null comment '租户id',
    id                 varchar(50) not null comment '工单工艺质检方案id',
    related_type       varchar(50)  default null comment '工单可能对应：1、工艺路线；2、生产bom。',
    work_order_line_id varchar(50)  default null comment '工单工艺路线id',
    seq                int         not null comment '序号',
    process_id         varchar(50)  default null comment '工序id',
    process_code       varchar(50)  default null comment '工序编码',
    process_name       varchar(50)  default null comment '工序名称',
    qc_method_name     varchar(50)  default null comment '质检方案名称',
    qc_method_id       varchar(50)  default null comment '质检方案id',
    qc_type            varchar(50)  default null comment '质检类型',
    note               varchar(256) default null comment '备注',
    create_by          varchar(50)  default null comment '创建人',
    create_time        datetime     default null comment '创建时间',
    update_by          varchar(50)  default null comment '修改人',
    update_time        datetime     default null comment '修改时间',
    is_deleted         tinyint      default '0' comment '删除标识',
    primary key (id),
    unique key uk_mes_work_order_line_method (tenant_id, work_order_line_id, process_id,qc_method_id)
);

alter table mes_work_order_line_method comment '工单工艺质检方案';

ALTER TABLE imes_dev.mes_qc_task ADD note varchar(256) NULL COMMENT '备注';
ALTER TABLE imes_dev.mes_work_order MODIFY COLUMN sale_order_id TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '销售订单号';
ALTER TABLE imes_dev.sys_third_account ADD third_user_id varchar(100) NULL COMMENT '第三方账号id';

CREATE TABLE `sys_third_platform` (
  `id` varchar(50)  NOT NULL COMMENT '主键 id',
  `tenant_id` varchar(50)  NOT NULL COMMENT '租户 id',
  `third_type` varchar(50)  NOT NULL COMMENT '第三方平台类型：微信、钉钉',
  `app_name` varchar(100)  DEFAULT NULL COMMENT '第三方平台名称',
  `app_id` varchar(100)  DEFAULT NULL COMMENT '第三方平台唯一 id',
  `agent_id` varchar(100)  DEFAULT NULL COMMENT '第三方平台应用 ID',
  `agent_secret` varchar(100)  DEFAULT NULL COMMENT '第三方平台应用密钥',
  `agent_name` varchar(100)  DEFAULT NULL COMMENT '第三方平台应用名称',
  `is_default_platform` tinyint DEFAULT NULL,
  `callback_url` varchar(100)  DEFAULT NULL COMMENT '服务回调地址',
  `token` varchar(100)  DEFAULT NULL COMMENT '服务回调密钥',
  `encoding_aes_key` varchar(100)  DEFAULT NULL COMMENT '服务回调加密',
  `oauth_app_key` varchar(100) DEFAULT NULL,
  `oauth_app_secret` varchar(100) DEFAULT NULL,
  `redirect_uri` varchar(100)  DEFAULT NULL COMMENT '第三方登录成功重定向地址',
  `create_by` varchar(50)  NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(50)  NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='第三方平台表';

ALTER TABLE imes_dev.mes_work_produce_material_record ADD unit_name varchar(50) NULL COMMENT '单位名称';
ALTER TABLE imes_dev.mes_work_produce_record ADD unit_name varchar(50) NULL COMMENT '单位名称';

-- 联产出字段
ALTER TABLE imes_dev.mes_work_produce_record ADD product_type TINYINT DEFAULT 1 NULL COMMENT '1、主产出；2、联产出。';
-- 物料提醒字段
ALTER TABLE imes_dev.mes_item_stock ADD valid_time_status varchar(50) DEFAULT 0 NULL COMMENT '启用有效期提醒';
ALTER TABLE imes_dev.mes_item_stock ADD safety_status varchar(50) DEFAULT 0 NULL;
ALTER TABLE imes_dev.mes_item_stock ADD safety_warn_frequency INT NULL COMMENT '安全库存提醒频率';
ALTER TABLE imes_dev.mes_item_stock ADD safety_warn_unit varchar(10) NULL COMMENT '安全库存提醒单位';
ALTER TABLE imes_dev.mes_item_stock ADD item_manager_id TEXT NULL COMMENT '物料管理人员id';
ALTER TABLE imes_dev.mes_item_stock ADD item_manager TEXT NULL COMMENT '物料管理人员名称';
--修改物料单元生产日期和有效期
ALTER TABLE imes_dev.mes_item_cell MODIFY COLUMN produce_date DATETIME NULL COMMENT '生产日期';
ALTER TABLE imes_dev.mes_item_cell MODIFY COLUMN valid_date DATETIME NULL COMMENT '有效期';
--物料转换单位启停
ALTER TABLE imes_dev.mes_item_unit ADD status varchar(100) DEFAULT 1 NULL COMMENT '状态';


/*==============================================================*/
/* Table: mes_parameter                                         */
/*==============================================================*/
create table mes_parameter
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '参数id',
   para_temp_type            varchar(50)  comment '参数分类:1、下发参数；2、监控参数。',
   para_code            varchar(50) not null  comment '参数编码',
   para_name            varchar(50) not null  comment '参数名称',
   note                 varchar(256)  comment '备注',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_process_parameter (tenant_id, para_code)
);

alter table mes_parameter comment '参数管理';




/*==============================================================*/
/* Table: mes_para_template_head                                */
/*==============================================================*/
create table mes_para_template_head
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '参数模板id',
   para_temp_name       varchar(50) not null  comment '参数模板名称',
   para_temp_type       varchar(10)  comment '模板分类:1,下发模板；2，监控模板。',
   note                 varchar(256)  comment '备注',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_para_template_head (tenant_id, para_temp_name)
);

alter table mes_para_template_head comment '参数模板主表';



/*==============================================================*/
/* Table: mes_para_template_detail                              */
/*==============================================================*/
create table mes_para_template_detail
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '参数模板明细项id',
   para_temp_id         varchar(50) not null  comment '参数模板id',
   para_id              varchar(50)  comment '参数项id',
   para_name            varchar(50)  comment '参数项名称',
   para_type            varchar(10)  comment '1、数值；2、百分比；3、开关；4、公式；5、下拉单选。',
   para_standard        varchar(10)  comment '1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_para_template_head (tenant_id, para_temp_id, para_id)
);

alter table mes_para_template_detail comment '参数模板明细表';




/*==============================================================*/
/* Table: mes_process_para_head                                 */
/*==============================================================*/
create table mes_process_para_head
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '工序参数主表id',
   process_id           varchar(50)  comment '工序id',
   para_temp_id         varchar(50)  comment '参数模板id',
   para_temp_name       varchar(50) not null  comment '参数模板名称',
   para_temp_type       varchar(10)  comment '模板分类:1,下发模板；2，监控模板。',
   note                 varchar(256)  comment '备注',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_process_para_head (tenant_id, para_temp_name, process_id)
);

alter table mes_process_para_head comment '工序关联工艺参数主表';



/*==============================================================*/
/* Table: mes_process_para_detail                               */
/*==============================================================*/
create table mes_process_para_detail
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '工序参数项id',
   para_head_id         varchar(50) not null  comment '工序参数主表id',
   process_id           varchar(50)  comment '工序id',
   para_id              varchar(50)  comment '参数项id',
   para_name            varchar(50)  comment '参数项名称',
   para_type            varchar(10)  comment '1、数值；2、百分比；3、开关；4、公式；5、下拉单选。',
   para_standard        varchar(10)  comment '1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；',
   min_value            decimal(18,6)  comment '最小值',
   max_value            decimal(18,6)  comment '最大值',
   equal_value          decimal(18,6)  comment '等于值',
   standard_value       decimal(18,6)  comment '标准值',
   up_deviation        decimal(18,6)  comment '上偏差',
   down_deviation      decimal(18,6)  comment '下偏差',
   value_unit           varchar(50)  comment '单位',
   switch_value         varchar(10)  comment '1、开启；0、关闭。',
   option_value         varchar(10)  comment '以半角逗号分开，供选择。',
   formula              text  comment '公式表达式',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_process_para_detail (tenant_id, para_head_id, para_id)
);

alter table mes_process_para_detail comment '工序关联工艺参明细表';




/*==============================================================*/
/* Table: mes_route_line_para_head                              */
/*==============================================================*/
create table mes_route_line_para_head
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '参数模板id',
   route_id             varchar(50)  comment '工艺路线id',
   route_line_id        varchar(50)  comment '工序路线明细id',
   para_temp_id         varchar(50)  comment '',
   para_temp_name       varchar(50) not null  comment '参数模板名称',
   para_temp_type       varchar(10)  comment '模板分类:1,下发模板；2，监控模板。',
   note                 varchar(256)  comment '备注',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_route_line_para_head (para_temp_name, route_line_id, tenant_id)
);

alter table mes_route_line_para_head comment '工艺路线工序参数主表';





/*==============================================================*/
/* Table: mes_route_line_para_detail                            */
/*==============================================================*/
create table mes_route_line_para_detail
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '参数模板明细项id',
   para_head_id         varchar(50) not null  comment '工序参数主表id',
   para_id              varchar(50)  comment '参数项id',
   para_name            varchar(50)  comment '参数项名称',
   para_type            varchar(10)  comment '1、数值；2、百分比；3、开关；4、公式；5、下拉单选。',
   para_standard        varchar(10)  comment '1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；',
   min_value            decimal(18,6)  comment '最小值',
   max_value            decimal(18,6)  comment '最大值',
   equal_value          decimal(18,6)  comment '等于值',
   standard_value       decimal(18,6)  comment '标准值',
   up_deviation         decimal(18,6)  comment '上偏差',
   down_deviation       decimal(18,6)  comment '下偏差',
   value_unit           varchar(50)  comment '单位',
   switch_value         varchar(10)  comment '1、开启；0、关闭。',
   option_value         varchar(10)  comment '以半角逗号分开，供选择。',
   formula              text  comment '公式表达式',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   route_line_id        varchar(50)  comment '工序路线明细id',
   primary key (id),
   unique key uk_mes_route_line_para_detail (tenant_id, para_id, route_line_id)
);

alter table mes_route_line_para_detail comment '工艺路线工序参数明细表';






/*==============================================================*/
/* Table: mes_product_route_para_head                           */
/*==============================================================*/
create table mes_product_route_para_head
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '参数主表id',
   product_id           varchar(50)  comment '产品id',
   product_line_id      varchar(50)  comment '产品工艺路线明细id',
   para_temp_id         varchar(50)  comment '参数模板id',
   para_temp_name       varchar(50) not null  comment '参数模板名称',
   para_temp_type       varchar(10)  comment '模板分类:1,下发模板；2，监控模板。',
   note                 varchar(256)  comment '备注',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_product_route_para_head (tenant_id, para_temp_name, product_line_id)
);

alter table mes_product_route_para_head comment '产品路线工序参数主表';


/*==============================================================*/
/* Table: mes_product_route_para_detail                         */
/*==============================================================*/
create table mes_product_route_para_detail
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '参数模板明细项id',
   para_head_id         varchar(50) not null  comment '产品路线参数主表id',
   para_id              varchar(50)  comment '参数项id',
   para_name            varchar(50)  comment '参数项名称',
   para_type            varchar(10)  comment '1、数值；2、百分比；3、开关；4、公式；5、下拉单选。',
   para_standard        varchar(10)  comment '1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；',
   min_value            decimal(18,6)  comment '最小值',
   max_value            decimal(18,6)  comment '最大值',
   equal_value          decimal(18,6)  comment '等于值',
   standard_value       decimal(18,6)  comment '标准值',
   up_deviation         decimal(18,6)  comment '上偏差',
   down_deviation       decimal(18,6)  comment '下偏差',
   value_unit           varchar(50)  comment '单位',
   switch_value         varchar(10)  comment '1、开启；0、关闭。',
   option_value         varchar(10)  comment '以半角逗号分开，供选择。',
   formula              text  comment '公式表达式',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_product_route_para_detail (tenant_id, para_id, para_head_id)
);

alter table mes_product_route_para_detail comment '产品路线工序参数明细表';




/*==============================================================*/
/* Table: mes_machine_type_para_head                            */
/*==============================================================*/
create table mes_machine_type_para_head
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '设备类型参数主表id',
   machine_type_id      varchar(50)  comment '设备类型id',
   para_temp_id         varchar(50)  comment '参数模板id',
   para_temp_name       varchar(50) not null  comment '参数模板名称',
   para_temp_type       varchar(10)  comment '模板分类:1,下发模板；2，监控模板。',
   note                 varchar(256)  comment '备注',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_type_para_head (tenant_id, para_temp_name, machine_type_id)
);

alter table mes_machine_type_para_head comment '设备类型关联参数主表';




/*==============================================================*/
/* Table: mes_machine_type_para_detail                          */
/*==============================================================*/
create table mes_machine_type_para_detail
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '设备类型参数项id',
   para_head_id         varchar(50) not null  comment '设备类型参数主表id',
   machine_type_id      varchar(50)  comment '设备类型id',
   para_id              varchar(50)  comment '参数项id',
   para_name            varchar(50)  comment '参数项名称',
   para_type            varchar(10)  comment '1、数值；2、百分比；3、开关；4、公式；5、下拉单选。',
   para_standard        varchar(10)  comment '1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；',
   min_value            decimal(18,6)  comment '最小值',
   max_value            decimal(18,6)  comment '最大值',
   equal_value          decimal(18,6)  comment '等于值',
   standard_value       decimal(18,6)  comment '标准值',
   up_deviation        decimal(18,6)  comment '上偏差',
   down_deviation      decimal(18,6)  comment '下偏差',
   value_unit           varchar(50)  comment '单位',
   switch_value         varchar(10)  comment '1、开启；0、关闭。',
   option_value         varchar(10)  comment '以半角逗号分开，供选择。',
   formula              text  comment '公式表达式',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_type_para_detail (tenant_id, para_id, machine_type_id, para_head_id)
);

alter table mes_machine_type_para_detail comment '设备类型关联参数明细表';




/*==============================================================*/
/* Table: mes_machine_para_head                                 */
/*==============================================================*/
create table mes_machine_para_head
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '设备参数主表id',
   machine_id           varchar(50)  comment '设备id',
   para_temp_id         varchar(50)  comment '参数模板id',
   para_temp_name       varchar(50) not null  comment '参数模板名称',
   para_temp_type       varchar(10)  comment '模板分类:1,下发模板；2，监控模板。',
   note                 varchar(256)  comment '备注',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_para_head (tenant_id, para_temp_name, machine_id)
);

alter table mes_machine_para_head comment '设备关联参数主表';




/*==============================================================*/
/* Table: mes_machine_para_detail                               */
/*==============================================================*/
create table mes_machine_para_detail
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '设备参数项id',
   para_head_id         varchar(50) not null  comment '设备参数主表id',
   machine_id           varchar(50)  comment '设备id',
   para_id              varchar(50)  comment '参数项id',
   para_name            varchar(50)  comment '参数项名称',
   para_type            varchar(10)  comment '1、数值；2、百分比；3、开关；4、公式；5、下拉单选。',
   para_standard        varchar(10)  comment '1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；',
   min_value            decimal(18,6)  comment '最小值',
   max_value            decimal(18,6)  comment '最大值',
   equal_value          decimal(18,6)  comment '等于值',
   standard_value       decimal(18,6)  comment '标准值',
   up_deviation        decimal(18,6)  comment '上偏差',
   down_deviation      decimal(18,6)  comment '下偏差',
   value_unit           varchar(50)  comment '单位',
   switch_value         varchar(10)  comment '1、开启；0、关闭。',
   option_value         varchar(10)  comment '以半角逗号分开，供选择。',
   formula              text  comment '公式表达式',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_machine_para_detail (tenant_id, para_head_id, para_id, machine_id)
);

alter table mes_machine_para_detail comment '设备关联参数';




/*==============================================================*/
/* Table: mes_work_order_line_para                              */
/*==============================================================*/
create table mes_work_order_line_para
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '工序参数项id',
   related_type         varchar(50)  comment '工单可能对应：1、工艺路线；2、生产BOM。',
   work_order_line_id   varchar(50)  comment '工单工艺路线id',
   seq                  int not null  comment '序号',
   process_id           varchar(50)  comment '工序id',
   process_code         varchar(50)  comment '工序编码',
   process_name         varchar(50)  comment '工序名称',
   para_id              varchar(50)  comment '参数项id',
   para_name            varchar(50)  comment '参数项名称',
   para_type            varchar(10)  comment '1、数值；2、百分比；3、开关；4、公式；5、下拉单选。',
   para_standard        varchar(10)  comment '1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；',
   min_value            decimal(18,6)  comment '最小值',
   max_value            decimal(18,6)  comment '最大值',
   equal_value          decimal(18,6)  comment '等于值',
   standard_value       decimal(18,6)  comment '标准值',
   up_deviation        decimal(18,6)  comment '上偏差',
   down_deviation      decimal(18,6)  comment '下偏差',
   value_unit           varchar(50)  comment '单位',
   switch_value         varchar(10)  comment '1、开启；0、关闭。',
   option_value         varchar(10)  comment '以半角逗号分开，供选择。',
   formula              text  comment '公式表达式',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_work_order_line_para (tenant_id, para_id, work_order_line_id)
);

alter table mes_work_order_line_para comment '工单工艺工序参数';



/*==============================================================*/
/* Table: mes_work_produce_task_para                            */
/*==============================================================*/
create table mes_work_produce_task_para
(
   tenant_id            varchar(50)  comment '租户id',
   id                   varchar(50) not null  comment '任务参数项id',
   produce_task_id      varchar(50)  comment '如果为空，则采取的是流转卡的生产方式；否则是生产任务的形式。',
   order_id             varchar(50)  comment '工单id',
   order_no             varchar(50)  comment '工单号',
   process_id           varchar(50)  comment '工序id',
   process_code         varchar(50)  comment '工序编码',
   process_name         varchar(50)  comment '工序名称',
   item_id              varchar(50)  comment '产品id',
   item_code            varchar(50)  comment '产品编码',
   item_name            varchar(50)  comment '产品编码',
   para_id              varchar(50)  comment '参数项id',
   para_name            varchar(50)  comment '参数项名称',
   para_type            varchar(10)  comment '1、数值；2、百分比；3、开关；4、公式；5、下拉单选。',
   para_standard        varchar(10)  comment '1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；',
   min_value            decimal(18,6)  comment '最小值',
   max_value            decimal(18,6)  comment '最大值',
   equal_value          decimal(18,6)  comment '等于值',
   standard_value       decimal(18,6)  comment '标准值',
   up_deviation        decimal(18,6)  comment '上偏差',
   down_deviation      decimal(18,6)  comment '下偏差',
   value_unit           varchar(50)  comment '单位',
   switch_value         varchar(10)  comment '1、开启；0、关闭。',
   option_value         varchar(10)  comment '以半角逗号分开，供选择。',
   formula              text  comment '公式表达式',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_work_produce_task_para (tenant_id, para_id, produce_task_id)
);

alter table mes_work_produce_task_para comment '执行生产任务参数';

/*==============================================================*/
/* Table: mes_item_container_type                               */
/*==============================================================*/
create table mes_item_container_type
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '类型id',
   type_code            varchar(50) not null  comment '载具类型编码',
   type_name            varchar(50) not null  comment '载具类型名称',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_item_container_type (tenant_id, type_code)
);

alter table mes_item_container_type comment 'mes_item_container_type载具类型';





/*==============================================================*/
/* Table: mes_item_container                                    */
/*==============================================================*/
create table mes_item_container
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '载具id',
   container_code       varchar(50) not null  comment '载具编码',
   container_name       varchar(50) not null  comment '载具名称',
   container_description varchar(200) not null  comment '载具描述',
   container_type_id    varchar(50)  comment '载具类型',
   status               varchar(50) default '1'  comment '状态 ：1，启用，0停用；',
   qrcode               varchar(50)  comment '标签码',
   attach               varchar(1000)  comment '附件',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_item_container (tenant_id, container_code)
);

alter table mes_item_container comment '载具定义';



/*==============================================================*/
/* Table: mes_item_container_qty                                */
/*==============================================================*/
create table mes_item_container_qty
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '载具物料id',
   container_id         varchar(50)  comment '载具id',
   container_code       varchar(50) not null  comment '载具编码',
   container_name       varchar(50) not null  comment '载具名称',
   item_id              varchar(50) not null  comment '物料id',
   item_code            varchar(50) not null  comment '物料编码',
   item_name            varchar(50) not null  comment '物料名称',
   qty                  decimal(18,6)  comment '',
   unit                 varchar(50)  comment '单位',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   note                 varchar(256)  comment '备注',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   unique key uk_mes_item_container_qty (tenant_id, container_id, item_id)
);

alter table mes_item_container_qty comment '载具关联物料';



/*==============================================================*/
/* Table: mes_item_container_manage                             */
/*==============================================================*/
create table mes_item_container_manage
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '载具管理id',
   container_id         varchar(50)  comment '载具id',
   container_code       varchar(50)  comment '载具编码',
   container_qrcode     varchar(50)  comment '载具标签码',
   container_name       varchar(50)  comment '载具名称',
   father_qrcode        varchar(50)  comment '父标签码',
   storage_id           varchar(50)  comment '对于在仓库中的才需要位置；原材料发到线边仓，也有个线边仓的位置。在制品不用位置表示，填入process。出库未入库的无位置状态

             1、有仓位编码的写入编码id;2、出库无位置的写入null;3、在制品写入process,最后一道工序也写入null标记生产完，与出库类似；',
   storage_code         varchar(50)  comment '仓位编码',
   storage_name         varchar(50)  comment '仓位名称',
   area_code            varchar(50)  comment '位置一级仓位编码',
   area_name            varchar(50)  comment '位置一级仓位名称',
   house_code           varchar(50)  comment '仓库编码',
   house_name           varchar(50)  comment '仓库名称',
   hope_in_ware_house_code varchar(50)  comment '期望入库位置编码',
   hope_in_ware_house_name varchar(50)  comment '期望入库位置名称',
   position_status      varchar(10)  comment '
             1、仓储中2、转运中;3、在制',
   qrcode_status        varchar(10)  comment '1、厂内；2、已发货；3、已投产；4、已置空；5、已退料；6、已报废；',
   qc_status            varchar(10)  comment '质量状态：1、合格；2、待检；3、不合格；',
   business_status      varchar(10)  comment '1、质检中；2、盘点中；3、无业务；4、生产中；5、生产完；',
   container_status     varchar(10)  comment '1、空载；2、满载；3、可载。',
   login_status         varchar(10)  comment '1、登入；0、登出。',
   qc_time              datetime  comment '上次质检时间',
   inventory_check_time datetime  comment '上次盘点时间',
   attach               varchar(1000)  comment '附件',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_item_container_manage comment '载具管理';




/*==============================================================*/
/* Table: mes_item_container_manage_detail                      */
/*==============================================================*/
create table mes_item_container_manage_detail
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '载具物料id',
   container_manage_id  varchar(50)  comment '载具关联物料主表id',
   container_id         varchar(50)  comment '载具id',
   container_code       varchar(50)  comment '载具编码',
   container_name       varchar(50)  comment '载具名称',
   container_qrcode     varchar(50)  comment '载具标签码',
   item_id              varchar(50)  comment '物料名称',
   item_code            varchar(50)  comment '物料编码',
   item_name            varchar(50)  comment '物料名称',
   spec                 varchar(50)  comment '规格',
   item_cell_id         varchar(50)  comment '物料单元id',
   item_cell_qrcode     varchar(50)  comment '物料标签码',
   qty                  decimal(18,6)  comment '数量',
   unit_id              varchar(50)  comment '单位，以生成时进入表中时第一次业务单位存储。',
   unit_name            varchar(50)  comment '单位名称，以生成时进入表中时第一次业务单位存储。',
   sequence             int  comment '序号:某些场景用于标注标签的位置',
   attach               varchar(1000)  comment '附件',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id),
   key uk_mes_item_container_manage_detail (tenant_id, container_manage_id, item_cell_id)
);

alter table mes_item_container_manage_detail comment '载具关联物料单元';



/*==============================================================*/
/* Table: mes_item_container_load_item_record                   */
/*==============================================================*/
create table mes_item_container_load_item_record
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '载具料id',
   container_manage_id  varchar(50)  comment '载具管理主表id',
   container_id         varchar(50)  comment '载具id',
   container_code       varchar(50)  comment '载具编码',
   container_name       varchar(50)  comment '载具名称',
   container_qrcode     varchar(50)  comment '载具标签码',
   item_id              varchar(50)  comment '物料名称',
   item_code            varchar(50)  comment '物料编码',
   item_name            varchar(50)  comment '物料名称',
   spec                 varchar(50)  comment '规格',
   item_cell_id         varchar(50)  comment '物料单元id',
   item_cell_qrcode     varchar(50)  comment '物料标签码',
   qty                  decimal(18,6)  comment '数量',
   unit_id              varchar(50)  comment '单位，以生成时进入表中时第一次业务单位存储。',
   unit_name            varchar(50)  comment '单位名称，以生成时进入表中时第一次业务单位存储。',
   load_type            varchar(10)  comment '1、装载；2、卸载。',
   attach               varchar(1000)  comment '附件',
   note                 varchar(256)  comment '备注',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_item_container_load_item_record comment '载具装载物料记录';



/*==============================================================*/
/* Table: mes_item_container_transfer_record                    */
/*==============================================================*/
create table mes_item_container_transfer_record
(
   tenant_id            varchar(50)  comment '租户ID',
   id                   varchar(50) not null  comment '转移记录id',
   container_manage_id  varchar(50)  comment '容器管理单元id',
   transfer_status      varchar(10)  comment '1、已出库；2、已入库；',
   father_qrcode        varchar(50)  comment '父标签码',
   container_qrcode     varchar(50)  comment '容器标签码',
   container_code       varchar(50)  comment '容器编码',
   container_name       varchar(50)  comment '容器名称',
   container_status     varchar(10)  comment '1、空载；2、满载；3、可载。',
   event_id             varchar(50)  comment '事务id',
   event_name           varchar(50)  comment '事务名称',
   event_object         varchar(50)  comment '事务对象',
   bill_code            varchar(50)  comment '单据编码',
   out_storage_code     varchar(50)  comment '出库位置编码',
   out_storage_name     varchar(50)  comment '出库位置名称',
   out_storage_employee varchar(50)  comment '发出人',
   out_time             datetime  comment '出库时间',
   out_note             varchar(256)  comment '出库备注',
   hope_in_house_code   varchar(50)  comment '期望入库位置编码',
   hope_in_house_name   varchar(50)  comment '期望入库位置名称',
   in_storage_code      varchar(50)  comment '接收仓位编码',
   in_storage_name      varchar(50)  comment '接收仓位名称',
   in_storage_employee  varchar(50)  comment '接收人',
   in_time              datetime  comment '接收时间',
   in_note              varchar(256)  comment '接收备注',
   quality_status       varchar(10)  comment '质量状态',
   sop_step_id          varchar(50)  comment '关联标准作业步骤id',
   sop_control_id       varchar(50)  comment '关联标准作业控件id',
   task_id              varchar(50)  comment '标准作业流程对应任务id',
   create_by            varchar(50)  comment '创建人',
   create_time          datetime  comment '创建时间',
   update_by            varchar(50)  comment '修改人',
   update_time          datetime  comment '修改时间',
   is_deleted           tinyint default 0  comment '删除标识',
   primary key (id)
);

alter table mes_item_container_transfer_record comment '载具出入库记录/转移记录';

ALTER TABLE imes_dev.mes_item_transfer_record CHANGE evnet_id event_id varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '事务id';

ALTER TABLE imes_dev.mes_item_transfer_record CHANGE evnet_name event_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '事务名称';

ALTER TABLE imes_dev.mes_item_transfer_record CHANGE evnet_object event_object varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '事务对象';
