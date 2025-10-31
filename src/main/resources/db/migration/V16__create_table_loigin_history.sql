-- CREATE TABLE history_login (
--                                id VARCHAR(36) NOT NULL,
--                                user_id VARCHAR(36) NOT NULL,
--                                login_at DATETIME(6) NOT NULL,
--                                ip_address VARCHAR(45) NOT NULL,
--                                user_agent VARCHAR(255),
--                                location VARCHAR(100),
--
--                                PRIMARY KEY (id),
--
--                                CONSTRAINT fk_history_login_user
--                                    FOREIGN KEY (user_id)
--                                        REFERENCES users(id)
--                                        ON DELETE CASCADE,
--
--                                INDEX idx_history_login_username (user_id),
--                                INDEX idx_history_login_loginAt (login_at)
-- );

# alter table users
# modify column password varchar(60) default null;

# alter  table history_login
# add column logout_at datetime(6) null after login_at,
# add column device varchar(100) null after user_agent;