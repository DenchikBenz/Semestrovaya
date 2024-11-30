ALTER TABLE users
    ADD CONSTRAINT role_check CHECK (role IN ('USER', 'ADMIN'));

DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
                       date_registered TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_programs (
                               user_id INT NOT NULL,
                               program_id INT NOT NULL,
                               PRIMARY KEY (user_id, program_id),
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               FOREIGN KEY (program_id) REFERENCES programs(id) ON DELETE CASCADE
)
;
CREATE TABLE programs (
                          id SERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          description TEXT NOT NULL,
                          duration INT NOT NULL
);
CREATE TABLE progress (
                          id SERIAL PRIMARY KEY,
                          user_id INT,
                          program_id INT,
                          day INT NOT NULL,
                          status BOOLEAN DEFAULT FALSE,
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (program_id) REFERENCES programs(id)
);