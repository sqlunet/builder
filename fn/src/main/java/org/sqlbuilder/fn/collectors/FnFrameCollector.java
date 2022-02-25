package org.sqlbuilder.fn.collectors;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.fn.joins.*;
import org.sqlbuilder.fn.objects.FE;
import org.sqlbuilder.fn.objects.Frame;
import org.sqlbuilder.fn.objects.LexUnit;
import org.sqlbuilder.fn.objects.Lexeme;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import edu.berkeley.icsi.framenet.FrameDocument;

public class FnFrameCollector extends FnCollector
{
	private final boolean skipLexUnits;

	public FnFrameCollector(final Properties props)
	{
		super("frame", props, "frame");
		this.skipLexUnits = props.getProperty("fnskiplu", "true").compareToIgnoreCase("true") == 0;
	}

	@Override
	protected void processFrameNetFile(final String fileName, final String name)
	{
		final File xmlFile = new File(fileName);
		try
		{
			final FrameDocument _document = FrameDocument.Factory.parse(xmlFile);

			// F R A M E

			final FrameDocument.Frame _frame = _document.getFrame();
			final int frameid = _frame.getID();
			Frame.make(_frame);

			// S E M T Y P E

			for (var _semtype : _frame.getSemTypeArray())
			{
				Frame_SemType.make(frameid, _semtype.getID());
			}

			// F E C O R E S E T S

			final Map<Integer, Integer> feToCoresetMap = new HashMap<>();
			int setid = 0;
			for (var _fecoreset : _frame.getFEcoreSetArray())
			{
				++setid;
				for (var _corefe : _fecoreset.getMemberFEArray())
				{
					final int feid = _corefe.getID();
					final Integer prev = feToCoresetMap.put(feid, setid);
					if (prev != null)
					{
						throw new IllegalArgumentException(String.format("FECoreSets are not disjoint %s %s", prev, feid));
					}
				}
			}

			// F E

			for (var _fe : _frame.getFEArray())
			{
				final int feid = _fe.getID();
				FE.make(_fe, feToCoresetMap.get(feid), frameid);

				// s e m t y p e s
				for (var _semtype : _fe.getSemTypeArray())
				{
					FE_SemType.make(feid, _semtype.getID());
				}

				// r e q u i r e s
				for (var _requiredfe : _fe.getRequiresFEArray())
				{
					FE_FERequired.make(feid, _requiredfe);
				}

				// e x c l u d e s / r e q u i r e s
				for (var _excludedfe : _fe.getExcludesFEArray())
				{
					FE_FEExcluded.make(feid, _excludedfe);
				}
			}

			// R E L A T E D F R A M E S

			for (var _framerelations : _frame.getFrameRelationArray())
			{
				final String relation = _framerelations.getType();
				for (var _relatedframe : _framerelations.getRelatedFrameArray())
				{
					Frame_FrameRelated.make(frameid, _relatedframe, relation);
				}
			}

			// L E X U N I T S

			if (!this.skipLexUnits)
			{
				for (var _lexunit : _frame.getLexUnitArray())
				{
					final int luid = _lexunit.getID();
					LexUnit.make(_lexunit, frameid, _frame.getName());

					// lexemes
					for (var _lexeme : _lexunit.getLexemeArray())
					{
						Lexeme.make(_lexeme, luid);
					}

					// semtypes
					for (var _semtype : _lexunit.getSemTypeArray())
					{
						LexUnit_SemType.make(luid, _semtype);
					}
				}
			}
		}
		catch (XmlException | ParserConfigurationException | SAXException | IOException | RuntimeException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e);
		}
	}
}
