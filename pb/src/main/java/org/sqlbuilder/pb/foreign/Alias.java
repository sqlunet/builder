package org.sqlbuilder.pb.foreign;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.Resolvable;
import org.sqlbuilder.pb.objects.Word;
import org.sqlbuilder.pb.objects.RoleSet;

import java.util.Comparator;
import java.util.Objects;

public abstract class Alias implements Insertable, Resolvable<String,Integer>
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

	protected final Word word;

	// C O N S T R U C T O R

	public static Alias make(final Db db, final String clazz, final String pos, final RoleSet pbRoleSet, final Word word)
	{
		var a = db.equals(Db.VERBNET) ? VnAlias.make(clazz, pos, pbRoleSet, word) : (db.equals(Db.FRAMENET) ? FnAlias.make(clazz, pos, pbRoleSet, word) : null);
		return a;
	}

	protected Alias(final String clazz, final String pos, final RoleSet pbRoleSet, final Word word)
	{
		this.ref = clazz;
		this.pos = "j".equals(pos) ? "a" : pos;
		this.pbRoleSet = pbRoleSet;
		this.word = word;
	}

	// A C C E S S

	public RoleSet getPbRoleSet()
	{
		return pbRoleSet;
	}

	public Word getPbWord()
	{
		return word;
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
		return ref.equals(that.ref) && pos.equals(that.pos) && pbRoleSet.equals(that.pbRoleSet) && word.equals(that.word);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ref, pos, pbRoleSet, word);
	}

	// I N S E R T

	@RequiresIdFrom(type = RoleSet.class)
	@RequiresIdFrom(type = Word.class)
	@Override
	public String dataRow()
	{
		// rolesetid,refid,ref,pos,pbwordid
		return String.format("%d,'%s',%d,'%s'", //
				pbRoleSet.getIntId(), //
				pos, //
				word.getIntId(), //
				ref);
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s", pbRoleSet.getName(), word.word);
	}

	// R E S O L V E

	@Override
	public String resolving()
	{
		return ref;
	}

	@Override
	public String toString()
	{
		return String.format("%s,%s,%s", ref, pos, word.word);
	}
}
