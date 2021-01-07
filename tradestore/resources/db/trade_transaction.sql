-- newDB.trade_transaction definition

CREATE TABLE `trade_transaction` (
  `trade_id` varchar(100) NOT NULL,
  `trade_version` int(11) NOT NULL,
  `counter_party_id` varchar(100) DEFAULT NULL,
  `booking_id` varchar(100) DEFAULT NULL,
  `maturity_date` date NOT NULL,
  `creation_date` date DEFAULT NULL,
  `expiry_status` varchar(5) DEFAULT NULL,
  Unique KEY `trade_transaction_trade_id_IDX` (`trade_id`,`trade_version`) USING BTREE,
  KEY `trade_transaction_maturity_date_IDX` (`maturity_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;