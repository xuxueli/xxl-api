#
# XXL-API
# Copyright (c) 2017-present, xuxueli.

CREATE database if NOT EXISTS `xxl_api` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `xxl_api`;


CREATE TABLE `xxl_api_biz` (
                               `id` int(11) NOT NULL AUTO_INCREMENT,
                               `biz_name` varchar(50) NOT NULL COMMENT '业务线名称',
                               `order` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_api_datatype` (
                                    `id` int(11) NOT NULL AUTO_INCREMENT,
                                    `name` varchar(100) NOT NULL COMMENT '数据类型名称',
                                    `about` varchar(200) DEFAULT NULL COMMENT '数据类型描述',
                                    `biz_id` int(11) NOT NULL COMMENT '业务线ID，为0表示公共',
                                    `owner` varchar(100) DEFAULT NULL COMMENT '负责人',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_api_datatype_fileds` (
                                           `id` int(11) NOT NULL AUTO_INCREMENT,
                                           `parent_datatype_id` int(11) NOT NULL COMMENT '所属，数据类型ID',
                                           `field_name` varchar(100) NOT NULL COMMENT '字段名称',
                                           `field_about` varchar(200) DEFAULT NULL COMMENT '字段描述',
                                           `field_datatype_id` int(11) NOT NULL COMMENT '字段数据类型ID',
                                           `field_type` tinyint(4) NOT NULL COMMENT '字段形式：0=默认、1=数组',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_api_document` (
                                    `id` int(11) NOT NULL AUTO_INCREMENT,
                                    `project_id` int(11) NOT NULL COMMENT '项目ID',
                                    `group_id` int(11) NOT NULL COMMENT '分组ID',
                                    `name` varchar(50) NOT NULL COMMENT '接口名称',
                                    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0-启用、1-维护、2-废弃',
                                    `star_level` tinyint(4) NOT NULL COMMENT '星标等级：0-普通接口、1-一星接口',
                                    `request_url` varchar(100) NOT NULL COMMENT 'Request URL：相对地址',
                                    `request_method` varchar(20) NOT NULL COMMENT 'Request Method：如POST、GET',
                                    `request_headers` text COMMENT 'Request Headers：Map-JSON格式字符串',
                                    `query_params` text COMMENT 'Query String Parameters：VO-JSON格式字符串',
                                    `response_params` text COMMENT 'Response Parameters：VO-JSON格式字符串',
                                    `response_datatype_id` int(11) NOT NULL DEFAULT '0' COMMENT '响应数据类型ID',
                                    `success_resp_type` varchar(50) NOT NULL COMMENT 'Response Content-type：成功接口，如JSON、XML、HTML、TEXT、JSONP',
                                    `success_resp_example` text COMMENT 'Response Content：成功接口',
                                    `fail_resp_type` varchar(255) NOT NULL COMMENT 'Response Content-type：失败接口',
                                    `fail_resp_example` text COMMENT 'Response Content：失败接口',
                                    `remark` text COMMENT '备注',
                                    `add_time` datetime NOT NULL COMMENT '创建时间',
                                    `update_time` datetime NOT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_api_group` (
                                 `id` int(11) NOT NULL AUTO_INCREMENT,
                                 `project_id` int(11) NOT NULL COMMENT '项目ID',
                                 `name` varchar(255) NOT NULL COMMENT '分组名称',
                                 `order` int(11) NOT NULL COMMENT '分组排序',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_api_mock` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `document_id` int(11) NOT NULL COMMENT '接口ID',
                                `uuid` varchar(50) NOT NULL COMMENT 'UUID',
                                `resp_type` varchar(50) NOT NULL COMMENT 'Response Content-type：如JSON、XML、HTML、TEXT、JSONP',
                                `resp_example` text COMMENT 'Response Content',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_api_project` (
                                   `id` int(11) NOT NULL AUTO_INCREMENT,
                                   `name` varchar(50) NOT NULL COMMENT '项目名称',
                                   `desc` varchar(200) DEFAULT NULL COMMENT '项目描述',
                                   `base_url_product` varchar(200) NOT NULL COMMENT '根地址：线上环境',
                                   `base_url_ppe` varchar(200) DEFAULT NULL COMMENT '根地址：预发布环境',
                                   `base_url_qa` varchar(200) DEFAULT NULL COMMENT '根地址：测试环境',
                                   `biz_id` int(11) NOT NULL DEFAULT '0' COMMENT '业务线ID',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_api_test_history` (
                                        `id` int(11) NOT NULL AUTO_INCREMENT,
                                        `document_id` int(11) NOT NULL COMMENT '接口ID',
                                        `add_time` datetime NOT NULL,
                                        `update_time` datetime NOT NULL,
                                        `request_url` varchar(200) NOT NULL COMMENT 'Request URL：绝对地址',
                                        `request_method` varchar(20) NOT NULL COMMENT 'Request Method：如POST、GET',
                                        `request_headers` text COMMENT 'Request Headers：Map-JSON格式字符串',
                                        `query_params` text COMMENT 'Query String Parameters：VO-JSON格式字符串',
                                        `resp_type` varchar(50) DEFAULT NULL COMMENT 'Response Content-type：如JSON',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_api_user` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `username` varchar(50) NOT NULL COMMENT '账号',
                                `password` varchar(50) NOT NULL COMMENT '密码',
                                `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户类型：0-普通用户、1-超级管理员',
                                `permission_biz` varchar(200) DEFAULT NULL COMMENT '业务线权限，多个逗号分隔',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


## —————————————————————— for default data ——————————————————

INSERT INTO `xxl_api_user`  (`id`, `username`, `password`, `type`, `permission_biz`)
VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '1', ''),
       ('2', 'user', 'e10adc3949ba59abbe56e057f20f883e', '0', '1');

INSERT INTO `xxl_api_biz` (`id`, `biz_name`, `order`) VALUES ('1', '默认业务线', '1');

INSERT INTO `xxl_api_datatype` (`id`, `name`, `about`, `biz_id`, `owner`)
VALUES  ('1', 'String', '字符串类型', '1', null),
        ('2', 'Integer', '数字整型', '1', null),
        ('3', 'Short', '短整型', '1', null),
        ('4', 'Long', '长整型', '1', null),
        ('5', 'Float', '单精度浮点数', '1', null),
        ('6', 'Double', '双精度浮点数', '1', null),
        ('7', 'Boolean', '布尔类型', '1', null),
        ('8', 'Date', '日期类型，格式“yyyy-MM-mm”', '1', null),
        ('9', 'DateTime', '日期类型，格式“yyyy-MM-mm HH:mm:ss”', '1', null),
        ('10', 'UserInfo', '用户基础信息', '1', null);

INSERT INTO `xxl_api_datatype_fileds` (`id`, `parent_datatype_id`, `field_name`, `field_about`, `field_datatype_id`, `field_type`)
VALUES  ('1','10', 'userId', '用户ID', 4, 0),
        ('2','10', 'userName', '用户名', 1, 0),
        ('3','10', 'nickName', '用户昵称', 1, 0);

INSERT INTO xxl_api_document (`id`, `project_id`, `group_id`, `name`, `status`, `star_level`, `request_url`, `request_method`, `request_headers`, `query_params`,
                              `response_params`, `response_datatype_id`, `success_resp_type`, `success_resp_example`, `fail_resp_type`, `fail_resp_example`, `remark`, `add_time`, `update_time`)
VALUES  (1, 1, 1, '查询用户信息', 0, 0, '/queryUser', 'POST', '[]', '[{"notNull":"true","type":"STRING","name":"userName","desc":"用户名称"}]', '[]', 10, 'JSON', '{
            "code": 200,
            "data": {
                "userId": 1000,
                "userName": "zhangsan",
                "nickName": "张三"
            }
        }', 'JSON', '{
            "code": 270,
            "msg": "查询失败，用户不存在"
        }', '', '2024-11-16 06:21:29', '2024-11-16 06:22:18');

INSERT INTO xxl_api_group (`id`, `project_id`, `name`, `order`)
VALUES  (1, 1, '用户分组', 1);

INSERT INTO xxl_api_mock (`id`, `document_id`, `uuid`, `resp_type`, `resp_example`)
VALUES  (1, 1, 'd528ef76-43f1-4149-9d5c-dbbae85bbba3', 'JSON', '{
            "code": 200,
            "data": {
                "userId": 1000,
                "userName": "zhangsan",
                "nickName": "张三"
            }
        }'),
        (2, 1, 'e2fa0c52-19e5-49ac-b938-0561e7e987b9', 'JSON', '{
            "code": 270,
            "msg": "查询失败，用户不存在"
        }');

INSERT INTO xxl_api.xxl_api_project (`id`, `name`, `desc`, `base_url_product`, `base_url_ppe`, `base_url_qa`, `biz_id`)
VALUES  (1, '示例项目', '', 'http://www.demo.com', 'http://www.st.demo.com', 'http://www.test.demo.com', 1);


COMMIT;