package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.pb.objects.RoleSet;
import org.sqlbuilder.pb.objects.Word;

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

	// C O N S T R U C T O R

	public static VnAlias make(final String clazz, final String pos, final RoleSet pbRoleSet, final Word word)
	{
		var a = new VnAlias(clazz, pos, pbRoleSet, word);
		SET.add(a);
		return a;
	}

	protected VnAlias(final String clazz, final String pos, final RoleSet pbRoleSet, final Word word)
	{
		super(clazz, pos, pbRoleSet, word);
	}
}
