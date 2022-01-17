package org.sqlbuilder.pb;

import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PbCrossRefsProcessor extends Processor
{
	public PbCrossRefsProcessor(final Properties props)
	{
		super("pbxref");
	}

	@Override
	protected void run()
	{
		Progress.traceHeader("pbxref", "inserting");
		try
		{
			insertAliases(PbRoleSet.SET);
		}
		catch (NotFoundException e)
		{
			e.printStackTrace();
		}
		Progress.traceTailer("pbxref", "");
	}

	public void insertAliases(final Collection<PbRoleSet> roleSets) throws NotFoundException
	{
		if (roleSets != null)
		{
			for (final PbRoleSet roleSet : roleSets)
			{
				// predicate
				final PbPredicate pbPredicate = roleSet.getPredicate();

				// pb word
				PbWord pbword = new PbWord(pbPredicate.getLemma());

				// predicate as roleset member
				final PbRoleSetMember predicateMember = new PbRoleSetMember(roleSet, pbword);

				// aliases
				final List<PbAlias> aliases = roleSet.getAliases();
				if (aliases != null)
				{
					for (final PbAlias alias : aliases)
					{
						// TODO
						// alias.setRoleSet(roleSet);

						// roleset member
						final PbRoleSetMember member = new PbRoleSetMember(roleSet, pbword);
					}
				}
			}
		}
	}
}
