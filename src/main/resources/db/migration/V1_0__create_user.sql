DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'user ID',
  `username` varchar(50) NOT NULL COMMENT 'user name',
  `first_name` varchar(50) NOT NULL COMMENT 'first name',
  `last_name` varchar(50) NOT NULL COMMENT 'last name',
  `email` varchar(20) NOT NULL COMMENT 'email',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;