CREATE TABLE telemetry_events (
    id BIGSERIAL PRIMARY KEY,
    imei VARCHAR(20) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    altitude INTEGER NOT NULL,
    angle INTEGER NOT NULL,
    satellites INTEGER NOT NULL,
    speed INTEGER NOT NULL
);