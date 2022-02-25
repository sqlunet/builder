package org.sqlbuilder.pm.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.ParseException;
import org.sqlbuilder.common.Utils;

public class PmEntry implements Insertable
{
//	static final int ID_LANG = 0; // this column contains the language of the predicate. id:eng
	static final int ID_POS = 1; // this columnn contains the part-of-speech of the predicate. id:n
	static final int ID_PRED = 2; // this column contains the predicate. id:abatement.01
	static final int ID_ROLE = 3; // this column contains the role. id:0

	static final int VN_CLASS = 4; // this column contains the information of the VerbNet class. vn:withdraw-82
//	static final int VN_CLASS_NUMBER = 5; // this column contains the information of the VerbNet class number. vn:82
	static final int VN_SUBCLASS = 6; // this column contains the information of VerbNet subclass. vn:withdraw-82-1
//	static final int VN_SUBCLASS_NUMBER = 7; // this column contains the information of the VerbNet subclass number. vn:82-1
	static final int VN_LEMMA = 8; // this column contains the information of the verb lemma. vn:pull_back
	static final int VN_ROLE = 9; // this column contains the information of the VerbNet thematic-role. vn:Source
	static final int WN_SENSE = 10; // this column contains the information of the word sense in WordNet. wn:pull_back%2:32:12
//	static final int MCR_ILI_OFFSET = 11; // this column contains the information of the ILI number in the MCR3.0. mcr:ili-30-00799383-v

	static final int FN_FRAME = 12; // this column contains the information of the frame in FrameNet. fn:Going_back_on_a_commitment
	static final int FN_LE = 13; // this column contains the information of the corresponding lexical-entry in FrameNet. fn:NULL
	static final int FN_FRAME_ELEMENT = 14; // this column contains the information of the frame-element in FrameNet. fn:NULL

	static final int PB_ROLESET = 15; // this column contains the information of the predicate in PropBank. pb:NULL
	static final int PB_ARG = 16; // this column contains the information of the predicate argument in PropBank. pb:NULL

//	static final int MCR_BC = 17; // this column contains the information if the verb sense it is Base Concept or not in the MCR3.0. mcr:1
//	static final int MCR_DOMAIN = 18; // this column contains the information of the WordNet domain aligned to WordNet 3.0 in the MCR3.0. mcr:factotum
	static final int MCR_SUMO = 19; // this column contains the information of the AdimenSUMO in the MCR3.0. mcr:Communication
//	static final int MCR_TO = 20; // this column contains the information of the MCR Top Ontology in the MCR3.0. mcr:Dynamic;communication;
//	static final int MCR_LEXNAME = 21; // this column contains the information of the MCR Lexicographical file name. mcr:communication
//	static final int MCR_BLC = 22; // this column contains the information of the Base Level Concept of the WordNet verb sense in the MCR3.0.mcr:back_away%2:32:00
//	static final int WN_SENSEFREC = 23; // this column contains the information of the frequency of the WordNet 3.0 verb sense. wn:0
//	static final int WN_SYNSET_REL_NUM = 24; // this column contains the information of the number of relations of the WordNet 3.0 verb sense. wn:004
//	static final int ESO_CLASS = 25; // this column contains the information of the class of the ESO ontology.
//	static final int ESO_ROLE = 26; // this column contains the information of the role of the ESO ontology.
	static final int SOURCE = 27; // this column contains the information of how the row has been obtained. SEMLINK;FRAME;SYNONYMS

	// sources
	static final int SEMLINK = 0x1;
	static final int SYNONYMS = 0x2;
	static final int FRAME = 0x10;
	static final int FN_FE = 0x20;
	static final int ADDED_FRAME_FN_ROLE = 0x40; // ADDED_FRAME-FN_ROLE
	static final int FN_MAPPING = 0x100;
	static final int VN_MAPPING = 0x200;
	static final int PREDICATE_MAPPING = 0x400;
	static final int ROLE_MAPPING = 0x800;
	static final int WN_MISSING = 0x1000;

	private PmRole role;

	// wordnet

	public String word;

	public String sensekey;

	// verbnet

	public final VnRoleAlias vn = new VnRoleAlias();

	// propbank

	public final PbRoleAlias pb = new PbRoleAlias();

	// framenet

	public final FnRoleAlias fn = new FnRoleAlias();

	// sumo

	private String sumoterm;

	// sources

	private long sources;

