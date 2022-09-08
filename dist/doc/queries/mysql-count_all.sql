-- mysql
-- this creates procedure that executes a statement for each table

SET @withnum='[a-z]+[0-9]+.*';
SET SESSION group_concat_max_len = 1000000;

SELECT GROUP_CONCAT(CONCAT('SELECT \'', table_name,'\' table_name,COUNT(*) `rows` FROM ', table_name) SEPARATOR ' UNION ')
INTO @sql 
FROM 
(
SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE() AND table_type = 'BASE TABLE' AND table_name NOT REGEXP @withnum
UNION 
SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE() AND table_type = 'BASE TABLE' AND table_name REGEXP @withnum
) table_list;

PREPARE s FROM  @sql;
EXECUTE s;
DEALLOCATE PREPARE s;

