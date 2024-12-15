package org.semantikos.fn.types

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.SetCollector
import org.semantikos.common.SqlId.getSqlId

object LabelType {

    val COMPARATOR: Comparator<String> = Comparator.naturalOrder()

    val COLLECTOR = SetCollector<String>(COMPARATOR)

    fun add(type: String) {
        COLLECTOR.add(type)
    }

    @RequiresIdFrom(type = LabelType::class)
    fun getIntId(value: String?): Int? {
        return if (value == null) null else COLLECTOR.invoke(value)
    }

    @RequiresIdFrom(type = LabelType::class)
    fun getSqlId(value: String?): Any {
        return getSqlId(getIntId(value))
    }
}

/*
# labeltypeid, labeltype
1, #
2, $
3, ''
4, (
5, )
6, ,
7, :
8, A
9, about
10, Abundant_entities
11, Abuser
12, Accessibility
13, Accessory
14, Accoutrement
15, Accuracy
16, Accused
17, Act
18, Action
19, Activists
20, Activity
21, Actor
22, Added_set
23, Addict
24, Addictant
25, Address
26, Addressee
27, Address_term
28, Adjective
29, Adjective_phrase
30, Adversity
31, Affected
32, Affected_party
33, Affliction
34, Age
35, Agency
36, Agent
37, Aggregate
38, Aggregate_property
39, Aggressor
40, Aggressors
41, ago
42, Agreement
43, Agriculturist
44, Ailment
45, Air
46, AJ0
47, AJ0-AV0
48, AJ0-NN1
49, AJ0-VVD
50, AJ0-VVG
51, AJ0-VVN
52, AJC
53, AJP
54, AJS
55, Alliance
56, Alter
57, Alterant
58, Amends
59, Ammunition
60, Amount
61, Amount_of_discussion
62, Amount_of_information
63, Amount_of_progress
64, Anchor
65, Angle
66, Animal
67, Ant
68, Antecedent
69, Antecedent_actual
70, Antecedent_potential
71, Anti_consequence
72, Apparatus
73, Appositive
74, Appraisal
75, Approximation
76, Area
77, Arguer
78, Arguer1
79, Arguer2
80, Arguers
81, Armor
82, Arraign_authority
83, Artifact
84, Artist
85, Artwork
86, as
87, Asp
88, Assailant
89, Assessor
90, Asset
91, Assets
92, Astronomical_entity
93, at
94, AT0
95, Attachment
96, Attack
97, Attended_person
98, Attendee
99, Attendees
100, Attitude
*/

