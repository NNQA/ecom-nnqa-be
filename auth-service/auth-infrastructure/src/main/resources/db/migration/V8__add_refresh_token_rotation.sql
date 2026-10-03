ALTER TABLE refresh_tokens ADD COLUMN replaced_by_token_id BIGINT NULL;
ALTER TABLE refresh_tokens ADD CONSTRAINT fk_refresh_tokens_replaced_by FOREIGN KEY (replaced_by_token_id) REFERENCES refresh_tokens(id);
CREATE INDEX idx_refresh_tokens_replaced_by ON refresh_tokens (replaced_by_token_id);
