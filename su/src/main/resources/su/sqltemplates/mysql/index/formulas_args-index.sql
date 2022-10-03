ALTER TABLE ${formulas_args.table} ADD KEY `k_@{formulas_args.table}_@{formulas_args.formulaid}` (${formulas_args.formulaid});
ALTER TABLE ${formulas_args.table} ADD KEY `k_@{formulas_args.table}_@{formulas_args.termid}` (${formulas_args.termid});
ALTER TABLE ${formulas_args.table} ADD KEY `k_@{formulas_args.table}_@{formulas_args.parsetype}` (${formulas_args.parsetype});
