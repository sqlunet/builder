package org.sqlbuilder.fn.collectors;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.fn.objects.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import edu.berkeley.icsi.framenet.*;
import edu.berkeley.icsi.framenet.FullTextAnnotationDocument.FullTextAnnotation;
import edu.berkeley.icsi.framenet.HeaderType.Frame;

public class FnFullTextProcessor extends FnProcessor
{
	public FnFullTextProcessor(final Properties props)
	{
		super("fulltext", props, "fulltext");
	}

	@Override
	protected void processFrameNetFile(final String fileName, final String name)
	{
		if (Logger.verbose)
		{
			Progress.traceHeader("framenet (fulltext)", name);
		}
		final int count = 0;
		final File xmlFile = new File(fileName);
		try
		{
			final FullTextAnnotationDocument _document = FullTextAnnotationDocument.Factory.parse(xmlFile);

			// F U L L T E X T A N N O T A T I O N

			final FullTextAnnotation _fulltextannotation = _document.getFullTextAnnotation();

			// H E A D E R

			final HeaderType _header = _fulltextannotation.getHeader();

			// F R A M E

			final Frame _frame = _header.getFrame();
			if (_frame != null)
			{
				// F E

				for (var _fe : _frame.getFEArray())
				{
					System.err.printf("%s %s%n", _fe.getName(), _fe.getType().toString());
				}
			}

			// C O R P U S

			for (var _corpus : _header.getCorpusArray())
			{
				final Corpus fnCorpus = new Corpus(_corpus, null);
				final boolean isNew = Corpus.SET.add(fnCorpus);
				if (!isNew)
				{
					Logger.instance.logWarn(FnModule.MODULE_ID, this.tag, "corpus-duplicate", fileName, -1, null, fnCorpus.toString());
				}

				for (var _doc : _corpus.getDocumentArray())
				{
					final Doc fnDocument = new Doc(_doc, _corpus);
					Doc.SET.add(fnDocument);
				}
			}

			// S E N T E N C E S

			for (var _sentence : _fulltextannotation.getSentenceArray())
			{
				long sentenceid = _sentence.getID();

				final Sentence fnSentence = new Sentence(_sentence, true);
				final boolean isNew = Sentence.SET.add(fnSentence);
				if (!isNew)
				{
					Logger.instance.logWarn(FnModule.MODULE_ID, this.tag, "sentence-duplicate", fileName, -1, null, fnSentence.toString());
				}

				// annotation sets
				for (var _annoset : _sentence.getAnnotationSetArray())
				{
					final AnnotationSet annoset = new AnnotationSet(_annoset, sentenceid);
					final boolean isNew2 = AnnotationSet.SET.add(annoset);
					if (!isNew2)
					{
						continue;
					}

					// layers
					for (var _layer : _annoset.getLayerArray())
					{
						final Layer layer = new Layer(_layer, _annoset.getID());
						Layer.SET.add(layer);

						// labels
						for (var _label : _layer.getLabelArray())
						{
							final Label label = new Label(_label, layer);
							Label.SET.add(label);
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
			Progress.traceTailer("framenet (fulltext)" + name, Long.toString(count));
		}
	}
}
