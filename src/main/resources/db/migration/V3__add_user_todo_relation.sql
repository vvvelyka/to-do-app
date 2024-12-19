DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM information_schema.columns
                       WHERE table_name = 'todo'
                         AND column_name = 'user_id') THEN
            ALTER TABLE todo
                ADD COLUMN user_id BIGINT;

        END IF;
    END
$$;

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM information_schema.table_constraints
                       WHERE table_name = 'todo' AND constraint_name = 'todo_user_id_fk') THEN
            ALTER TABLE todo
                ADD CONSTRAINT todo_user_id_fk
                    FOREIGN KEY (user_id) REFERENCES "user" (id);
        END IF;
    END
$$;