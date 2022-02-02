UPDATE
    ${fnwords.table} AS f
    INNER JOIN ${words.table} AS w ON f.${fnwords.word} = w.${words.word}
SET f.${fnwords.wordid} = w.${words.wordid};