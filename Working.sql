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
INSERT INTO programs (title, description, duration)
VALUES
    ('Зарядка на каждый день', 'Программа для начинающих, включает 10-минутные тренировки каждый день.', 30),
    ('Силовые тренировки', 'Интенсивная программа для набора мышечной массы. Подходит для среднего уровня подготовки.', 60),
    ('Йога для здоровья', 'Йога-комплекс для расслабления и восстановления организма.', 20);

CREATE TABLE progress (
                          id SERIAL PRIMARY KEY,
                          user_id INT,
                          program_id INT,
                          day INT NOT NULL,
                          status BOOLEAN DEFAULT FALSE,
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (program_id) REFERENCES programs(id)
);
ALTER TABLE programs ADD COLUMN created_by INT;
ALTER TABLE programs ADD FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE;

-- Таблица для тренировок
CREATE TABLE workouts (
    id SERIAL PRIMARY KEY,
    program_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    day_number INT NOT NULL,
    FOREIGN KEY (program_id) REFERENCES programs(id) ON DELETE CASCADE
);

-- Таблица для отслеживания прогресса пользователей по тренировкам
CREATE TABLE workout_progress (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    workout_id INT NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    completion_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE,
    UNIQUE(user_id, workout_id)
);
ALTER TABLE users ADD COLUMN photo_path VARCHAR(255);
-- Таблица групп мышц
CREATE TABLE muscle_groups (
                               id SERIAL PRIMARY KEY,
                               name VARCHAR(255) NOT NULL
);

-- Таблица упражнений
CREATE TABLE exercises (
                           id SERIAL PRIMARY KEY,
                           workout_id INT NOT NULL,
                           muscle_group_id INT NOT NULL,
                           name VARCHAR(255) NOT NULL,
                           description TEXT,
                           sets INT NOT NULL CHECK (sets > 0),
                           reps INT NOT NULL CHECK (reps > 0),
                           FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE,
                           FOREIGN KEY (muscle_group_id) REFERENCES muscle_groups(id)
);

-- Базовые группы мышц
INSERT INTO muscle_groups (name) VALUES
                                     ('Грудные мышцы'),
                                     ('Спина'),
                                     ('Ноги'),
                                     ('Плечи'),
                                     ('Руки'),
                                     ('Пресс'),
                                     ('Кардио');

-- Добавляем тренировки для программы "Зарядка на каждый день"
INSERT INTO workouts (program_id, title, description, day_number) VALUES
    (1, 'Утренняя разминка - Понедельник', 'Легкая разминка для всего тела, чтобы начать неделю с энергией', 1),
    (1, 'Растяжка - Вторник', 'Комплекс упражнений для развития гибкости', 2),
    (1, 'Кардио - Среда', 'Кардиотренировка для укрепления сердечно-сосудистой системы', 3),
    (1, 'Силовая - Четверг', 'Легкие силовые упражнения для основных групп мышц', 4),
    (1, 'Йога - Пятница', 'Йога для расслабления и восстановления', 5),
    (1, 'Функциональная тренировка - Суббота', 'Комплексная тренировка для всего тела', 6),
    (1, 'Активный отдых - Воскресенье', 'Легкая растяжка и дыхательные упражнения', 7);

-- Добавляем тренировки для программы "Силовые тренировки"
INSERT INTO workouts (program_id, title, description, day_number) VALUES
    (2, 'Грудь и трицепс', 'Тренировка верхней части тела с акцентом на грудные мышцы и трицепс', 1),
    (2, 'Спина и бицепс', 'Тренировка верхней части тела с акцентом на мышцы спины и бицепс', 2),
    (2, 'Ноги и плечи', 'Тренировка нижней части тела и плечевого пояса', 3),
    (2, 'Отдых и восстановление', 'День активного отдыха и восстановления', 4);

-- Добавляем тренировки для программы "Йога для здоровья"
INSERT INTO workouts (program_id, title, description, day_number) VALUES
    (3, 'Основы йоги', 'Знакомство с базовыми асанами и дыхательными техниками', 1),
    (3, 'Сурья Намаскар', 'Комплекс "Приветствие солнцу" для утренней практики', 2),
    (3, 'Баланс и гибкость', 'Асаны для развития равновесия и гибкости', 3),
    (3, 'Медитация и расслабление', 'Техники медитации и глубокого расслабления', 4);

-- Добавляем упражнения для тренировки "Утренняя разминка - Понедельник"
INSERT INTO exercises (workout_id, muscle_group_id, name, description, sets, reps) VALUES
    (1, 7, 'Прыжки на месте', 'Прыжки с поднятием рук вверх', 3, 20),
    (1, 3, 'Приседания', 'Классические приседания с собственным весом', 3, 15),
    (1, 6, 'Скручивания', 'Упражнение для мышц пресса', 3, 20),
    (1, 2, 'Отжимания от пола', 'Классические отжимания', 3, 10);

-- Добавляем упражнения для тренировки "Грудь и трицепс"
INSERT INTO exercises (workout_id, muscle_group_id, name, description, sets, reps) VALUES
    (8, 1, 'Жим гантелей лежа', 'Жим гантелей лежа на скамье', 4, 12),
    (8, 5, 'Разгибания на трицепс', 'Разгибания рук на трицепс с гантелями', 3, 15),
    (8, 1, 'Отжимания на брусьях', 'Отжимания на параллельных брусьях', 3, 12),
    (8, 5, 'Французский жим', 'Французский жим лежа с гантелей', 3, 12);

-- Добавляем упражнения для тренировки "Основы йоги"
INSERT INTO exercises (workout_id, muscle_group_id, name, description, sets, reps) VALUES
    (12, 6, 'Поза собаки мордой вниз', 'Адхо Мукха Шванасана', 1, 5),
    (12, 2, 'Поза кошки-коровы', 'Марджариасана-Битиласана', 1, 10),
    (12, 3, 'Поза воина 1', 'Вирабхадрасана I', 1, 5),
    (12, 6, 'Поза ребенка', 'Баласана', 1, 3);

ALTER TABLE programs DROP COLUMN IF EXISTS image_path;
