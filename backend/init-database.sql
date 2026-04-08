-- Hair Accessory E-commerce Image Generator
-- Database Initialization Script
-- Version: 1.0.0
-- Date: 2026-04-08

-- ============================================
-- 1. 创建数据库
-- ============================================

CREATE DATABASE IF NOT EXISTS hair_accessory_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE hair_accessory_db;

-- ============================================
-- 2. 用户表
-- ============================================

CREATE TABLE IF NOT EXISTS `user` (
    `id` VARCHAR(64) PRIMARY KEY COMMENT '用户ID',
    `username` VARCHAR(128) UNIQUE NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `email` VARCHAR(255) UNIQUE COMMENT '邮箱',
    `phone` VARCHAR(32) COMMENT '手机号',
    `role` VARCHAR(32) DEFAULT 'USER' COMMENT '角色：USER, ADMIN',
    `status` VARCHAR(32) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE, INACTIVE, BANNED',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (`username`),
    INDEX idx_email (`email`),
    INDEX idx_status (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 3. 产品表
-- ============================================

CREATE TABLE IF NOT EXISTS `product` (
    `id` VARCHAR(64) PRIMARY KEY COMMENT '产品ID',
    `user_id` VARCHAR(64) NOT NULL COMMENT '所属用户ID',
    `name` VARCHAR(255) NOT NULL COMMENT '产品名称',
    `category` VARCHAR(64) NOT NULL COMMENT '产品类目',
    `target_market` VARCHAR(64) COMMENT '目标市场',
    `description` TEXT COMMENT '产品描述',
    `price` DECIMAL(10,2) COMMENT '价格',
    `currency` VARCHAR(10) DEFAULT 'RUB' COMMENT '货币单位',
    `keywords` JSON COMMENT '关键词标签',
    `seo_title` VARCHAR(255) COMMENT 'SEO标题',
    `seo_description` TEXT COMMENT 'SEO描述',
    `status` VARCHAR(32) DEFAULT 'DRAFT' COMMENT '状态：DRAFT, GENERATING, PUBLISHED, ARCHIVED',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX idx_user_id (`user_id`),
    INDEX idx_category (`category`),
    INDEX idx_status (`status`),
    INDEX idx_created_at (`created_at`),
    FULLTEXT KEY ft_name_desc (`name`, `description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- ============================================
-- 4. 产品部件表
-- ============================================

CREATE TABLE IF NOT EXISTS `product_component` (
    `id` VARCHAR(64) PRIMARY KEY COMMENT '部件ID',
    `product_id` VARCHAR(64) NOT NULL COMMENT '所属产品ID',
    `name` VARCHAR(255) COMMENT '部件名称',
    `description` TEXT COMMENT '部件描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`) ON DELETE CASCADE,
    INDEX idx_product_id (`product_id`),
    INDEX idx_sort_order (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品部件表';

-- ============================================
-- 5. 部件图片表
-- ============================================

CREATE TABLE IF NOT EXISTS `component_image` (
    `id` VARCHAR(64) PRIMARY KEY COMMENT '图片ID',
    `component_id` VARCHAR(64) NOT NULL COMMENT '所属部件ID',
    `image_url` VARCHAR(512) NOT NULL COMMENT '图片URL',
    `thumbnail_url` VARCHAR(512) COMMENT '缩略图URL',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `file_size` BIGINT COMMENT '文件大小（字节）',
    `width` INT COMMENT '宽度（像素）',
    `height` INT COMMENT '高度（像素）',
    `format` VARCHAR(10) COMMENT '格式：jpg, png, webp',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`component_id`) REFERENCES `product_component`(`id`) ON DELETE CASCADE,
    INDEX idx_component_id (`component_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部件图片表';

-- ============================================
-- 6. 用户痛点表
-- ============================================

CREATE TABLE IF NOT EXISTS `user_pain_point` (
    `id` VARCHAR(64) PRIMARY KEY COMMENT '痛点ID',
    `product_id` VARCHAR(64) NOT NULL COMMENT '所属产品ID',
    `content` TEXT NOT NULL COMMENT '痛点内容',
    `source` VARCHAR(128) COMMENT '来源：竞品分析、用户评论、市场调研等',
    `priority` VARCHAR(16) DEFAULT 'medium' COMMENT '优先级：high, medium, low',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`) ON DELETE CASCADE,
    INDEX idx_product_id (`product_id`),
    INDEX idx_priority (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户痛点表';

-- ============================================
-- 7. 用户需求表
-- ============================================

CREATE TABLE IF NOT EXISTS `user_requirement` (
    `id` VARCHAR(64) PRIMARY KEY COMMENT '需求ID',
    `product_id` VARCHAR(64) NOT NULL COMMENT '所属产品ID',
    `content` TEXT NOT NULL COMMENT '需求内容',
    `category` VARCHAR(64) COMMENT '需求分类：功能性、价格、包装等',
    `priority` VARCHAR(16) DEFAULT 'medium' COMMENT '优先级：high, medium, low',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`) ON DELETE CASCADE,
    INDEX idx_product_id (`product_id`),
    INDEX idx_category (`category`),
    INDEX idx_priority (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户需求表';

-- ============================================
-- 8. AI生成任务表
-- ============================================

CREATE TABLE IF NOT EXISTS `ai_generation_task` (
    `id` VARCHAR(64) PRIMARY KEY COMMENT '任务ID',
    `product_id` VARCHAR(64) NOT NULL COMMENT '关联产品ID',
    `user_id` VARCHAR(64) NOT NULL COMMENT '用户ID',
    `status` VARCHAR(32) DEFAULT 'PENDING' COMMENT '状态：PENDING, PROCESSING, COMPLETED, FAILED',
    `task_type` VARCHAR(32) COMMENT '任务类型：FULL, MAIN_IMAGE, DETAIL_IMAGE, INFO_IMAGE',
    `progress` INT DEFAULT 0 COMMENT '进度百分比（0-100）',
    `status_message` VARCHAR(255) COMMENT '状态描述',
    `error_message` TEXT COMMENT '错误信息',
    `config` JSON COMMENT '生成配置（提示词等）',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `started_at` TIMESTAMP NULL COMMENT '开始时间',
    `completed_at` TIMESTAMP NULL COMMENT '完成时间',
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX idx_product_id (`product_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_status (`status`),
    INDEX idx_created_at (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI生成任务表';

-- ============================================
-- 9. 生成结果图片表
-- ============================================

CREATE TABLE IF NOT EXISTS `generated_image` (
    `id` VARCHAR(64) PRIMARY KEY COMMENT '图片ID',
    `task_id` VARCHAR(64) NOT NULL COMMENT '关联任务ID',
    `product_id` VARCHAR(64) NOT NULL COMMENT '关联产品ID',
    `image_url` VARCHAR(512) NOT NULL COMMENT '原始图片URL',
    `compressed_url` VARCHAR(512) COMMENT '压缩后图片URL',
    `thumbnail_url` VARCHAR(512) COMMENT '缩略图URL',
    `image_type` VARCHAR(32) COMMENT '图片类型：MAIN, DETAIL, INFO',
    `image_index` INT COMMENT '图片序号（1-10）',
    `file_size` BIGINT COMMENT '文件大小（字节）',
    `compressed_size` BIGINT COMMENT '压缩后大小（字节）',
    `width` INT COMMENT '宽度（像素）',
    `height` INT COMMENT '高度（像素）',
    `format` VARCHAR(10) COMMENT '格式：jpg, png, webp',
    `prompt` TEXT COMMENT '生成提示词',
    `generation_params` JSON COMMENT '生成参数',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`task_id`) REFERENCES `ai_generation_task`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`) ON DELETE CASCADE,
    INDEX idx_task_id (`task_id`),
    INDEX idx_product_id (`product_id`),
    INDEX idx_image_type (`image_type`),
    INDEX idx_image_index (`image_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生成结果图片表';

-- ============================================
-- 10. 任务日志表
-- ============================================

CREATE TABLE IF NOT EXISTS `task_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    `task_id` VARCHAR(64) NOT NULL COMMENT '任务ID',
    `log_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '日志时间',
    `log_type` VARCHAR(32) COMMENT '日志类型：INFO, SUCCESS, WARNING, ERROR',
    `message` TEXT COMMENT '日志消息',
    `details` JSON COMMENT '详细信息',
    FOREIGN KEY (`task_id`) REFERENCES `ai_generation_task`(`id`) ON DELETE CASCADE,
    INDEX idx_task_id (`task_id`),
    INDEX idx_log_time (`log_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务日志表';

-- ============================================
-- 11. 系统配置表
-- ============================================

CREATE TABLE IF NOT EXISTS `system_config` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '配置ID',
    `config_key` VARCHAR(128) UNIQUE NOT NULL COMMENT '配置键',
    `config_value` TEXT COMMENT '配置值',
    `config_type` VARCHAR(32) DEFAULT 'STRING' COMMENT '类型：STRING, JSON, NUMBER, BOOLEAN',
    `description` VARCHAR(255) COMMENT '描述',
    `is_public` BOOLEAN DEFAULT FALSE COMMENT '是否公开',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_config_key (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ============================================
-- 12. 用户配额表
-- ============================================

CREATE TABLE IF NOT EXISTS `user_quota` (
    `id` VARCHAR(64) PRIMARY KEY COMMENT '配额ID',
    `user_id` VARCHAR(64) UNIQUE NOT NULL COMMENT '用户ID',
    `generation_quota` INT DEFAULT 100 COMMENT '生成配额（次数/月）',
    `generation_used` INT DEFAULT 0 COMMENT '已使用次数',
    `storage_quota` BIGINT DEFAULT 10737418240 COMMENT '存储配额（字节，默认10GB）',
    `storage_used` BIGINT DEFAULT 0 COMMENT '已使用存储（字节）',
    `reset_date` DATE COMMENT '配额重置日期',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX idx_user_id (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户配额表';

-- ============================================
-- 13. 操作审计日志表
-- ============================================

CREATE TABLE IF NOT EXISTS `audit_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    `user_id` VARCHAR(64) COMMENT '用户ID',
    `action` VARCHAR(128) NOT NULL COMMENT '操作：CREATE_PRODUCT, DELETE_PRODUCT, START_GENERATION等',
    `resource_type` VARCHAR(64) COMMENT '资源类型：PRODUCT, IMAGE, TASK等',
    `resource_id` VARCHAR(64) COMMENT '资源ID',
    `ip_address` VARCHAR(64) COMMENT 'IP地址',
    `user_agent` VARCHAR(512) COMMENT '用户代理',
    `request_data` JSON COMMENT '请求数据',
    `response_status` INT COMMENT '响应状态码',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (`user_id`),
    INDEX idx_action (`action`),
    INDEX idx_created_at (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作审计日志表';

-- ============================================
-- 14. 插入初始数据
-- ============================================

-- 系统配置
INSERT INTO `system_config` (`config_key`, `config_value`, `config_type`, `description`, `is_public`) VALUES
('ai.generation.max_concurrent_tasks', '5', 'NUMBER', 'AI生成最大并发任务数', FALSE),
('ai.generation.timeout_seconds', '600', 'NUMBER', 'AI生成超时时间（秒）', FALSE),
('image.max_file_size_mb', '5', 'NUMBER', '单张图片最大上传大小（MB）', TRUE),
('image.compression.target_size_bytes', '1048576', 'NUMBER', '目标压缩大小（字节，1MB for OZON）', FALSE),
('user.default_generation_quota', '100', 'NUMBER', '默认生成配额（次/月）', FALSE),
('user.default_storage_quota_gb', '10', 'NUMBER', '默认存储配额（GB）', FALSE),
('ozon.platform.enabled', 'true', 'BOOLEAN', 'OZON平台集成是否启用', FALSE);

-- 创建默认管理员账户 (密码: Admin123456)
INSERT INTO `user` (`id`, `username`, `password`, `email`, `role`, `status`) VALUES
('admin_001', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lXXS6bZuPHYtTxP.O', 'admin@example.com', 'ADMIN', 'ACTIVE');

-- 为管理员创建配额
INSERT INTO `user_quota` (`id`, `user_id`, `generation_quota`, `storage_quota`, `reset_date`) VALUES
('quota_admin_001', 'admin_001', 999999, 107374182400, DATE_ADD(CURDATE(), INTERVAL 1 MONTH));

-- ============================================
-- 15. 创建视图（可选）
-- ============================================

-- 产品统计视图
CREATE OR REPLACE VIEW `v_product_stats` AS
SELECT 
    p.id AS product_id,
    p.name AS product_name,
    p.user_id,
    p.status,
    COUNT(DISTINCT pc.id) AS component_count,
    COUNT(DISTINCT ci.id) AS component_image_count,
    COUNT(DISTINCT upp.id) AS pain_point_count,
    COUNT(DISTINCT ur.id) AS requirement_count,
    COUNT(DISTINCT gi.id) AS generated_image_count,
    p.created_at,
    p.updated_at
FROM `product` p
LEFT JOIN `product_component` pc ON p.id = pc.product_id
LEFT JOIN `component_image` ci ON pc.id = ci.component_id
LEFT JOIN `user_pain_point` upp ON p.id = upp.product_id
LEFT JOIN `user_requirement` ur ON p.id = ur.product_id
LEFT JOIN `generated_image` gi ON p.id = gi.product_id
GROUP BY p.id;

-- 任务统计视图
CREATE OR REPLACE VIEW `v_task_stats` AS
SELECT 
    agt.id AS task_id,
    agt.product_id,
    agt.user_id,
    agt.status,
    agt.progress,
    COUNT(gi.id) AS generated_image_count,
    SUM(gi.compressed_size) AS total_size_bytes,
    TIMESTAMPDIFF(SECOND, agt.started_at, agt.completed_at) AS duration_seconds,
    agt.created_at,
    agt.completed_at
FROM `ai_generation_task` agt
LEFT JOIN `generated_image` gi ON agt.id = gi.task_id
GROUP BY agt.id;

-- ============================================
-- 16. 创建存储过程（可选）
-- ============================================

DELIMITER //

-- 重置用户月度配额
CREATE PROCEDURE `sp_reset_monthly_quota`()
BEGIN
    UPDATE `user_quota`
    SET 
        `generation_used` = 0,
        `reset_date` = DATE_ADD(CURDATE(), INTERVAL 1 MONTH)
    WHERE `reset_date` <= CURDATE();
END //

-- 清理过期任务日志（保留30天）
CREATE PROCEDURE `sp_cleanup_old_logs`()
BEGIN
    DELETE FROM `task_log`
    WHERE `log_time` < DATE_SUB(NOW(), INTERVAL 30 DAY);
    
    DELETE FROM `audit_log`
    WHERE `created_at` < DATE_SUB(NOW(), INTERVAL 90 DAY);
END //

DELIMITER ;

-- ============================================
-- 17. 创建定时任务（需要MySQL Event Scheduler）
-- ============================================

-- 启用事件调度器
SET GLOBAL event_scheduler = ON;

-- 每月1号重置配额
CREATE EVENT IF NOT EXISTS `evt_reset_monthly_quota`
ON SCHEDULE EVERY 1 MONTH
STARTS DATE_ADD(DATE_ADD(LAST_DAY(CURDATE()), INTERVAL 1 DAY), INTERVAL 0 HOUR)
DO CALL sp_reset_monthly_quota();

-- 每周日凌晨清理旧日志
CREATE EVENT IF NOT EXISTS `evt_cleanup_old_logs`
ON SCHEDULE EVERY 1 WEEK
STARTS DATE_ADD(DATE_ADD(CURDATE(), INTERVAL (7 - DAYOFWEEK(CURDATE())) DAY), INTERVAL 2 HOUR)
DO CALL sp_cleanup_old_logs();

-- ============================================
-- 完成
-- ============================================

SELECT '数据库初始化完成！' AS message;
SELECT CONCAT('默认管理员账户: admin / Admin123456') AS credentials;
