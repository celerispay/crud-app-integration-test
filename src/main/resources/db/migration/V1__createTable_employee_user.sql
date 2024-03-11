CREATE TABLE `employee` (
  `id` int(11) PRIMARY KEY AUTO_INCREMENT,
  `address` varchar(100),
  `designation` varchar(100),
  `email` varchar(100) UNIQUE,
  `gender` varchar(1),
  `name` varchar(100),
  `salary` bigint(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user` (
  `id` int(11) PRIMARY KEY AUTO_INCREMENT,
  `password` varchar(100),
  `roles` varchar(255),
  `username` varchar(100) UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;