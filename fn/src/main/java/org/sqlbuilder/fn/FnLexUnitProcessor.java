package org.sqlbuilder.fn;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import edu.berkeley.icsi.framenet.*;
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
	protected void processFrameNetFile(final String fileName, final String name)
	{
		if (Logger.verbose)
		{
			Progress.traceHeader("framenet (lu)", name);
		}

		// clear map
		FnValenceUnitBase.MAP.clear();

		final int count = 0;
		final File file = new File(fileName);
		try
		{
			final LexUnitDocument _document = LexUnitDocument.Factory.parse(file);

			// L E X U N I T

			final LexUnit _lexunit = _document.getLexUnit();
			final long luid = _lexunit.getID();
			final FnLexUnit lexunit = new FnLexUnit(_lexunit);
			final boolean isNew = FnLexUnit.SET.add(lexunit);
			if (!isNew)
			{
				Logger.instance.logWarn(FnModule.MODULE_ID, this.tag, "lu-duplicate", fileName, -1, null, lexunit.toString());
			}

			// H E A D E R

			for (var _corpus : _lexunit.getHeader().getCorpusArray())
			{
				final FnCorpus corpus = new FnCorpus(_corpus, lexunit);
				FnCorpus.SET.add(corpus);

				for (var _doc : _corpus.getDocumentArray())
				{
					final FnDocument doc = new FnDocument(corpus, _doc);
					FnDocument.SET.add(doc);
				}
			}

			// L E X E M E S

			for (var _lexeme : _lexunit.getLexemeArray())
			{
				final String lemma = FnLexeme.makeWord(_lexeme.getName());

				final FnWord word = new FnWord(lemma);
				FnWord.SET.add(word);

				final FnLexeme lexeme = new FnLexeme(luid, word, _lexeme);
				FnLexeme.SET.add(lexeme);
			}

			// S E M T Y P E S

			for (var _semtype : _lexunit.getSemTypeArray())
			{
				final FnLexUnit_SemType lexunit_semtype = new FnLexUnit_SemType(luid, _semtype);
				FnLexUnit_SemType.SET.add(lexunit_semtype);
			}

			// V A L E N C E S

			final ValencesType _valences = _lexunit.getValences();

			// g o v e r n o r s

			for (var _governor : _valences.getGovernorArray())
			{
				final String lemma = _governor.getLemma();

				final FnWord word = new FnWord(lemma);
				FnWord.SET.add(word);

				final FnGovernor governor = new FnGovernor(word, _governor);
				FnGovernor.SET.add(governor);

				final FnLexUnit_Governor lexunit_governor = new FnLexUnit_Governor(luid, governor);
				FnLexUnit_Governor.SET.add(lexunit_governor);

				for (var _annoset : _governor.getAnnoSetArray())
				{
					final FnGovernor_AnnoSet governor_annoset = new FnGovernor_AnnoSet(governor, _annoset);
					FnGovernor_AnnoSet.SET.add(governor_annoset);
				}
			}

			// F E r e a l i z a t i o n s

			for (var _fer : _valences.getFERealizationArray())
			{
				final FnFERealization fer = new FnFERealization(luid, _fer);
				FnFERealization.SET.add(fer);

				// p a t t e r n s
				for (var _pattern : _fer.getPatternArray())
				{
					// v a l e n c e u n i t
					final ValenceUnitType _valenceunit = _pattern.getValenceUnit();
					final FnValenceUnit valenceunit = new FnValenceUnit(fer, _valenceunit);
					FnValenceUnit.SET.add(valenceunit);

					// a n n o s e t s
					for (var _annoset : _pattern.getAnnoSetArray())
					{
						final FnValenceUnit_AnnoSet valenceunit_annoset = new FnValenceUnit_AnnoSet(valenceunit, _annoset);
						FnValenceUnit_AnnoSet.SET.add(valenceunit_annoset);
					}
				}
			}

			// F E g r o u p r e a l i z a t i o n s

			for (var _fegr : _valences.getFEGroupRealizationArray())
			{
				final FnFEGroupRealization fegr = new FnFEGroupRealization(luid, _fegr);
				FnFEGroupRealization.SET.add(fegr);

				// f e s
				for (var _fe : _fegr.getFEArray())
				{
					final FnFEGroupRealization_Fe fegr_fe = new FnFEGroupRealization_Fe(fegr, _fe);
					FnFEGroupRealization_Fe.SET.add(fegr_fe);
				}

				// p a t t e r n s
				for (var _grouppattern : _fegr.getPatternArray())
				{
					final FnGroupPattern grouppattern = new FnGroupPattern(fegr, _grouppattern);
					FnGroupPattern.SET.add(grouppattern);

					// v a l e n c e u n i t s
					for (var _valenceunit : _grouppattern.getValenceUnitArray())
					{
						final FnValenceUnitBase valenceunit = new FnValenceUnitBase(_valenceunit);
						FnValenceUnitBase.SET.add(valenceunit);

						final FnPattern_ValenceUnit pattern_valenceunit = new FnPattern_ValenceUnit(grouppattern, valenceunit);
						FnPattern_ValenceUnit.SET.add(pattern_valenceunit);
					}

					// a n n o s e t s
					for (var _annoset : _grouppattern.getAnnoSetArray())
					{
						final FnPattern_AnnoSet pattern_annoset = new FnPattern_AnnoSet(grouppattern, _annoset);
						FnPattern_AnnoSet.SET.add(pattern_annoset);
					}
				}
			}

			// S U B C O R P U S

			for (var _subcorpus : _lexunit.getSubCorpusArray())
			{
				final FnSubCorpus subcorpus = new FnSubCorpus(luid, _subcorpus);
				FnSubCorpus.SET.add(subcorpus);

				for (var _sentence : _subcorpus.getSentenceArray())
				{
					final FnSentence sentence = new FnSentence(_sentence, false);
					final boolean isNew2 = FnSentence.SET.add(sentence);
					if (!isNew2)
					{
						Logger.instance.logWarn(FnModule.MODULE_ID, this.tag, "sentence-duplicate", fileName, -1, null, sentence.toString());
					}

					final FnSubCorpus_Sentence subcorpus_sentence = new FnSubCorpus_Sentence(subcorpus, sentence);
					FnSubCorpus_Sentence.SET.add(subcorpus_sentence);

					for (final AnnotationSetType _annoset : _sentence.getAnnotationSetArray())
					{
						final FnAnnotationSet annoset = new FnAnnotationSet(_sentence.getID(), _annoset, luid, _lexunit.getFrameID());
						final boolean isNew3 = FnAnnotationSet.SET.add(annoset);
						if (!isNew3 || this.skipLayers)
						{
							continue;
						}

						// layers
						for (var _layer : _annoset.getLayerArray())
						{
							final FnLayer layer = new FnLayer(annoset, _layer);
							FnLayer.SET.add(layer);

							// labels
							for (var _label : _layer.getLabelArray())
							{
								final FnLabel label = new FnLabel(layer, _label);
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
	}
}
