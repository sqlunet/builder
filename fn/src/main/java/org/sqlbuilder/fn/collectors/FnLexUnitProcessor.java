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
			final int frameid = _lexunit.getFrameID();
			LexUnit.make(_lexunit);

			// H E A D E R

			for (var _corpus : _lexunit.getHeader().getCorpusArray())
			{
				Corpus.make(_corpus, luid);
				for (var _doc : _corpus.getDocumentArray())
				{
					Doc.make(_doc, _corpus);
				}
			}

			// L E X E M E S

			for (var _lexeme : _lexunit.getLexemeArray())
			{
				Lexeme.make(_lexeme, luid);
			}

			// S E M T Y P E S

			for (var _semtype : _lexunit.getSemTypeArray())
			{
				LexUnit_SemType.make(luid, _semtype);
			}

			// V A L E N C E S

			final ValencesType _valences = _lexunit.getValences();

			// g o v e r n o r s

			for (var _governor : _valences.getGovernorArray())
			{
				final Governor governor = Governor.make(_governor);
				LexUnit_Governor.make(luid, governor);

				for (var _annoset : _governor.getAnnoSetArray())
				{
					Governor_AnnoSet.make(governor, _annoset);
				}
			}

			// F E r e a l i z a t i o n s

			for (var _fer : _valences.getFERealizationArray())
			{
				final FERealization fer = FERealization.make(_fer, luid, frameid);

				// p a t t e r n s
				for (var _pattern : _fer.getPatternArray())
				{
					// v a l e n c e u n i t
					final ValenceUnitType _valenceunit = _pattern.getValenceUnit();
					final ValenceUnit valenceunit = ValenceUnit.make(_valenceunit);
					ValenceUnit_FERealization.make(valenceunit, fer);

					// a n n o s e t s
					for (var _annoset : _pattern.getAnnoSetArray())
					{
						ValenceUnit_AnnoSet.make(valenceunit, _annoset.getID());
					}
				}
			}

			// F E g r o u p r e a l i z a t i o n s

			for (var _fegr : _valences.getFEGroupRealizationArray())
			{
				final FEGroupRealization fegr = FEGroupRealization.make(_fegr, luid, frameid);

				// f e s
				for (var _fe : _fegr.getFEArray())
				{
					FE_FEGroupRealization.make(_fe, fegr);
				}

				// p a t t e r n s
				for (var _grouppattern : _fegr.getPatternArray())
				{
					final Pattern grouppattern = Pattern.make(_grouppattern, fegr);

					// v a l e n c e u n i t s
					for (var _valenceunit : _grouppattern.getValenceUnitArray())
					{
						final ValenceUnit valenceunit = ValenceUnit.make(_valenceunit);
						Pattern_ValenceUnit.make(grouppattern, valenceunit);
					}

					// a n n o s e t s
					for (var _annoset : _grouppattern.getAnnoSetArray())
					{
						Pattern_AnnoSet.make(grouppattern, _annoset);
					}
				}
			}

			// S U B C O R P U S

			for (var _subcorpus : _lexunit.getSubCorpusArray())
			{
				final SubCorpus subcorpus = SubCorpus.make(_subcorpus, luid);

				for (var _sentence : _subcorpus.getSentenceArray())
				{
					final Sentence sentence = Sentence.make(_sentence, false);
					SubCorpus_Sentence.make(subcorpus, sentence);

					for (var _annoset : _sentence.getAnnotationSetArray())
					{
						try
						{
							AnnotationSet.make(_annoset, _sentence.getID(), luid, _lexunit.getFrameID());
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
							final Layer layer = Layer.make(_layer, _annoset.getID());

							// labels
							for (var _label : _layer.getLabelArray())
							{
								Label.make(_label, layer);
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
