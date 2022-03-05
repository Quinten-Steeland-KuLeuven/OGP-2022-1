import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.beans.BeanProperty;

/**
 * the file class has the following objects: name, size, creation_time, modification_time, writable
 * @invar The File size must be valid.
 *        | isValidSize(getSize())
 * @version 1.0
 * @author Robbe Vanslambrouck
 * @author Quinten Steeland
 * @author
 */

public class File {

    // constants
    private static final int MAX_SIZE = Integer.MAX_VALUE;
    private static final int MIN_SIZE = 0;

    // class objects
    private String name;
    private int size;

    // constructors

    public File (String filename) {
        this.setName(filename);
        this.setSize(0);
    }

    // getters & setters

    /**
     *  get name of file
     * @return  name:
     *          name of the file
     */
    @Basic
    public String getName() {
        return name;
    }

    /**
     * sets name of file
     * @param   name:
     *          name of the file
     * @post    if the name is valid, the name will be changed to the input name
     *          any invalid characters will be filtered out
     *          if the name is empty it will be set to "."
     */
    public void setName(String name) {
        String filteredName = name.replaceAll("[^a-zA-Z0-9._\\-]","");
        if (filteredName.equals(""))
            filteredName = ".";
        this.name = filteredName;
    }

    /**
     * Returns the size of the File object
     * @return size
     */
    @Basic @Raw
    public int getSize() {
        return this.size;
    }

    /**
     * sets the size of this File object to the given parameter size
     *
     * @param size
     * the new size of this File
     * @pre the size specified is a valid File size.
     *      | isValidSize(size)
     * @post the size of this File are set to the given parameter size.
     *       | new.getSize() == size
     */
    @Raw
    public void setSize(int size) {
        this.size = size;
    }

    // class methods

    /**
     * Adds the given amount to the current size
     * @param amount
     *        amount that is added to the size
     * @effect the current size is incremented by the given amount and
     *         set as the new size if this File
     */
    @Raw
    public void enlarge(int amount) {
        setSize(getSize() + amount);
    }

    /**
     * Subtracts the given amount from the current size
     * @param amount
     *        amount that is subtracted from the size
     * @effect the current size is decremented by the given amount and
     *         set as the new size if this File
     */
    @Raw
    public void shorten(int amount) {
        setSize(getSize() - amount);
    }

    /**
     * Checks if the given size is a valid size.
     * @param size
     *        the size the check
     * @return True if the given size is a valid size, False if not.
     */
    @Raw
    public boolean isValidSize(int size) {
        return size >= MIN_SIZE && size <= MAX_SIZE;
    }
}
