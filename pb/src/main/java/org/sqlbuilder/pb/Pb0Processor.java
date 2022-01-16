package org.sqlbuilder.pb;

import org.sqlbuilder.common.Logger;
import org.sqlbuilder.common.MapFactory;
import org.sqlbuilder.common.Progress;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.Properties;

import javax.xml.xpath.XPathExpressionException;

public class Pb0Processor extends PbProcessor
{
	public Pb0Processor(final Properties conf, final String tag)
	{
		super(conf, tag);
	}

	public Pb0Processor(final Properties conf)
	{
		super(conf, "pb0");
	}

	@Override
	protected void run()
	{
		Progress.traceHeader("propbank pass0", null);
		super.run();

		// roles
		Progress.traceHeader("roles", "allocating");
		PbRole.MAP = MapFactory.makeMap(PbRole.SET);
		Progress.traceTailer("allocated", PbRole.MAP.size());

		// rolesets
		Progress.traceHeader("rolesets", "allocating");
		PbRoleSet.MAP = MapFactory.makeMap(PbRoleSet.SET);
		Progress.traceTailer("allocated", PbRoleSet.MAP.size());

		Progress.traceTailer("propbank pass0", 2);
	}

	@Override
	protected int processFrameset(final PbVerbDocument document, final Node start, final String head)
	{
		long count = 0;
		try
		{
			// predicates
			final Collection<PbPredicate> predicates = PbVerbDocument.getPredicates(head, start);
			if (predicates != null)
			{
				for (final PbPredicate predicate : predicates)
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
			final Collection<PbLexItem> aliasLexItems = PbVerbDocument.getAliasPredicates(start);
			if (aliasLexItems != null)
			{
				for (final PbLexItem lexItem : aliasLexItems)
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
			final Collection<PbRoleSet> roleSets = PbVerbDocument.getRoleSets(head, start);
			if (roleSets != null)
			{
				PbRoleSet.SET.addAll(roleSets);
			}

			// roles
			final Collection<PbRole> roles = PbVerbDocument.getRoles(head, start);
			if (roles != null)
			{
				PbRole.SET.addAll(roles);
			}

			// examples
			final Collection<PbExample> examples = PbVerbDocument.getExamples(head, start);
			if (examples != null)
			{
				PbExample.SET.addAll(examples);
			}

			// args
			final Collection<PbArg> args = PbVerbDocument.getExampleArgs(head, start);
			if (args != null)
			{
				PbArg.SET.addAll(args);
			}
		}
		catch (XPathExpressionException e)
		{
			Logger.instance.logXmlException(PbModule.MODULE_ID, this.tag, "read-frameset", document.getFileName(), -1, null, "xpath", e);
		}
		return (int) count;
	}
}
