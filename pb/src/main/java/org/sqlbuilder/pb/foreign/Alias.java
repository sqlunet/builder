package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.pb.objects.PbWord;
import org.sqlbuilder.pb.objects.RoleSet;

import java.util.Comparator;

public abstract class Alias implements Insertable
{
	public static final Comparator<? extends Alias> COMPARATOR = Comparator //
			.comparing(Alias::getPbRoleSet) //
			.thenComparing(Alias::getPbWord) //
			.thenComparing(Alias::getLemma) //
			.thenComparing(Alias::getRef) //
			.thenComparing(Alias::getPos);

	public enum Db
	{
		VERBNET, FRAMENET
	}

	private final String lemma;

	private final String ref;

	private final String pos;

	private final RoleSet pbRoleSet;

	private final PbWord pbWord;

	// C O N S T R U C T

	public static Alias make(final Db db, final String clazz, final String pos, final String lemma, final RoleSet pbRoleSet, final PbWord pbWord)
	{
		var a = db.equals(Db.VERBNET) ? VnAlias.make(clazz, pos, lemma, pbRoleSet, pbWord) : (db.equals(Db.FRAMENET) ? FnAlias.make(clazz, pos, lemma, pbRoleSet, pbWord) : null);
		return a;
	}

	protected Alias(final String clazz, final String pos, final String lemma, final RoleSet pbRoleSet, final PbWord pbWord)
	{
		this.ref = clazz;
		this.pos = "j".equals(pos) ? "a" : pos;
		this.lemma = lemma;
		this.pbRoleSet = pbRoleSet;
		this.pbWord = pbWord;
	}

	// A C C E S S

	public RoleSet getPbRoleSet()
	{
		return pbRoleSet;
	}

	public PbWord getPbWord()
	{
		return pbWord;
	}

	public String getLemma()
	{
		return lemma;
	}

	public String getRef()
	{
		return ref;
	}

	public String getPos()
	{
		return pos;
	}

	// I N S E R T

	@RequiresIdFrom(type = RoleSet.class)
	@RequiresIdFrom(type = PbWord.class)
	@Override
	public String dataRow()
	{
		// rolesetid,ref,refid,pos,pbwordid
		return String.format("%d,%s,'%s',%d", //
				pbRoleSet.getIntId(), //
				"NULL", //
				pos, //
				pbWord.getIntId());
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s,%s", pbRoleSet.getName(), ref, lemma);
	}

	@Override
	public String toString()
	{
		return String.format("%s,%s,%s", ref, pos, lemma);
	}
}
