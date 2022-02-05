package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.pb.objects.PbWord;
import org.sqlbuilder.pb.objects.RoleSet;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class FnAlias extends Alias
{
	public static final Comparator<FnAlias> COMPARATOR = Comparator //
		.comparing(FnAlias::getPbRoleSet) //
		.thenComparing(FnAlias::getPbWord) //
		.thenComparing(FnAlias::getLemma) //
		.thenComparing(FnAlias::getRef) //
		.thenComparing(FnAlias::getPos);

	 public static final Set<FnAlias> SET = new HashSet<>();

	// C O N S T R U C T

	public static FnAlias make(final String clazz, final String pos, final String lemma, final RoleSet pbRoleSet, final PbWord pbWord)
	{
		var a = new FnAlias(clazz, pos, lemma, pbRoleSet, pbWord);
		SET.add(a);
		return a;
	}

	private FnAlias(final String clazz, final String pos, final String lemma, final RoleSet pbRoleSet, final PbWord pbWord)
	{
		super(clazz, pos, lemma, pbRoleSet, pbWord);
	}
}
