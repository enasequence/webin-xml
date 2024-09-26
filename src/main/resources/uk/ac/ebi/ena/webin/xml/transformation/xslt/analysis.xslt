<?xml version="1.0"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="2.0">

    <xsl:include href="filter.xslt"/>

    <!-- filter links -->
    <xsl:template match="ANALYSIS_LINKS[ not( ./child::*[ normalize-space() ] ) ]" />
    <xsl:template match="ANALYSIS_LINKS/child::*[ not( normalize-space() ) ]" />
    <xsl:template match="ANALYSIS_LINKS/child::*/child::*[ not( normalize-space() ) ]" />


    <!-- filter attributes -->
    <xsl:variable name="to-filter" select="$filtered-attribute-tags-cs" />
    <xsl:template match="ANALYSIS_ATTRIBUTES[ ./child::*[ normalize-space() ] = '' or not( ./child::*[ not( contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ) ] ) ]"/> 
   	<xsl:template match="ANALYSIS_ATTRIBUTE[ contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ]"/>

    
    <!-- trim file names -->
    <xsl:template match="/ANALYSIS_SET/ANALYSIS/FILES/FILE/@filename">
        <xsl:call-template name="trimTextAttribute"/>
    </xsl:template>

    
    <!-- replace file types -->
    <!--contig_fasta , scaffold_fasta , chromosome_fasta-> fasta -->
    <xsl:template match="/ANALYSIS_SET/ANALYSIS/FILES/FILE/@filetype[.=('contig_fasta','scaffold_fasta','chromosome_fasta')]">
        <xsl:attribute name="filetype">
            <xsl:value-of select="'fasta'"/>
        </xsl:attribute>
    </xsl:template>

    <!--scaffold_flatfile , contig_flatfile , chromosome_flatfile-> flatfile -->
    <xsl:template match="/ANALYSIS_SET/ANALYSIS/FILES/FILE/@filetype[.=('scaffold_flatfile','contig_flatfile','chromosome_flatfile')]">
        <xsl:attribute name="filetype">
            <xsl:value-of select="'flatfile'"/>
        </xsl:attribute>
    </xsl:template>

    <!-- unlocalised_contig_list , unlocalised_scaffold_list-> unlocalised_list -->
    <xsl:template match="/ANALYSIS_SET/ANALYSIS/FILES/FILE/@filetype[.=('unlocalised_contig_list' ,'unlocalised_contig_list')]">
        <xsl:attribute name="filetype">
            <xsl:value-of select="'unlocalised_list'"/>
        </xsl:attribute>
    </xsl:template>

</xsl:stylesheet>
