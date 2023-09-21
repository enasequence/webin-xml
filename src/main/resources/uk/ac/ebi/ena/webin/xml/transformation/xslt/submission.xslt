<?xml version="1.0"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="2.0">

    <xsl:include href="filter.xslt"/>

    <!-- filter links -->
    <xsl:template match="SUBMISSION_LINKS[ not( ./child::*[ normalize-space() ] ) ]" />
    <xsl:template match="SUBMISSION_LINKS/child::*[ not( normalize-space() ) ]" />
    <xsl:template match="SUBMISSION_LINKS/child::*/child::*[ not( normalize-space() ) ]" />


    <!-- filter attributes -->
    <xsl:variable name="to-filter" select="$filtered-attribute-tags-cs" />
    <xsl:template match="SUBMISSION_ATTRIBUTES[ ./child::*[ normalize-space() ] = '' or not( ./child::*[ not( contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ) ] ) ]"/> 
   	<xsl:template match="SUBMISSION_ATTRIBUTE[ contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ]"/>


    <!-- filter -->
    <xsl:template match="/SUBMISSION_SET/SUBMISSION/ACTIONS/ACTION/ADD/@notes" />
    <xsl:template match="/SUBMISSION_SET/SUBMISSION/ACTIONS/ACTION/HOLD/@notes" />
    <xsl:template match="/SUBMISSION_SET/SUBMISSION/ACTIONS/ACTION/RELEASE/@notes" />
    <xsl:template match="/SUBMISSION_SET/SUBMISSION/FILES" />
    <xsl:template match="/SUBMISSION_SET/SUBMISSION/@submission_id" />
    <xsl:template match="/SUBMISSION_SET/SUBMISSION/@handle" />

</xsl:stylesheet>
