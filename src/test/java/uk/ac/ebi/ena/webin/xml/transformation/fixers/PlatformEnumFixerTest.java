package uk.ac.ebi.ena.webin.xml.transformation.fixers;

import org.junit.Assert;
import org.junit.Test;

public class PlatformEnumFixerTest {

    private PlatformEnumFixer platformEnumFixer = new PlatformEnumFixer();

    @Test
    public void test() {
        Assert.assertEquals("LS454", platformEnumFixer.fixValue("454 GS"));
        Assert.assertEquals("LS454", platformEnumFixer.getValue("454 GS"));
    }
}
