<?xml version="1.0" encoding="UTF-8"?>

<!--TREEBOLIC 2 HTML 10/03/2008 (C) 2008   Author: Bernard Bou -->

<xsl:transform version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="yes" encoding="UTF-8"/>
<xsl:strip-space elements="label"/>

<xsl:template match="/">
	<HTML>
	<HEAD>
		<META http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<TITLE>MYSQL</TITLE>
		<LINK rel="stylesheet" href="style-sql.css" type="text/css"/>
	</HEAD>
	<BODY>
	<xsl:apply-templates select="./mysql"/>
	</BODY>
	</HTML>
</xsl:template>

<xsl:template match="mysql">
	<xsl:apply-templates select="./resultset"/>
</xsl:template>

<xsl:template match="resultset">
	<xsl:choose>
		<xsl:when test="(count(./row) = 1) and (count(./row[1]/field) = 1) and (./row[1]/field[1]/@name='comment' or ./row[1]/field[1]/@name='section' or ./row[1]/field[1]/@name='subsection')">
			<xsl:element name="DIV">
				<xsl:attribute name="class">
					<xsl:value-of select="concat('sql',./row[1]/field[1]/@name)" />
				</xsl:attribute>
				<xsl:value-of select="./row[1]/field[1]/text()" />
			</xsl:element>
		</xsl:when>
		<xsl:otherwise>
			<DIV class="sqlquery">
				<DIV class="sqlstatement">
					<xsl:value-of select="./@statement" />
				</DIV>
				<TABLE class="sqlresultset" border="1">
					<TBODY>
					<TR class="sqlheader">
						<xsl:for-each select="row[1]/field">
							<TH>
							<xsl:value-of select="./@name" />
							</TH>
						</xsl:for-each>
						</TR>
						<xsl:apply-templates select="./row"/>
					</TBODY>
				</TABLE>
			</DIV>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template match="row">
	<TR class="sqlrow">
		<xsl:apply-templates select="./field"/>
	</TR>
</xsl:template>

<xsl:template match="field">
	<TD class="sqlfield">
		<xsl:value-of select="./text()" />
	</TD>
</xsl:template>

<xsl:template match="@*">
	<xsl:attribute name="{name()}">
		<xsl:value-of select="." />
	</xsl:attribute>
</xsl:template>

<xsl:template match="text()">
	<xsl:value-of select="normalize-space()"/>
</xsl:template>

</xsl:transform>
