/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlbuilder.common;

import org.sqlunet.wn.Q0;
import org.sqlunet.wn.Q1;
import org.sqlunet.wn.Q2;
import org.sqlunet.wn.QV;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * WordNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Lib
{
	/**
	 * Append items to projection
	 *
	 * @param projection original projection
	 * @param items      items to addItem to projection
	 * @return augmented projection
	 */
	public static String[] appendProjection(final String[] projection, final String... items)
	{
		String[] projection2;
		int i = 0;
		if (projection == null)
		{
			projection2 = new String[1 + items.length];
			projection2[i++] = "*";
		}
		else
		{
			projection2 = new String[projection.length + items.length];
			for (final String item : projection)
			{
				projection2[i++] = item;
			}
		}

		for (final String item : items)
		{
			projection2[i++] = item;
		}
		return projection2;
	}

	/**
	 * Add items to projection
	 *
	 * @param projection original projection
	 * @param items      items to addItem to projection
	 * @return augmented projection
	 */
	static String[] prependProjection(final String[] projection, final String... items)
	{
		String[] projection2;
		if (projection == null)
		{
			projection2 = new String[1 + items.length];
		}
		else
		{
			projection2 = new String[projection.length + items.length];
		}
		int i = 0;
		for (final String item : items)
		{
			projection2[i++] = item;
		}
		if (projection == null)
		{
			projection2[i] = "*";
		}
		else
		{
			for (final String item : projection)
			{
				projection2[i++] = item;
			}
		}
		return projection2;
	}
}
