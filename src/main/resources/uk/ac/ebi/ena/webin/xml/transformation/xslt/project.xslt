<?xml version="1.0"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="2.0">

    <xsl:include href="filter.xslt"/>

    <!-- filter links -->
    <xsl:template match="PROJECT_LINKS[ not( ./child::*[ normalize-space() ] ) ]" />
    <xsl:template match="PROJECT_LINKS/child::*[ not( normalize-space() ) ]" />
    <xsl:template match="PROJECT_LINKS/child::*/child::*[ not( normalize-space() ) ]" />


    <!-- filter attributes -->
    <xsl:variable name="to-filter" select="$filtered-attribute-tags-cs" />
    <xsl:template match="PROJECT_ATTRIBUTES[ ./child::*[ normalize-space() ] = '' or not( ./child::*[ not( contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ) ] ) ]"/> 
   	<xsl:template match="PROJECT_ATTRIBUTE[ contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ]"/>


    <!-- rename ORGANISM/ISOLATENAME -->
    <xsl:template name="rename-isolate">
        <xsl:element name="ISOLATE">
            <xsl:apply-templates select="@* | node()"/>
        </xsl:element>
    </xsl:template>
    <xsl:template match="/PROJECT_SET/PROJECT/SUBMISSION_PROJECT/ORGANISM/ISOLATENAME" >
        <xsl:call-template name="rename-isolate"/>
    </xsl:template>
    <xsl:template match="/PROJECT_SET/PROJECT/UMBRELLA_PROJECT/ORGANISM/ISOLATENAME" >
        <xsl:call-template name="rename-isolate"/>
    </xsl:template>

    <!-- filter -->
    <xsl:template match="/PROJECT_SET/PROJECT/SUBMISSION_PROJECT/@*" /> <!-- test -->
    <xsl:template match="/PROJECT_SET/PROJECT/SUBMISSION_PROJECT/OBJECTIVE" />
    <xsl:template match="/PROJECT_SET/PROJECT/RELATED_CHROMOSOMES" /> <!-- test -->

</xsl:stylesheet>
