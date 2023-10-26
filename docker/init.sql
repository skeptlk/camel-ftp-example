-- create schedule table
CREATE TABLE IF NOT EXISTS SCHEDULE (
    id SERIAL PRIMARY KEY,
    flight VARCHAR(256),
    company VARCHAR(256),
    time VARCHAR(256),
    arr VARCHAR(256),
    dep VARCHAR(256)
);
