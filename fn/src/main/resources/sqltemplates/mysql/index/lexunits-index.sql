${lexunits}.index1=CREATE INDEX IF NOT EXISTS k_${lexunits.table}_${frameid} ON ${lexunits.table} (${frameid});
${lexunits}.no-index1=DROP INDEX IF EXISTS k_${lexunits.table}_${frameid};
