package org.sqlbuilder.fn;

public class Names
{
	static public class ANNOSETS
	{
		public static final String FILE = "annosets.sql";
		public static final String TABLE = "fn_annosets";
		public static final String COLUMNS = "annosetid,sentenceid,frameid,luid,cxnid,statusid,cdate,noccurs";
	}

	static public class CORETYPES
	{
		public static final String FILE = "coretypes.sql";
		public static final String TABLE = "fn_coretypes";
		public static final String COLUMNS = "coretypeid,coretype";
	}

	static public class CORPUSES
	{
		public static final String FILE = "corpuses.sql";
		public static final String TABLE = "fn_corpuses";
		public static final String COLUMNS = "corpusid,corpus,corpusdesc,luid,noccurs";
	}

	static public class CXNS
	{
		public static final String FILE = "cxns.sql";
		public static final String TABLE = "fn_cxns";
		public static final String COLUMNS = "cxnid,cxn";
	}

	static public class DOCUMENTS
	{
		public static final String FILE = "documents.sql";
		public static final String TABLE = "fn_documents";
		public static final String COLUMNS = "documentid,corpusid,documentdesc,noccurs";
	}

	static public class FEGROUPREALIZATIONS
	{
		public static final String FILE = "fegrouprealizations.sql";
		public static final String TABLE = "fn_fegrouprealizations";
		public static final String COLUMNS = "fegrid,luid,total";
	}

	static public class FEGROUPREALIZATIONS_FES
	{
		public static final String FILE = "fegrouprealizations_fes.sql";
		public static final String TABLE = "fn_fegrouprealizations_fes";
		public static final String COLUMNS = "rfeid,fegrid,feid,fetypeid";
	}

	static public class FEREALIZATIONS
	{
		public static final String FILE = "ferealizations.sql";
		public static final String TABLE = "fn_ferealizations";
		public static final String COLUMNS = "ferid,luid,fetypeid,feid,total";
	}

	static public class FES
	{
		public static final String FILE = "fes.sql";
		public static final String TABLE = "fn_fes";
		public static final String COLUMNS = "feid,frameid,fetypeid,feabbrev,fedefinition,coretypeid,coreset,fgcolor,bgcolor,cdate,cby";
	}

	static public class FES_EXCLUDED
	{
		public static final String FILE = "fes_excluded.sql";
		public static final String TABLE = "fn_fes_excluded";
		public static final String COLUMNS = "feid,fe2id";
	}

	static public class FES_REQUIRED
	{
		public static final String FILE = "fes_required.sql";
		public static final String TABLE = "fn_fes_required";
		public static final String COLUMNS = "feid,fe2id";
	}

	static public class FES_SEMTYPES
	{
		public static final String FILE = "fes_semtypes.sql";
		public static final String TABLE = "fn_fes_semtypes";
		public static final String COLUMNS = "feid,semtypeid";
	}

	static public class FETYPES
	{
		public static final String FILE = "fetypes.sql";
		public static final String TABLE = "fn_fetypes";
		public static final String COLUMNS = "fetypeid,fetype";
	}

	static public class FRAMERELATIONS
	{
		public static final String FILE = "framerelations.sql";
		public static final String TABLE = "fn_framerelations";
		public static final String COLUMNS = "relationid,relation";
	}

	static public class FRAMES
	{
		public static final String FILE = "frames.sql";
		public static final String TABLE = "fn_frames";
		public static final String COLUMNS = "frameid,frame,framedefinition,cdate,cby";
	}

	static public class FRAMES_RELATED
	{
		public static final String FILE = "frames_related.sql";
		public static final String TABLE = "fn_frames_related";
		public static final String COLUMNS = "frameid,frame2id,relationid";
	}

	static public class FRAMES_SEMTYPES
	{
		public static final String FILE = "frames_semtypes.sql";
		public static final String TABLE = "fn_frames_semtypes";
		public static final String COLUMNS = "frameid,semtypeid";
	}

	static public class GFTYPES
	{
		public static final String FILE = "gftypes.sql";
		public static final String TABLE = "fn_gftypes";
		public static final String COLUMNS = "gfid,gf";
	}

	static public class GOVERNORS
	{
		public static final String FILE = "governors.sql";
		public static final String TABLE = "fn_governors";
		public static final String COLUMNS = "governorid,fnwordid,governortype";
	}

	static public class GOVERNORS_ANNOSETS
	{
		public static final String FILE = "governors_annosets.sql";
		public static final String TABLE = "fn_governors_annosets";
		public static final String COLUMNS = "governorid,annosetid";
	}

	static public class LABELITYPES
	{
		public static final String FILE = "labelitypes.sql";
		public static final String TABLE = "fn_labelitypes";
		public static final String COLUMNS = "labelitypeid,labelitype,labelitypedescr";
	}

	static public class LABELS
	{
		public static final String FILE = "labels.sql";
		public static final String TABLE = "fn_labels";
		public static final String COLUMNS = "labelid,layerid,labeltypeid,labelitypeid,feid,start,end,fgcolor,bgcolor,cby";
	}

