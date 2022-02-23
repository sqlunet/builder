package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.pb.objects.Word;
import org.sqlbuilder.pb.objects.RoleSet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class FnAlias extends Alias
{
	public static final Comparator<FnAlias> COMPARATOR = Comparator //
			.comparing(FnAlias::getPbRoleSet) //
			.thenComparing(FnAlias::getPbWord) //
			.thenComparing(FnAlias::getRef) //
			.thenComparing(FnAlias::getPos);

	public static final Set<FnAlias> SET = new HashSet<>();

	// C O N S T R U C T O R

	public static FnAlias make(final String clazz, final String pos, final RoleSet pbRoleSet, final Word word)
	{
		var a = new FnAlias(clazz, pos, pbRoleSet, word);
		SET.add(a);
		return a;
	}

	private FnAlias(final String clazz, final String pos, final RoleSet pbRoleSet, final Word word)
	{
		super(clazz, pos, pbRoleSet, word);
	}
}
