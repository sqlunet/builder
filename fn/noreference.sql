-- mysql
-- this selects fk-pk null reference

-- SET @table1='fn_annosets';
-- SET @col1='cxnid';
-- SET @table2='fn_cxns';
-- SET @col2='cxnid';

-- SET @table1='fn_fegrouprealizations';
-- SET @col1='luid';
-- SET @table2='fn_lexunits';
-- SET @col2='luid';

-- SET @table1='fn_lexunits';
-- SET @col1='incorporatedfeid';
-- SET @table2='fn_fes';
-- SET @col2='feid';

-- SET @table1='fn_lexunits';
-- SET @col1='incorporatedfeid';
-- SET @table2='fn_fes';
-- SET @col2='feid';

-- SET @table1='fn_lexunits';
-- SET @col1='incorporatedfeid';
-- SET @table2='fn_fes';
-- SET @col2='feid';

-- SET @col1='documentid';
-- SET @table2='fn_documents';
-- SET @col2='documentid';

-- SET @table1='fn_governors_annosets';
-- SET @col1='annosetid';
-- SET @table2='fn_annosets';
-- SET @col2='annosetid';

-- SET @table1='fn_grouppatterns_annosets';
-- SET @col1='annosetid';
-- SET @table2='fn_annosets';
-- SET @col2='annosetid';

SET @table1='fn_valenceunits_annosets';
SET @col1='annosetid';
SET @table2='fn_annosets';
SET @col2='annosetid';

SET @sql = CONCAT("SELECT COUNT(*) FROM ",@table1," LEFT JOIN ",@table2," ON ",@table1,".",@col1,"=",@table2,".",@col2," WHERE ",@table1,".",@col1," IS NOT NULL AND ",@table2,".",@col2," IS NULL;");
SET @sql = CONCAT("SELECT * FROM ",@table1," LEFT JOIN ",@table2," ON ",@table1,".",@col1,"=",@table2,".",@col2," WHERE ",@table1,".",@col1," IS NOT NULL AND ",@table2,".",@col2," IS NULL ORDER BY ",@table1,".",@col1,";");
SELECT @sql;

PREPARE stmt FROM  @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
