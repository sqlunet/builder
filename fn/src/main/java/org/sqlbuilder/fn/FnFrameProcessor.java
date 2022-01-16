package org.sqlbuilder.fn;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.Insertable;
import org.sqlbuilder.Logger;
import org.sqlbuilder.NotFoundException;
import org.sqlbuilder.Progress;
import org.sqlbuilder.SQLUpdateException;
import org.sqlbuilder.wordnet.db.DBWordFinder;
import org.sqlbuilder.wordnet.id.WordId;
import org.xml.sax.SAXException;

import edu.berkeley.icsi.framenet.FEType;
import edu.berkeley.icsi.framenet.FrameDocument;
import edu.berkeley.icsi.framenet.FrameDocument.Frame;
import edu.berkeley.icsi.framenet.FrameDocument.Frame.FEcoreSet;
import edu.berkeley.icsi.framenet.FrameIDNameType;
import edu.berkeley.icsi.framenet.FrameLUType;
import edu.berkeley.icsi.framenet.InternalFrameRelationFEType;
import edu.berkeley.icsi.framenet.LexemeType;
import edu.berkeley.icsi.framenet.RelatedFramesType;
import edu.berkeley.icsi.framenet.SemTypeRefType;

public class FnFrameProcessor extends FnProcessor
{
	private final boolean matchToWn;

	private final boolean skipLexUnits;

	public FnFrameProcessor(final Properties props)
	{
		super("frame", props);
		this.processorTag = "frame";
		this.matchToWn = props.getProperty("fnmatchwn", "").compareToIgnoreCase("true") == 0;
		this.skipLexUnits = props.getProperty("fnskiplu", "true").compareToIgnoreCase("true") == 0;
	}

	@Override
	protected int processFrameNetFile(final String fileName, final String name, final Connection connection)
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
			insert(fnFrame, connection);

			// S E M T Y P E

			final SemTypeRefType[] semtypes = frame.getSemTypeArray();
			for (final SemTypeRefType semtype : semtypes)
			{
				final long semtypeid = semtype.getID();
				final FnFrame_SemType fE_SemType = new FnFrame_SemType(frameid, semtypeid);
				insert(fE_SemType, connection);
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
						throw new RuntimeException(String.format("FECoreSets are not disjoint %s %s", prev, feid));
				}
			}

			// F E

			final FEType[] fes = frame.getFEArray();
			for (final FEType fe : fes)
			{
				final long feid = fe.getID();
				final FnFE fE = new FnFE(frameid, fe, feToCoresetMap.get(feid));
				insert(fE, connection);

				// s e m t y p e s
				final SemTypeRefType[] fesemtypes = fe.getSemTypeArray();
				for (final SemTypeRefType fesemtype : fesemtypes)
				{
					final long fesemtypeid = fesemtype.getID();
					final FnFE_SemType fE_SemType = new FnFE_SemType(feid, fesemtypeid);
					insert(fE_SemType, connection);
				}

				// r e q u i r e s
				final InternalFrameRelationFEType[] requiredFEs = fe.getRequiresFEArray();
				for (final InternalFrameRelationFEType requiredFE : requiredFEs)
				{
					final long feid2 = requiredFE.getID();
					final FnFERequired requiredFE2 = new FnFERequired(feid, feid2);
					insert(requiredFE2, connection);
				}

				// e x c l u d e s / r e q u i r e s
				final InternalFrameRelationFEType[] excludedFEs = fe.getExcludesFEArray();
				for (final InternalFrameRelationFEType excludedFE : excludedFEs)
				{
					final long feid2 = excludedFE.getID();
					final FnFEExcluded excludedFE2 = new FnFEExcluded(feid, feid2);
					insert(excludedFE2, connection);
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
					final FnFrameRelated relatedFrame = new FnFrameRelated(frameid, name2, frame2id, t);
					insert(relatedFrame, connection);
				}
			}

			// L E X U N I T S
			if (!this.skipLexUnits)
			{
				final FrameLUType[] lexunits = frame.getLexUnitArray();
				for (final FrameLUType lexunit : lexunits)
				{
					final long luid = lexunit.getID();
					final FnFrameLexUnit frameLexUnit = new FnFrameLexUnit(frameid, lexunit);
					final int insertLexUnitCount = insert(frameLexUnit, connection);
					if (insertLexUnitCount == 0)
					{
						Logger.instance.logWarn(FnModule.MODULE_ID, this.processorTag, "frame-lu-duplicate", fileName, -1, null, frameLexUnit.toString());
						continue;
					}

					// lexemes
					final LexemeType[] lexemes = lexunit.getLexemeArray();
					for (final LexemeType lexeme : lexemes)
					{
						final String word = FnLexeme.makeWord(lexeme.getName());
						final WordId wordId = this.matchToWn ? DBWordFinder.findOrCreateWord(word, connection) : null;

						final FnWord fnWord = new FnWord(word, wordId);
						insert(fnWord, connection);
						final long fnwordid = fnWord.getId();

						final FnLexeme fnLexeme = new FnLexeme(luid, fnwordid, lexeme);
						insert(fnLexeme, connection);
					}

					// semtypes
					final SemTypeRefType[] semtyperefs = lexunit.getSemTypeArray();
					for (final SemTypeRefType semtyperef : semtyperefs)
					{
						final FnLexUnit_SemType fnLexUnit_SemType = new FnLexUnit_SemType(luid, semtyperef.getID());
						insert(fnLexUnit_SemType, connection);
					}
				}
			}
		}
		catch (XmlException | ParserConfigurationException | SAXException | IOException | RuntimeException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, this.processorTag, "xml-document", fileName, -1L, null, "document=[" + fileName + "]", e);
		}
		if (Logger.verbose)
		{
			Progress.traceTailer("framenet (frame)", name, count);
		}
		return 1;
	}

	// convenience function

	int insert(final Insertable insertable, final Connection connection)
	{
		try
		{
			return insertable.insert(connection);
		}
		catch (NotFoundException nfe)
		{
			Logger.instance.logNotFoundException(FnModule.MODULE_ID, this.processorTag, "find-" + insertable.getClass().getSimpleName().toLowerCase(), this.filename, -1L, null, insertable.toString(), nfe);
		}
		catch (SQLUpdateException sqlue)
		{
			Logger.instance.logSQLUpdateException(FnModule.MODULE_ID, this.processorTag, "insert-" + insertable.getClass().getSimpleName().toLowerCase(), this.filename, -1L, null, insertable.toString(), sqlue);
		}
		return 0;
	}
}
