package org.sqlbuilder.pb;

import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;

import java.util.Set;
import java.util.TreeSet;

public class Semlink2Processor extends Processor
{
	public Semlink2Processor()
	{
		super("semlink2");
	}

	@Override
	protected void run()
	{
		Progress.traceHeader("semlink2", "processing");
		Semlink2Processor.processSemlinks();
		Progress.traceTailer("semlink2", 1L);
	}

	@SuppressWarnings("UnusedReturnValue")
	protected static int processSemlinks()
	{
		int nTotal = 0;
		int nRoleNotFound = 0;
		int nFound = 0;

		final Set<PbVnRole> pbVnRoles = new TreeSet<>(PbVnRoleMapping.semlinkMap.values());
		for (final PbVnRole vnRole : pbVnRoles)
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
		return PbVnRoleMapping.semlinkMap.size();
	}
}
