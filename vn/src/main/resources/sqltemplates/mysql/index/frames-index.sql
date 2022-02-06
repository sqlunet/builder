CREATE UNIQUE INDEX unq_${frames.table}_${frames.number}_${frames.xtag}_${frames.framenameid}_${frames.framesubnameid}_${frames.syntaxid}_${frames.semanticsid} ON ${frames.table} (${frames.number},${frames.xtag},${frames.framenameid},${frames.framesubnameid},${frames.syntaxid},${frames.semanticsid});
CREATE INDEX k_${frames.table}_${frames.framenameid} ON ${frames.table} (${frames.framenameid});
CREATE INDEX k_${frames.table}_${frames.framesubnameid} ON ${frames.table} (${frames.framesubnameid});
CREATE INDEX k_${frames.table}_${frames.syntaxid} ON ${frames.table} (${frames.syntaxid});
CREATE INDEX k_${frames.table}_${frames.semanticsid} ON ${frames.table} (${frames.semanticsid});
