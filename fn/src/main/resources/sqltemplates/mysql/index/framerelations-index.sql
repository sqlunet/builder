CREATE UNIQUE INDEX IF NOT EXISTS unq_${framerelations.table}_${framerelations.relation} ON ${framerelations.table} (${framerelations.relation});
