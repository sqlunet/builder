package org.sqlbuilder.fn.collectors;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.fn.joins.*;
import org.sqlbuilder.fn.objects.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import edu.berkeley.icsi.framenet.LexUnitDocument;
import edu.berkeley.icsi.framenet.ValenceUnitType;
import edu.berkeley.icsi.framenet.ValencesType;

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
		ValenceUnit.MAP.clear();

		final int count = 0;
		final File file = new File(fileName);
		try
		{
			final LexUnitDocument _document = LexUnitDocument.Factory.parse(file);

			// L E X U N I T

			final LexUnitDocument.LexUnit _lexunit = _document.getLexUnit();
			final int luid = _lexunit.getID();
			final LexUnit lexunit = new LexUnit(_lexunit);

			// H E A D E R

			for (var _corpus : _lexunit.getHeader().getCorpusArray())
			{
				final Corpus corpus = new Corpus(_corpus, luid);
				for (var _doc : _corpus.getDocumentArray())
				{
					final Doc doc = new Doc(_doc, _corpus);
				}
			}

			// L E X E M E S

			for (var _lexeme : _lexunit.getLexemeArray())
			{
				final Lexeme lexeme = new Lexeme(_lexeme, luid);
			}

			// S E M T Y P E S

			for (var _semtype : _lexunit.getSemTypeArray())
			{
				final LexUnit_SemType lexunit_semtype = new LexUnit_SemType(luid, _semtype);
			}

			// V A L E N C E S

			final ValencesType _valences = _lexunit.getValences();

			// g o v e r n o r s

			for (var _governor : _valences.getGovernorArray())
			{
				final Governor governor = new Governor(_governor);
				final LexUnit_Governor lexunit_governor = new LexUnit_Governor(luid, governor);

				for (var _annoset : _governor.getAnnoSetArray())
				{
					final Governor_AnnoSet governor_annoset = new Governor_AnnoSet(governor, _annoset);
				}
			}

			// F E r e a l i z a t i o n s

			for (var _fer : _valences.getFERealizationArray())
			{
				final FERealization fer = new FERealization(_fer, luid);

				// p a t t e r n s
				for (var _pattern : _fer.getPatternArray())
				{
					// v a l e n c e u n i t
					final ValenceUnitType _valenceunit = _pattern.getValenceUnit();
					final ValenceUnit valenceunit = new ValenceUnit(_valenceunit);

					final ValenceUnit_FERealization valenceunit_fer = new ValenceUnit_FERealization(valenceunit, fer);

					// a n n o s e t s
					for (var _annoset : _pattern.getAnnoSetArray())
					{
						final ValenceUnit_AnnoSet valenceunit_annoset = new ValenceUnit_AnnoSet(valenceunit, _annoset);
					}
				}
			}

			// F E g r o u p r e a l i z a t i o n s

			for (var _fegr : _valences.getFEGroupRealizationArray())
			{
				final FEGroupRealization fegr = new FEGroupRealization(_fegr, luid);

				// f e s
				for (var _fe : _fegr.getFEArray())
				{
					final FE_FEGroupRealization fe_fegr = new FE_FEGroupRealization(_fe, fegr);
				}

				// p a t t e r n s
				for (var _grouppattern : _fegr.getPatternArray())
				{
					final Pattern grouppattern = new Pattern(_grouppattern, fegr);

					// v a l e n c e u n i t s
					for (var _valenceunit : _grouppattern.getValenceUnitArray())
					{
						final ValenceUnit valenceunit = new ValenceUnit(_valenceunit);
						final Pattern_ValenceUnit pattern_valenceunit = new Pattern_ValenceUnit(grouppattern, valenceunit);
					}

					// a n n o s e t s
					for (var _annoset : _grouppattern.getAnnoSetArray())
					{
						final Pattern_AnnoSet pattern_annoset = new Pattern_AnnoSet(grouppattern, _annoset);
					}
				}
			}

			// S U B C O R P U S

			for (var _subcorpus : _lexunit.getSubCorpusArray())
			{
				final SubCorpus subcorpus = new SubCorpus(_subcorpus, luid);

				for (var _sentence : _subcorpus.getSentenceArray())
				{
					final Sentence sentence = new Sentence(_sentence, false);
					final SubCorpus_Sentence subcorpus_sentence = new SubCorpus_Sentence(subcorpus, sentence);

					for (var _annoset : _sentence.getAnnotationSetArray())
					{
						try
						{
							final AnnotationSet annoset = new AnnotationSet(_annoset, _sentence.getID(), luid, _lexunit.getFrameID());
						}
						catch (RuntimeException re)
						{
							continue;
						}
						if (this.skipLayers)
						{
							continue;
						}

						// layers
						for (var _layer : _annoset.getLayerArray())
						{
							final Layer layer = new Layer(_layer, _annoset.getID());

							// labels
							for (var _label : _layer.getLabelArray())
							{
								final Label label = new Label(_label, layer);
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
