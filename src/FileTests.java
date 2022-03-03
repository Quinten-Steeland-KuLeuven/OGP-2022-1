import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FileTests {

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
}
