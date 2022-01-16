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

import edu.berkeley.icsi.framenet.AnnotationSetType;
import edu.berkeley.icsi.framenet.CorpDocType;
import edu.berkeley.icsi.framenet.CorpDocType.Document;
import edu.berkeley.icsi.framenet.FullTextAnnotationDocument;
import edu.berkeley.icsi.framenet.FullTextAnnotationDocument.FullTextAnnotation;
import edu.berkeley.icsi.framenet.HeaderType;
import edu.berkeley.icsi.framenet.HeaderType.Frame;
import edu.berkeley.icsi.framenet.HeaderType.Frame.FE;
import edu.berkeley.icsi.framenet.LabelType;
import edu.berkeley.icsi.framenet.LayerType;
import edu.berkeley.icsi.framenet.SentenceType;

public class FnFullTextProcessor extends FnProcessor
{
	public FnFullTextProcessor(final Properties props)
	{
		super("fulltext", props);
		this.processorTag = "fulltext";
	}

	@Override
	protected int processFrameNetFile(final String fileName, final String name, final Connection connection)
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
				final long corpusid = corpus.getID();
				final FnCorpus fnCorpus = new FnCorpus(corpus, 0);
				final int insertCorpusCount = insert(fnCorpus, connection);
				if (insertCorpusCount == 0)
				{
					Logger.instance.logWarn(FnModule.MODULE_ID, this.processorTag, "corpus-duplicate", fileName, -1, null, fnCorpus.toString());
				}

				final Document[] documents = corpus.getDocumentArray();
				for (final Document document2 : documents)
				{
					final FnDocument fnDocument = new FnDocument(corpusid, document2);
					insert(fnDocument, connection);
				}
			}

			// S E N T E N C E S

			final SentenceType[] sentences = fulltextannotation.getSentenceArray();
			for (final SentenceType sentence : sentences)
			{
				final FnSentence fnSentence = new FnSentence(sentence, true);
				final long sentenceid = fnSentence.getId();
				final int insertCount = insert(fnSentence, connection);
				if (insertCount == 0)
				{
					Logger.instance.logWarn(FnModule.MODULE_ID, this.processorTag, "sentence-duplicate", fileName, -1, null, fnSentence.toString());
				}

				// annotation sets
				final AnnotationSetType[] annosets = sentence.getAnnotationSetArray();
				for (final AnnotationSetType annoset : annosets)
				{
					final FnAnnotationSet fnAnnotationSet = new FnAnnotationSet(sentenceid, annoset);
					final long annosetid = fnAnnotationSet.getId();
					final int insertCount2 = insert(fnAnnotationSet, connection);
					if (insertCount2 == 0)
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
		catch (XmlException | IOException e)
		{
			Logger.instance.logXmlException(FnModule.MODULE_ID, this.processorTag, "xml-document", fileName, -1L, null, "document=[" + fileName + "]", e);
		}
		if (Logger.verbose)
		{
			Progress.traceTailer("framenet (fulltext)", name, count);
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
