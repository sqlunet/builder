CREATE TABLE IF NOT EXISTS ${frames_semtypes.table} (
    ${frames_semtypes.frameid} INTEGER NOT NULL,
    ${frames_semtypes.semtypeid} INTEGER NOT NULL,
PRIMARY KEY (${frames_semtypes.frameid},${frames_semtypes.semtypeid}) );
