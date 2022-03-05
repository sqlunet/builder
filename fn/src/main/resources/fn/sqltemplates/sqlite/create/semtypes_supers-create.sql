CREATE TABLE IF NOT EXISTS ${semtypes_supers.table} (
${semtypes_supers.semtypeid} INTEGER NOT NULL,
${semtypes_supers.supersemtypeid} INTEGER NOT NULL,
PRIMARY KEY (${semtypes_supers.semtypeid},${semtypes_supers.supersemtypeid}) );
