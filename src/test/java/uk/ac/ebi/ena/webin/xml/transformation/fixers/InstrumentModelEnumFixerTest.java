package uk.ac.ebi.ena.webin.xml.transformation.fixers;

import org.junit.Assert;
import org.junit.Test;

public class InstrumentModelEnumFixerTest {

    private InstrumentModelEnumFixer instrumentModelEnumFixer = new InstrumentModelEnumFixer();

    @Test
    public void test() {
        String withoutExplicitFix = "HiSeq X Five";
        String withoutExplicitFixWrongCase = "hIsEQ x fIVE";

        // The fix of a value that does not have an explicit fix defined for it is the value itself.
        String fixedValue = withoutExplicitFix;

        Assert.assertEquals(fixedValue, instrumentModelEnumFixer.fixValue(withoutExplicitFix));
        Assert.assertEquals(fixedValue, instrumentModelEnumFixer.getValue(withoutExplicitFix));

        Assert.assertEquals(fixedValue, instrumentModelEnumFixer.fixValue(withoutExplicitFixWrongCase));
        Assert.assertEquals(fixedValue, instrumentModelEnumFixer.getValue(withoutExplicitFixWrongCase));

        String withExplicitFix = "AB SOLiD 5500xl";
        String withExplicitFixWrongCase = "ab solId 5500XL";

        fixedValue = "AB 5500xl Genetic Analyzer";

        Assert.assertEquals(fixedValue, instrumentModelEnumFixer.fixValue(withExplicitFix));
        Assert.assertEquals(fixedValue, instrumentModelEnumFixer.getValue(withExplicitFix));

        Assert.assertEquals(fixedValue, instrumentModelEnumFixer.fixValue(withExplicitFixWrongCase));
        Assert.assertEquals(fixedValue, instrumentModelEnumFixer.getValue(withExplicitFixWrongCase));
    }
}
