CREATE TABLE events
(
    id          SERIAL PRIMARY KEY,
    upload_time timestamp,
    user_id     integer references users (id),
    file_id     integer references files (id) ON DELETE CASCADE
)