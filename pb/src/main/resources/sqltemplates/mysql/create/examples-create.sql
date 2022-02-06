CREATE TABLE IF NOT EXISTS ${examples.table} (
    ${examples.exampleid} INTEGER NOT NULL,
    ${examples.rolesetid} INTEGER NOT NULL,
    ${examples.examplename} VARCHAR (180) NOT NULL,
    ${examples.text} TEXT,
    ${examples.aspect} INTEGER NULL,
    ${examples.form} INTEGER NULL,
    ${examples.tense} INTEGER NULL,
    ${examples.voice} INTEGER NULL,
    ${examples.person} INTEGER NULL,
PRIMARY KEY (${examples.exampleid}));
