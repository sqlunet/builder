CREATE TABLE ${formulas_args.table} (
${formulas_args.formulaid} INT NOT NULL,
${formulas_args.termid} INT NOT NULL,
${formulas_args.parsetype} CHARACTER (1) CHECK( ${formulas_args.parsetype} IN ('a','s','p','c') ) NULL,
${formulas_args.argnum} INT DEFAULT NULL
);
