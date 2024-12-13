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
    (1, 'Кардио - Среда', 'Кардио-тренировка для укрепления сердечно-сосудистой системы', 3),
    (1, 'Силовая - Четверг', 'Базовые упражнения для укрепления основных групп мышц', 4),
    (1, 'Пресс и спина - Пятница', 'Упражнения для укрепления кора и правильной осанки', 5),
    (1, 'Интервальная тренировка - Суббота', 'Чередование интенсивных упражнений и отдыха', 6),
    (1, 'Йога - Воскресенье', 'Расслабляющие упражнения для восстановления', 7);

-- Добавляем упражнения для каждой тренировки
-- Понедельник
INSERT INTO exercises (workout_id, muscle_group_id, name, description, sets, reps) VALUES
    (1, 7, 'Прыжки на месте', 'Прыжки с подъемом рук вверх', 3, 20),
    (1, 3, 'Приседания', 'Классические приседания с собственным весом', 3, 15),
    (1, 5, 'Отжимания с колен', 'Облегченный вариант отжиманий', 2, 10);

-- Вторник
INSERT INTO exercises (workout_id, muscle_group_id, name, description, sets, reps) VALUES
    (2, 2, 'Наклоны вперед', 'Растяжка задней поверхности бедра', 3, 10),
    (2, 4, 'Круговые движения руками', 'Разминка плечевого пояса', 2, 15),
    (2, 3, 'Выпады', 'Статические выпады для растяжки ног', 3, 12);

-- Среда
INSERT INTO exercises (workout_id, muscle_group_id, name, description, sets, reps) VALUES
    (3, 7, 'Бег на месте', 'Высокое поднимание колен', 4, 30),
    (3, 7, 'Прыжки со скакалкой', 'Базовые прыжки через скакалку', 3, 50),
    (3, 6, 'Скручивания', 'Простые скручивания лежа на спине', 3, 15);

-- Четверг
INSERT INTO exercises (workout_id, muscle_group_id, name, description, sets, reps) VALUES
    (4, 1, 'Отжимания', 'Классические отжимания от пола', 3, 10),
    (4, 3, 'Приседания с выпрыгиванием', 'Динамические приседания', 3, 12),
    (4, 5, 'Подтягивания в наклоне', 'Подтягивания к столу или низкой перекладине', 3, 12);

-- Пятница
INSERT INTO exercises (workout_id, muscle_group_id, name, description, sets, reps) VALUES
    (5, 6, 'Планка', 'Удержание планки', 3, 30),
    (5, 2, 'Супермен', 'Упражнение для мышц спины лежа на животе', 3, 15),
    (5, 6, 'Велосипед', 'Скручивания с поочередным подтягиванием колен', 3, 20);

-- Суббота
INSERT INTO exercises (workout_id, muscle_group_id, name, description, sets, reps) VALUES
    (6, 7, 'Берпи', 'Комплексное упражнение', 4, 8),
    (6, 3, 'Приседания с выпадами', 'Чередование приседаний и выпадов', 3, 16),
    (6, 6, 'Подъемы ног', 'Подъемы ног лежа на спине', 3, 15);

-- Воскресенье
INSERT INTO exercises (workout_id, muscle_group_id, name, description, sets, reps) VALUES
    (7, 2, 'Поза кошки-коровы', 'Плавные прогибы спины', 3, 10),
    (7, 3, 'Поза собаки мордой вниз', 'Растяжка всего тела', 3, 30),
    (7, 6, 'Скручивания в положении лежа', 'Мягкие скручивания для позвоночника', 2, 15);
