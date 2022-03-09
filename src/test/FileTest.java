//junit
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

//for delay
import java.util.concurrent.TimeUnit;

/*
    @FixMethodOrder(MethodSorters.NAME_ASCENDING):
    This is used to run test with delay last.
    This is why they are named test_`letter`_`name`
    The letter gives priorities to the test that don't take long.
    (run first <- a...z -> run last)
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileTest {

    private File file1;
    private File file2;

    @Before
    public void runBeforeEveryTest() {
        file1 = new File("filename", true);
        file2 = new File("file_2", true);
    }


    //Filename tests
    @Test
    public void test_a_EmptyName() {
        file1.setName("");
        assertEquals(file1.getName(), ".");
    }

    @Test
    public void test_a_IllegalName() {
        file1.setName("+*");
        assertEquals(file1.getName(), ".");
    }

    @Test
    public void test_a_SemiIllegalName() {
        file1.setName("A@2");
        assertEquals(file1.getName(), "A2");
    }

    @Test
    public void test_a_SemiIllegalNameTwo() {
        file1.setName("~.!@#$%^&*()_+Az09-+/*");
        assertEquals(file1.getName(), "._Az09-");
    }

    @Test
    public void test_a_NormalName() {
        file1.setName("Aa_1.2-3");
        assertEquals(file1.getName(), "Aa_1.2-3");
    }

    //Creation / Modification tests
    @Test
    public void test_a_NoModificationTime() {
        assertEquals(file1.getModificationTime(),"File has not been modified yet.");
    }

    @Test
    public void test_b_AModificationTime() {
        file1.setName("change");
        assertNotEquals(file1.getModificationTime(),"File has not been modified yet.");
    }

    @Test
    public void test_n_UseOverlapOne() {
        File file3 = new File("3");
        //sleep so creationTime is different from modificationTime
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        file1.setName("change");
        file3.setName("change2");

        assertTrue(file1.hasOverlappingUsePeriod(file3));
    }

    @Test
    public void test_n_UseOverlapTwo() {
        File file3 = new File("3");
        //sleep so creationTime is different from modificationTime
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        file1.setName("change");
        file3.setName("change2");

        assertTrue(file3.hasOverlappingUsePeriod(file1));
    }

    @Test
    public void test_n_UseOverlapThree() {
        file1.setName("change");
        //sleep
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        File file3 = new File("3");

        file3.setName("change2");

        assertFalse(file1.hasOverlappingUsePeriod(file3));
    }

    @Test
    public void test_n_UseOverlapFour() {
        file1.setName("change");
        //sleep
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        File file3 = new File("3");

        file3.setName("change2");

        assertFalse(file3.hasOverlappingUsePeriod(file1));
    }

    @Test
    public void test_m_UseOverlapFive() {
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        File file3 = new File("3");
        //sleep so creationTime is different from modificationTime
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        file1.setName("change");
        file3.setName("change2");

        assertTrue(file1.hasOverlappingUsePeriod(file3));
    }

    @Test
    public void test_m_UseOverlapSix() {
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        File file3 = new File("3");
        //sleep so creationTime is different from modificationTime
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        file1.setName("change");
        file3.setName("change2");

        assertTrue(file3.hasOverlappingUsePeriod(file1));
    }

    @Test
    public void test_n_UseOverlapNoModification() {
        File file3 = new File("3");
        //sleep
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        file1.setName("change");

        assertFalse(file3.hasOverlappingUsePeriod(file1));
    }

    @Test
    public void test_a_NoOverlap() {
        File file3 = new File("3");
        file3.setName("change");

        assertFalse(file3.hasOverlappingUsePeriod(file1));
    }

    @Test
    public void test_z_DelayedModificationTime() {
        //sleep so creationTime is different from modificationTime
        try {
            TimeUnit.MILLISECONDS.sleep(1500);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        file1.setName("change");
        assertNotEquals(file1.getModificationTime(),file1.getCreationTime());
    }
}

