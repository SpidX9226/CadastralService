-- =========================================
-- CadastralService database initialization
-- PostgreSQL
-- =========================================

CREATE TABLE IF NOT EXISTS citizens (
    id UUID PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    date_of_birth DATE NOT NULL,
    passport_series INTEGER NOT NULL,
    passport_number INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS requests (
    id UUID PRIMARY KEY,
    citizen_id UUID NOT NULL REFERENCES citizens(id),
    status VARCHAR(20) NOT NULL
        CHECK (status IN ('NEW', 'IN_PROGRESS', 'DONE', 'CANCELLED')),
    open_at TIMESTAMPTZ NOT NULL,
    closed_at TIMESTAMPTZ,

    CHECK (
        (status IN ('NEW', 'IN_PROGRESS') AND closed_at IS NULL)
        OR
        (status IN ('DONE', 'CANCELLED') AND closed_at IS NOT NULL)
    ),
    CHECK (closed_at IS NULL OR closed_at >= open_at)
);

CREATE INDEX IF NOT EXISTS idx_requests_citizen_id
    ON requests (citizen_id);

CREATE INDEX IF NOT EXISTS idx_requests_status
    ON requests (status);

CREATE INDEX IF NOT EXISTS idx_requests_open_at
    ON requests (open_at);

CREATE INDEX IF NOT EXISTS idx_citizens_full_name
    ON citizens (full_name);