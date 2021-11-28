DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact`
(
    `id`        bigint          NOT NULL AUTO_INCREMENT,
    `name`      varchar(255) NOT NULL,
    `last_name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `contact_phone`;
CREATE TABLE `contact_phone`
(
    `id`           bigint          NOT NULL AUTO_INCREMENT,
    `contact_id`   bigint          NOT NULL,
    `phone_number` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);