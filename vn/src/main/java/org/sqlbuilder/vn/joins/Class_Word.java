package org.sqlbuilder.vn.joins;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.vn.objects.VnClass;
import org.sqlbuilder.vn.objects.VnWord;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Class_Word implements Insertable, Comparable<Class_Word>
{
	static public final Comparator<Class_Word> COMPARATOR = Comparator.comparing(Class_Word::getWord).thenComparing(Class_Word::getClazz);

	public static final Set<Class_Word> SET = new HashSet<>();

	public final VnClass clazz;

	public final VnWord word;

	// C O N S T R U C T O R

	public static Class_Word make(final VnClass clazz, final VnWord word)
	{
		var m = new Class_Word(clazz, word);
		SET.add(m);
		return m;
	}

	private Class_Word(final VnClass clazz, final VnWord word)
	{
		this.clazz = clazz;
		this.word = word;
	}

	// A C C E S S

	public VnWord getWord()
	{
		return word;
	}

	public VnClass getClazz()
	{
		return clazz;
	}

	// O R D E R I N G

	@Override
	public int compareTo(final Class_Word that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = VnClass.class)
	@RequiresIdFrom(type = VnWord.class)
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
		return String.format("%s,%s",clazz.getName(), word.getWord());
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return String.format("%s-%s", this.clazz, this.word);
	}
}
