DROP TABLE IF EXISTS `pm_vn`;
CREATE TABLE IF NOT EXISTS `pm_vn` (`pmid` INTEGER PRIMARY KEY,`wordid` INTEGER,`synsetid` INTEGER,`vnwordid` INTEGER,`classid` INTEGER);
INSERT INTO `pm_vn` (`pmid`,`wordid`,`synsetid`,`vnwordid`,`classid`) SELECT `pmid`,`wordid`,`synsetid`,`vnwordid`,`vnclassid` FROM `pm_pms` WHERE `vnclassid` IS NOT NULL;
DELETE FROM `pm_vn` WHERE `pmid` NOT IN (SELECT MIN(`pmid`) FROM `pm_vn` GROUP BY `wordid`,`synsetid`,`vnwordid`,`classid`);

DROP TABLE IF EXISTS `pm_pb`;
CREATE TABLE IF NOT EXISTS `pm_pb` (`pmid` INTEGER PRIMARY KEY,`wordid` INTEGER,`synsetid` INTEGER,`pbwordid` INTEGER,`rolesetid` INTEGER);
INSERT INTO `pm_pb` (`pmid`,`wordid`,`synsetid`,`pbwordid`,`rolesetid`) SELECT `pmid`,`wordid`,`synsetid`,`pbwordid`,`pbrolesetid` FROM `pm_pms` WHERE `pbrolesetid` IS NOT NULL;
DELETE FROM `pm_pb` WHERE `pmid` NOT IN (SELECT MIN(`pmid`) FROM `pm_pb` GROUP BY `wordid`,`synsetid`,`pbwordid`,`rolesetid`);

DROP TABLE IF EXISTS `pm_fn`;
CREATE TABLE IF NOT EXISTS `pm_fn` (`pmid` INTEGER PRIMARY KEY,`wordid` INTEGER,`synsetid` INTEGER,`fnwordid` INTEGER,`luid` INTEGER,`frameid` INTEGER);
INSERT INTO `pm_fn` (`pmid`,`wordid`,`synsetid`,`fnwordid`,`luid`,`frameid`) SELECT `pmid`,`wordid`,`synsetid`,`fnwordid`,`fnluid`,`fnframeid` FROM `pm_pms` WHERE `fnframeid` IS NOT NULL;
DELETE FROM `pm_fn` WHERE `pmid` NOT IN (SELECT MIN(`pmid`) FROM `pm_fn` GROUP BY `wordid`,`synsetid`,`fnwordid`,`luid`,`frameid`);

