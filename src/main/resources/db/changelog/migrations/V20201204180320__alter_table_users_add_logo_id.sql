ALTER TABLE users
    ADD COLUMN logo_id BIGINT NULL;
ALTER TABLE users
    ADD CONSTRAINT fk_logo_id
        FOREIGN KEY (logo_id)
            REFERENCES attachments (attachment_id);
