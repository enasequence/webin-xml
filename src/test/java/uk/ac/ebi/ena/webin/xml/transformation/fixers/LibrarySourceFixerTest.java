package uk.ac.ebi.ena.webin.xml.transformation.fixers;

import org.junit.Assert;
import org.junit.Test;

public class LibrarySourceFixerTest {

    private LibrarySourceEnumFixer librarySourceEnumFixer = new LibrarySourceEnumFixer();

    @Test
    public void test() {
        Assert.assertEquals("GENOMIC", librarySourceEnumFixer.fixValue("GENOMIC"));
        Assert.assertEquals("GENOMIC", librarySourceEnumFixer.getValue("GENOMIC"));

        Assert.assertEquals("GENOMIC", librarySourceEnumFixer.fixValue("genomic"));
        Assert.assertEquals("GENOMIC", librarySourceEnumFixer.getValue("genomic"));
    }
}
