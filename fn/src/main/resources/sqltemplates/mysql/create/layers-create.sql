CREATE TABLE IF NOT EXISTS ${layers.table} (
    ${layers.layerid} INTEGER NOT NULL,
    ${layers.annosetid} INTEGER NOT NULL,
    ${layers.layertypeid} INTEGER,${layers.rank} INTEGER,
PRIMARY KEY (${layers.layerid}) );

-- ALTER TABLE ${layers.table} ADD COLUMN ${layers.layertype} VARCHAR(6) AFTER ${layers.layertypeid};
