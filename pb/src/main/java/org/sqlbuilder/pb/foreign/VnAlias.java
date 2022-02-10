package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.pb.objects.PbWord;
import org.sqlbuilder.pb.objects.RoleSet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class VnAlias extends Alias implements Insertable
{
	public static final Comparator<VnAlias> COMPARATOR = Comparator //
			.comparing(VnAlias::getPbRoleSet) //
			.thenComparing(VnAlias::getPbWord) //
			.thenComparing(VnAlias::getRef) //
			.thenComparing(VnAlias::getPos);

	static public final Set<VnAlias> SET = new HashSet<>();

	// C O N S T R U C T

	public static VnAlias make(final String clazz, final String pos, final RoleSet pbRoleSet, final PbWord pbWord)
	{
		var a = new VnAlias(clazz, pos, pbRoleSet, pbWord);
		SET.add(a);
		return a;
	}

	protected VnAlias(final String clazz, final String pos, final RoleSet pbRoleSet, final PbWord pbWord)
	{
		super(clazz, pos, pbRoleSet, pbWord);
	}
}
