import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/**
 * The file class has the following objects: name, size, creation_time, modification_time, writable.
 * @invar The File size must be valid.
 *        | isValidSize(getSize())
 * @invar The File must be writable.
 *        | isWritable()
 * @version 1.0
 * @author Robbe Vanslambrouck
 * @author Quinten Steeland
 * @author Wout Voet
 */

public class File {

    // constants
    /** Maximum size of a file. */
    private static final int MAX_SIZE = Integer.MAX_VALUE;
    /** Minimum size of a file. */
    private static final int MIN_SIZE = 0;
    /** Boolean that shows if file is writable or read-only. */
    private boolean writable = true;
    // class objects
    /** Name of the file. */
    private String name;
    /** Size of the file (bytes). */
    private int size;
    /** Epoch of file creation time. */
    private final long creationEpoch;
    /** Epoch of file modification time. */
    private long modificationEpoch;
    // constructors

    /**
     * Constructor for a file, with filename, size and writable as parameters.
     * @param   filename:
     *          Name of the file.
     * @param   size:
     *          Size of the file (in bytes).
     * @param   writable:
     *          If the name and the size of the file can change or not.
     */
    public File(String filename, int size, boolean writable){
        this.setName(filename);
        this.setSize(size);
        this.writable = writable;
        this.creationEpoch = getCurrentEpoch();
        this.modificationEpoch = -1;

    }

    /**
     * Constructor for a file, with filename as parameter.
     * Size will be 0 and writable will be true.
     * @param   filename:
     *          Name of the file.
     */
    public File (String filename) {
        this.setName(filename);
        this.setSize(0);
        this.writable = true;
        this.creationEpoch = getCurrentEpoch();
        this.modificationEpoch = -1;
    }

    // getters & setters

    /**
     * Getter for the creation epoch of the file.
     * @return  long:
     *          Epoch of the file creation time.
     *
     */
    @Basic
    private long getCreationEpoch() {
        return this.creationEpoch;
    }

    /**
     * Setter for the modification epoch.
     * @param   modificationEpoch:
     *          The epoch of when the file was modified.
     */
    private void setModificationEpoch(long modificationEpoch) {
        this.modificationEpoch = modificationEpoch;
    }

    /**
     * Getter for the creation time of the file.
     * @return  String:
     *          Timestamp of the file creation time.
     *
     */
    @Basic
    public String getCreationTime() {
        return epochToHumanReadable(getCreationEpoch());
    }

    /**
     * Getter for the modification epoch of the file.
     * @return  long:
     *          Epoch of the file modification time.
     *
     */
    @Basic
    private long getModificationEpoch() {
        return this.modificationEpoch;
    }

    /**
     * Getter for the modification time of the file.
     * If the file has not been modified yet, it will return the string "File has not been modified yet.".
     * @return  String:
     *          Timestamp of the file modification time.
     *
     */
    @Basic
    public String getModificationTime() {
        if (getModificationEpoch() < 0) {
            return "File has not been modified yet.";
        } else {
            return epochToHumanReadable(getModificationEpoch());
        }
    }

    /**
     * Returns name of File object.
     * @return  String name:
     *          The name of the file.
     */
    @Basic
    public String getName() {
        return this.name;
    }

    /**
     * Sets name of file.
     * @param   name:
     *          The name of the file.
     * @post    If the name is valid, the name will be changed to the input name,
     *          any invalid characters will be filtered out.
     *          If the name is empty it will be set to ".".
     *          If there are no write permissions, the name will not change.
     */
    public void setName(String name) {
        if (this.isWritable()) {
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
     * @pre  the size specified is a valid File size.
     *       | isValidSize(size)
     * @pre  the file must be writable.
     *       | isWritable()
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
     * Checks if File is Writable or Read only.
     * @return True if file Writable, False if Read only.
     */
    public boolean isWritable() {
        return this.writable;
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

    /**
     * Function that changes the modification epoch to the current epoch.
     */
    private void updateModificationEpoch(){
        setModificationEpoch(getCurrentEpoch());
    }

    /**
     * Function that returns the current epoch.
     * @return  Long:
     *          The current epoch.
     */
    @Basic
    private Long getCurrentEpoch(){
        return System.currentTimeMillis();
    }

    /**
     * Function that converts a given epoch to a human-readable timestamp using the java.time.
     * @param   epoch:
     *          The epoch that needs to be converted to human-readable timestamp.
     * @return  String:
     *          Timestamp of input epoch.
     */
    private String epochToHumanReadable(long epoch){
        LocalDateTime myDateObj = LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }
}
