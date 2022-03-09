import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/**
 * the file class has the following objects: name, size, creation_time, modification_time, writable
 * @invar The File size must be valid.
 *        | isValidSize(getSize())
 * @version 1.0
 * @author Robbe Vanslambrouck
 * @author Quinten Steeland
 * @author ...
 */

//TODO (1) = aanpassen aan implementatie Wout

public class File {

    // constants
    private static final int MAX_SIZE = Integer.MAX_VALUE;
    private static final int MIN_SIZE = 0;

    // class objects
    private String name;
    private int size;
    private final long creationEpoch;
    private long modificationEpoch;

    //TODO (1)
    private boolean writable;

    // constructors

    public File(String filename, int size, boolean writable){
        this.setName(filename);
        this.setSize(size);
        this.writable = writable;
        this.creationEpoch = getCurrentEpoch();
        this.modificationEpoch = -1;

    }

    public File (String filename) {
        this.setName(filename);
        this.setSize(0);
        //TODO (1)
        this.writable = true;
        this.creationEpoch = getCurrentEpoch();
        this.modificationEpoch = -1;
    }

    // getters & setters

    /**
     * Getter for creation epoch
     * @return  creationEpoch:
     *          the epoch of the time the file was created
     */
    @Basic
    private long getCreationEpoch() {
        return this.creationEpoch;
    }

    /**
     * Setter for the modification epoch
     * @param   modificationEpoch:
     *          The epoch of when the file was modified.
     */
    @Basic
    private void setModificationEpoch(long modificationEpoch) {
        this.modificationEpoch = modificationEpoch;
    }

    public String getCreationTime() {
        return epochToHumanReadable(getCreationEpoch());
    }

    private long getModificationEpoch() {
        return this.modificationEpoch;
    }

    public String getModificationTime() {
        if (getModificationEpoch() < 0) {
            return "File has not been modified yet.";
        } else {
            return epochToHumanReadable(getModificationEpoch());
        }
    }

    /**
     * Returns name of File object.
     * @return  name
     *          the name of the file
     */
    @Basic
    public String getName() {
        return name;
    }

    /**
     * sets name of file
     * @param   name
     *          the name of the file
     * @post    if the name is valid, the name will be changed to the input name
     *          any invalid characters will be filtered out
     *          if the name is empty it will be set to "."
     *          if there are no write permissions, the name will not change
     */
    public void setName(String name) {
        //TODO (1)
        if (writable) {
            String filteredName = name.replaceAll("[^a-zA-Z0-9._\\-]", "");
            if (filteredName.equals(""))
                filteredName = ".";
            this.name = filteredName;
            updateModificationEpoch();
        }
    }

    /**
     * Returns the size of the File object
     * @return size
     */
    @Basic
    @Raw
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

    /**
     *
     * Check if two files have an overlapping use period.
     * WARNING: if the first file is modified within the millisecond that the second file is created,
     *          the function may return false when it should return true!
     * @param   otherFile:
     *          the file to compare with
     * @return  boolean:
     *          true if overlapping use period, false if not.
     */
    public boolean hasOverlappingUsePeriod(File otherFile) {
        if (this.getCreationEpoch() < otherFile.getCreationEpoch()) {
            return  this.getCreationEpoch() <= otherFile.getCreationEpoch() &&
                    otherFile.getCreationEpoch() < this.getModificationEpoch() &&
                    otherFile.getModificationEpoch() >= 0;
        } else {
            return  otherFile.getCreationEpoch() <= this.getCreationEpoch() &&
                    this.getCreationEpoch() < otherFile.getModificationEpoch() &&
                    this.getModificationEpoch() >= 0;
        }
    }

    //
    private void updateModificationEpoch(){
        setModificationEpoch(getCurrentEpoch());
    }

    private Long getCurrentEpoch(){
        return System.currentTimeMillis();
    }

    private String epochToHumanReadable(long epoch){
        LocalDateTime myDateObj = LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }
}
