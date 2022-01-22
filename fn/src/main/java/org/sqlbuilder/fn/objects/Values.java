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
	/*
	poses.table=fnposes
	poses.create=CREATE TABLE IF NOT EXISTS %Fn_poses.table% ( posid INTEGER NOT NULL AUTO_INCREMENT,pos VARCHAR(8),PRIMARY KEY (posid) );
	poses.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_poses.table%_pos ON %Fn_poses.table% (pos);
	poses.no-unq1=DROP INDEX IF EXISTS unq_%Fn_poses.table%_pos;
	poses.insert=INSERT INTO %Fn_poses.table% (pos) VALUES(?);
	 */
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

	/*
	coretypes.table=fncoretypes
	coretypes.create=CREATE TABLE IF NOT EXISTS %Fn_coretypes.table% ( coretypeid INTEGER NOT NULL AUTO_INCREMENT,coretype VARCHAR(16),PRIMARY KEY (coretypeid) );
	coretypes.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_coretypes.table%_coretype ON %Fn_coretypes.table% (coretype);
	coretypes.no-unq1=DROP INDEX IF EXISTS unq_%Fn_coretypes.table%_coretype;
	coretypes.insert=INSERT INTO %Fn_coretypes.table% (coretype) VALUES(?);
	 */
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

	/*
	labelitypes.table=fnlabelitypes
	labelitypes.create=CREATE TABLE IF NOT EXISTS %Fn_labelitypes.table% ( labelitypeid INTEGER NOT NULL AUTO_INCREMENT,labelitype VARCHAR(4),labelitypedescr VARCHAR(16) DEFAULT NULL,PRIMARY KEY (labelitypeid) );
	labelitypes.insert=INSERT INTO %Fn_labelitypes.table% (labelitype) VALUES(?);
	 */
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
