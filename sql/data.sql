CREATE TABLE `user` (
  `user_id` varchar(255) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `create_time` bigint NOT NULL,
  `email_address` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` int DEFAULT '1',
  `token` varchar(255) DEFAULT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `article` (
  `article_id` varchar(255) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `avartar_url` varchar(255) DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `create_time` bigint NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` int NOT NULL,
  `update_time` bigint NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;