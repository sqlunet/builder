${lexemes}.index1=CREATE INDEX IF NOT EXISTS k_${lexemes.table}_${luid} ON ${lexemes.table} (${luid});
${lexemes}.no-index1=DROP INDEX IF EXISTS k_${lexemes.table}_${luid};
${lexemes}.index2=CREATE INDEX IF NOT EXISTS k_${lexemes.table}_${fnwordid} ON ${lexemes.table} (${fnwordid});
${lexemes}.no-index2=DROP INDEX IF EXISTS k_${lexemes.table}_${fnwordid};
