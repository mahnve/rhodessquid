-- name: create-key<!
-- Insert a new key with translations.
INSERT INTO keys (key_name) VALUES (:key)

-- name: create-lang
-- Insert a new lang
INSERT INTO lang_codes (lang_code) VALUES (:lang)

-- name: create-phrase!
-- Insert a new phrase
INSERT INTO phrases (key_id, lang_code_id, phrase) VALUES (
        (SELECT k.id FROM keys k WHERE k.key_name=:key),
        (SELECT l.id FROM lang_codes l WHERE l.lang_code=:lang),
        :phrase)

-- name: update-phrase!
-- Update existing phrase
UPDATE phrases
SET phrase = :phrase
WHERE key_id = (SELECT id FROM keys WHERE key_name = :key)
AND lang_code_id = (SELECT id FROM lang_codes WHERE lang_code = :lang)

-- name: find-phrase
-- returns the translation for key/lang pair.
SELECT p.phrase
FROM phrases p
INNER JOIN keys k ON p.key_id = k.id
INNER JOIN lang_codes l ON p.lang_code_id = l.id
WHERE k.key_name = :key AND l.lang_code = :lang

-- name: all-phrases
SELECT k.key_name, l.lang_code, p.phrase
FROM phrases p
INNER JOIN keys k ON p.key_id = k.id
INNER JOIN lang_codes l ON p.lang_code_id = l.id
