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
			final FrameDocument _document = FrameDocument.Factory.parse(xmlFile);

			// F R A M E

			final Frame _frame = _document.getFrame();
			final long frameid = _frame.getID();
			final FnFrame frame = new FnFrame(_frame);
			FnFrame.SET.add(frame);

			// S E M T Y P E

			for (var _semtype : _frame.getSemTypeArray())
			{
				final FnSemTypeRef semtype = new FnSemTypeRef(_semtype);
				final FnFrame_SemType frame_semtype = new FnFrame_SemType(frameid, semtype);
				FnFrame_SemType.SET.add(frame_semtype);
			}

			// F E C O R E S E T S

			final Map<Long, Integer> feToCoresetMap = new HashMap<>();
			int setid = 0;
			for (var _fecoreset : _frame.getFEcoreSetArray())
			{
				++setid;
				for (var _corefe : _fecoreset.getMemberFEArray())
				{
					final long feid = _corefe.getID();
					final Integer prev = feToCoresetMap.put(feid, setid);
					if (prev != null)
					{
						throw new RuntimeException(String.format("FECoreSets are not disjoint %s %s", prev, feid));
					}
				}
			}

			// F E

			for (var _fe : _frame.getFEArray())
			{
				final long feid = _fe.getID();
				final FnFE fe = new FnFE(frameid, _fe, feToCoresetMap.get(feid));
				FnFE.SET.add(fe);

				// s e m t y p e s
				for (var _semtype : _fe.getSemTypeArray())
				{
					FnSemTypeRef semtype = new FnSemTypeRef(_semtype);
					final FnFE_SemType fe_semtype = new FnFE_SemType(feid, semtype);
					FnFE_SemType.SET.add(fe_semtype);
				}

				// r e q u i r e s
				for (var _requiredfe : _fe.getRequiresFEArray())
				{
					final FnFERequired requiredfe = new FnFERequired(feid, _requiredfe);
					FnFERequired.SET.add(requiredfe);
				}

				// e x c l u d e s / r e q u i r e s
				for (var _excludedfe : _fe.getExcludesFEArray())
				{
					final FnFEExcluded excludedfe = new FnFEExcluded(feid, _excludedfe);
					FnFEExcluded.SET.add(excludedfe);
				}
			}

			// R E L A T E D F R A M E S

			for (var _framerelations : _frame.getFrameRelationArray())
			{
				final String t = _framerelations.getType();
				for (var _relatedframe : _framerelations.getRelatedFrameArray())
				{
					final FnFrameRelated relatedframe = new FnFrameRelated(frame.frame.getID(), _relatedframe, t);
					FnFrameRelated.SET.add(relatedframe);
				}
			}

			// L E X U N I T S

			if (!this.skipLexUnits)
			{
				for (var _lexunit : _frame.getLexUnitArray())
				{
					final long luid = _lexunit.getID();
					final FnFrame_LexUnit frame_lexunit = new FnFrame_LexUnit(frameid, _lexunit);
					final boolean isNew = FnFrame_LexUnit.SET.add(frame_lexunit);
					if (!isNew)
					{
						Logger.instance.logWarn(FnModule.MODULE_ID, this.tag, "frame-lu-duplicate", fileName, -1, null, frame_lexunit.toString());
						continue;
					}

					// lexemes
					for (var _lexeme : _lexunit.getLexemeArray())
					{
						final String lemma = FnLexeme.makeWord(_lexeme.getName());
						final FnWord word = new FnWord(lemma);
						FnWord.SET.add(word);

						final FnLexeme lexeme = new FnLexeme(luid, word, _lexeme);
						FnLexeme.SET.add(lexeme);
					}

					// semtypes
					for (var _semtype : _lexunit.getSemTypeArray())
					{
						final FnLexUnit_SemType lexunit_semtype = new FnLexUnit_SemType(luid, _semtype);
						FnLexUnit_SemType.SET.add(lexunit_semtype);
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
