${fes}.index1=CREATE INDEX IF NOT EXISTS k_${fes.table}_${frameid} ON ${fes.table} (${frameid});
${fes}.no-index1=DROP INDEX IF EXISTS k_${fes.table}_${frameid};
${fes}.index2=CREATE INDEX IF NOT EXISTS k_${fes.table}_${fetypeid} ON ${fes.table} (${fetypeid});
${fes}.no-index2=DROP INDEX IF EXISTS k_${fes.table}_${fetypeid};
