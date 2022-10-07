CREATE TABLE ${formulas_args.table} (
  ${formulas_args.formulaid} INT NOT NULL,
  ${formulas_args.termid} INT NOT NULL,
  ${formulas_args.argtype} ENUM('a','s','p','c') NULL,
  ${formulas_args.argnum} INT DEFAULT NULL
);
