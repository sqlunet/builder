#/bin/bash

./_sizes.py sqlunet.sqlite 		db/sqlunet.db 			db/sqlunet.db.zip 			samples_sample synsets_definition words_word vn_examples_example vn_words_word pb_examples_text pb_words_word fn_sentences_text
read
./_sizes.py sqlunet-ewn.sqlite 	db-ewn/sqlunet-ewn.db 	db-ewn/sqlunet-ewn.db.zip 	samples_sample synsets_definition words_word
read
./_sizes.py sqlunet-wn.sqlite 	db-wn/sqlunet-wn.db 	db-wn/sqlunet-wn.db.zip 	samples_sample synsets_definition words_word
read
./_sizes.py sqlunet-vn.sqlite 	db-vn/sqlunet-vn.db 	db-vn/sqlunet-vn.db.zip 	samples_sample synsets_definition words_word vn_examples_example vn_words_word pb_examples_text pb_words_word
read
./_sizes.py sqlunet-sn.sqlite 	db-sn/sqlunet-sn.db 	db-sn/sqlunet-sn.db.zip 	samples_sample synsets_definition words_word
read
./_sizes.py sqlunet-fn.sqlite	db-fn/sqlunet-fn.db 	db-fn/sqlunet-fn.db.zip 	fn_words_word fn_sentences_text
read

