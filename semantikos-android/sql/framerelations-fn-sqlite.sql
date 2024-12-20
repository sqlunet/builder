ALTER TABLE `fn_framerelations` ADD `type` INTEGER;
ALTER TABLE `fn_framerelations` ADD `gloss` VARCHAR(32);

UPDATE `fn_framerelations` SET `type` = 20 , `gloss` = '%s has %s as subframe'          WHERE `relationid` = 1 ; -- Has Subframe(s)
UPDATE `fn_framerelations` SET `type` = 11 , `gloss` = '%s inherits %s'                 WHERE `relationid` = 2 ; -- Inherits from
UPDATE `fn_framerelations` SET `type` = 60 , `gloss` = '%s is causative of %s'          WHERE `relationid` = 3 ; -- Is Causative of
UPDATE `fn_framerelations` SET `type` = 70 , `gloss` = '%s is inchoative of %s'         WHERE `relationid` = 4 ; -- Is Inchoative of
UPDATE `fn_framerelations` SET `type` = 10 , `gloss` = '%s is inherited by %s'          WHERE `relationid` = 5 ; -- Is Inherited by
UPDATE `fn_framerelations` SET `type` = 51 , `gloss` = '%s is perspectivized in %s'     WHERE `relationid` = 6 ; -- Is Perspectivized in
UPDATE `fn_framerelations` SET `type` = 41 , `gloss` = '%s is preceded by %s'           WHERE `relationid` = 7 ; -- Is Preceded by
UPDATE `fn_framerelations` SET `type` = 31 , `gloss` = '%s is used by %s'               WHERE `relationid` = 8 ; -- Is Used by
UPDATE `fn_framerelations` SET `type` = 50 , `gloss` = '%s perspectivizes %s'           WHERE `relationid` = 9 ; -- Perspective on
UPDATE `fn_framerelations` SET `type` = 40 , `gloss` = '%s precedes %s'                 WHERE `relationid` = 10; -- Precedes
UPDATE `fn_framerelations` SET `type` = 80 , `gloss` = '%s has see-also relation to %s' WHERE `relationid` = 11; -- See also
UPDATE `fn_framerelations` SET `type` = 21 , `gloss` = '%s is subframe of %s'           WHERE `relationid` = 12; -- Subframe of
UPDATE `fn_framerelations` SET `type` = 30 , `gloss` = '%s uses %s'                     WHERE `relationid` = 13; -- Uses

