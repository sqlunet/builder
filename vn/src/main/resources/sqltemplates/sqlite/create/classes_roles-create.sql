CREATE TABLE IF NOT EXISTS ${classes_roles.table} (
    -- ${classes_roles.class_role_id} INTEGER NOT NULL,
    ${classes_roles.classid} INTEGER NOT NULL,
    ${classes_roles.roleid} INTEGER NOT NULL,
-- PRIMARY KEY (${classes_roles.class_role_id}),
PRIMARY KEY (${classes_roles.classid},${classes_roles.roleid}));
