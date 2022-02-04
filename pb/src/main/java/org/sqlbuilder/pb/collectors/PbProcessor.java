package org.sqlbuilder.pb.collectors;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.pb.PbDocument;
import org.sqlbuilder.pb.objects.Example;
import org.sqlbuilder.pb.PbModule;
import org.sqlbuilder.pb.PbVerbDocument;
import org.sqlbuilder.pb.objects.*;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class PbProcessor extends Processor
{
	protected final Properties conf;

	protected final String propBankHome;

	protected int fileCount;

	public PbProcessor(final Properties conf)
	{
		super("pb");
		this.conf = conf;
		this.propBankHome = conf.getProperty("pbhome", System.getenv().get("PBHOME"));
		this.fileCount = 0;
	}

	@Override
	public void run()
	{
		final File folder = new File(this.propBankHome);
		final FilenameFilter filter = (dir, name) -> name.endsWith(".xml");
		final File[] fileArray = folder.listFiles(filter);
		if (fileArray == null)
			return;
		final List<File> files = Arrays.asList(fileArray);
		files.sort(Comparator.comparing(File::getName));
		this.fileCount = 0;
		Progress.traceHeader("reading propbank files", "");
		for (final File file : files)
		{
			this.fileCount += 1;
			processPropBankFile(file.getAbsolutePath(), file.getName());

			Progress.trace(this.fileCount);
		}
		Progress.traceTailer(this.fileCount);
	}

	@SuppressWarnings("UnusedReturnValue")
	private int processPropBankFile(final String fileName, final String name)
	{
		final String head = name.split("\\.")[0];
		int count = 0;
		try
		{
			final PbVerbDocument document = new PbVerbDocument(fileName);
			count = processFrameset(document, PbDocument.getXPath(document.getDocument(), "./frameset"), head);
		}
		catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException e)
		{
			Logger.instance.logXmlException("pb", this.tag, "xml-document", fileName, -1, null, "document=[" + fileName + "]", e);
		}
		return count;
	}

	protected int processFrameset(final PbVerbDocument document, final Node start, final String head)
	{
		long count = 0;
		try
		{
			// predicates
			final Collection<Predicate> predicates = PbVerbDocument.getPredicates(head, start);
			if (predicates != null)
			{
				for (final Predicate predicate : predicates)
				{
					try
					{
						predicate.put();
					}
					catch (RuntimeException e)
					{
						// Logger.logger.logException(PbModule.id, this.logTag, "predicate", document.getFileName(), -1, "predicate-duplicate", e);
					}
				}
				count += predicates.size();
			}
			final Collection<LexItem> aliasLexItems = PbVerbDocument.getAliasPredicates(start);
			if (aliasLexItems != null)
			{
				for (final LexItem lexItem : aliasLexItems)
				{
					try
					{
						lexItem.put();
					}
					catch (RuntimeException e)
					{
						// Logger.logger.logException(PbModule.id, this.logTag, "lexitem", document.getFileName(), -1, "lexitem-duplicate", e);
					}
				}
				count += aliasLexItems.size();
			}

			// rolesets
			PbVerbDocument.makeRoleSets(head, start);

			// roles
			PbVerbDocument.makeRoles(head, start);

			// examples
			final Collection<Example> examples = PbVerbDocument.getExamples(head, start);
			if (examples != null)
			{
				Example.SET.addAll(examples);
			}

			// args
			final Collection<Arg> args = PbVerbDocument.getExampleArgs(head, start);
			if (args != null)
			{
				Arg.SET.addAll(args);
			}
		}
		catch (XPathExpressionException e)
		{
			Logger.instance.logXmlException(PbModule.MODULE_ID, this.tag, "read-frameset", document.getFileName(), -1, null, "xpath", e);
		}
		return (int) count;
	}
}
