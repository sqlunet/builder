package org.sqlbuilder.fn;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import edu.berkeley.icsi.framenet.*;
import edu.berkeley.icsi.framenet.CorpDocType.Document;
import edu.berkeley.icsi.framenet.LexUnitDocument.LexUnit;

public class FnLexUnitProcessor extends FnProcessor
{
	private final boolean skipLayers;

	public FnLexUnitProcessor(final Properties props)
	{
		super("lu", props, "lu");
		this.skipLayers = props.getProperty("fnskiplayers", "").compareToIgnoreCase("true") == 0;
	}

	@Override
	protected int processFrameNetFile(final String fileName, final String name)
	{
		if (Logger.verbose)
		{
			Progress.traceHeader("framenet (lu)", name);
		}

		// clear map
		// FnValenceUnitBase.clearMap();

		final int count = 0;
		final File file = new File(fileName);
		// System.out.printf("file=<%s>\n", file);
		try
		{
			final LexUnitDocument document = LexUnitDocument.Factory.parse(file);

			// L E X U N I T

			final LexUnit lu = document.getLexUnit();
			final long frameid = lu.getFrameID();

			final FnLexUnit lexUnit = new FnLexUnit(lu);
			// System.out.println(lexUnit);
			final boolean isNew = FnLexUnit.SET.add(lexUnit);
			if (!isNew)
			{
				// Exception now raised
				Logger.instance.logWarn(FnModule.MODULE_ID, this.tag, "lu-duplicate", fileName, -1, null, lexUnit.toString());
			}

			// H E A D E R

			final HeaderType header = lu.getHeader();
			final CorpDocType[] corpuses = header.getCorpusArray();
			for (final CorpDocType corpus : corpuses)
			{
				final FnCorpus fnCorpus = new FnCorpus(corpus, lexUnit);
				FnCorpus.SET.add(fnCorpus);

				final Document[] docs = corpus.getDocumentArray();
				for (final Document doc : docs)
				{
					final FnDocument fnDocument = new FnDocument(fnCorpus, doc);
					FnDocument.SET.add(fnDocument);
				}
			}

			// L E X E M E S

			final LexemeType[] lexemes = lu.getLexemeArray();
			for (final LexemeType lexeme : lexemes)
			{
				final String word = FnLexeme.makeWord(lexeme.getName());

				final FnWord fnWord = new FnWord(word);
				FnWord.SET.add(fnWord);

				final FnLexeme fnLexeme = new FnLexeme(lexUnit.lu.getID(), fnWord, lexeme);
				FnLexeme.SET.add(fnLexeme);
			}

			// S E M T Y P E S

			for (final SemTypeRefType semtyperef : lu.getSemTypeArray())
			{
				final FnLexUnit_SemType fnLu_SemType = new FnLexUnit_SemType(lexUnit.lu.getID(), semtyperef);
				FnLexUnit_SemType.SET.add(fnLu_SemType);
			}

			// V A L E N C E S

			final ValencesType valences = lu.getValences();

			// g o v e r n o r s

			final GovernorType[] governors = valences.getGovernorArray();
			for (final GovernorType governor : governors)
			{
				final String word = governor.getLemma();
				final FnWord fnWord = new FnWord(word);
				FnWord.SET.add(fnWord);

				final FnGovernor fnGovernor = new FnGovernor(fnWord, governor);
				FnGovernor.SET.add(fnGovernor);

				final FnLexUnit_Governor fnLexUnit_Governor = new FnLexUnit_Governor(lexUnit, fnGovernor);
				FnLexUnit_Governor.SET.add(fnLexUnit_Governor);

				final AnnoSetType[] annosets = governor.getAnnoSetArray();
				for (final AnnoSetType annoset : annosets)
				{
					final FnGovernor_AnnoSet fnGovernor_Annoset = new FnGovernor_AnnoSet(fnGovernor, annoset);
					FnGovernor_AnnoSet.SET.add(fnGovernor_Annoset);
				}
			}

			// F E r e a l i z a t i o n s

			final FERealizationType[] FErealizations = valences.getFERealizationArray();
			for (final FERealizationType fer : FErealizations)
			{
				final FnFERealization fnFERealization = new FnFERealization(lexUnit, fer);
				FnFERealization.SET.add(fnFERealization);

				// p a t t e r n s
				final FERealizationType.Pattern[] patterns = fer.getPatternArray();
				for (final FERealizationType.Pattern pattern : patterns)
				{
					// v a l e n c e u n i t
					final ValenceUnitType vu = pattern.getValenceUnit();
					final FnValenceUnit fnValenceUnit = new FnValenceUnit(fnFERealization, vu);
					FnValenceUnit.SET.add(fnValenceUnit);

					// a n n o s e t s
					final AnnoSetType[] annosets = pattern.getAnnoSetArray();
					for (final AnnoSetType annoset : annosets)
					{
						final FnValenceUnit_AnnoSet fnAnnosetRef = new FnValenceUnit_AnnoSet(fnValenceUnit, annoset);
						FnValenceUnit_AnnoSet.SET.add(fnAnnosetRef);
					}
				}
			}

			// F E g r o u p r e a l i z a t i o n s

			final FEGroupRealizationType[] FEgrealizations = valences.getFEGroupRealizationArray();
			for (final FEGroupRealizationType fegr : FEgrealizations)
			{
				final FnFEGroupRealization fEGroupRealization = new FnFEGroupRealization(lexUnit, fegr);
				FnFEGroupRealization.SET.add(fEGroupRealization);

				// f e s
				final FEValenceType[] fevs = fegr.getFEArray();
				for (final FEValenceType fev : fevs)
				{
					final FnFEGroupRealization_Fe fnFE = new FnFEGroupRealization_Fe(fEGroupRealization, fev);
					FnFEGroupRealization_Fe.SET.add(fnFE);
				}

				// p a t t e r n s
				final FEGroupRealizationType.Pattern[] patterns = fegr.getPatternArray();
				for (final FEGroupRealizationType.Pattern pattern : patterns)
				{
					final FnGroupPattern groupPattern = new FnGroupPattern(fEGroupRealization, pattern);
					FnGroupPattern.SET.add(groupPattern);

					// v a l e n c e u n i t s
					final ValenceUnitType[] vus = pattern.getValenceUnitArray();
					for (final ValenceUnitType vu : vus)
					{
						final FnValenceUnitBase valenceUnit = new FnValenceUnitBase(vu);
						FnValenceUnitBase.SET.add(valenceUnit);

						final FnPattern_ValenceUnit pattern_ValenceUnit = new FnPattern_ValenceUnit(groupPattern, valenceUnit);
						FnPattern_ValenceUnit.SET.add(pattern_ValenceUnit);
					}

					// a n n o s e t s
					final AnnoSetType[] annosets = pattern.getAnnoSetArray();
					for (final AnnoSetType annoset : annosets)
					{
						final FnPattern_AnnoSet fnPattern_Annoset = new FnPattern_AnnoSet(groupPattern, annoset);
						FnPattern_AnnoSet.SET.add(fnPattern_Annoset);
					}
				}
			}

			// S U B C O R P U S

			final SubCorpusType[] subcorpuses = lu.getSubCorpusArray();
			for (final SubCorpusType subcorpus : subcorpuses)
			{
				final FnSubCorpus fnSubCorpus = new FnSubCorpus(lexUnit, subcorpus);
				FnSubCorpus.SET.add(fnSubCorpus);

				final SentenceType[] sentences = subcorpus.getSentenceArray();
				for (final SentenceType sentence : sentences)
				{
					final FnSentence fnSentence = new FnSentence(sentence, false);
					final boolean isNew2 = FnSentence.SET.add(fnSentence);
					if (!isNew2)
					{
						Logger.instance.logWarn(FnModule.MODULE_ID, this.tag, "sentence-duplicate", fileName, -1, null, fnSentence.toString());
					}

					final FnSubCorpus_Sentence fnSubCorpus_Sentence = new FnSubCorpus_Sentence(fnSubCorpus, fnSentence);
					FnSubCorpus_Sentence.SET.add(fnSubCorpus_Sentence);

					final AnnotationSetType[] annosets = sentence.getAnnotationSetArray();
					for (final AnnotationSetType annoset : annosets)
					{
						final FnAnnotationSet fnAnnotationSet = new FnAnnotationSet(fnSentence, annoset, lexUnit);
						final boolean isNew3 = FnAnnotationSet.SET.add(fnAnnotationSet);
						if (!isNew3 || this.skipLayers)
						{
							continue;
						}

						// layers
						final LayerType[] layerTypes = annoset.getLayerArray();
						for (final LayerType layerType : layerTypes)
						{
							final FnLayer layer = new FnLayer(fnAnnotationSet, layerType);
							FnLayer.SET.add(layer);

							// labels
							final LabelType[] labelTypes = layerType.getLabelArray();
							for (final LabelType labelType : labelTypes)
							{
								final FnLabel label = new FnLabel(layer, labelType);
								FnLabel.SET.add(label);
							}
						}
					}
				}
			}
		}
		catch (XmlException | IOException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, this.tag, "xml-document", fileName, -1, null, "document=[" + fileName + "]", e);
		}
		if (Logger.verbose)
		{
			Progress.traceTailer("framenet (lu) " + name, Integer.toString(count));
		}
		return 1;
	}
}
