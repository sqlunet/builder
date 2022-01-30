${framerelations}.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_${framerelations.table}_${relation} ON ${framerelations.table} (${relation});
${framerelations}.no-unq1=DROP INDEX IF EXISTS unq_${framerelations.table}_${relation};
