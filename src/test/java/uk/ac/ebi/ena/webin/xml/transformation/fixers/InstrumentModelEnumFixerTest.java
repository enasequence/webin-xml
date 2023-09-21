package uk.ac.ebi.ena.webin.xml.transformation.fixers;

import org.junit.Assert;
import org.junit.Test;

public class InstrumentModelEnumFixerTest {

    private InstrumentModelEnumFixer instrumentModelEnumFixer = new InstrumentModelEnumFixer();

    @Test
    public void test() {
        String instrumentModelWithoutFix = "HiSeq X Five";

        // The fix of a value that does not have an explicit fix defined for it is the value itself.
        Assert.assertEquals(instrumentModelWithoutFix,
            instrumentModelEnumFixer.fixValue(instrumentModelWithoutFix));
        Assert.assertNull(instrumentModelEnumFixer.getValue(instrumentModelWithoutFix));

        String instrumentModelWithFix = "AB SOLiD 5500xl";
        String fixedValue = "AB 5500xl Genetic Analyzer";

        Assert.assertEquals(fixedValue, instrumentModelEnumFixer.fixValue(instrumentModelWithFix));
        Assert.assertEquals(fixedValue, instrumentModelEnumFixer.getValue(instrumentModelWithFix));
    }
}
