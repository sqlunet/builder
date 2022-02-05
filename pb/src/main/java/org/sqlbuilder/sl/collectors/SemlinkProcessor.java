package org.sqlbuilder.sl.collectors;

import org.sqlbuilder.common.*;
import org.sqlbuilder.pb.PbModule;
import org.sqlbuilder.pb.PbVnFinder;
import org.sqlbuilder.pb.foreign.RoleIds;
import org.sqlbuilder.pb.foreign.PbRole_VnRole;
import org.sqlbuilder.pb.foreign.PbRoleSet_VnClass;
import org.sqlbuilder.pb.foreign.PbVnRoleMapping;
import org.sqlbuilder.pb.objects.Role;
import org.sqlbuilder.pb.objects.RoleSet;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

public class SemlinkProcessor extends Processor
{
	public SemlinkProcessor()
	{
		super("semlink");
	}

	@Override
	public void run()
	{
		Progress.traceHeader("semlink3", "processing");
		processSemlinks();
		Progress.traceTailer("semlink3", "");
	}

	@RequiresIdFrom(type = RoleSet.class)
	@RequiresIdFrom(type = Role.class)
	@SuppressWarnings("UnusedReturnValue")
	protected int processSemlinks()
	{
		long nTotal = 0;
		long nPbRolesNotFound = 0;
		long nVnRolesNotFound = 0;
		long nMappings = 0;

		for (final Entry<Role, PbVnRoleMapping> entry : PbRole_VnRole.semlinkMap.entrySet())
		{
			nTotal++;

			// pb
			final Role role = entry.getKey();
			RoleIds pbRoleIds = new RoleIds(RoleSet.COLLECTOR.get(role.getRoleSet()), Role.COLLECTOR.get(role));

			// vn
			final PbVnRoleMapping vnRole = entry.getValue();
			RoleIds vnRoleIds;
			try
			{
				// find match
				vnRoleIds = PbVnFinder.findVnRole(vnRole);
			}
			catch (NotFoundException nfe)
			{
				nVnRolesNotFound++;
				Logger.instance.logNotFoundExceptionSilently(PbModule.MODULE_ID, this.tag, "vnrole-notfound", null, -1, null, "vnrole=[" + vnRole + "]", nfe);
				try
				{
					final PbRoleSet_VnClass vnClass = PbVnFinder.findVnClass(vnRole.getVnClass().getClassName());
					Logger.instance.logNotFoundExceptionSilently(PbModule.MODULE_ID, this.tag, "vnrole-notfound-but-vnclass-found", null, -1, null, "vnclass=[" + vnClass + "]", nfe);
				}
				catch (Exception e2)
				{
					//
					SemlinkProcessor.exploreVnClass(vnRole.getVnClass(), vnRole.getVnClass().getHead(), role.toString());
				}
				continue;
			}

			// insert
			final PbRole_VnRole pbVnRoleMap = new PbRole_VnRole(pbRoleIds, vnRoleIds);
			pbVnRoleMap.dataRow();
			nMappings++;
		}
		Progress.traceTailer("total", Long.toString(nTotal));
		Progress.traceTailer("mappings", Long.toString(nMappings));
		Progress.traceTailer("pbrole not found", Long.toString(nPbRolesNotFound));
		Progress.traceTailer("vnrole not found", Long.toString(nVnRolesNotFound));
		return PbRole_VnRole.semlinkMap.size();
	}

	public static void exploreVnClass(final PbRoleSet_VnClass vnClass, final String head, final String context)
	{
		try
		{
			final Collection<PbRoleSet_VnClass> vnClasses2 = PbVnFinder.findVnClasses(vnClass.getClassName());
			if (vnClasses2.size() > 1)
			{
				// ambiguous
				System.err.printf("AMBIGUOUS CLASS vnclass=%s, context=<%s>%n", vnClass, context);
				for (final PbRoleSet_VnClass vnClass2 : vnClasses2)
				{
					System.err.printf("\t-CLASS %s%n", vnClass2);
				}
			}
			else if (vnClasses2.isEmpty())
			{
				// not found
				System.err.printf("NOT FOUND CLASS vnclass=%s, context=<%s>%n", vnClass, context);
			}
			else
			{
				// one found
				final PbRoleSet_VnClass vnClass2 = vnClasses2.iterator().next();
				System.err.printf("FOUND CLASS %s for vnclass=%s, context=<%s>%n", vnClass2, vnClass, context);

				// check
				final Set<String> words = PbVnFinder.findLemmasForClass(vnClass2.getClassId());
				if (words == null)
				{
					System.err.printf("\tNO LEMMAS vnclass=%s %s has no lemma, context=<%s>%n", vnClass, vnClass2, context);
				}
				else if (!words.contains(head))
				{
					System.err.printf("\tDOES NOT CONTAIN HEAD vnclass=%s head=%s, context=<%s>%n", head, vnClass2, context);

					final Collection<PbRoleSet_VnClass> vnClasses3 = PbVnFinder.findVnClassesForLemma(head);
					for (final PbRoleSet_VnClass vnClass3 : vnClasses3)
					{
						final Set<String> words3 = PbVnFinder.findLemmasForClass(vnClass3.getClassId());
						System.err.printf("\tSUGGESTED vnclass=%s contains %s%n", vnClass3.getClassId(), PbRoleSet_VnClass.toString(words3));
					}
				}
			}
		}
		catch (NotFoundException nfe)
		{
			System.err.printf("CLASS vnclass=%s NOT FOUND, context=<%s>%n", vnClass, context);
		}
	}
}
