package org.sqlbuilder.fn;

import org.apache.xmlbeans.XmlException;
import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Progress;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import edu.berkeley.icsi.framenet.*;
import edu.berkeley.icsi.framenet.CorpDocType.Document;
import edu.berkeley.icsi.framenet.FullTextAnnotationDocument.FullTextAnnotation;
import edu.berkeley.icsi.framenet.HeaderType.Frame;
import edu.berkeley.icsi.framenet.HeaderType.Frame.FE;

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
		// System.out.printf("file=<%s>\n", xmlFile);
		try
		{
			final FullTextAnnotationDocument document = FullTextAnnotationDocument.Factory.parse(xmlFile);

			// F U L L T E X T A N N O T A T I O N

			final FullTextAnnotation fulltextannotation = document.getFullTextAnnotation();

			// H E A D E R

			final HeaderType header = fulltextannotation.getHeader();

			// F R A M E

			final Frame frame = header.getFrame();
			if (frame != null)
			{
				// F E

				final FE[] fes = frame.getFEArray();
				for (final FE fe : fes)
				{
					final String fEName = fe.getName();
					final String type = fe.getType().toString();
					System.err.printf("%s %s%n", fEName, type);
				}
			}

			// C O R P U S

			final CorpDocType[] corpuses = header.getCorpusArray();
			for (final CorpDocType corpus : corpuses)
			{
				final FnCorpus fnCorpus = new FnCorpus(corpus, null);
				final boolean isNew = FnCorpus.SET.add(fnCorpus);
				if (!isNew)
				{
					Logger.instance.logWarn(FnModule.MODULE_ID, this.tag, "corpus-duplicate", fileName, -1, null, fnCorpus.toString());
				}

				final Document[] documents = corpus.getDocumentArray();
				for (final Document document2 : documents)
				{
					final FnDocument fnDocument = new FnDocument(fnCorpus, document2);
					FnDocument.SET.add(fnDocument);
				}
			}

			// S E N T E N C E S

			final SentenceType[] sentences = fulltextannotation.getSentenceArray();
			for (final SentenceType sentence : sentences)
			{
				final FnSentence fnSentence = new FnSentence(sentence, true);
				final boolean isNew = FnSentence.SET.add(fnSentence);
				if (!isNew)
				{
					Logger.instance.logWarn(FnModule.MODULE_ID, this.tag, "sentence-duplicate", fileName, -1, null, fnSentence.toString());
				}

				// annotation sets
				final AnnotationSetType[] annosets = sentence.getAnnotationSetArray();
				for (final AnnotationSetType annoset : annosets)
				{
					final FnAnnotationSet fnAnnotationSet = new FnAnnotationSet(fnSentence, annoset);
					final boolean isNew2 = FnAnnotationSet.SET.add(fnAnnotationSet);
					if (!isNew2)
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
