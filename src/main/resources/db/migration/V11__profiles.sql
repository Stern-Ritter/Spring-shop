SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS profiles;

CREATE TABLE profiles (
    id  INT(11) NOT NULL AUTO_INCREMENT,
    user_id INT(11) NOT NULL REFERENCES users(id),
    information VARCHAR (255),
    PRIMARY KEY (id),
    CONSTRAINT FK_PROFILE_USER_ID FOREIGN KEY (user_id)
    REFERENCES users(id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO profiles (user_id, information)
VALUES
(1, 'Информация о пользователе');

SET FOREIGN_KEY_CHECKS = 1;