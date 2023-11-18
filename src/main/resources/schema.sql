DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

GRANT ALL ON SCHEMA public TO shareiter;
GRANT ALL ON SCHEMA public TO public;

CREATE TABLE IF NOT EXISTS users (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
email VARCHAR(320),
name VARCHAR(100),

UNIQUE(email)
);

CREATE TABLE IF NOT EXISTS requests (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
description VARCHAR(500),
requester_id BIGINT,
created timestamp,

CONSTRAINT fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS items (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name VARCHAR(100),
description VARCHAR(500),
is_available BOOLEAN,
owner_id BIGINT,
request_id BIGINT,

CONSTRAINT fk_items_to_users FOREIGN KEY(owner_id) REFERENCES users(id),
CONSTRAINT fk_items_to_requests FOREIGN KEY(request_id) REFERENCES requests(id)
);

--DROP TYPE IF EXISTS status;
--CREATE TYPE status AS ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');

CREATE TABLE IF NOT EXISTS bookings (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
start_date timestamp,
end_date timestamp,
item_id BIGINT,
booker_id BIGINT,
--status_current status,
status_current VARCHAR(500),

CONSTRAINT fk_bookings_to_users FOREIGN KEY(booker_id) REFERENCES users(id),
CONSTRAINT fk_bookings_to_items FOREIGN KEY(item_id) REFERENCES items(id)

);

CREATE TABLE IF NOT EXISTS comments (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
text VARCHAR(1000),
item_id BIGINT,
author_id BIGINT,
created timestamp,

CONSTRAINT fk_comments_to_users FOREIGN KEY(author_id) REFERENCES users(id),
CONSTRAINT fk_comments_to_items FOREIGN KEY(item_id) REFERENCES items(id)
);