${fetypes}.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_${fetypes.table}_${fetype} ON ${fetypes.table} (${fetype});
${fetypes}.no-unq1=DROP INDEX IF EXISTS unq_${fetypes.table}_${fetype};
