<?xml version="1.0"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="2.0">

    <xsl:include href="filter.xslt"/>

    <!-- NCBI XML mirroring fixes -->
    <xsl:template match="/STUDY_SET/STUDY/DESCRIPTOR/STUDY_TYPE/@existing_study_type" priority="0.25">
        <xsl:attribute name="existing_study_type">
            <xsl:value-of select="normalize-space()" />
        </xsl:attribute>
    </xsl:template>
    <xsl:template match="/STUDY_SET/STUDY/DESCRIPTOR/STUDY_TYPE/@existing_study_type[normalize-space(.) = 'Transcriptome Sequencing']">
        <xsl:attribute name="existing_study_type">Transcriptome Analysis</xsl:attribute>
    </xsl:template>

    <!-- filter links -->
    <xsl:template match="STUDY_LINKS[ not( ./child::*[ normalize-space() ] ) ]" />
    <xsl:template match="STUDY_LINKS/child::*[ not( normalize-space() ) ]" />
    <xsl:template match="STUDY_LINKS/child::*/child::*[ not( normalize-space() ) ]" />


    <!-- filter attributes -->
    <xsl:variable name="to-filter" select="$filtered-attribute-tags-cs" />
    <xsl:template match="STUDY_ATTRIBUTES[ ./child::*[ normalize-space() ] = '' or not( ./child::*[ not( contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ) ] ) ]"/> 
   	<xsl:template match="STUDY_ATTRIBUTE[ contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ]"/>
    

    <!-- filter -->

    <xsl:template match="/STUDY_SET/STUDY/DESCRIPTOR/PROJECT_ID" />
    <xsl:template match="/STUDY_SET/STUDY/DESCRIPTOR/CENTER_NAME" />

</xsl:stylesheet>
