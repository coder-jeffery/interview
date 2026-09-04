show databases;

create database interview;

use interview;

CREATE TABLE `user` (
                        `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `username` VARCHAR(50) NOT NULL COMMENT '用户名',
                        `password` VARCHAR(128) NOT NULL COMMENT '密码(存加密后的密文，不要明文)',
                        `nickname` VARCHAR(50) DEFAULT '' COMMENT '昵称',
                        `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                        `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
                        `avatar` VARCHAR(255) DEFAULT '' COMMENT '头像地址',
                        `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1正常',
                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_username` (`username`),
                        UNIQUE KEY `uk_phone` (`phone`),
                        UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';



CREATE TABLE `order` (
                         `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单主键',
                         `order_no` VARCHAR(64) NOT NULL COMMENT '订单编号，业务唯一',
                         `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户id，关联user表id',
                         `total_amount` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
                         `pay_amount` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '实付金额',
                         `freight_amount` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '运费',
                         `discount_amount` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
                         `pay_status` TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态 0未支付 1已支付 2已退款',
                         `order_status` TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态 0待付款 1待发货 2待收货 3已完成 4已取消',
                         `pay_time` DATETIME NULL COMMENT '支付时间',
                         `receive_time` DATETIME NULL COMMENT '确认收货时间',
                         `receiver_name` VARCHAR(50) NOT NULL COMMENT '收件人姓名',
                         `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收件人手机号',
                         `receiver_address` VARCHAR(255) NOT NULL COMMENT '收货地址',
                         `remark` VARCHAR(512) DEFAULT '' COMMENT '订单备注',
                         `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0未删 1已删',
                         `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `uk_order_no` (`order_no`),
                         KEY `idx_user_id` (`user_id`),
                         KEY `idx_order_status` (`order_status`),
                         KEY `idx_pay_status` (`pay_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单主表';



CREATE TABLE `order_item` (
                              `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '明细主键',
                              `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单主表id，order_main.id',
                              `order_no` VARCHAR(64) NOT NULL COMMENT '冗余订单号，方便查询',
                              `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
                              `product_name` VARCHAR(255) NOT NULL COMMENT '商品名称（冗余，防止商品修改历史订单错乱）',
                              `product_img` VARCHAR(255) DEFAULT '' COMMENT '商品图片',
                              `product_price` DECIMAL(12,2) NOT NULL COMMENT '下单时商品单价',
                              `quantity` INT NOT NULL DEFAULT 1 COMMENT '购买数量',
                              `subtotal_amount` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '小计金额=单价*数量',
                              `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              KEY `idx_order_id` (`order_id`),
                              KEY `idx_order_no` (`order_no`),
                              KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单明细表';


CREATE TABLE `biz_record` (
                              `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                              `biz_no` VARCHAR(64) NOT NULL COMMENT '业务单号，幂等键',
                              `user_name` VARCHAR(50) NOT NULL COMMENT '用户名',
                              `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
                              `amount` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
                              `source` VARCHAR(16) NOT NULL DEFAULT '' COMMENT '来源 csv/json',
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uk_biz_no` (`biz_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Camel CSV/JSON 统一入库业务表';


