CREATE UNIQUE INDEX unq_${roles.table}_${roles.roletypeid}_${roles.restrsid} ON ${roles.table} (${roles.roletypeid},${roles.restrsid});
CREATE INDEX k_${roles.table}_${roles.roletypeid} ON ${roles.table} (${roles.roletypeid});
CREATE INDEX k_${roles.table}_${roles.restrsid} ON ${roles.table} (${roles.restrsid});
