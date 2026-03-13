CREATE TABLE IF NOT EXISTS `users`
(
    `user_id`    INT AUTO_INCREMENT PRIMARY KEY,
    `username`   VARCHAR(50)  NOT NULL,
    `email`      VARCHAR(100),
    `phone`      VARCHAR(20),
    `password`   VARCHAR(100) NOT NULL,
    `role`       VARCHAR(20)  NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表，存储用户的基本信息';

INSERT INTO users (username, email, phone, password, role)
VALUES ('admin',
        'admin@backend,com',
        '13800138000',
        '123456',
        'ADMIN')
