#!/bin/bash

export umls="uml-wordnet1 uml-wordnet2 uml-verbnet uml-propbank uml-framenet1 uml-framenet2 uml-bnc uml-sumo uml-predicatematrix"
export schemas="schemas-wordnet1 schemas-wordnet2 schemas-verbnet schemas-propbank1 schemas-propbank2 schemas-framenet1 schemas-framenet2 schemas-framenet3 schemas-framenet4 schemas-bnc schemas-sumo schemas-predicatematrix1 schemas-predicatematrix2 schemas-predicatematrix3"
export fks="fks-wordnet fks-verbnet fks-propbank fks-framenet1 fks-framenet2 fks-bnc fks-sumo fks-predicatematrix"
export tables="tables-wordnet tables-verbnet tables-propbank tables-framenet tables-bnc tables-sumo tables-predicatematrix"

export files="${umls} ${schemas} ${tables} ${fks}"
export domains="wordnet verbnet propbank framenet bnc sumo predicatematrix"
