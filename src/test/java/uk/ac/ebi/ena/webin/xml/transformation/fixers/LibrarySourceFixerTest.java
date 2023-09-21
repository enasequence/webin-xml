package uk.ac.ebi.ena.webin.xml.transformation.fixers;

import org.junit.Assert;
import org.junit.Test;

public class LibrarySourceFixerTest {

    private LibrarySourceEnumFixer librarySourceEnumFixer = new LibrarySourceEnumFixer();

    @Test
    public void test() {
        Assert.assertEquals("GENOMIC", librarySourceEnumFixer.fixValue("GENOMIC"));
        Assert.assertNull(librarySourceEnumFixer.getValue("GENOMIC"));
    }
}
