CREATE SCHEMA shortener;

CREATE TABLE shortener.customer
(
    id   UUID UNIQUE PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

INSERT INTO shortener.customer (id, name)
VALUES ('11111111-1111-1111-1111-111111111111', 'Default Customer'),
       ('22222222-2222-2222-2222-222222222222', 'VIP Customer'),
       ('33333333-3333-3333-3333-333333333333', 'Anonymous');


CREATE TABLE shortener.urls
(
    short_code           VARCHAR(12) PRIMARY KEY,                                                                         -- Human-readable or generated code
    original_url         TEXT                                                                                   NOT NULL, -- Long URL
    original_url_hash    BYTEA                                                                                  NOT NULL, -- SHA-256 hash of original_url; SHA-256 produces a 32-byte binary digest (not a string)
    created_by           UUID REFERENCES shortener.customer (id) DEFAULT '11111111-1111-1111-1111-111111111111' NOT NULL, -- Link owner
    expiration_date_time TIMESTAMP,                                                                                       -- Optional expiry
    created_date_time    TIMESTAMP                                                                              NOT NULL  -- Used for partitioning
);

CREATE INDEX idx_expiration_date_time ON shortener.urls (expiration_date_time);
CREATE UNIQUE INDEX idx_original_url_hash ON shortener.urls (original_url_hash);


