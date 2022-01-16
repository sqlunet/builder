package org.sqlbuilder.fn;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.Insertable;
import org.sqlbuilder.Logger;
import org.sqlbuilder.NotFoundException;
import org.sqlbuilder.Progress;
import org.sqlbuilder.SQLUpdateException;
import org.sqlbuilder.wordnet.db.DBWordFinder;
import org.sqlbuilder.wordnet.id.WordId;

import edu.berkeley.icsi.framenet.AnnoSetType;
import edu.berkeley.icsi.framenet.AnnotationSetType;
import edu.berkeley.icsi.framenet.CorpDocType;
import edu.berkeley.icsi.framenet.CorpDocType.Document;
import edu.berkeley.icsi.framenet.FEGroupRealizationType;
import edu.berkeley.icsi.framenet.FERealizationType;
import edu.berkeley.icsi.framenet.FEValenceType;
import edu.berkeley.icsi.framenet.GovernorType;
import edu.berkeley.icsi.framenet.HeaderType;
import edu.berkeley.icsi.framenet.LabelType;
import edu.berkeley.icsi.framenet.LayerType;
import edu.berkeley.icsi.framenet.LexUnitDocument;
import edu.berkeley.icsi.framenet.LexUnitDocument.LexUnit;
import edu.berkeley.icsi.framenet.LexemeType;
import edu.berkeley.icsi.framenet.SemTypeRefType;
import edu.berkeley.icsi.framenet.SentenceType;
import edu.berkeley.icsi.framenet.SubCorpusType;
import edu.berkeley.icsi.framenet.ValenceUnitType;
import edu.berkeley.icsi.framenet.ValencesType;

public class FnLexUnitProcessor extends FnProcessor
{
	private final boolean matchToWn;

	private final boolean skipLayers;

	public FnLexUnitProcessor(final Properties props)
	{
		super("lu", props);
		this.processorTag = "lu";
		this.matchToWn = props.getProperty("fnmatchwn", "").compareToIgnoreCase("true") == 0;
		this.skipLayers = props.getProperty("fnskiplayers", "").compareToIgnoreCase("true") == 0;
	}

