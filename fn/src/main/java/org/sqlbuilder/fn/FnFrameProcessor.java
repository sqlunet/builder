package org.sqlbuilder.fn;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import edu.berkeley.icsi.framenet.FrameDocument;
import edu.berkeley.icsi.framenet.FrameDocument.Frame;

public class FnFrameProcessor extends FnProcessor
{
	private final boolean skipLexUnits;

	public FnFrameProcessor(final Properties props)
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
			final FrameDocument document = FrameDocument.Factory.parse(xmlFile);

			// F R A M E

			final Frame frame = document.getFrame();
			final long frameid = frame.getID();
			final FnFrame fnFrame = new FnFrame(frame);
			FnFrame.SET.add(fnFrame);

			// S E M T Y P E

			for (var semtype : frame.getSemTypeArray())
			{
				final FnSemTypeRef fnSemtype = new FnSemTypeRef(semtype);
				final FnFrame_SemType frame_semtype = new FnFrame_SemType(frameid, fnSemtype);
				FnFrame_SemType.SET.add(frame_semtype);
			}

			// F E C O R E S E T S

			final Map<Long, Integer> feToCoresetMap = new HashMap<>();
			int setid = 0;
			for (var fecoreset : frame.getFEcoreSetArray())
			{
				++setid;
				for (var coreFE : fecoreset.getMemberFEArray())
				{
					final long feid = coreFE.getID();
					final Integer prev = feToCoresetMap.put(feid, setid);
					if (prev != null)
					{
						throw new RuntimeException(String.format("FECoreSets are not disjoint %s %s", prev, feid));
					}
				}
			}

			// F E

			for (var fe : frame.getFEArray())
			{
				final long feid = fe.getID();
				final FnFE fnFE = new FnFE(frameid, fe, feToCoresetMap.get(feid));
				FnFE.SET.add(fnFE);

				// s e m t y p e s
				for (var fesemtype : fe.getSemTypeArray())
				{
					FnSemTypeRef fnSemtypeRef = new FnSemTypeRef(fesemtype);
					final FnFE_SemType fE_semtype = new FnFE_SemType(feid, fnSemtypeRef);
					FnFE_SemType.SET.add(fE_semtype);
				}

				// r e q u i r e s
				for (var requiredFE : fe.getRequiresFEArray())
				{
					final FnFERequired fnRequiredFE = new FnFERequired(feid, requiredFE);
					FnFERequired.SET.add(fnRequiredFE);
				}

				// e x c l u d e s / r e q u i r e s
				for (var excludedFE : fe.getExcludesFEArray())
				{
					final FnFEExcluded fnExcludedFE = new FnFEExcluded(feid, excludedFE);
					FnFEExcluded.SET.add(fnExcludedFE);
				}
			}

			// R E L A T E D F R A M E S

			for (var relatedFrame : frame.getFrameRelationArray())
			{
				final String t = relatedFrame.getType();
				for (var rf : relatedFrame.getRelatedFrameArray())
				{
					final FnFrameRelated fnRelatedFrame = new FnFrameRelated(fnFrame.frame.getID(), rf, t);
					FnFrameRelated.SET.add(fnRelatedFrame);
				}
			}

			// L E X U N I T S
			if (!this.skipLexUnits)
			{
				for (var lexunit : frame.getLexUnitArray())
				{
					final FnFrameLexUnit frameLexUnit = new FnFrameLexUnit(frameid, lexunit);
					final boolean isNew = FnFrameLexUnit.SET.add(frameLexUnit);
					if (!isNew)
					{
						Logger.instance.logWarn(FnModule.MODULE_ID, this.tag, "frame-lu-duplicate", fileName, -1, null, frameLexUnit.toString());
						continue;
					}

					// lexemes
					for (var lexeme : lexunit.getLexemeArray())
					{
						final String word = FnLexeme.makeWord(lexeme.getName());
						final FnWord fnWord = new FnWord(word);
						FnWord.SET.add(fnWord);

						final FnLexeme fnLexeme = new FnLexeme(lexunit.getID(), fnWord, lexeme);
						FnLexeme.SET.add(fnLexeme);
					}

					// semtypes
					for (var semtype : lexunit.getSemTypeArray())
					{
						final FnLexUnit_SemType fnLexUnit_SemType = new FnLexUnit_SemType(lexunit.getID(), semtype);
						FnLexUnit_SemType.SET.add(fnLexUnit_SemType);
					}
				}
			}
		}
		catch (XmlException | ParserConfigurationException | SAXException | IOException | RuntimeException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, this.tag, "xml-document", fileName, -1, null, "document=[" + fileName + "]", e);
		}
		if (Logger.verbose)
		{
			Progress.traceTailer("framenet (frame)", name);
		}
	}
}
