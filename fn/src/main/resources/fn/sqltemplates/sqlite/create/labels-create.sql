CREATE TABLE IF NOT EXISTS ${labels.table} (
${labels.labelid} INTEGER NOT NULL,
${labels.layerid} INTEGER,
${labels.labeltypeid} INTEGER DEFAULT NULL,
${labels.labelitypeid} INTEGER DEFAULT NULL,
${labels.feid} INTEGER DEFAULT NULL,
${labels.start} INTEGER DEFAULT NULL,
${labels.end} INTEGER DEFAULT NULL
);
