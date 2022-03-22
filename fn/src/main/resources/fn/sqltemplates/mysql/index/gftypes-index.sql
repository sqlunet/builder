ALTER TABLE ${gftypes.table} ADD CONSTRAINT `pk_@{gftypes.table}` PRIMARY KEY (${gftypes.gfid});
ALTER TABLE ${gftypes.table} ADD CONSTRAINT `uk_@{gftypes.table}_@{gftypes.gf}` UNIQUE KEY (${gftypes.gf});
