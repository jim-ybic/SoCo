/* 
    SQL statements for MySQL
    Author: John.WU
    Date: 2015-04-13
    Copyright @ soco technology limited
    
    Update history
    Description: Update user and event tables
    John.WU
    Date: 2015-09-27
*/

DROP DATABASE IF EXISTS socoserver1;

/* CREATE DATABASE socoserver */
CREATE DATABASE IF NOT EXISTS socoserver1 DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_unicode_ci;

use socoserver1;

/* TABLE user */
CREATE TABLE IF NOT EXISTS user (
    uid BIGINT NOT NULL PRIMARY KEY,
    name varchar(40) DEFAULT NULL,
    email varchar(20) NOT NULL UNIQUE KEY,
    mobile_phone varchar(20) DEFAULT NULL,
    encrypt_password varchar(60) NOT NULL,
    plain_password varchar(30) NOT NULL,
    gender tinyint DEFAULT 2,
    latitude  FLOAT DEFAULT NULL,
    longitude  FLOAT DEFAULT NULL,
    hometown varchar(60) DEFAULT NULL,
    biography varchar(200) DEFAULT NULL,
    photo_path varchar(40) DEFAULT NULL,
    num_of_friends INT DEFAULT 0,
    num_of_following INT DEFAULT 0,
    num_of_followers INT DEFAULT 0,
    num_of_like INT DEFAULT 0,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    modify_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    is_enabled boolean DEFAULT true,
    is_validated boolean DEFAULT false,
    is_deleted boolean DEFAULT false
);

/* TABLE user_attribute */
CREATE TABLE IF NOT EXISTS user_attribute (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uid BIGINT NOT NULL,
    name varchar(30) NOT NULL,
    type varchar(20) NOT NULL,
    value varchar(100) NOT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    modify_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uid (name, type, value),
    FOREIGN KEY (uid) 
		REFERENCES user(uid)
        ON UPDATE CASCADE
);

/* TABLE fb_user for social connection */
CREATE TABLE IF NOT EXISTS fb_user (
	uid BIGINT NOT NULL,
    fb_id BIGINT NOT NULL PRIMARY KEY,  
	name varchar(80) DEFAULT NULL,
    email varchar(80) DEFAULT NULL,
	first_name varchar(40) DEFAULT NULL,
	last_name varchar(40) DEFAULT NULL,
	age_range varchar(40) DEFAULT NULL,
	link varchar(60) DEFAULT NULL,
	gender TINYINT DEFAULT 2,
	locale  varchar(40) DEFAULT NULL,
	timezone FLOAT DEFAULT NULL,
	updated_time DATETIME DEFAULT NULL,
	verified BOOLEAN
);

/* TABLE authentication token */
CREATE TABLE IF NOT EXISTS authentication_token (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uid BIGINT NOT NULL,
    token varchar(130) DEFAULT NULL,
    token_key varchar(64) DEFAULT NULL,
    start_time DATETIME DEFAULT NULL,
    validity_millionsecond BIGINT DEFAULT 0
);

/* TABLE register_code */
CREATE TABLE IF NOT EXISTS register_code (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uid BIGINT NOT NULL,
    code varchar(100) DEFAULT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

/* TABLE role */
CREATE TABLE IF NOT EXISTS role (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) NOT NULL
);

/* TABLE user_role */
CREATE TABLE IF NOT EXISTS user_role (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uid BIGINT NOT NULL,
    rid BIGINT NOT NULL
);

/* TABLE interest */
CREATE TABLE IF NOT EXISTS interest (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(40)
);

/* TABLE user_interest */
CREATE TABLE IF NOT EXISTS user_interest (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uid BIGINT NOT NULL,
    iid BIGINT NOT NULL
);

/* TABLE event */
CREATE TABLE IF NOT EXISTS event (
    eid BIGINT NOT NULL PRIMARY KEY,
    name varchar(30) NOT NULL,
    start_date  DATETIME DEFAULT CURRENT_TIMESTAMP,
    end_date    DATETIME DEFAULT CURRENT_TIMESTAMP,
    location    varchar(100) DEFAULT NULL,
    description varchar(1000) DEFAULT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    modify_date DATETIME ON UPDATE CURRENT_TIMESTAMP
);


/* TABLE event_attribute */
CREATE TABLE IF NOT EXISTS event_attribute (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    eid BIGINT NOT NULL,
    name varchar(30) NOT NULL,
    type varchar(20) NOT NULL,
    value varchar(100) NOT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    modify_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY eid (name, type, value),
    FOREIGN KEY (eid)
		REFERENCES event(eid)
        ON UPDATE CASCADE
);

/* TABLE user_event */
CREATE TABLE IF NOT EXISTS user_event (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uid BIGINT NOT NULL,
    eid BIGINT NOT NULL,
    relation varchar(30) NOT NULL,
    status varchar(10) NOT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    modify_date DATETIME ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uid (eid),
    FOREIGN KEY (uid) REFERENCES user(uid) ON UPDATE CASCADE,
    FOREIGN KEY (eid) REFERENCES event(eid) ON UPDATE CASCADE
);

/* TABLE friend */
CREATE TABLE IF NOT EXISTS friend (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uid BIGINT NOT NULL,
    f_uid BIGINT NOT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uid) REFERENCES user(uid) ON UPDATE CASCADE
);

/* TABLE following */
CREATE TABLE IF NOT EXISTS following (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uid BIGINT NOT NULL,
    f_uid BIGINT NOT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uid) REFERENCES user(uid) ON UPDATE CASCADE
);

/* TABLE invite_activity */
CREATE TABLE IF NOT EXISTS invite_event (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

/* TABLE message */
CREATE TABLE IF NOT EXISTS message (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    from_type INT NOT NULL,
    from_id varchar(50) NOT NULL,
    to_type INT NOT NULL,
    to_id varchar(50) NOT NULL,
    title varchar(50) DEFAULT NULL,
    context_type INT DEFAULT NULL,
    context varchar(512) DEFAULT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

/* TABLE user_message */
CREATE TABLE IF NOT EXISTS user_message (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

/* TABLE heart_beat */
CREATE TABLE IF NOT EXISTS heart_beat (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

/* TABLE activity_model */
CREATE TABLE IF NOT EXISTS event_model (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

/* TABLE activity_option */
CREATE TABLE IF NOT EXISTS event_option (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);
