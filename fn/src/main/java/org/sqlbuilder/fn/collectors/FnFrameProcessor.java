package org.sqlbuilder.fn.collectors;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.fn.FnFrame_LexUnit;
import org.sqlbuilder.fn.FnLexeme;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.fn.joins.*;
import org.sqlbuilder.fn.objects.FE;
import org.sqlbuilder.fn.objects.Frame;
import org.sqlbuilder.fn.objects.Word;
import org.sqlbuilder.fn.refs.SemTypeRef;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import edu.berkeley.icsi.framenet.FrameDocument;

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

			final FrameDocument.Frame _frame = _document.getFrame();
			final int frameid = _frame.getID();
			final Frame frame = new Frame(_frame);
			Frame.SET.add(frame);

			// S E M T Y P E

			for (var _semtype : _frame.getSemTypeArray())
			{
				final SemTypeRef semtype = new SemTypeRef(_semtype);
				final Frame_SemType frame_semtype = new Frame_SemType(frameid, semtype);
				Frame_SemType.SET.add(frame_semtype);
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
				final FE fe = new FE(_fe, feToCoresetMap.get(feid), frameid);
				FE.SET.add(fe);

				// s e m t y p e s
				for (var _semtype : _fe.getSemTypeArray())
				{
					SemTypeRef semtype = new SemTypeRef(_semtype);
					final FE_SemType fe_semtype = new FE_SemType(feid, semtype);
					FE_SemType.SET.add(fe_semtype);
				}

				// r e q u i r e s
				for (var _requiredfe : _fe.getRequiresFEArray())
				{
					final FE_FERequired requiredfe = new FE_FERequired(feid, _requiredfe);
					FE_FERequired.SET.add(requiredfe);
				}

				// e x c l u d e s / r e q u i r e s
				for (var _excludedfe : _fe.getExcludesFEArray())
				{
					final FE_FEExcluded excludedfe = new FE_FEExcluded(feid, _excludedfe);
					FE_FEExcluded.SET.add(excludedfe);
				}
			}

			// R E L A T E D F R A M E S

			for (var _framerelations : _frame.getFrameRelationArray())
			{
				final String t = _framerelations.getType();
				for (var _relatedframe : _framerelations.getRelatedFrameArray())
				{
					final Frame_FrameRelated relatedframe = new Frame_FrameRelated(frame.frame.getID(), _relatedframe, t);
					Frame_FrameRelated.SET.add(relatedframe);
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

						final Word word = new Word(lemma);
						Word.SET.add(word);

						final FnLexeme lexeme = new FnLexeme(luid, word, _lexeme);
						FnLexeme.SET.add(lexeme);
					}

					// semtypes
					for (var _semtype : _lexunit.getSemTypeArray())
					{
						final LexUnit_SemType lexunit_semtype = new LexUnit_SemType(luid, _semtype);
						LexUnit_SemType.SET.add(lexunit_semtype);
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
