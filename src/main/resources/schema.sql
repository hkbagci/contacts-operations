# ALTER TABLE `contact_phone` DROP FOREIGN KEY `contact_phone_ibfk_1`;
DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact`
(
    `id`        int          NOT NULL AUTO_INCREMENT,
    `name`      varchar(255) NOT NULL,
    `last_name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `contact_phone`;
CREATE TABLE `contact_phone`
(
    `id`           int          NOT NULL AUTO_INCREMENT,
    `contact_id`   int          NOT NULL,
    `phone_number` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);