-- name: insert-word
-- Insert a new key with translations.
insert into phrases (key, translations) values (:key, :translations);

-- name: find-translation
-- returns the translation for key/lang pair.
select translations->:lang as translation from phrases where key=:key;
