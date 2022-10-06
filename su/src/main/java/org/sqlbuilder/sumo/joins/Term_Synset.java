package org.sqlbuilder.sumo.joins;

import com.articulate.sigma.NotNull;

import org.sqlbuilder.common.AlreadyFoundException;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.sumo.Utils;
import org.sqlbuilder.sumo.objects.Term;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Term_Synset implements Insertable, Serializable, Comparable<Term_Synset>
{
	private static final Comparator<Term_Synset> COMPARATOR = Comparator.comparing(Term_Synset::getTerm).thenComparing(Term_Synset::getSynsetId).thenComparing(Term_Synset::getPosId).thenComparing(Term_Synset::getMapType);

	public static final Set<Term_Synset> SET = new TreeSet<>();

	public final long synsetId;

	public final char posId;

	public final Term term;

	public final String mapType;

	// C O N S T R U C T

	private Term_Synset(final Term term, final long synsetId, char pos, final String mapType)
	{
		this.synsetId = synsetId;
		this.posId = pos;
		this.term = term;
		this.mapType = mapType;
	}

	public static Term_Synset make(final Term term, final long synsetId, char pos, final String mapType) throws AlreadyFoundException
	{
		Term_Synset map = new Term_Synset(term, synsetId, pos, mapType);
		boolean wasThere = !SET.add(map);
		if (wasThere)
		{
			throw new AlreadyFoundException(map.toString());
		}
		return map;
	}

	public static Term_Synset parse(final String termstr, final String line, final char pos) throws IllegalArgumentException
	{
		// split into fields
		// Each SUMOTerm concept is designated with the prefix '&%'. Note
		// that each concept also has a suffix, '=', ':', '+', '[', ']' or '@', which indicates
		// the precise relationship between the SUMOTerm concept and the WordNet synset.
		// The symbols '=', '+', and '@' mean, respectively, that the WordNet synset
		// is equivalent in meaning to the SUMOTerm concept, is subsumed by the SUMOTerm
		// concept or is an instance of the SUMOTerm concept. ':', '[', and ']' are the
		// complements of those relations. For example, a mapping expressed as
		// ; (%ComplementFn &%Motion)+ now appears as &%Motion[
		// Note also that ']' has not currently been needed.

		final int breakPos = line.indexOf(' ');
		final String offsetField = line.substring(0, breakPos);
		final long synsetId = Long.parseLong(offsetField);
		final Term term = Term.make(termstr);
		final String mapType = line.substring(line.length() - 1);
		return Term_Synset.make(term, synsetId, pos, mapType);
	}

	// A C C E S S

	public long getSynsetId()
	{
		return synsetId;
	}

	public char getPosId()
	{
		return posId;
	}

	public Term getTerm()
	{
		return term;
	}

	public String getMapType()
	{
		return mapType;
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
		Term_Synset that = (Term_Synset) o;
		return synsetId == that.synsetId && posId == that.posId && term.equals(that.term);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(synsetId, posId, term);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final Term_Synset that)
	{
		return COMPARATOR.compare(this, that);
	}

	// T O S T R I N G

	@Override
	public String toString()
	{
		return this.term + " -> " + this.synsetId + " [" + this.posId + "] (" + mapType + ")";
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s','%s',%s", //
				synsetId, // 1
				posId, // 2
				mapType, // 3
				Utils.nullableInt(resolvedTermId()) // 4
		);
	}

	@Override
	public String comment()
	{
		return getTerm().term;
	}

	// R E S O L V E

	public Integer resolvedTermId()
	{
		return term.resolve();
	}
}
