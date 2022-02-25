package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.NotNull;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.vn.objects.VnClass;
import org.sqlbuilder.vn.objects.Word;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Class_Word implements Insertable, Comparable<Class_Word>
{
	static public final Comparator<Class_Word> COMPARATOR = Comparator.comparing(Class_Word::getWord).thenComparing(Class_Word::getClazz);

	public static final Set<Class_Word> SET = new HashSet<>();

	public final VnClass clazz;

	public final Word word;

	// C O N S T R U C T O R

	public static Class_Word make(final VnClass clazz, final Word word)
	{
		var m = new Class_Word(clazz, word);
		SET.add(m);
		return m;
	}

	private Class_Word(final VnClass clazz, final Word word)
	{
		this.clazz = clazz;
		this.word = word;
	}

	// A C C E S S

	public Word getWord()
	{
		return word;
	}

	public VnClass getClazz()
	{
		return clazz;
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
		Class_Word that = (Class_Word) o;
		return clazz.equals(that.clazz) && word.equals(that.word);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(clazz, word);
	}

	// O R D E R I N G

	@Override
	public int compareTo(@NotNull final Class_Word that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = VnClass.class)
	@RequiresIdFrom(type = Word.class)
	@Override
	public String dataRow()
	{
		// class.id
		// word.id
		return String.format("%d,%d", clazz.getIntId(), word.getIntId());
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s", clazz.getName(), word.getWord());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s-%s", this.clazz, this.word);
	}
}