	static public class LABELTYPES
	{
		public static final String FILE = "labeltypes.sql";
		public static final String TABLE = "fn_labeltypes";
		public static final String COLUMNS = "labeltypeid,labeltype";
	}

	static public class LAYERS
	{
		public static final String FILE = "layers.sql";
		public static final String TABLE = "fn_layers";
		public static final String COLUMNS = "layerid,annosetid,layertypeid,rank";
	}

	static public class LAYERTYPES
	{
		public static final String FILE = "layertypes.sql";
		public static final String TABLE = "fn_layertypes";
		public static final String COLUMNS = "layertypeid,layertype";
	}

	static public class LEXEMES
	{
		public static final String FILE = "lexemes.sql";
		public static final String TABLE = "fn_lexemes";
		public static final String COLUMNS = "lexemeid,luid,fnwordid,posid,breakbefore,headword,lexemeidx";
	}

	static public class LEXUNITS
	{
		public static final String FILE = "lexunits.sql";
		public static final String TABLE = "fn_lexunits";
		public static final String COLUMNS = "luid,frameid,lexunit,posid,ludefinition,ludict,statusid,totalannotated,incorporatedfeid,incorporatedfetypeid,noccurs";
	}

	static public class LEXUNITS_GOVERNORS
	{
		public static final String FILE = "lexunits_governors.sql";
		public static final String TABLE = "fn_lexunits_governors";
		public static final String COLUMNS = "luid,governorid";
	}

	static public class LEXUNITS_SEMTYPES
	{
		public static final String FILE = "lexunits_semtypes.sql";
		public static final String TABLE = "fn_lexunits_semtypes";
		public static final String COLUMNS = "luid,semtypeid";
	}

	static public class PATTERNS
	{
		public static final String FILE = "patterns.sql";
		public static final String TABLE = "fn_patterns";
		public static final String COLUMNS = "patternid,fegrid,total";
	}

	static public class PATTERNS_ANNOSETS
	{
		public static final String FILE = "patterns_annosets.sql";
		public static final String TABLE = "fn_patterns_annosets";
		public static final String COLUMNS = "patternid,annosetid";
	}

	static public class PATTERNS_VALENCEUNITS
	{
		public static final String FILE = "patterns_valenceunits.sql";
		public static final String TABLE = "fn_patterns_valenceunits";
		public static final String COLUMNS = "pvid,patternid,vuid,feid,fetypeid";
	}

	static public class POSES
	{
		public static final String FILE = "poses.sql";
		public static final String TABLE = "fn_poses";
		public static final String COLUMNS = "posid,pos";
	}

	static public class PTTYPES
	{
		public static final String FILE = "pttypes.sql";
		public static final String TABLE = "fn_pttypes";
		public static final String COLUMNS = "ptid,pt";
	}

	static public class SEMTYPES
	{
		public static final String FILE = "semtypes.sql";
		public static final String TABLE = "fn_semtypes";
		public static final String COLUMNS = "semtypeid,semtype,semtypeabbrev,semtypedefinition";
	}

	static public class SEMTYPES_SUPERS
	{
		public static final String FILE = "semtypes_supers.sql";
		public static final String TABLE = "fn_semtypes_supers";
		public static final String COLUMNS = "semtypeid,supersemtypeid";
	}

	static public class SENTENCES
	{
		public static final String FILE = "sentences.sql";
		public static final String TABLE = "fn_sentences";
		public static final String COLUMNS = "sentenceid,documentid,corpusid,paragno,sentno,text,apos,noccurs";
	}

	static public class STATUSES
	{
		public static final String FILE = "statuses.sql";
		public static final String TABLE = "fn_statuses";
		public static final String COLUMNS = "statusid,status";
	}

	static public class SUBCORPUSES
	{
		public static final String FILE = "subcorpuses.sql";
		public static final String TABLE = "fn_subcorpuses";
		public static final String COLUMNS = "subcorpusid,luid,subcorpus";
	}

	static public class SUBCORPUSES_SENTENCES
	{
		public static final String FILE = "subcorpuses_sentences.sql";
		public static final String TABLE = "fn_subcorpuses_sentences";
		public static final String COLUMNS = "subcorpusid,sentenceid";
	}

	static public class VALENCEUNITS
	{
		public static final String FILE = "valenceunits.sql";
		public static final String TABLE = "fn_valenceunits";
		public static final String COLUMNS = "vuid,ferid,ptid,gfid";
	}

	static public class VALENCEUNITS_ANNOSETS
	{
		public static final String FILE = "valenceunits_annosets.sql";
		public static final String TABLE = "fn_valenceunits_annosets";
		public static final String COLUMNS = "vuid,annosetid";
	}

	static public class WORDS
	{
		public static final String FILE = "words.sql";
		public static final String TABLE = "fn_words";
		public static final String COLUMNS = "fnwordid,wordid,word";
	}
}
