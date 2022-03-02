UPDATE ${words.table} AS f INNER JOIN ${wnwords.table} AS w ON f.${words.word} = w.${wnwords.word}SET f.${words.wordid} = w.${wnwords.wordid};
