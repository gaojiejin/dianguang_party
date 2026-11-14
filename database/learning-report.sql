CREATE TABLE IF NOT EXISTS learning_report (
 report_id VARCHAR(80) PRIMARY KEY,
 user_id INT NOT NULL, class_id INT NOT NULL, chapter_id INT NOT NULL,
 if_learned INT NOT NULL, learn_duration INT NOT NULL,
 result VARCHAR(255), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 INDEX idx_report_user(user_id,created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
