import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FileTest {

    private File file1;

    @Before
    public void runBeforeEveryTest() {
        file1 = new File("filename");
    }

    @Test
    public void testEmptyName() {
        file1.setName("");
        assertEquals(file1.getName(), ".");
    }

    @Test
    public void testIllegalName() {
        file1.setName("+*");
        assertEquals(file1.getName(), ".");
    }

    @Test
    public void testNormalName() {
        file1.setName("Aa_1.2-3");
        assertEquals(file1.getName(), "Aa_1.2-3");
    }
}

