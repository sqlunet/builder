package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder.fn.HasId;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Values
{
	public static class Pos implements HasId, Insertable<Pos>
	{
		public static final Set<Pos> SET = new HashSet<>();

		public static Map<Pos, Integer> MAP;

		public static final Comparator<Pos> COMPARATOR = Comparator.comparing(t -> t.pos);

		private final String pos;

		public Pos(final String pos)
		{
			this.pos = pos;
			SET.add(this);
		}

		@Override
		public Object getId()
		{
			Integer id = MAP.get(this);
			if (id != null)
			{
				return id;
			}
			return "NULL";
		}

		@Override
		public String dataRow()
		{
			return String.format("'%s'", pos);
		}
	}

	public static class CoreType implements HasId, Insertable<CoreType>
	{
		public static final Set<CoreType> SET = new HashSet<>();

		public static Map<CoreType, Integer> MAP;

		private final String coretype;

		public static final Comparator<CoreType> COMPARATOR = Comparator.comparing(t -> t.coretype);

		public CoreType(final String coretype)
		{
			this.coretype = coretype;
			SET.add(this);
		}

		@Override
		public Object getId()
		{
			Integer id = MAP.get(this);
			if (id != null)
			{
				return id;
			}
			return "NULL";
		}

		@Override
		public String dataRow()
		{
			return String.format("'%s'", Utils.escape(coretype));
		}
	}

	public static class LabelIType implements HasId, Insertable<LabelIType>
	{
		public static final Set<LabelIType> SET = new HashSet<>();

		public static Map<LabelIType, Integer> MAP;

		public static final Comparator<LabelIType> COMPARATOR = Comparator.comparing(t -> t.labelitype);

		private final String labelitype;

		public LabelIType(final String labelitype)
		{
			this.labelitype = labelitype;
			SET.add(this);
		}

		@Override
		public Object getId()
		{
			Integer id = MAP.get(this);
			if (id != null)
			{
				return id;
			}
			return "NULL";
		}

		@Override
		public String dataRow()
		{
			return String.format("'%s'", labelitype);
		}
	}
}
