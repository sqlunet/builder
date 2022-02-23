package org.sqlbuilder.fn.collectors;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.fn.joins.FE_FEExcluded;
import org.sqlbuilder.fn.joins.FE_FERequired;
import org.sqlbuilder.fn.joins.FE_SemType;
import org.sqlbuilder.fn.joins.Frame_SemType;
import org.sqlbuilder.fn.objects.FE;
import org.sqlbuilder.fn.objects.Frame;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import edu.berkeley.icsi.framenet.FrameDocument;

public class FnExportingProcessor extends FnProcessor
{
	@SuppressWarnings("FieldCanBeLocal")
	private final boolean skipLexUnits;

	public FnExportingProcessor(final Properties props)
	{
		super("frame", props, "frame");
		this.skipLexUnits = props.getProperty("fnskiplu", "true").compareToIgnoreCase("true") == 0;
	}

	@Override
	protected void processFrameNetFile(final String fileName, final String name)
	{
		if (Logger.verbose)
		{
			Progress.traceHeader("framenet (frame)", name);
		}
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
		}
		catch (XmlException | ParserConfigurationException | SAXException | IOException | RuntimeException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e);
		}
		if (Logger.verbose)
		{
			Progress.traceTailer("framenet (frame)", name);
		}
	}
}
