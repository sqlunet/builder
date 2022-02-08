ALTER TABLE ${frames.table} ADD CONSTRAINT `fk_@{frames.table}_@{frames.framenameid}` FOREIGN KEY (${frames.framenameid}) REFERENCES ${framenames.table} (${framenames.framenameid});
ALTER TABLE ${frames.table} ADD CONSTRAINT `fk_@{frames.table}_@{frames.framesubnameid}` FOREIGN KEY (${frames.framesubnameid}) REFERENCES ${framesubnames.table} (${framesubnames.framesubnameid});
ALTER TABLE ${frames.table} ADD CONSTRAINT `fk_@{frames.table}_@{frames.syntaxid}` FOREIGN KEY (${frames.syntaxid}) REFERENCES ${syntaxes.table} (${syntaxes.syntaxid});
ALTER TABLE ${frames.table} ADD CONSTRAINT `fk_@{frames.table}_@{frames.semanticsid}` FOREIGN KEY (${frames.semanticsid}) REFERENCES ${semantics.table} (${semantics.semanticsid});
