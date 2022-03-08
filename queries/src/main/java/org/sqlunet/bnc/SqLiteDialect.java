/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.bnc;

class SqLiteDialect
{
	static private final String BNCBaseWordQuery = "SELECT " + //
			"${bncs.pos},${bncs.posname},${bncs.freq},${bncs.range},${bncs.disp}," + //
			"${convtasks.table}.${bncs.freq1},${convtasks.table}.${bncs.range1},${convtasks.table}.${bncs.disp1}," + //
			"${convtasks.table}.${bncs.freq2},${convtasks.table}.${bncs.range2},${convtasks.table}.${bncs.disp2}," + //
			"${imaginfs.table}.${bncs.freq1},${imaginfs.table}.${bncs.range1},${imaginfs.table}.${bncs.disp1}," + //
			"${imaginfs.table}.${bncs.freq2},${imaginfs.table}.${bncs.range2},${imaginfs.table}.${bncs.disp2}," + //
			"${spwrs.table}.${bncs.freq1},${spwrs.table}.${bncs.range1},${spwrs.table}.${bncs.disp1}," + //
			"${spwrs.table}.${bncs.freq2},${spwrs.table}.${bncs.range2},${spwrs.table}.${bncs.disp2}," + //
			"${bncs.wordid} " + //
			"FROM ${words.table} " + //
			"LEFT JOIN ${bncs.table} USING (words.wordid) " + //
			"LEFT JOIN ${spwrs.table} USING (${words.wordid},${bncs.posid}) " + //
			"LEFT JOIN ${convtasks.table} USING (${words.wordid},${bncs.posid}) " + //
			"LEFT JOIN ${imaginfs.table} USING (${words.wordid},${bncs.posid}) " + //
			"LEFT JOIN ${bncs.posid} USING (${bncs.posid}) ";

	static String BNCWordPosQuery = SqLiteDialect.BNCBaseWordQuery + //
			"WHERE ${words.word} = ? AND ${bncs.posid} = ?;";

	static String BNCWordQuery = SqLiteDialect.BNCBaseWordQuery + //
			"WHERE ${words.word} = ?;";

	static private final String BNCBaseQuery = "SELECT " + //
			"${bncs.posid},${bncs.posname},${bncs.freq},${bncs.range},${bncs.disp}," + //
			"${convtasks.table}.${bncs.freq1},${convtasks.table}.${bncs.range1},${convtasks.table}.${bncs.disp1}," + //
			"${convtasks.table}.${bncs.freq2},${convtasks.table}.${bncs.range2},${convtasks.table}.${bncs.disp2}," + //
			"${imaginfs.table}.${bncs.freq1},${imaginfs.table}.${bncs.range1},${imaginfs.table}.${bncs.disp1}," + //
			"${imaginfs.table}.${bncs.freq2},${imaginfs.table}.${bncs.range2},${imaginfs.table}.${bncs.disp2}," + //
			"${spwrs.table}.${bncs.freq1},${spwrs.table}.${bncs.range1},${spwrs.table}.${bncs.disp1}," + //
			"${spwrs.table}.${bncs.freq2},${spwrs.table}.${bncs.range2},${spwrs.table}.${bncs.disp2} " + //
			"FROM ${bncs.table} " + //
			"LEFT JOIN ${spwrs.table} USING (${bncs.wordid},${bncs.posid}) " + //
			"LEFT JOIN ${convtasks.table} USING (${bncs.wordid},${bncs.posid}) " + //
			"LEFT JOIN ${imaginfs.table} USING (${bncs.wordid},${bncs.posid}) " + //
			"LEFT JOIN ${poses.table} USING (${bncs.posid}) ";

	static final String BNCQueryFromWordIdAndPos = SqLiteDialect.BNCBaseQuery + //
			"WHERE ${words.wordid} = ? AND ${bncs.posid} = ?;";

	static final String BNCQueryFromWordId = SqLiteDialect.BNCBaseQuery + //
			"WHERE ${words.wordid} = ?;";
}
