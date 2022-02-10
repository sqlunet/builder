package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.pb.objects.PbWord;
import org.sqlbuilder.pb.objects.RoleSet;

import java.util.Comparator;
import java.util.Objects;

public abstract class Alias implements Insertable
{
	public static final Comparator<? extends Alias> COMPARATOR = Comparator //
			.comparing(Alias::getPbRoleSet) //
			.thenComparing(Alias::getPbWord) //
			.thenComparing(Alias::getRef) //
			.thenComparing(Alias::getPos);

	public enum Db
	{
		VERBNET, FRAMENET
	}

	protected final String ref;

	protected final String pos;

	protected final RoleSet pbRoleSet;

	protected final PbWord pbWord;

	// C O N S T R U C T

	public static Alias make(final Db db, final String clazz, final String pos, final RoleSet pbRoleSet, final PbWord pbWord)
	{
		var a = db.equals(Db.VERBNET) ? VnAlias.make(clazz, pos, pbRoleSet, pbWord) : (db.equals(Db.FRAMENET) ? FnAlias.make(clazz, pos, pbRoleSet, pbWord) : null);
		return a;
	}

	protected Alias(final String clazz, final String pos, final RoleSet pbRoleSet, final PbWord pbWord)
	{
		this.ref = clazz;
		this.pos = "j".equals(pos) ? "a" : pos;
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

	public String getRef()
	{
		return ref;
	}

	public String getPos()
	{
		return pos;
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Alias that = (Alias) o;
		return ref.equals(that.ref) && pos.equals(that.pos) && pbRoleSet.equals(that.pbRoleSet) && pbWord.equals(that.pbWord);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ref, pos, pbRoleSet, pbWord);
	}

	// I N S E R T

	@RequiresIdFrom(type = RoleSet.class)
	@RequiresIdFrom(type = PbWord.class)
	@Override
	public String dataRow()
	{
		// rolesetid,refid,ref,pos,pbwordid
		return String.format("%d,'%s',%d,%s,'%s'", //
				pbRoleSet.getIntId(), //
				pos, //
				pbWord.getIntId(), //
				"NULL", //
				ref);
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s", pbRoleSet.getName(), pbWord.word);
	}

	@Override
	public String toString()
	{
		return String.format("%s,%s,%s", ref, pos, pbWord.word);
	}
}
