CREATE TABLE IF NOT EXISTS ${examples.table} (
    ${examples.exampleid} INTEGER NOT NULL,
    ${examples.rolesetid} INTEGER NOT NULL,
    ${examples.examplename} VARCHAR (180) NOT NULL,
    ${examples.text} TEXT,
    ${examples.aspectid} INTEGER NULL,
    ${examples.formid} INTEGER NULL,
    ${examples.tenseid} INTEGER NULL,
    ${examples.voiceid} INTEGER NULL,
    ${examples.personid} INTEGER NULL,
PRIMARY KEY (${examples.exampleid}));
