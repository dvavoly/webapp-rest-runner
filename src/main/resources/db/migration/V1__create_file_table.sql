CREATE TABLE files
(
    id                SERIAL PRIMARY KEY,
    file_name         VARCHAR(255),
    file_type         VARCHAR(255),
    file_download_url VARCHAR(255)
)