-- =========================================
-- CadastralService database dummy data
-- PostgreSQL
-- =========================================

INSERT INTO citizens
    (id, full_name, date_of_birth, passport_series, passport_number)
VALUES
    ('11111111-1111-4111-8111-111111111111',
     'Alexander Ivanov', '1985-03-14', 4510, 123456),

    ('22222222-2222-4222-8222-222222222222',
     'Maria Petrova', '1992-07-22', 4512, 654321),

    ('33333333-3333-4333-8333-333333333333',
     'Dmitry Sokolov', '1978-11-05', 4508, 234567),

    ('44444444-4444-4444-8444-444444444444',
     'Elena Smirnova', '2001-01-30', 4515, 345678),

    ('55555555-5555-4555-8555-555555555555',
     'Nikolay Kuznetsov', '1969-09-18', 4502, 456789)
ON CONFLICT (id) DO NOTHING;

INSERT INTO requests
    (id, citizen_id, status, open_at, closed_at)
VALUES
    -- Alexander Ivanov
    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa1',
     '11111111-1111-4111-8111-111111111111',
     'NEW', '2026-01-05 09:30:00+03', NULL),

    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa2',
     '11111111-1111-4111-8111-111111111111',
     'DONE', '2026-01-10 14:15:00+03',
     '2026-01-12 11:00:00+03'),

    -- Maria Petrova
    ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbb1',
     '22222222-2222-4222-8222-222222222222',
     'IN_PROGRESS', '2026-02-01 10:00:00+03', NULL),

    ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbb2',
     '22222222-2222-4222-8222-222222222222',
     'CANCELLED', '2026-02-04 16:30:00+03',
     '2026-02-05 09:45:00+03'),

    -- Dmitry Sokolov
    ('cccccccc-cccc-4ccc-8ccc-ccccccccccc1',
     '33333333-3333-4333-8333-333333333333',
     'DONE', '2026-03-12 08:00:00+03',
     '2026-03-15 12:30:00+03'),

    ('cccccccc-cccc-4ccc-8ccc-ccccccccccc2',
     '33333333-3333-4333-8333-333333333333',
     'NEW', '2026-03-20 13:45:00+03', NULL),

    -- Elena Smirnova
    ('dddddddd-dddd-4ddd-8ddd-ddddddddddd1',
     '44444444-4444-4444-8444-444444444444',
     'IN_PROGRESS', '2026-04-02 11:20:00+03', NULL),

    ('dddddddd-dddd-4ddd-8ddd-ddddddddddd2',
     '44444444-4444-4444-8444-444444444444',
     'DONE', '2026-04-10 15:00:00+03',
     '2026-04-11 10:15:00+03'),

    -- Nikolay Kuznetsov
    ('eeeeeeee-eeee-4eee-8eee-eeeeeeeeeee1',
     '55555555-5555-4555-8555-555555555555',
     'CANCELLED', '2026-05-01 09:00:00+03',
     '2026-05-03 14:00:00+03'),

    ('eeeeeeee-eeee-4eee-8eee-eeeeeeeeeee2',
     '55555555-5555-4555-8555-555555555555',
     'NEW', '2026-05-15 17:30:00+03', NULL)
ON CONFLICT (id) DO NOTHING;

-- Verify data insertion

SELECT COUNT(*) AS citizens_count FROM citizens;
SELECT COUNT(*) AS requests_count FROM requests;

SELECT status, COUNT(*) AS request_count
FROM requests
GROUP BY status
ORDER BY status;