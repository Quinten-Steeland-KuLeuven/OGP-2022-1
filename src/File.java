import be.kuleuven.cs.som.annotate.Basic;

import java.beans.BeanProperty;

/**
 * the file class has the following objects: name, size, creation_time, modification_time, writable
 *
 * @version 1.0
 * @author Robbe Vanslambrouck
 * @author Quinten Steeland
 * @author
 */

public class File {

    // class objects
    private String name;
    private int size;

    // constructors

    public File (String filename) {
        this.setName(filename);
        this.size = 0;
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

    // class methods
}
