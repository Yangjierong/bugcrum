# create tables fro scrum integration

CREATE TABLE scrum_sprint (
  id mediumint(9) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  description mediumtext NOT NULL,
  start_date datetime NOT NULL,
  end_date datetime NOT NULL,
  product_id smallint(6) NOT NULL,
  goals mediumtext DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

CREATE TABLE scrum_sprint_bug (
  id mediumint(9) NOT NULL AUTO_INCREMENT,
  sprint_id mediumint(9) NOT NULL,
  bug_id mediumint(9) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;