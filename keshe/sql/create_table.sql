
USE mall_system;
/*
-- 删除原有的三个表（如果存在）
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS merchants;
DROP TABLE IF EXISTS admins;

-- 创建统一的用户表
CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     role VARCHAR(10) NOT NULL, -- 身份：user、merchant、admin
                                     username VARCHAR(50) NOT NULL UNIQUE, -- 用户名
                                     phone VARCHAR(20) NOT NULL UNIQUE, -- 手机号
                                     password VARCHAR(255) NOT NULL -- 密码
);
-- 添加几组数据
INSERT INTO users (role, username, phone, password) VALUES
('user', 'user1', '13800138001', 'password1'),
('user', 'user2', '13800138002', 'password2'),
('user', 'user3', '13800138003', 'password3'),
('merchant', 'merchant1', '13800138004', 'password4'),
('merchant', 'merchant2', '13800138005', 'password5'),
('admin',  'admin1', '13800138006', 'password6')
;
 */
-- 创建商家申请注册表
# CREATE TABLE IF NOT EXISTS merchant_apply (
#                                              id INT AUTO_INCREMENT PRIMARY KEY,
#                                              role VARCHAR(10) NOT NULL, -- 身份：user、merchant、admin
#                                              username VARCHAR(50) NOT NULL UNIQUE, -- 用户名
#                                              phone VARCHAR(20) NOT NULL UNIQUE, -- 手机号
#                                              password VARCHAR(255) NOT NULL -- 密码
# );
# CREATE TABLE IF NOT EXISTS apply_form(
#                                                      id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 主键，自增
#                                                      shop_name VARCHAR(255) NOT NULL, -- 店铺名称（必填）
#                                                      legal_representative VARCHAR(50) NOT NULL, -- 法定代表人姓名（必填）
#                                                      id_number VARCHAR(18) NOT NULL UNIQUE, -- 身份证号（18位，唯一）
#                                                      contact_number VARCHAR(20) NOT NULL, -- 联系方式（手机号/电话）
#                                                      business_license VARCHAR(255) NOT NULL, -- 营业执照文件存储路径（如OSS地址）
#                                                      shop_category VARCHAR(50) NOT NULL, -- 店铺分类（电子产品/服装鞋帽等，对应下拉选项）
#                                                      shop_description TEXT, -- 店铺简介（可空）
#                                                      id_card_front VARCHAR(255) NOT NULL, -- 身份证正面照片存储路径
#                                                      id_card_back VARCHAR(255) NOT NULL, -- 身份证背面照片存储路径
#                                                      apply_time DATETIME DEFAULT CURRENT_TIMESTAMP, -- 申请时间（自动记录）
#                                                      audit_status TINYINT NOT NULL DEFAULT 0, -- 审核状态（0-待审核，1-通过，2-拒绝，默认0）
#                                                      audit_reason VARCHAR(255) -- 审核拒绝原因（仅当audit_status=2时有效）
# );
# ALTER TABLE apply_form
#     ADD COLUMN shop_address VARCHAR(255) NOT NULL;
# INSERT INTO apply_form (
#     shop_name, shop_address, legal_representative, id_number,
#     contact_number, business_license, shop_category,
#     shop_description, id_card_front, id_card_back,
#     apply_time, audit_status
# ) VALUES (
#              'Tech Solutions', 'No. 123, Technology Park, Beijing', '张三', '110101199001012345',
#              '13800138004', 'http://example.com/uploads/business_license_merchant1.jpg', 'electronics',
#              '我们提供最新的电子产品和解决方案，致力于为客户提供高质量的技术支持和服务。',
#              'http://example.com/uploads/id_card_front_merchant1.jpg', 'http://example.com/uploads/id_card_back_merchant1.jpg',
#              '2025-05-25 10:00:00', 1
#          );
# INSERT INTO apply_form (
#     shop_name, shop_address, legal_representative, id_number,
#     contact_number, business_license, shop_category,
#     shop_description, id_card_front, id_card_back,
#     apply_time, audit_status
# ) VALUES (
#              'Fashion World', 'No. 456, Fashion Street, Shanghai', '李四', '310101199002023456',
#              '13800138005', 'http://example.com/uploads/business_license_merchant2.jpg', 'clothing',
#              '我们专注于时尚服饰，提供最新潮流的服装和配饰，满足您的时尚需求。',
#              'http://example.com/uploads/id_card_front_merchant2.jpg', 'http://example.com/uploads/id_card_back_merchant2.jpg',
#              '2025-05-25 10:30:00', 1
#          );
# CREATE TABLE `product_audit` (
#                                  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '审核记录ID',
#                                  `product_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '关联产品ID（审核通过后生成）',
#                                  `shop_id` BIGINT UNSIGNED NOT NULL COMMENT '提交审核的店铺ID',
#                                  `product_name` VARCHAR(255) NOT NULL COMMENT '产品名称',
#                                  `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '产品价格（元）',
#                                  `stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
#                                  `category` VARCHAR(50) NOT NULL COMMENT '产品分类',
#                                  `description` TEXT COMMENT '产品描述',
#                                  `image_url` VARCHAR(512) COMMENT '产品图片URL',
#                                  `status` VARCHAR(20) NOT NULL DEFAULT 'pending_audit' COMMENT '审核状态（pending_audit/approved/rejected）',
#                                  `submit_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交审核时间',
#                                  `audit_time` DATETIME DEFAULT NULL COMMENT '审核完成时间',
#                                  `reject_reason` VARCHAR(512) DEFAULT NULL COMMENT '拒绝原因',
#                                  PRIMARY KEY (`id`),
#                                  INDEX `idx_shop_id` (`shop_id`) COMMENT '按店铺ID快速查询审核记录',
#                                  INDEX `idx_status` (`status`) COMMENT '按审核状态筛选记录'
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品发布审核记录表';
#
# -- 删掉表中的product_id这一属性
# ALTER TABLE `product_audit` DROP COLUMN `product_id`;
# -- 删掉表中的audit_time这一属性
# ALTER TABLE `product_audit` DROP COLUMN `audit_time`;
-- 填入一些数据
# INSERT INTO product_audit (shop_id, product_name, price, stock, category, description, image_url, status) VALUES
# (
#  4, 'iPhone 13 Pro Max', 999.00, 100, '电子产品', 'The most powerful iPhone yet.', 'https://example.com/iphone13promax.jpg', '通过'
# )
# , (
#  4, 'MacBook Pro', 1999.00, 50, '电子产品', 'The most powerful MacBook ever.', 'https://example.com/macbookpro.jpg', '通过'
# ),
#                                                                                                               (
#  5, 'AirPods Pro', 249.00, 200, '电子产品', 'The best wireless earbuds.', 'https://example.com/airpodspro.jpg', '通过'
# ),
#                                                                                                               (
#  5, 'AirTag', 29.00, 500, '电子产品', 'The most convenient way to track your stuff.', 'https://example.com/airtag.jpg', '通过'
# )

