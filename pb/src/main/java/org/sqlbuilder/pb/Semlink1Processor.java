package org.sqlbuilder.pb;

import org.sqlbuilder.common.Processor;
import org.sqlbuilder.common.Progress;

import java.util.List;
import java.util.Map.Entry;

public class Semlink1Processor extends Processor
{
	public Semlink1Processor()
	{
		super("semlink1");
	}

	@Override
	protected void run()
	{
		Progress.traceHeader("semlink1", "processing");
		Semlink1Processor.processSemlinks();
		Progress.traceTailer("semlink1", 1L);
	}

	@SuppressWarnings("UnusedReturnValue")
	protected static int processSemlinks()
	{
		int nSlTotal = 0;
		int nPbTotal = 0;
		int nPbNotFound = 0;
		int nPbNull = 0;
		int nPbFound = 0;
		int nSlPbClassMatch = 0;
		int nPbClassPartial = 0;
		int nSlClassPartial = 0;

		for (final Entry<PbRole, PbVnRole> entry : PbVnRoleMapping.semlinkMap.entrySet())
		{
			nSlTotal++;

			final PbRole role = entry.getKey();
			final PbVnRole semlinkVnRole = entry.getValue();
			final List<PbVnRole> propbankVnRoles = PbVnRoleMapping.map.get(role);

			if (propbankVnRoles != null)
			{
				nPbTotal += propbankVnRoles.size();

				int nRoleFound = 0;
				int nRoleClassMatch = 0;
				int nPbRoleClassPartial = 0;
				int nSlRoleClassPartial = 0;

				for (final PbVnRole propbankVnRole : propbankVnRoles)
				{
					if (semlinkVnRole.compareTo(propbankVnRole) == 0)
					{
						nRoleFound++;
					}
					else
					{
						final String semlinkVnClassName = semlinkVnRole.getVnClass().getClassName();
						final String propbankVnClassName = propbankVnRole.getVnClass().getClassName();
						if (semlinkVnClassName.equals(propbankVnClassName))
						{
							nRoleClassMatch++;
						}
						else if (semlinkVnClassName.startsWith(propbankVnClassName))
						{
							nPbRoleClassPartial++;
							//System.out.printf("+role=%s%n", role);
							//System.out.printf("\tsl=%s | pb=%s%n", semlinkVnClassName, propbankVnClassName);
						}
						else if (propbankVnClassName.startsWith(semlinkVnClassName))
						{
							nSlRoleClassPartial++;
							//System.out.printf("-role=%s%n", role);
							//System.out.printf("\tsl=%s | pb=%s%n", semlinkVnClassName, propbankVnClassName);
						}
					}
				}
				if (nRoleFound != 0)
				{
					nPbFound++;
				}
				else
				{
					nPbNotFound++;
					if (nRoleClassMatch != 0)
					{
						nSlPbClassMatch++;
					}
					if (nPbRoleClassPartial != 0)
					{
						nPbClassPartial++;
					}
					if (nSlRoleClassPartial != 0)
					{
						nSlClassPartial++;
						// System.out.printf("role=%s sl-vnrole=%s%n", role, semlinkVnRole);
						// for (PbVnRole pbVnRole : propbankVnRoles)
						// System.out.printf("\tpb-vnrole=%s%n", pbVnRole);
					}
				}
			}
			else
			{
				nPbNull++;
			}
		}

		System.out.printf("SL total %s%n", nSlTotal);
		System.out.printf("PB total %s%n", nPbTotal);
		System.out.printf("SL roles found in PB %s%n", nPbFound);
		System.out.printf("SL roles null in PB %s%n", nPbNull);
		System.out.printf("SL roles not found in PB %s%n", nPbNotFound);
		System.out.printf("SL-PB class match %s%n", nSlPbClassMatch);
		System.out.printf("partial (-) SL class found %s%n", nSlClassPartial);
		System.out.printf("partial (+) PB class found %s%n", nPbClassPartial);

		return PbVnRoleMapping.semlinkMap.size();
	}
}
