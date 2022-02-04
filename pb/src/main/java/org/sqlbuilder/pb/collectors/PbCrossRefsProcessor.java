package org.sqlbuilder.pb.collectors;

import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.pb.objects.*;

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
	public void run()
	{
		Progress.traceHeader("pbxref", "inserting");
		try
		{
			insertAliases(RoleSet.COLLECTOR.keySet());
		}
		catch (NotFoundException e)
		{
			e.printStackTrace();
		}
		Progress.traceTailer("pbxref", "");
	}

	public void insertAliases(final Collection<RoleSet> roleSets) throws NotFoundException
	{
		if (roleSets != null)
		{
			for (final RoleSet roleSet : roleSets)
			{
				// predicate
				final Predicate pbPredicate = roleSet.getPredicate();

				// pb word
				PbWord pbword = PbWord.make(pbPredicate.getLemma());

				// predicate as roleset member
				final RoleSetMember predicateMember = new RoleSetMember(roleSet, pbword);

				// aliases
				final List<Alias> aliases = roleSet.getAliases();
				if (aliases != null)
				{
					for (final Alias alias : aliases)
					{
						// TODO
						// alias.setRoleSet(roleSet);

						// roleset member
						final RoleSetMember member = new RoleSetMember(roleSet, pbword);
					}
				}
			}
		}
	}
}