	@Override
	protected int processFrameNetFile(final String fileName, final String name, final Connection connection)
	{
		if (Logger.verbose)
		{
			Progress.traceHeader("framenet (lu)", name);
		}

		// clear map
		FnValenceUnitBase.clearMap();

		final int count = 0;
		final File file = new File(fileName);
		// System.out.printf("file=<%s>\n", file);
		try
		{
			final LexUnitDocument document = LexUnitDocument.Factory.parse(file);

			// L E X U N I T

			final LexUnit lu = document.getLexUnit();
			final long luid = lu.getID();
			final long frameid = lu.getFrameID();

			final FnLexUnit lexUnit = new FnLexUnit(lu);
			// System.out.println(lexUnit);
			final int insertLexUnitCount = insert(lexUnit, connection);
			if (insertLexUnitCount == 0)
			{
				// Exception now raised
				Logger.instance.logWarn(FnModule.MODULE_ID, this.processorTag, "lu-duplicate", fileName, -1, null, lexUnit.toString());
			}

			// H E A D E R

			final HeaderType header = lu.getHeader();
			final CorpDocType[] corpuses = header.getCorpusArray();
			for (final CorpDocType corpus : corpuses)
			{
				final FnCorpus fnCorpus = new FnCorpus(corpus, luid);
				insert(fnCorpus, connection);

				final Document[] docs = corpus.getDocumentArray();
				for (final Document doc : docs)
				{
					final FnDocument fnDocument = new FnDocument(corpus.getID(), doc);
					insert(fnDocument, connection);
				}
			}

			// L E X E M E S

			final LexemeType[] lexemes = lu.getLexemeArray();
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

			// S E M T Y P E S

			for (final SemTypeRefType semtyperef : lu.getSemTypeArray())
			{
				final FnLexUnit_SemType fnLu_SemType = new FnLexUnit_SemType(luid, semtyperef.getID());
				insert(fnLu_SemType, connection);
			}

			// V A L E N C E S

			final ValencesType valences = lu.getValences();

			// g o v e r n o r s

			final GovernorType[] governors = valences.getGovernorArray();
			for (final GovernorType governor : governors)
			{
				final String word = governor.getLemma();
				final WordId wordId = this.matchToWn ? DBWordFinder.findOrCreateWord(word, connection) : null;

				final FnWord fnWord = new FnWord(word, wordId);
				insert(fnWord, connection);
				final long fnwordid = fnWord.getId();

				final FnGovernor fnGovernor = new FnGovernor(fnwordid, governor);
				final long governorid = fnGovernor.getId();
				insert(fnGovernor, connection);

				final FnLexUnit_Governor fnLexUnit_Governor = new FnLexUnit_Governor(luid, governorid);
				insert(fnLexUnit_Governor, connection);

				final AnnoSetType[] annosets = governor.getAnnoSetArray();
				for (final AnnoSetType annoset : annosets)
				{
					final FnGovernor_AnnoSet fnGovernor_Annoset = new FnGovernor_AnnoSet(governorid, annoset.getID());
					insert(fnGovernor_Annoset, connection);
				}
			}

			// F E r e a l i z a t i o n s

			final FERealizationType[] FErealizations = valences.getFERealizationArray();
			for (final FERealizationType fer : FErealizations)
			{
				final FnFERealization fnFERealization = new FnFERealization(luid, fer);
				final long ferid = fnFERealization.getId();
				insert(fnFERealization, connection);

				// p a t t e r n s
				final FERealizationType.Pattern[] patterns = fer.getPatternArray();
				for (final FERealizationType.Pattern pattern : patterns)
				{
					// v a l e n c e u n i t
					final ValenceUnitType vu = pattern.getValenceUnit();
					final FnValenceUnit fnValenceUnit = new FnValenceUnit(ferid, vu);
					final long vuid = fnValenceUnit.getId();
					insert(fnValenceUnit, connection);

					// a n n o s e t s
					final AnnoSetType[] annosets = pattern.getAnnoSetArray();
					for (final AnnoSetType annoset : annosets)
					{
						final FnValenceUnit_AnnoSet fnAnnosetRef = new FnValenceUnit_AnnoSet(vuid, annoset.getID());
						insert(fnAnnosetRef, connection);
					}
				}
			}

			// F E g r o u p r e a l i z a t i o n s

			final FEGroupRealizationType[] FEgrealizations = valences.getFEGroupRealizationArray();
			for (final FEGroupRealizationType fegr : FEgrealizations)
			{
				final FnFEGroupRealization fERealization = new FnFEGroupRealization(luid, fegr);
				final long fegrid = fERealization.getId();
				insert(fERealization, connection);

				// f e s
				final FEValenceType[] fevs = fegr.getFEArray();
				for (final FEValenceType fev : fevs)
				{
					final FnFEGroupRealization_Fe fnFE = new FnFEGroupRealization_Fe(fegrid, fev);
					insert(fnFE, connection);
				}

				// p a t t e r n s
				final FEGroupRealizationType.Pattern[] patterns = fegr.getPatternArray();
				for (final FEGroupRealizationType.Pattern pattern : patterns)
				{
					final FnGroupPattern groupPattern = new FnGroupPattern(fegrid, pattern);
					final long patternid = groupPattern.getId();
					insert(groupPattern, connection);

					// v a l e n c e u n i t s
					final ValenceUnitType[] vus = pattern.getValenceUnitArray();
					for (final ValenceUnitType vu : vus)
					{
						final FnValenceUnitBase valenceUnit = new FnValenceUnitBase(vu);
						final Long vuid = valenceUnit.findId();
						if (vuid == null)
						{
							Logger.instance.logWarn(FnModule.MODULE_ID, this.processorTag, "vu-not-found", fileName, -1, null, valenceUnit.toString());
							continue;
						}
						final FnPattern_ValenceUnit pattern_ValenceUnit = new FnPattern_ValenceUnit(patternid, vuid, vu.getFE());
						insert(pattern_ValenceUnit, connection);
					}

					// a n n o s e t s
					final AnnoSetType[] annosets = pattern.getAnnoSetArray();
					for (final AnnoSetType annoset : annosets)
					{
						final FnPattern_AnnoSet fnPattern_Annoset = new FnPattern_AnnoSet(patternid, annoset.getID());
						insert(fnPattern_Annoset, connection);
					}
				}
			}

			// S U B C O R P U S

			final SubCorpusType[] subcorpuses = lu.getSubCorpusArray();
			for (final SubCorpusType subcorpus : subcorpuses)
			{
				final FnSubCorpus fnSubCorpus = new FnSubCorpus(luid, subcorpus);
				final long subcorpusid = fnSubCorpus.getId();
				insert(fnSubCorpus, connection);

				final SentenceType[] sentences = subcorpus.getSentenceArray();
				for (final SentenceType sentence : sentences)
				{
					final FnSentence fnSentence = new FnSentence(sentence, false);
					final long sentenceid = fnSentence.getId();
					final int insertSentenceCount = insert(fnSentence, connection);
					if (insertSentenceCount == 0)
					{
						Logger.instance.logWarn(FnModule.MODULE_ID, this.processorTag, "sentence-duplicate", fileName, -1, null, fnSentence.toString());
					}

					final FnSubCorpus_Sentence fnSubCorpus_Sentence = new FnSubCorpus_Sentence(subcorpusid, sentenceid);
					insert(fnSubCorpus_Sentence, connection);

					final AnnotationSetType[] annosets = sentence.getAnnotationSetArray();
					for (final AnnotationSetType annoset : annosets)
					{
						final FnAnnotationSet fnAnnotationSet = new FnAnnotationSet(sentenceid, annoset, frameid, luid);
						final long annosetid = fnAnnotationSet.getId();
						final int insertAnnosetCount = insert(fnAnnotationSet, connection);
						if (insertAnnosetCount == 0 || this.skipLayers)
						{
							continue;
						}

						// layers
						final LayerType[] layerTypes = annoset.getLayerArray();
						for (final LayerType layerType : layerTypes)
						{
							final FnLayer layer = new FnLayer(annosetid, layerType);
							final long layerid = layer.getId();
							insert(layer, connection);

							// labels
							final LabelType[] labelTypes = layerType.getLabelArray();
							for (final LabelType labelType : labelTypes)
							{
								final FnLabel label = new FnLabel(layerid, labelType);
								insert(label, connection);
							}
						}
					}
				}
			}
		}
		catch (XmlException | IOException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, this.processorTag, "xml-document", fileName, -1L, null, "document=[" + fileName + "]", e);
		}
		if (Logger.verbose)
		{
			Progress.traceTailer("framenet (lu)", name, count);
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
