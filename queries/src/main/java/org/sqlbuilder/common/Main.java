/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlbuilder.common;

import org.sqlunet.vn.QC;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Utilities
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Main
{
	static public String[] WN_KEYS = Arrays.stream(org.sqlunet.wn.QV.Key.values()).map(Enum::toString).toArray(String[]::new);

	static public String[] BNC_KEYS = Arrays.stream(org.sqlunet.bnc.QV.Key.values()).map(Enum::toString).toArray(String[]::new);

	static public String[] FN_KEYS = Arrays.stream(org.sqlunet.fn.QV.Key.values()).map(Enum::toString).toArray(String[]::new);

	static public String[] VN_KEYS = Arrays.stream(org.sqlunet.vn.QV.Key.values()).map(Enum::toString).toArray(String[]::new);

	static public String[] PB_KEYS = Arrays.stream(org.sqlunet.pb.QV.Key.values()).map(Enum::toString).toArray(String[]::new);
	;
	static public String[] SN_KEYS = Arrays.stream(org.sqlunet.sn.QV.Key.values()).map(Enum::toString).toArray(String[]::new);

	// H E L P E R S

	public static void compare(String[] keys, Function<String, String[]> q1, Function<String, String[]> q2)
	{
		for (String key : keys)
		{
			String[] result1 = q1.apply(key);
			String[] result2 = q2.apply(key);
			if (!Arrays.equals(result1, result2))
			{
				System.out.println(key);
				if (!Objects.equals(result1[0], result2[0]))
				{
					System.out.println(result1[0] + "\n" + result2[0]);
				}
				if (!Objects.equals(result1[1], result2[1]))
				{
					System.out.println(result1[1] + "\n" + result2[1]);
				}
				if (!Objects.equals(result1[2], result2[2]))
				{
					System.out.println(result1[2] + "\n" + result2[2]);
				}
				if (!Objects.equals(result1[3], result2[3]))
				{
					System.out.println(result1[3] + "\n" + result2[3]);
				}
				if (!Objects.equals(result1[4], result2[4]))
				{
					System.out.println(result1[4] + "\n" + result2[4]);
				}
				System.out.println();
			}
		}
	}

	private static Function<String,String[]> qFrom(String module, String s)
	{
		switch (module)
		{
			case "wn":
				switch (s)
				{
					case "0":
						return new org.sqlunet.wn.Q0();
					case "1":
						return new org.sqlunet.wn.Q1();
					case "2":
						return new org.sqlunet.wn.Q2();
					case "QV":
						return new org.sqlunet.wn.QV();
				}
				return null;
			case "bnc":
				switch (s)
				{
					case "Q0":
						return new org.sqlunet.bnc.Q0();
						/*
					case "Q1":
						return new org.sqlunet.bnc.Q1();
					case "Q2":
						return new org.sqlunet.bnc.Q2();
						*/
					case "QV":
						return new org.sqlunet.bnc.QV();
				}
				return null;
			case "sn":
				switch (s)
				{
					case "Q0":
						return new org.sqlunet.sn.Q0();
					case "QV":
						return new org.sqlunet.sn.QV();
				}
				return null;
			case "vn":
				switch (s)
				{
					case "Q0":
						return new org.sqlunet.vn.Q0();
					case "Q1":
						return new org.sqlunet.vn.Q1();
					case "QC":
						return new QC();
					case "QV":
						return new org.sqlunet.vn.QV();
				}
				return null;
			case "pb":
				switch (s)
				{
					case "Q0":
						return new org.sqlunet.pb.Q0();
					case "Q1":
						return new org.sqlunet.pb.Q1();
					case "Q2":
						return new org.sqlunet.pb.Q2();
					case "QV":
						return new org.sqlunet.pb.QV();
				}
				return null;
			case "fn":
				switch (s)
				{
					case "Q0":
						return new org.sqlunet.fn.Q0();
					case "Q1":
						return new org.sqlunet.fn.Q1();
					case "QV":
						return new org.sqlunet.fn.QV();
				}
				return null;
		}
		return null;
	}

	private static String[] keysFrom(String module)
	{
		switch (module)
		{
			case "wn":
				return WN_KEYS;
			case "bnc":
				return BNC_KEYS;
			case "sn":
				return SN_KEYS;
			case "vn":
				return VN_KEYS;
			case "pb":
				return PB_KEYS;
			case "fn":
				return FN_KEYS;
		}
		throw new IllegalArgumentException(module);
	}

	public static void main(final String[] args) throws IOException
	{
		if ("compare".equals(args[0]))
		{
			String module = args[1];
			String source1 = args[2];
			String source2 = args[3];
			compare(keysFrom(module), qFrom(module, source1), qFrom(module, source2));
		}
	}
}