-- 创建产品销售详情表
# CREATE TABLE product_sale(
#                                        id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '销售详情ID',
#                                        product_id BIGINT UNSIGNED NOT NULL COMMENT '产品ID',
#                                        shop_id BIGINT UNSIGNED NOT NULL COMMENT '店铺ID',
#                                        sold INT NOT NULL DEFAULT 0 COMMENT '已售数量',
#                                        stock INT NOT NULL DEFAULT 0 COMMENT '当前库存',
#                                        income DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '销售收入',
#                                        PRIMARY KEY (id),
#                                        INDEX idx_product_id (product_id),
#                                        INDEX idx_shop_id (shop_id)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品销售详情表';
# -- 将product_audit表数据插入到product_sales表
# INSERT INTO product_sale (product_id, shop_id, income)
# SELECT id, shop_id, price * stock
# FROM product_audit
# WHERE status = '通过';
#
# -- 在product_sale添加一个属性price
# ALTER TABLE product_sale
# ADD COLUMN price DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '产品价格（元）';
# -- 删除product_sale表中的所有内容
# DELETE FROM product_sale;
#
# -- 将product_audit表数据插入到product_sales表
# INSERT INTO product_sale (product_id, shop_id, price)
# SELECT id, shop_id, price
# FROM product_audit
# WHERE status = '通过';

#
# -- 建立一个视图，存储已经审核通过的商家的信息，存储的属性包括shop_id,shop_name, shop_address, shop_category,apply_time,
# -- shop_id是user表中的id,其他信息从apply_form表中获取，两个表的关系为apply_form.contact_number=user.phone
# -- 修复后的视图创建语句
# CREATE VIEW merchant_info AS
# SELECT users.id AS shop_id,
#        apply_form.shop_name,
#        apply_form.shop_address,
#        apply_form.shop_category,
#        apply_form.apply_time
# FROM apply_form
#          JOIN users ON apply_form.contact_number = users.phone
# WHERE apply_form.audit_status = 1;
#
# --  product_sale增加一个属性imageUrl
# ALTER TABLE product_sale ADD COLUMN imageUrl VARCHAR(255);
# --  product_sale增加一个属性product_name
# --ALTER TABLE product_sale ADD COLUMN product_name VARCHAR(255);
# CREATE TABLE orders (
#                         order_id BIGINT UNSIGNED PRIMARY KEY COMMENT '订单 ID',
#                         user_id INT NOT NULL COMMENT '用户 ID',
#                         shop_id INT NOT NULL COMMENT '店铺 ID',
#                         status ENUM('待发货', '已发货', '已完成') COMMENT '订单状态',
#                         create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
#                         product_name VARCHAR(225) NOT NULL COMMENT '商品名称',
#                         product_id VARCHAR(225) NOT NULL COMMENT '产品ID',
#                         product_buy INT NOT NULL COMMENT '购买数量',
#                         buy_info VARCHAR(225) COMMENT '购买信息',
#                         total_amount DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
#                         receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
#                         receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人联系电话',
#                         receiver_address VARCHAR(255) NOT NULL COMMENT '收货地址',
#                         payment_time DATETIME COMMENT '支付时间',
#                         logistics_company VARCHAR(50) COMMENT '物流公司',
#                         ship_time DATETIME COMMENT '发货时间',
#                         receive_time DATETIME COMMENT '收货时间'
# );

