SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS seat;
DROP TABLE IF EXISTS screening;
DROP TABLE IF EXISTS theatre;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS otp_verification;

SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE movie (
    id BIGINT UNSIGNED PRIMARY KEY,
    title VARCHAR(100),
    certificate VARCHAR(10),
    duration VARCHAR(20),
    language VARCHAR(50),
    poster VARCHAR(255)
);

CREATE TABLE theatre (
    id BIGINT UNSIGNED PRIMARY KEY,
    name VARCHAR(100),
    location VARCHAR(100)
);

CREATE TABLE screening (
    id BIGINT UNSIGNED PRIMARY KEY,
    movie_id BIGINT UNSIGNED,
    theatre_id BIGINT UNSIGNED,
    show_time DATETIME,
    CONSTRAINT fk_movie FOREIGN KEY (movie_id) REFERENCES movie(id),
    CONSTRAINT fk_theatre FOREIGN KEY (theatre_id) REFERENCES theatre(id)
);

CREATE TABLE seat (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    screening_id BIGINT UNSIGNED NOT NULL,
    seat_row CHAR(1),      -- A–O
    seat_col INT,          -- 1–25
    is_booked BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_seat_screening FOREIGN KEY (screening_id) REFERENCES screening(id)
);

CREATE TABLE booking (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    seats_booked INT NOT NULL,
    seats VARCHAR(255) NOT NULL,
    booked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    screening_id BIGINT UNSIGNED NOT NULL,
    qr_code TEXT,
    movie_name VARCHAR(255),
    theatre_name VARCHAR(255),
    show_time DATETIME,
    CONSTRAINT fk_booking_screening FOREIGN KEY (screening_id) REFERENCES screening(id)
);


ALTER TABLE seat MODIFY seat_row CHAR(1);
ALTER TABLE seat MODIFY seat_col INT;

CREATE TABLE IF NOT EXISTS otp_verification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(15),
    otp VARCHAR(6),
    expiry_time DATETIME,
    verified BOOLEAN DEFAULT FALSE
);
