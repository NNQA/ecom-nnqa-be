ALTER TABLE users
    ADD COLUMN email_verified_at      TIMESTAMPTZ,
    ADD COLUMN failed_login_attempts  INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN locked_until           TIMESTAMPTZ,
    ADD COLUMN password_changed_at    TIMESTAMPTZ,
    ADD COLUMN last_login_at          TIMESTAMPTZ;

ALTER TABLE users
    ADD CONSTRAINT ck_users_failed_login_attempts
        CHECK (failed_login_attempts >= 0);