#
# DELIMITER $$
#
# CREATE TRIGGER before_insert_orders
#     BEFORE INSERT ON orders
#     FOR EACH ROW
# BEGIN
#     DECLARE new_order_id BIGINT UNSIGNED;
#
#     -- 生成10位随机订单号
#     SET new_order_id = FLOOR(1000000000 + RAND() * 9000000000);
#
#     -- 确保唯一性（如果存在则重新生成）
#     WHILE EXISTS (SELECT 1 FROM orders WHERE order_id = new_order_id) DO
#             SET new_order_id = FLOOR(1000000000 + RAND() * 9000000000);
#         END WHILE;
#
#     SET NEW.order_id = new_order_id;
# END$$
#
# DELIMITER ;
#
# -- 用户 user1 (id=1) 购买 MacBook Pro 从 merchant1 (shop_id=4)
# INSERT INTO orders (
#     user_id, shop_id, status,
#     product_name, product_id, product_buy, buy_info,
#     total_amount, receiver_name, receiver_phone, receiver_address
# ) VALUES (
#              1, 4, '待发货',
#              'MacBook Pro', '2', 2, '配置: 16GB RAM, 512GB SSD',
#              3998.00, '张三', '13800138001', '北京市海淀区某小区'
#          );
#
# -- 用户 user2 (id=2) 购买 AirPods Pro 从 merchant2 (shop_id=5)
# INSERT INTO orders (
#     user_id, shop_id, status,
#     product_name, product_id, product_buy, buy_info,
#     total_amount, receiver_name, receiver_phone, receiver_address
# ) VALUES (
#              2, 5, '已完成',
#              'AirPods Pro', '3', 1, '颜色: 白色',
#              249.00, '李四', '13800138002', '上海市浦东新区某街道'
#          );
#
# -- 用户 user3 (id=3) 购买 AirTag 从 merchant2 (shop_id=5)
# INSERT INTO orders (
#     user_id, shop_id, status,
#     product_name, product_id, product_buy, buy_info,
#     total_amount, receiver_name, receiver_phone, receiver_address
# ) VALUES (
#              3, 5, '已发货',
#              'AirTag', '4', 2, '数量: 2个',
#              58.00, '王五', '13800138003', '广州市天河区某大厦'
#          );
#
# -- 用户 user1 (id=1) 再次购买 AirPods Pro
# INSERT INTO orders (
#     user_id, shop_id, status,
#     product_name, product_id, product_buy, buy_info,
#     total_amount, receiver_name, receiver_phone, receiver_address
# ) VALUES (
#              1, 5, '待发货',
#              'AirPods Pro', '3', 1, '颜色: 黑色',
#              249.00, '张三', '13800138001', '北京市朝阳区某公寓'
#          );
-- 用户 user2 (id=2) 购买 iPhone 13 Pro Max 从 merchant1 (shop_id=4)
# INSERT INTO orders (
#     user_id, shop_id, status,
#     product_name, product_id, product_buy, buy_info,
#     total_amount, receiver_name, receiver_phone, receiver_address
# ) VALUES (
#              2, 4, '待发货',
#              'iPhone 13 Pro Max', '1', 1, '颜色: 深空灰',
#              999.00, '李四', '13800138002', '上海市浦东新区某路123号'
#          );
#
# -- 用户 user3 (id=3) 购买 MacBook Pro 从 merchant1 (shop_id=4)
# INSERT INTO orders (
#     user_id, shop_id, status,
#     product_name, product_id, product_buy, buy_info,
#     total_amount, receiver_name, receiver_phone, receiver_address
# ) VALUES (
#              3, 4, '已发货',
#              'MacBook Pro', '2', 1, '配置: M2芯片, 16GB内存',
#              1999.00, '王五', '13800138003', '广州市天河区某大厦B座'
#          );
#
# -- 用户 user1 (id=1) 购买 AirTag 从 merchant2 (shop_id=5)
# INSERT INTO orders (
#     user_id, shop_id, status,
#     product_name, product_id, product_buy, buy_info,
#     total_amount, receiver_name, receiver_phone, receiver_address
# ) VALUES (
#              1, 5, '已完成',
#              'AirTag', '4', 4, '数量: 4个',
#              116.00, '张三', '13800138001', '北京市朝阳区某小区10号楼'
#          );
#
# -- 用户 user2 (id=2) 购买 AirPods Pro 从 merchant2 (shop_id=5)
# INSERT INTO orders (
#     user_id, shop_id, status,
#     product_name, product_id, product_buy, buy_info,
#     total_amount, receiver_name, receiver_phone, receiver_address
# ) VALUES (
#              2, 5, '待发货',
#              'AirPods Pro', '3', 2, '颜色: 白色',
#              498.00, '李四', '13800138002', '上海市徐汇区某街道23号'
#          );
# ALTER TABLE orders ADD COLUMN product_imageUrl VARCHAR(255) COMMENT '商品图片URL';
#
# -- 更新 MacBook Pro 的图片地址
# UPDATE orders
# SET product_imageUrl = 'https://example.com/macbookpro.jpg'
# WHERE product_name = 'MacBook Pro';
#
# -- 更新 AirPods Pro 的图片地址
# UPDATE orders
# SET product_imageUrl = 'https://example.com/airpodspro.jpg'
# WHERE product_name = 'AirPods Pro';
#
# -- 更新 AirTag 的图片地址
# UPDATE orders
# SET product_imageUrl = 'https://example.com/airtag.jpg'
# WHERE product_name = 'AirTag';
#
# -- 更新 iPhone 13 Pro Max 的图片地址
# UPDATE orders
# SET product_imageUrl = 'https://example.com/iphone13promax.jpg'
# WHERE product_name = 'iPhone 13 Pro Max';
#
# -- 填入几组数据到product_audit表中
# INSERT INTO product_audit (shop_id, product_name, price, stock, category, description, image_url, status, submit_time)
# VALUES
#     (4, 'MacBook Pro', 1999.00, 10, '电脑', '苹果笔记本电脑', 'https://example.com/macbookpro.jpg', 'pending_audit', NOW()),
#     (5, 'AirPods Pro', 249.00, 20, '耳机', '苹果耳机', 'https://example.com/airpodspro.jpg','pending_audit', NOW()),
#     (5, 'AirTag', 58.00, 50, '标签', '苹果标签', 'https://example.com/airtag.jpg', 'pending_audit', NOW()),
#     (4, 'iPhone 13 Pro Max', 999.00, 30, '手机', '苹果手机', 'https://example.com/iphone13promax.jpg', 'pending_audit', NOW());

