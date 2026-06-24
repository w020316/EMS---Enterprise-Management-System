CREATE DATABASE IF NOT EXISTS ems DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ems;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像路径',
  `role` VARCHAR(20) DEFAULT 'user' COMMENT '角色(user/admin)',
  `status` TINYINT DEFAULT 1 COMMENT '状态(1启用/0禁用)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 部门表
CREATE TABLE IF NOT EXISTS `department` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '部门名称',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '部门描述',
  `manager` VARCHAR(50) DEFAULT NULL COMMENT '部门负责人',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 员工表
CREATE TABLE IF NOT EXISTS `employee` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `gender` VARCHAR(10) DEFAULT NULL COMMENT '性别',
  `birth_date` DATE DEFAULT NULL COMMENT '出生日期',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `department_id` BIGINT DEFAULT NULL COMMENT '部门ID',
  `position` VARCHAR(50) DEFAULT NULL COMMENT '职位',
  `salary` DECIMAL(10,2) DEFAULT NULL COMMENT '薪资',
  `hire_date` DATE DEFAULT NULL COMMENT '入职日期',
  `status` TINYINT DEFAULT 1 COMMENT '状态(1在职/0离职)',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像路径',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_department_id` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 默认管理员账号由应用启动时自动创建 (admin / admin123)

-- 插入示例部门
INSERT INTO `department` (`name`, `description`, `manager`) VALUES
('技术部', '负责产品研发和技术支持', '张三'),
('市场部', '负责市场推广和品牌建设', '李四'),
('人事部', '负责人力资源管理', '王五'),
('财务部', '负责财务管理和预算控制', '赵六');

-- 插入示例员工
INSERT INTO `employee` (`name`, `gender`, `birth_date`, `phone`, `email`, `department_id`, `position`, `salary`, `hire_date`, `status`) VALUES
('张三', '男', '1990-05-15', '13800138001', 'zhangsan@ems.com', 1, '高级工程师', 25000.00, '2020-03-01', 1),
('李四', '男', '1992-08-20', '13800138002', 'lisi@ems.com', 2, '市场经理', 22000.00, '2021-06-15', 1),
('王五', '女', '1995-03-10', '13800138003', 'wangwu@ems.com', 3, '人事专员', 15000.00, '2022-01-10', 1),
('赵六', '男', '1988-11-25', '13800138004', 'zhaoliu@ems.com', 4, '财务主管', 20000.00, '2019-09-01', 1),
('孙七', '女', '1993-07-08', '13800138005', 'sunqi@ems.com', 1, '前端工程师', 18000.00, '2022-04-20', 1);
