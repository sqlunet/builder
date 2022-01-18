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

import edu.berkeley.icsi.framenet.*;
import edu.berkeley.icsi.framenet.FrameDocument.Frame;
import edu.berkeley.icsi.framenet.FrameDocument.Frame.FEcoreSet;

public class FnFrameProcessor extends FnProcessor
{
	private final boolean matchToWn;

	private final boolean skipLexUnits;

	public FnFrameProcessor(final Properties props)
	{
		super("frame", props, "frame");
		this.matchToWn = props.getProperty("fnmatchwn", "").compareToIgnoreCase("true") == 0;
		this.skipLexUnits = props.getProperty("fnskiplu", "true").compareToIgnoreCase("true") == 0;
	}

	@Override
	protected int processFrameNetFile(final String fileName, final String name)
	{
		if (Logger.verbose)
		{
			Progress.traceHeader("framenet (frame)", name);
		}
		final int count = 0;
		final File xmlFile = new File(fileName);
		// System.out.printf("file=<%s>\n", xmlFile);
		try
		{
			final FrameDocument document = FrameDocument.Factory.parse(xmlFile);

			// F R A M E

			final Frame frame = document.getFrame();
			final long frameid = frame.getID();
			final FnFrame fnFrame = new FnFrame(frame);
			// System.out.println(fnFrame);
			FnFrame.SET.add(fnFrame);

			// S E M T Y P E

			final SemTypeRefType[] semtypes = frame.getSemTypeArray();
			for (final SemTypeRefType semtype : semtypes)
			{
				final FnSemTypeRef fnsemtype = new FnSemTypeRef(semtype);
				final FnFrame_SemType frame_SemType = new FnFrame_SemType(fnFrame, fnsemtype);
				FnFrame_SemType.SET.add(frame_SemType);
			}

			// F E C O R E S E T S

			final Map<Long, Integer> feToCoresetMap = new HashMap<>();
			final FEcoreSet[] fecoresets = frame.getFEcoreSetArray();
			int setid = 0;
			for (final FEcoreSet fecoreset : fecoresets)
			{
				++setid;
				final InternalFrameRelationFEType[] fecoremembers = fecoreset.getMemberFEArray();
				for (final InternalFrameRelationFEType coreFE : fecoremembers)
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

			final FEType[] fes = frame.getFEArray();
			for (final FEType fe : fes)
			{
				final long feid = fe.getID();
				final FnFE fE = new FnFE(frameid, fe, feToCoresetMap.get(feid));
				FnFE.SET.add(fE);

				// s e m t y p e s
				final SemTypeRefType[] fesemtypes = fe.getSemTypeArray();
				for (final SemTypeRefType fesemtype : fesemtypes)
				{
					FnSemTypeRef fnSemTypeRef = new FnSemTypeRef(fesemtype);
					FnSemTypeRef.SET.add(fnSemTypeRef);
					final FnFE_SemType fE_SemType = new FnFE_SemType(fE, fnSemTypeRef);
					FnFE_SemType.SET.add(fE_SemType);
				}

				// r e q u i r e s
				final InternalFrameRelationFEType[] requiredFEs = fe.getRequiresFEArray();
				for (final InternalFrameRelationFEType requiredFE : requiredFEs)
				{
					final FnFERequired requiredFE2 = new FnFERequired(fe, requiredFE);
					FnFERequired.SET.add(requiredFE2);
				}

				// e x c l u d e s / r e q u i r e s
				final InternalFrameRelationFEType[] excludedFEs = fe.getExcludesFEArray();
				for (final InternalFrameRelationFEType excludedFE : excludedFEs)
				{
					final FnFEExcluded excludedFE2 = new FnFEExcluded(fe, excludedFE);
					FnFEExcluded.SET.add(excludedFE2);
				}
			}

			// R E L A T E D F R A M E S

			final RelatedFramesType[] relatedframes = frame.getFrameRelationArray();
			for (final RelatedFramesType relatedframe : relatedframes)
			{
				final String t = relatedframe.getType();
				final FrameIDNameType[] rfs = relatedframe.getRelatedFrameArray();
				for (final FrameIDNameType rf : rfs)
				{
					final String name2 = rf.getStringValue();
					final int frame2id = rf.getID();
					final FnFrameRelated relatedFrame = new FnFrameRelated(fnFrame, rf, t);
					FnFrameRelated.SET.add(relatedFrame);
				}
			}

			// L E X U N I T S
			if (!this.skipLexUnits)
			{
				final FrameLUType[] lexunits = frame.getLexUnitArray();
				for (final FrameLUType lexunit : lexunits)
				{
					final FnFrameLexUnit frameLexUnit = new FnFrameLexUnit(fnFrame, lexunit);
					final boolean isNew = FnFrameLexUnit.SET.add(frameLexUnit);
					if (!isNew)
					{
						Logger.instance.logWarn(FnModule.MODULE_ID, this.tag, "frame-lu-duplicate", fileName, -1, null, frameLexUnit.toString());
						continue;
					}

					// lexemes
					final LexemeType[] lexemes = lexunit.getLexemeArray();
					for (final LexemeType lexeme : lexemes)
					{
						final String word = FnLexeme.makeWord(lexeme.getName());
						final FnWord fnWord = new FnWord(word);
						FnWord.SET.add(fnWord);

						final FnLexeme fnLexeme = new FnLexeme(lexunit.getID(), fnWord, lexeme);
						FnLexeme.SET.add(fnLexeme);
					}

					// semtypes
					final SemTypeRefType[] semtyperefs = lexunit.getSemTypeArray();
					for (final SemTypeRefType semtyperef : semtyperefs)
					{
						final FnLexUnit_SemType fnLexUnit_SemType = new FnLexUnit_SemType(lexunit.getID(), semtyperef);
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
		return 1;
	}
}