# -- 插入几组审核未通过的数据
# INSERT INTO product_audit (shop_id, product_name, price, stock, category, description, image_url, status, submit_time, reject_reason)
# VALUES
#     (4, 'MacBook Pro', 1999.00, 10, '电脑', '苹果笔记本电脑', 'https://example.com/macbookpro.jpg', 'rejected', NOW(), '价格低于市场average'),
#     (4,'HUAWEI', 999.00, 30, '手机', '华为手机', 'https://example.com/iphone13promax.jpg', 'rejected', NOW(), '图片有问题'),
#     (5, 'OPPO A5', 249.00, 20, '耳机', '苹果耳机', 'https://example.com/airpodspro.jpg','rejected', NOW(), '价格低于市场average');
#
# -- 将属性id改为PAudit
# ALTER TABLE product_audit RENAME COLUMN PAudit to PAudit_id;
# -- 在Product_sale表中增加一个属性category,产品有四个分类：电子产品，家居日常，服装鞋包，食品饮料
#  ALTER TABLE Product_sale ADD COLUMN category VARCHAR(20) NOT NULL COMMENT '产品分类';

# -- apply_form表中的id_card_front和id_card_back还有business_license字段类型改为varchar(225)
#  ALTER TABLE apply_form MODIFY COLUMN id_card_front VARCHAR(225);
#  ALTER TABLE apply_form MODIFY COLUMN id_card_back VARCHAR(225);
#  ALTER TABLE apply_form MODIFY COLUMN business_license VARCHAR(225);

CREATE TABLE ShippingAddress
(
    AddressId       INT PRIMARY KEY AUTO_INCREMENT,                                 -- 收货地址唯一标识符
    UserId          INT          NOT NULL,                                          -- 关联用户表的外键
    RecipientName   VARCHAR(50)  NOT NULL,                                          -- 收件人姓名
    ShippingAddress VARCHAR(255) NOT NULL,                                          -- 收货地址详细信息
    RecipientPhone  VARCHAR(20)  NOT NULL                                          -- 收件人电话号码

);