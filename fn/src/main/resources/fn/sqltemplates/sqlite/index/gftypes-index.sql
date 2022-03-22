CREATE UNIQUE INDEX `pk_@{gftypes.table}` ON ${gftypes.table} (${gftypes.gfid});
CREATE UNIQUE INDEX `uk_@{gftypes.table}_@{gftypes.gf}` ON ${gftypes.table} (${gftypes.gf});
