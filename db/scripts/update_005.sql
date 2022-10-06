CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  email varchar(255),
  password TEXT
);