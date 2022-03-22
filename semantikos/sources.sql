DROP TABLE IF EXISTS sources;
CREATE TABLE IF NOT EXISTS sources (
  idsource INTEGER NOT NULL,
  name VARCHAR(45) NOT NULL,
  version VARCHAR(12) DEFAULT NULL,
  wnversion VARCHAR(12) DEFAULT NULL,
  url TEXT DEFAULT NUL,
  provider varchar(45) DEFAULT NULL,
  reference TEXT DEFAULT NULL,
  PRIMARY KEY (idsource)
);
INSERT INTO `sources` (idsource,name,version,wnversion,url,provider,reference) VALUES (1,'WordNet','3.1','3.1','http://wordnet.princeton.edu/','Princeton University','George A. Miller (1995). WordNet: A Lexical Database for English. Communications of the ACM Vol. 38, No. 11: 39-41.
Christiane Fellbaum (1998, ed.) WordNet: An Electronic Lexical Database. Cambridge, MA: MIT Press.'),
 (7,'BNC','2001','any','http://ucrel.lancs.ac.uk/bncfreq/',NULL,NULL),
 (30,'Open English Wordnet','2021','3.1','https://github.com/globalwordnet/english-wordnet','Global WordNet Association',NULL);

