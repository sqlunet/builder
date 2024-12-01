package org.sqlbuilder.fn.collectors;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.AlreadyFoundException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.fn.FnModule;
import org.sqlbuilder.fn.objects.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import edu.berkeley.icsi.framenet.FullTextAnnotationDocument;
import edu.berkeley.icsi.framenet.FullTextAnnotationDocument.FullTextAnnotation;
import edu.berkeley.icsi.framenet.HeaderType;
import edu.berkeley.icsi.framenet.HeaderType.Frame;

public class FnFullTextCollector extends FnCollector
{
	public FnFullTextCollector(final Properties props)
	{
		super("fulltext", props, "fulltext");
	}

	@Override
	protected void processFrameNetFile(final String fileName)
	{
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
				Corpus.make(_corpus, null);
				for (var _doc : _corpus.getDocumentArray())
				{
					Doc.make(_doc, _corpus);
				}
			}

			// S E N T E N C E S

			for (var _sentence : _fulltextannotation.getSentenceArray())
			{
				int sentenceid = _sentence.getID();
				Sentence.make(_sentence);

				// annotation sets
				for (var _annoset : _sentence.getAnnotationSetArray())
				{
					try
					{
						AnnotationSet.make(_annoset, sentenceid);
					}
					catch (AlreadyFoundException afe)
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
		catch (XmlException | IOException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, tag, fileName, e);
		}
	}
}
