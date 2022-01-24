package org.sqlbuilder.fn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class Normalizer
{
	public static final boolean PASSTHROUGH = false;

	private static final String[] SQLS_CREATE = new String[]{ //
			Resources.resources.getString("Normalizer.drop-table"), Resources.resources.getString("Normalizer.create-table"), Resources.resources.getString("Normalizer.create-unq-index"),};

	private static final String[] SQLS_CREATE2 = new String[]{ //
			Resources.resources.getString("Normalizer.drop-table"), Resources.resources.getString("Normalizer.create2-table"), Resources.resources.getString("Normalizer.create-unq-index"),};

	private static final String[] SQLS_INSERT = new String[]{ //
			Resources.resources.getString("Normalizer.insert"),};

	private static final String[] SQLS_INSERT2 = new String[]{ //
			Resources.resources.getString("Normalizer.insert2"),};

	private static final String[] SQLS_CREATE_FK_COLUMN = new String[]{ //
			Resources.resources.getString("Normalizer.add-column-fk"),};

	private static final String[] SQLS_DROP_FK_COLUMN = new String[]{ //
			Resources.resources.getString("Normalizer.drop-column-fk"),};

	private static final String[] SQLS_UPDATE_FK_COLUMN = new String[]{ //
			Resources.resources.getString("Normalizer.update"),};

	private static final String[] SQLS_UPDATE_FK_COLUMN2 = new String[]{ //
			Resources.resources.getString("Normalizer.update2"),};

	private static final String[] SQL_CLEANUP = new String[]{ //
			Resources.resources.getString("Normalizer.drop-column-data"),};

	private static final String[] SQLS_SWAP_PK = new String[]{ //
			Resources.resources.getString("Normalizer.drop-auto-pk"), Resources.resources.getString("Normalizer.change-pk"), Resources.resources.getString("Normalizer.drop-pk"),};

	private static final String[] SQLS_NEW_PK = new String[]{ //
			Resources.resources.getString("Normalizer.new-pk"),};

	private static final String SQLS_LENGTH = Resources.resources.getString("Normalizer.length");

	private final Properties props;

	private final Collection<String> sqls;

	private final Collection<String> ignoreExceptionSqls;

	// C O N S T R U C T

	public Normalizer(final String newTable, final String newCol, final String newIdCol)
	{
		this.sqls = new ArrayList<>();
		this.ignoreExceptionSqls = new ArrayList<>();
		this.props = new Properties();
		this.props.put("newtable", Normalizer.getTable(newTable));
		this.props.put("newcol", newCol);
		this.props.put("newidcol", newIdCol);
		this.props.put("collength", "80");
	}

	// T A R G E T

	public Normalizer targets(final String oldTable, final String oldCol, final String oldIdCol)
	{
		this.props.put("oldtable", Normalizer.getTable(oldTable));
		this.props.put("oldcol", oldCol);
		this.props.put("oldidcol", oldIdCol);
		return this;
	}

	// O P E R A T I O N S

	public Normalizer create()
	{
		this.sqls.addAll(expand(Normalizer.SQLS_CREATE));
		return this;
	}

	public Normalizer create2()
	{
		this.sqls.addAll(expand(Normalizer.SQLS_CREATE2));
		return this;
	}

	public Normalizer insert()
	{
		this.sqls.addAll(expand(Normalizer.SQLS_INSERT));
		return this;
	}

	public Normalizer insert2()
	{
		this.sqls.addAll(expand(Normalizer.SQLS_INSERT2));
		return this;
	}

	public Normalizer reference()
	{
		this.ignoreExceptionSqls.addAll(expand(Normalizer.SQLS_DROP_FK_COLUMN));
		this.sqls.addAll(expand(Normalizer.SQLS_CREATE_FK_COLUMN));
		this.sqls.addAll(expand(Normalizer.SQLS_UPDATE_FK_COLUMN));
		return this;
	}

	public Normalizer referenceThrough(final String... args)
	{
		final String meanExpr = Normalizer.joinAs("m", args);
		this.props.put("through", meanExpr);
		this.sqls.addAll(expand(Normalizer.SQLS_UPDATE_FK_COLUMN2));
		return this;
	}

	public Normalizer cleanup()
	{
		this.sqls.addAll(expand(Normalizer.SQL_CLEANUP));
		return this;
	}

	public Normalizer swapPk(final String oldPk, final String newPk)
	{
		this.props.put("oldpk", oldPk);
		this.props.put("newpk", newPk);
		this.sqls.addAll(expand(Normalizer.SQLS_SWAP_PK));
		return this;
	}

	public Normalizer newPk(final String newPk)
	{
		this.props.put("newpk", newPk);
		this.sqls.addAll(expand(Normalizer.SQLS_NEW_PK));
		return this;
	}

	private static String joinAs(@SuppressWarnings("SameParameterValue") final String as, final String... args)
	{
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i += 2)
		{
			final String table = args[i];
			final String joinColumn = args[i + 1];
			if (i > 0)
			{
				sb.append(' ');
			}
			sb.append("LEFT JOIN ");
			sb.append(Normalizer.getTable(table));
			if (i + 2 >= args.length)
			{
				sb.append(" AS ").append(as);
			}
			sb.append(" USING(");
			sb.append(joinColumn);
			sb.append(")");
		}
		return sb.toString();
	}

	// H E L P E R S

	private Collection<String> expand(final String[] strs)
	{
		return Normalizer.expand(strs, this.props);
	}

	private static Collection<String> expand(final String[] strs, final Properties props)
	{
		final Collection<String> strs2 = new ArrayList<>();
		for (final String str : strs)
		{
			strs2.add(Resources.expand(str, props));
		}
		return strs2;
	}

	// E X E C

	public void dump()
	{
		this.props.list(System.out);
		System.out.println("-- SQL");
		Normalizer.dump(this.sqls);
	}

	private static void dump(final Collection<String> strs)
	{
		for (final String str : strs)
		{
			System.out.println(str);
		}
	}

	private static String getTable(final String tableKey)
	{
		if (Normalizer.PASSTHROUGH)
		{
			return tableKey;
		}
		return Resources.resources.getString(tableKey + ".table");
	}
}
