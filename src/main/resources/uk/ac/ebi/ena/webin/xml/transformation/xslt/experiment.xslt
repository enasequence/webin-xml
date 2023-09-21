<?xml version="1.0"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="2.0">

    <xsl:include href="filter.xslt"/>

    <!-- NCBI XML mirroring fixes -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/DESIGN/LIBRARY_DESCRIPTOR/LIBRARY_STRATEGY/text()">
        <xsl:value-of select="normalize-space()" />
    </xsl:template>
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/DESIGN/LIBRARY_DESCRIPTOR/LIBRARY_SOURCE/text()">
        <xsl:value-of select="normalize-space()" />
    </xsl:template>
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/DESIGN/LIBRARY_DESCRIPTOR/LIBRARY_SELECTION/text()">
        <xsl:value-of select="normalize-space()" />
    </xsl:template>
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PLATFORM/*/INSTRUMENT_MODEL/text()">
        <xsl:value-of select="normalize-space()" />
    </xsl:template>

    <!-- filter links -->
    <xsl:template match="EXPERIMENT_LINKS[ not( ./child::*[ normalize-space() ] ) ]" />
    <xsl:template match="EXPERIMENT_LINKS/child::*[ not( normalize-space() ) ]" />
    <xsl:template match="EXPERIMENT_LINKS/child::*/child::*[ not( normalize-space() ) ]" />


    <!-- filter attributes -->
    <xsl:variable name="to-filter" select="$filtered-attribute-tags-cs" />
    <xsl:template match="EXPERIMENT_ATTRIBUTES[ ./child::*[ normalize-space() ] = '' or not( ./child::*[ not( contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ) ] ) ]"/> 
   	<xsl:template match="EXPERIMENT_ATTRIBUTE[ contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ]"/>

 
    <!-- filter -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/@expected_number_runs" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/@expected_number_spots" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/@expected_number_reads" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PLATFORM/ABI_SOLID/SEQUENCE_LENGTH" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PLATFORM/ABI_SOLID/COLOR_MATRIX" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PLATFORM/ABI_SOLID/COLOR_MATRIX_CODE" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PLATFORM/ABI_SOLID/CYCLE_COUNT" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PLATFORM/LS454/KEY_SEQUENCE" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PLATFORM/LS454/FLOW_SEQUENCE" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PLATFORM/LS454/FLOW_COUNT" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PLATFORM/ILLUMINA/SEQUENCE_LENGTH" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PLATFORM/ILLUMINA/CYCLE_COUNT" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PLATFORM/ILLUMINA/CYCLE_SEQUENCE" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/DESIGN/GAP_DESCRIPTOR" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PROCESSING/BASE_CALLS" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/PROCESSING/QUALITY_SCORES" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/DESIGN/LIBRARY_DESCRIPTOR/LIBRARY_LAYOUT/PAIRED/@ORIENTATION" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/DESIGN/SPOT_DESCRIPTOR/SPOT_DECODE_METHOD" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/DESIGN/SPOT_DESCRIPTOR/SPOT_DECODE_SPEC/NUMBER_OF_READS_PER_SPOT" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/DESIGN/SPOT_DESCRIPTOR/SPOT_DECODE_SPEC/ADAPTER_SPEC" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/DESIGN/SPOT_DESCRIPTOR/SPOT_DECODE_SPEC/READ_SPEC/CYCLE_COORD" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/DESIGN/SPOT_DESCRIPTOR/SPOT_DECODE_SPEC/READ_SPEC/EXPECTED_BASECALL" /> <!-- test -->
    <xsl:template match="/EXPERIMENT_SET/EXPERIMENT/DESIGN/SAMPLE_DESCRIPTOR[POOL]/IDENTIFIERS" /> <!-- test -->

</xsl:stylesheet>
