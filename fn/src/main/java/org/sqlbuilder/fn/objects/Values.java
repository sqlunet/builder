package org.sqlbuilder.fn.objects;

import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.fn.HasId;
import org.sqlbuilder.fn.RequiresIdFrom;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Values
{
	public static class Pos implements HasId, Insertable<Pos>
	{
		public static final Comparator<Pos> COMPARATOR = Comparator.comparing(t -> t.pos);

		public static final Map<Pos, Integer> MAP = new TreeMap<>(COMPARATOR);

		private final String pos;

		public static Pos make(final String pos, final int idx)
		{
			var p = new Pos(pos);
			MAP.put(p, idx);
			return p;
		}

		private Pos(final String pos)
		{
			this.pos = pos;
		}

		@RequiresIdFrom(type = Pos.class)
		@Override
		public Integer getIntId()
		{
			return MAP.get(this);
		}

		@Override
		public String dataRow()
		{
			return String.format("'%s'", pos);
		}
	}

	public static class CoreType implements HasId, Insertable<CoreType>
	{
		public static final Comparator<CoreType> COMPARATOR = Comparator.comparing(t -> t.coretype);

		public static final Map<CoreType, Integer> MAP = new TreeMap<>(COMPARATOR);

		private final String coretype;

		public static CoreType make(final String coretype, final int idx)
		{
			var t = new CoreType(coretype);
			MAP.put(t, idx);
			return t;
		}

		private CoreType(final String coretype)
		{
			this.coretype = coretype;
		}

		@RequiresIdFrom(type = CoreType.class)
		@Override
		public Integer getIntId()
		{
			return MAP.get(this);
		}

		@Override
		public String dataRow()
		{
			return String.format("'%s'", coretype);
		}
	}

	public static class LabelIType implements HasId, Insertable<LabelIType>
	{
		public static final Comparator<LabelIType> COMPARATOR = Comparator.comparing(t -> t.labelitype);

		public static final Map<LabelIType, Integer> MAP = new TreeMap<>(COMPARATOR);

		private final String labelitype;

		public static LabelIType make(final String labelitype, final int idx)
		{
			var l = new LabelIType(labelitype);
			MAP.put(l, idx);
			return l;
		}

		private LabelIType(final String labelitype)
		{
			this.labelitype = labelitype;
		}

		@RequiresIdFrom(type = LabelIType.class)
		@Override
		public Integer getIntId()
		{
			return MAP.get(this);
		}

		@Override
		public String dataRow()
		{
			return String.format("'%s'", labelitype);
		}
	}
}