	public static PmEntry parse(final String line) throws ParseException
	{
		// split into fields
		final String[] columns = line.split("\t");
		if (columns.length > PmEntry.SOURCE + 1)
		{
			throw new ParseException("Line has more fields than expected");
		}

		final PmEntry entry = new PmEntry();

		// predicate, role, pos
		entry.role = PmRole.parse(columns);

		// lemma
		final String lemma = columns[PmEntry.VN_LEMMA].trim();
		entry.word = lemma.substring(3);

		// source
		final String prefix = lemma.substring(0, 2);
		if ("vn".equals(prefix))
		{
		}
		else if ("fn".equals(prefix))
		{
		}
		else
		{
			throw new ParseException(prefix);
		}
		if ("NULL".equals(entry.word))
		{
			entry.word = entry.role.predicate.substring(0, entry.role.predicate.indexOf('.'));
		}

		// sensekey
		String sensekey = columns[PmEntry.WN_SENSE].trim().substring(3); // .replace('_', ' ');
		if (!sensekey.isEmpty() && !"NULL".equals(sensekey))
		{
			if (sensekey.startsWith("?"))
			{
				sensekey = sensekey.substring(1);
			}
		}
		else if (sensekey.isEmpty())
		{
			throw new ParseException("Empty sensekey for " + line);
		}
		entry.sensekey = "NULL".equalsIgnoreCase(sensekey) ? null : sensekey + "::";

		// verbnet
		String vnsubclass = columns[PmEntry.VN_SUBCLASS] == null || "vn:NULL".equals(columns[PmEntry.VN_SUBCLASS]) ? null : columns[PmEntry.VN_SUBCLASS].trim().substring(3);
		entry.vn.clazz = vnsubclass != null ? vnsubclass : columns[PmEntry.VN_CLASS] == null || "vn:NULL".equals(columns[PmEntry.VN_CLASS]) ? null : columns[PmEntry.VN_CLASS].trim().substring(3);
		entry.vn.theta = columns[PmEntry.VN_ROLE] == null || "vn:NULL".equals(columns[PmEntry.VN_ROLE]) ? null : columns[PmEntry.VN_ROLE].trim().substring(3);

		// propbank
		entry.pb.roleset = columns[PmEntry.PB_ROLESET] == null || "pb:NULL".equals(columns[PmEntry.PB_ROLESET]) ? null : columns[PmEntry.PB_ROLESET].trim().substring(3);
		entry.pb.arg = columns[PmEntry.PB_ARG] == null || "pb:NULL".equals(columns[PmEntry.PB_ARG]) ? null : columns[PmEntry.PB_ARG].trim().substring(3);

		// framenet
		entry.fn.frame = columns[PmEntry.FN_FRAME] == null || "fn:NULL".equals(columns[PmEntry.FN_FRAME]) ? null : columns[PmEntry.FN_FRAME].trim().substring(3);
		entry.fn.fetype = columns[PmEntry.FN_FRAME_ELEMENT] == null || "fn:NULL".equals(columns[PmEntry.FN_FRAME_ELEMENT]) ? null : columns[PmEntry.FN_FRAME_ELEMENT].trim().substring(3);
		entry.fn.lu = columns[PmEntry.FN_LE] == null || "fn:NULL".equals(columns[PmEntry.FN_LE]) ? null : columns[PmEntry.FN_LE].trim().substring(3);

		// sumo
		entry.sumoterm = columns[PmEntry.MCR_SUMO] == null || "mcr:NULL".equals(columns[PmEntry.MCR_SUMO]) ? null : columns[PmEntry.MCR_SUMO].trim().substring(4);

		// source
		entry.sources = 0;
		if (columns.length > PmEntry.SOURCE)
		{
			for (final String source : columns[PmEntry.SOURCE].split(";"))
			{
				if ("SEMLINK".equals(source))
				{
					entry.sources |= PmEntry.SEMLINK;
				}
				if ("SYNONYMS".equals(source))
				{
					entry.sources |= PmEntry.SYNONYMS;
				}
				if ("FRAME".equals(source))
				{
					entry.sources |= PmEntry.FRAME;
				}
				if ("FN_FE".equals(source))
				{
					entry.sources |= PmEntry.FN_FE;
				}
				if ("ADDED_FRAME-FN_ROLE".equals(source))
				{
					entry.sources |= PmEntry.ADDED_FRAME_FN_ROLE;
				}
				if ("FN_MAPPING".equals(source))
				{
					entry.sources |= PmEntry.FN_MAPPING;
				}
				if ("VN_MAPPING".equals(source))
				{
					entry.sources |= PmEntry.VN_MAPPING;
				}
				if ("PREDICATE_MAPPING".equals(source))
				{
					entry.sources |= PmEntry.PREDICATE_MAPPING;
				}
				if ("ROLE_MAPPING".equals(source))
				{
					entry.sources |= PmEntry.ROLE_MAPPING;
				}
				if ("WN_MISSING".equals(source))
				{
					entry.sources |= PmEntry.WN_MISSING;
				}
			}
		}
		return entry;
	}

	@Override
	public String dataRow()
	{
		//return String.format("PM[%s], WN['%s','%s'], VN[%s], PB[%s], FN[%s], SUMO['%s'], SRC[%s]", //
		return String.format("%s,'%s',%s,%s,%s,%s,'%s',%s", //
				role.getIntId(), // pm
				word, Utils.nullable(sensekey),  // wordnet
				vn.dataRow(), // verbnet
				pb.dataRow(), // propbank
				fn.dataRow(), // framenet
				sumoterm, // sumo
				sources); // sources
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s,%c", role.getPredicate(), role.getRole(), role.getPos());
	}
}
