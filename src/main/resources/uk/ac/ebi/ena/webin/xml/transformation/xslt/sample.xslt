<?xml version="1.0"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="2.0">

    <xsl:include href="filter.xslt"/>

    <!-- filter links -->
    <xsl:template match="SAMPLE_LINKS[ not( ./child::*[ normalize-space() ] ) ]" />
    <xsl:template match="SAMPLE_LINKS/child::*[ not( normalize-space() ) ]" />
    <xsl:template match="SAMPLE_LINKS/child::*/child::*[ not( normalize-space() ) ]" />


    <!-- filter attributes -->
    <xsl:variable name="to-filter" select="$filtered-attribute-tags-cs" />
    <xsl:template match="SAMPLE_ATTRIBUTES[ ./child::*[ normalize-space() ] = '' or not( ./child::*[ not( contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ) ] ) ]"/> 
   	<xsl:template match="SAMPLE_ATTRIBUTE[ contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ]"/>
    
    
    <!-- filter anonymized and individual name elements -->
    <xsl:template match="/SAMPLE_SET/SAMPLE/SAMPLE_NAME/ANONYMIZED_NAME" /> <!-- test -->
    <xsl:template match="/SAMPLE_SET/SAMPLE/SAMPLE_NAME/INDIVIDUAL_NAME" /> <!-- test -->

    <!-- trim sample name taxon id, scientific, name and common name -->
    <xsl:template match="/SAMPLE_SET/SAMPLE/SAMPLE_NAME/SCIENTIFIC_NAME/text()" >
        <xsl:call-template name="trimText"/>
    </xsl:template>
    
    <xsl:template match="/SAMPLE_SET/SAMPLE/SAMPLE_NAME/COMMON_NAME/text()" >
        <xsl:call-template name="trimText"/>
    </xsl:template>

</xsl:stylesheet>
