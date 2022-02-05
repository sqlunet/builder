package org.sqlbuilder.sl.collectors;

import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.pb.foreign.PbVnRoleMapping;
import org.sqlbuilder.pb.foreign.PbRole_VnRole;

import java.util.Set;
import java.util.TreeSet;

public class Semlink2Processor extends Processor
{
	public Semlink2Processor()
	{
		super("semlink2");
	}

	@Override
	public void run()
	{
		Progress.traceHeader("semlink2", "processing");
		Semlink2Processor.processSemlinks();
		Progress.traceTailer("semlink2", "");
	}

	@SuppressWarnings("UnusedReturnValue")
	protected static int processSemlinks()
	{
		int nTotal = 0;
		int nRoleNotFound = 0;
		int nFound = 0;

		final Set<PbVnRoleMapping> pbVnRoleMappings = new TreeSet<>(PbRole_VnRole.semlinkMap.values());
		for (final PbVnRoleMapping vnRole : pbVnRoleMappings)
		{
			nTotal++;

			try
			{
				// TODO find match
				// RoleIds vnRoleIds =
				//PbVnFinder.findVnRole(vnRole);
				//System.err.printf("VNROLE FOUND vnrole=<%s> roleids=<%s>\n", vnRole, vnRoleIds);
				nFound++;
				throw new NotFoundException("dummy");
			}
			catch (NotFoundException nfe)
			{
				nRoleNotFound++;
				System.err.printf("VNROLE NOT FOUND vnrole=<%s>%n", vnRole);
				try
				{
					//TODO find vnclass
					//final PbVnClass vnClass = PbVnFinder.findVnClass(vnRole.getVnClass().getClassName());
					//System.err.printf("\tVNCLASS FOUND %s%n", vnClass);
				}
				catch (Exception e2)
				{
					//
				}
			}
		}
		System.out.printf("total %s%n", nTotal);
		System.out.printf("vnrole found %s%n", nFound);
		System.out.printf("vnrole not found %s%n", nRoleNotFound);
		return PbRole_VnRole.semlinkMap.size();
	}
}
