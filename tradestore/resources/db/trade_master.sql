CREATE TABLE `trade_master` (
  `trade_id` varchar(100) NOT NULL,
  `latest_trade_version` int(11) NOT NULL,
  UNIQUE KEY `trade_master_trade_id_IDX` (`trade_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;