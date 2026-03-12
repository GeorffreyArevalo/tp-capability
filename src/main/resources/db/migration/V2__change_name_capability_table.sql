DO $$
BEGIN
    IF to_regclass('public.technology') IS NOT NULL
       AND to_regclass('public.capability') IS NULL THEN
        ALTER TABLE technology RENAME TO capability;
    END IF;
END $$;
