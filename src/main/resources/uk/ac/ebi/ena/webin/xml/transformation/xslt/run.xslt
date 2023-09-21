<?xml version="1.0"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="2.0">

    <xsl:include href="filter.xslt"/>

    <!-- filter links -->
    <xsl:template match="RUN_LINKS[ not( ./child::*[ normalize-space() ] ) ]" />
    <xsl:template match="RUN_LINKS/child::*[ not( normalize-space() ) ]" />
    <xsl:template match="RUN_LINKS/child::*/child::*[ not( normalize-space() ) ]" />


    <!-- filter attributes -->
    <xsl:variable name="to-filter" select="$filtered-attribute-tags-cs" />
    <xsl:template match="RUN_ATTRIBUTES[ ./child::*[ normalize-space() ] = '' or not( ./child::*[ not( contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ) ] ) ]"/> 
   	<xsl:template match="RUN_ATTRIBUTE[ contains( $to-filter, concat( ',', normalize-space( TAG/text() ), ',' ) ) ]"/>
   	

    <!-- filter -->
    <xsl:template match="/RUN_SET/RUN/@instrument_model" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/@instrument_name" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/@run_file" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/@total_data_blocks" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/DATA_BLOCK/@name" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/DATA_BLOCK/@sector" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/DATA_BLOCK/@region" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/DATA_BLOCK/@total_spots" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/DATA_BLOCK/@total_reads" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/DATA_BLOCK/@number_channels" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/DATA_BLOCK/@format_code" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/DATA_BLOCK/@serial" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/DATA_BLOCK/FILES/FILE/DATA_SERIES_LABEL" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/PLATFORM/ABI_SOLID/SEQUENCE_LENGTH" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/PLATFORM/ABI_SOLID/COLOR_MATRIX" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/PLATFORM/ABI_SOLID/COLOR_MATRIX_CODE" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/PLATFORM/ABI_SOLID/CYCLE_COUNT" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/PLATFORM/LS454/KEY_SEQUENCE" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/PLATFORM/LS454/FLOW_SEQUENCE" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/PLATFORM/LS454/FLOW_COUNT" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/PLATFORM/ILLUMINA/SEQUENCE_LENGTH" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/PLATFORM/ILLUMINA/CYCLE_COUNT" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/PLATFORM/ILLUMINA/CYCLE_SEQUENCE" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/GAP_DESCRIPTOR" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/SPOT_DESCRIPTOR/SPOT_DECODE_METHOD" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/SPOT_DESCRIPTOR/SPOT_DECODE_SPEC/NUMBER_OF_READS_PER_SPOT" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/SPOT_DESCRIPTOR/SPOT_DECODE_SPEC/ADAPTER_SPEC" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/SPOT_DESCRIPTOR/SPOT_DECODE_SPEC/READ_SPEC/CYCLE_COORD" /> <!-- test -->
    <xsl:template match="/RUN_SET/RUN/SPOT_DESCRIPTOR/SPOT_DECODE_SPEC/READ_SPEC/EXPECTED_BASECALL" /> <!-- test -->

    <!-- trim file names -->
    <xsl:template match="/RUN_SET/RUN/DATA_BLOCK/FILES/FILE/@filename">
        <xsl:call-template name="trimTextAttribute"/>
    </xsl:template>


    <!-- replace file types -->
    <!--illumina_native_fastq -> fastq -->
    <xsl:template match="/RUN_SET/RUN/DATA_BLOCK/FILES/FILE/@filetype[lower-case(.)='illumina_native_fastq']">
        <xsl:attribute name="filetype">
            <xsl:value-of select="'fastq'"/>
        </xsl:attribute>
    </xsl:template>

</xsl:stylesheet>
