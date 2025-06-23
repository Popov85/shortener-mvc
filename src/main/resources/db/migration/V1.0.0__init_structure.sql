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


CREATE TABLE shortener.short_codes
(
    short_code           VARCHAR(12) PRIMARY KEY,                                                                         -- Human-readable or generated code
    original_url         TEXT                                                                                   NOT NULL, -- Long URL
    original_url_hash    BYTEA                                                                                  NOT NULL, -- Long URL sha256 hash
    created_by           UUID REFERENCES shortener.customer (id) DEFAULT '11111111-1111-1111-1111-111111111111' NOT NULL, -- Link owner
    expiration_date_time TIMESTAMP                               DEFAULT (now() + interval '1 year'),                     -- Expiry, default to 1 year after creating
    created_date_time    TIMESTAMP                               DEFAULT now()                                  NOT NULL  -- Used for partitioning
) PARTITION BY HASH (short_code);

-- Partitions of short_codes
CREATE TABLE shortener.short_codes_p0 PARTITION OF shortener.short_codes
    FOR VALUES WITH (MODULUS 3, REMAINDER 0);

CREATE TABLE shortener.short_codes_p1 PARTITION OF shortener.short_codes
    FOR VALUES WITH (MODULUS 3, REMAINDER 1);

CREATE TABLE shortener.short_codes_p2 PARTITION OF shortener.short_codes
    FOR VALUES WITH (MODULUS 3, REMAINDER 2);


-- Partition 0 indexes of short_codes
CREATE INDEX idx_short_codes_p0_expiration_date_time ON shortener.short_codes_p0 (expiration_date_time);
CREATE UNIQUE INDEX idx_short_codes_p0_original_url_hash ON shortener.short_codes_p0 (original_url_hash);

-- Partition 1 indexes of short_codes
CREATE INDEX idx_short_codes_p1_expiration_date_time ON shortener.short_codes_p1 (expiration_date_time);
CREATE UNIQUE INDEX idx_short_codes_p1_original_url_hash ON shortener.short_codes_p1 (original_url_hash);

-- Partition 2 indexes of short_codes
CREATE INDEX idx_short_codes_p2_expiration_date_time ON shortener.short_codes_p2 (expiration_date_time);
CREATE UNIQUE INDEX idx_short_codes_p2_original_url_hash ON shortener.short_codes_p2 (original_url_hash);


-- Used only for checking for existence
CREATE TABLE shortener.original_urls
(
    original_url_hash    BYTEA PRIMARY KEY,                                                                               -- SHA-256 hash of original_url; SHA-256 produces a 32-byte binary digest (not a string)                                                                      -- Human-readable or generated code
    short_code           VARCHAR(12)                                                                            NOT NULL, --Short code associated with long URL
    created_by           UUID REFERENCES shortener.customer (id) DEFAULT '11111111-1111-1111-1111-111111111111' NOT NULL, -- Link owner
    expiration_date_time TIMESTAMP                               DEFAULT (now() + interval '1 year'),                     -- Expiry, default to one year after creating
    created_date_time    TIMESTAMP                               DEFAULT now()                                  NOT NULL  -- Used for partitioning
) PARTITION BY HASH (original_url_hash);

-- Partitions of original_urls
CREATE TABLE shortener.original_urls_p0 PARTITION OF shortener.original_urls
    FOR VALUES WITH (MODULUS 3, REMAINDER 0);

CREATE TABLE shortener.original_urls_p1 PARTITION OF shortener.original_urls
    FOR VALUES WITH (MODULUS 3, REMAINDER 1);

CREATE TABLE shortener.original_urls_p2 PARTITION OF shortener.original_urls
    FOR VALUES WITH (MODULUS 3, REMAINDER 2);

-- Partition 0 indexes of original_urls
CREATE INDEX idx_original_urls_p0_expiration_date_time ON shortener.original_urls_p0 (expiration_date_time);
CREATE UNIQUE INDEX idx_original_urls_p0_short_code ON shortener.original_urls_p0 (short_code);

-- Partition 1 indexes of short_codes
CREATE INDEX idx_original_urls_p1_expiration_date_time ON shortener.original_urls_p1 (expiration_date_time);
CREATE UNIQUE INDEX idx_original_urls_p1_short_code ON shortener.original_urls_p1 (short_code);

-- Partition 2 indexes of short_codes
CREATE INDEX idx_original_urls_p2_expiration_date_time ON shortener.original_urls_p2 (expiration_date_time);
CREATE UNIQUE INDEX idx_original_urls_p2_short_code ON shortener.original_urls_p2 (short_code);