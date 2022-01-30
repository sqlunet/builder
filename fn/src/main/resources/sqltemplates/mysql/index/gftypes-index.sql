${gftypes}.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_${gftypes.table}_${gf} ON ${gftypes.table} (${gf});
${gftypes}.no-unq1=DROP INDEX IF EXISTS unq_${gftypes.table}_${gf};
