package org.sqlbuilder.fn.types;

import org.sqlbuilder.fn.Collector;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PtType
{
	// pttypes.table=fnpttypes
	// pttypes.create=CREATE TABLE IF NOT EXISTS %Fn_pttypes.table% ( ptid INTEGER NOT NULL AUTO_INCREMENT,pt VARCHAR(20),PRIMARY KEY (ptid) );
	// pttypes.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_pttypes.table%_pt ON %Fn_pttypes.table% (pt);
	// pttypes.no-unq1=DROP INDEX IF EXISTS unq_%Fn_pttypes.table%_pt;

	public static final Comparator<String> COMPARATOR = Comparator.naturalOrder();

	public static final Collector<String> COLLECTOR = new Collector<>(COMPARATOR);

	public static Object getId(String value)
	{
		if (value != null)
		{
			Integer id = COLLECTOR.get(value);
			if (id != null)
			{
				return id;
			}
		}
		return "NULL";
	}

/*
# ptid, pt
1, --
2, 2nd
3, 3rd
4, A
5, AJP
6, APos
7, AVP
8, CNI
9, DEN
10, DNI
11, INC
12, INI
13, N
14, NP
15, Num
16, Obj
17, Poss
18, PPadjP
19, PPing[about]
20, PPing[after]
21, PPing[against]
22, PPing[along]
23, PPing[among]
24, PPing[around]
25, PPing[as]
26, PPing[at]
27, PPing[before]
28, PPing[between]
29, PPing[by]
30, PPing[during]
31, PPing[for]
32, PPing[from]
33, PPing[into]
34, PPing[in]
35, PPing[later]
36, PPing[like]
37, PPing[off]
38, PPing[of]
39, PPing[on]
40, PPing[outside]
41, PPing[over]
42, PPing[regarding]
43, PPing[since]
44, PPing[than]
45, PPing[through]
46, PPing[to]
47, PPing[until]
48, PPing[upon]
49, PPing[while]
50, PPing[without]
51, PPing[with]
52, PPinterrog
53, PP[aboard]
54, PP[about]
55, PP[above]
56, PP[according to]
57, PP[according]
58, PP[across]
59, PP[after]
60, PP[against]
61, PP[ago]
62, PP[ahead of]
63, PP[alongside]
64, PP[along]
65, PP[amidst]
66, PP[amid]
67, PP[amongst]
68, PP[among]
69, PP[around]
70, PP[astride]
71, PP[as]
72, PP[athwart]
73, PP[at]
74, PP[away]
75, PP[because of]
76, PP[before]
77, PP[behind]
78, PP[below]
79, PP[beneath]
80, PP[besides]
81, PP[beside]
82, PP[between]
83, PP[betwixt]
84, PP[beyond]
85, PP[by]
86, PP[concerning]
87, PP[depending on]
88, PP[despite]
89, PP[down]
90, PP[due]
91, PP[during]
92, PP[earlier]
93, PP[except]
94, PP[following]
95, PP[for]
96, PP[from]
97, PP[including]
98, PP[inside]
99, PP[into]
100, PP[in]
101, PP[irrespective of]
102, PP[later]
103, PP[less]
104, PP[like]
105, PP[nearby]
106, PP[near]
107, PP[o']
108, PP[off]
109, PP[of]
110, PP[onto]
111, PP[on]
112, PP[opposite]
113, PP[outside]
114, PP[out]
115, PP[over]
116, PP[past]
117, PP[per]
118, PP[regarding]
119, PP[re]
120, PP[round]
121, PP[since]
122, PP[than]
123, PP[throughout]
124, PP[through]
125, PP[till]
126, PP[together with]
127, PP[towards]
128, PP[toward]
129, PP[to]
130, PP[underneath]
131, PP[under]
132, PP[unlike]
133, PP[until]
134, PP[unto]
135, PP[upon]
136, PP[up]
137, PP[via]
138, PP[while]
139, PP[whilst]
140, PP[within]
141, PP[without]
142, PP[with]
143, PP[worth]
144, QUO
145, Sabs
146, Sbrst
147, Sfin
148, Sforto
149, Sing
150, Sinterrog
151, Srel
152, Sto
153, Sub
154, Sun
155, Swhether
156, unknown
157, VPbrst
158, VPed
159, VPfin
160, VPing
161, VPto
162, VPtorel
 */
}
