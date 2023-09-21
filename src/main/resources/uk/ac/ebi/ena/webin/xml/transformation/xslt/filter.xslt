<?xml version="1.0"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="2.0">

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- filter white space only -->
    <xsl:strip-space elements="*"/>

    <!-- trim text -->
    <xsl:template name="trimText">
        <xsl:value-of select="replace(., '^\s+|\s+$', '')"/>
    </xsl:template>

    <xsl:template name="trimTextAttribute">
        <xsl:attribute name="{local-name()}" namespace="{namespace-uri()}">
            <xsl:value-of select="replace(., '^\s+|\s+$', '')"/>
        </xsl:attribute>
    </xsl:template>

    <!-- remove empty -->
    <xsl:template match="//IDENTIFIERS/SUBMITTER_ID[1][normalize-space()='']"/>
    <!-- add empty SUBMITTER_ID/@namespace -->
    <xsl:template match="//IDENTIFIERS/SUBMITTER_ID[1][not(normalize-space()='') and not(@namespace)]">
        <xsl:element name="SUBMITTER_ID">
            <xsl:attribute name="namespace">
            </xsl:attribute>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:element>
    </xsl:template>
    <!-- remove empty PRIMARY_ID/@label -->
    <!-- remove empty SECONDARY_ID/@label -->
    <!-- remove empty EXTERNAL_ID/@label -->
    <!-- remove empty SUBMITTER_ID/@label -->
    <!-- remove UUID -->
    <!-- remove all but first SUBMITTER_ID -->
    <xsl:template match="//IDENTIFIERS/PRIMARY_ID/@label[normalize-space()='']"/>
    <xsl:template match="//IDENTIFIERS/SECONDARY_ID/@label[normalize-space()='']"/>
    <xsl:template match="//IDENTIFIERS/EXTERNAL_ID/@label[normalize-space()='']"/>
    <xsl:template match="//IDENTIFIERS/SUBMITTER_ID/@label[normalize-space()='']"/>
    <xsl:template match="//IDENTIFIERS/UUID"/>
    <xsl:template match="//IDENTIFIERS/SUBMITTER_ID[preceding-sibling::SUBMITTER_ID]"/>

	<!--new-->
   	<xsl:template match="VALUE[ contains( local-name( parent::* ), '_ATTRIBUTE' ) and normalize-space() = '']"/>
    <xsl:template match="UNITS[ contains( local-name( parent::* ), '_ATTRIBUTE' ) and normalize-space() = '']"/>
    <!--links-->
    <xsl:template match="LABEL[ local-name( parent::* ) = 'ENTREZ_LINK' or local-name( parent::* ) = 'XREF_LINK' and not( normalize-space() ) ]"/>
	<!--end of new-->


    <!-- Fix Ncbi invalid instrument values for run and experiment -->
   <xsl:template match="//PLATFORM/BGISEQ[./INSTRUMENT_MODEL = 'Illumina HiSeq 2000']">
       <ILLUMINA><INSTRUMENT_MODEL>Illumina HiSeq 2000</INSTRUMENT_MODEL></ILLUMINA>
   </xsl:template>

    <!-- Convert ILLUMINA/INSTRUMENT_MODEL/Illumina HiSeq X Ten to HiSeq X Ten-->
    <xsl:template match="//PLATFORM/ILLUMINA[./INSTRUMENT_MODEL = 'Illumina HiSeq X Ten']">
        <ILLUMINA><INSTRUMENT_MODEL>HiSeq X Ten</INSTRUMENT_MODEL></ILLUMINA>
    </xsl:template>



   <!-- filter attributes -->
    <xsl:variable name="filtered-attribute-tags" select="normalize-space( ' ENA-SPOT-COUNT ENA-BASE-COUNT ENA-FIRST-PUBLIC ENA-LAST-UPDATE ENA-LAST-UPDATED ' )" />
    <xsl:variable name="filtered-attribute-tags-cs" select="concat( ',,', translate( $filtered-attribute-tags, ' ', ',' ), ',' )" />
    <xsl:template name="filter-attributes">
        <xsl:variable name="attribute-tag" select="TAG" />
        <xsl:variable name="attribute-value" select="VALUE" />
        <xsl:variable name="attribute-units" select="UNITS" />
        <xsl:if test="not( empty( normalize-space( $attribute-tag ) ) ) and not(contains(
            concat( ', ', normalize-space($filtered-attribute-tags), ', ' ),
            concat( ' ', $attribute-tag, ' ' ) ) )">
			<!--xsl:if test="normalize-space( ./TAG )"-->
	            <xsl:copy>
    	            <xsl:apply-templates select="TAG"/>
    	            <xsl:attribute name="copied"/>
			
        	        <xsl:if test="not( normalize-space($attribute-value)='' )">
            	        <xsl:apply-templates select="VALUE"/>
                	</xsl:if>
                	<xsl:if test="not( normalize-space($attribute-units)='' )">
                    	<xsl:apply-templates select="UNITS"/>
                    </xsl:if>
            	</xsl:copy>
			<!--/xsl:if-->
            <!--
            <xsl:copy>
                <xsl:apply-templates select="@*|node()"/>
            </xsl:copy>
            -->

        </xsl:if>
        <!--
        -->
    </xsl:template>


</xsl:stylesheet>

<!--
<xsl:template match="/PROJECT_SET/PROJECT/PROJECT_ATTRIBUTES/PROJECT_ATTRIBUTE/VALUE[normalize-space()='']" />
<xsl:template match="/PROJECT_SET/PROJECT/PROJECT_ATTRIBUTES/PROJECT_ATTRIBUTE/UNITS[normalize-space()='']" />
-->
