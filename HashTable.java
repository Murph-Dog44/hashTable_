import java.util.*;
import java.io.*;

public class HashTable {
    public Node[] hashTable = new Node[1];
    int width = hashTable.length;
    int numItems;  

    // Methods you have to supply:

    public void put(String key) {
        int hashCode = key.hashCode();
        if (hashTable[hashCode%width] != null){
            Node node = hashTable[hashCode%width];
            while (node.next != null){
                node = node.next;
            }
            node.next = new Node(key);
        } else {
            hashTable[hashCode%width] = new Node(key);
        }
        //resize hashTable to 2x
        if (numItems>=(2.0/3.0)*width){
            Node[] newHashTable = new Node[width*2];
            for(int i = 0; i<width; i++){
                if (hashTable[i] != null){
                    Node currNode = hashTable[i];
                    while (currNode != null){
                        if (newHashTable[currNode.value.hashCode()%width] != null){
                            Node node = newHashTable[currNode.value.hashCode()%width];
                            while (node.next != null){
                                node = node.next;
                            }
                            node.next = currNode;
                        } else {
                            hashTable[hashCode%width] = currNode;
                        }
                        currNode = currNode.next;
                    }
                }
            }
            width*=2;
        }
        numItems++;
    }

    public String get(String key) {

    }

    public String remove(String key){
        for (int i = 0; i<width; i++){
            Node papaNode = null;
            Node node = hashTable[i];
            while (node != null){
                if (node.value.equals(key)){
                    if (papaNode != null) {
                        papaNode.next = node.next;
                        return node.value;
                    }
                    hashTable[i] = null;
                    return node.value;
                }
                papaNode = node;
                node = node.next;
            }
        }
        return null;
	}

    public Iterator keys() {
        return new myItr();
    }

    // start at first spot, and iterate through the hashTable one at a time
    private class myItr implements Iterator{
        Node nodeOn = hashTable[0];
        int spotOn = 0;

        @Override
        public boolean hasNext() {
            if (nodeOn.next != null) return true;
            for (int i = spotOn+1; i<width; i++){
                if (hashTable[spotOn] == null); //do nothing
                else return true;
            }
            return false;
        }

        @Override
        public Node next() {
            if (hasNext()){
                if (nodeOn != null && nodeOn.next != null){
                    nodeOn = nodeOn.next;
                    return nodeOn;
                } else {
                    spotOn++;
                    while (hashTable[spotOn] == null){
                        spotOn++;
                    }
                    nodeOn = hashTable[spotOn];
                    return nodeOn;
                }
            }
            return null;
        }

        public void remove(){

        }
    }


    public void print(){

	}

	/**
	* Loads this HashTable from a file named "Lookup.dat".
	*/

    public void load() {
        FileReader fileReader;
        BufferedReader bufferedReader = null;
        
        // Open the file for reading
        try {
            File f = new File(System.getProperty("user.home"), "Lookup.dat");
            fileReader = new FileReader(f);
            bufferedReader = new BufferedReader(fileReader);
        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot find input file \"Lookup.dat\"");
        }
        
        // Read the file contents and save in the HashTable
        try {
            while (true) {
                String key = bufferedReader.readLine();
                if (key == null) return;
                String value = bufferedReader.readLine();
                if (value == null) {
                    System.out.println("Error in input file");
                    System.exit(1);
                }
                String blankLine = bufferedReader.readLine();
                if (!"".equals(blankLine)) {
                    System.out.println("Error in input file");
                    System.exit(1);
                }
                put(key, value);
            }
        }
        catch (IOException e) {
            e.printStackTrace(System.out);
        }
        
        // Close the file when we're done
        try {
            bufferedReader.close( );
        }
        catch(IOException e) {
            e.printStackTrace(System.out);
        }
    }

	/**
	 * Saves this HashTable onto a file named "Lookup.dat".
	 */
	public void save() {
        FileOutputStream stream;
        PrintWriter printWriter = null;
        Iterator iterator;
        
        // Open the file for writing
        try {
            File f = new File(System.getProperty("user.home"), "Lookup.dat");
            stream = new FileOutputStream(f);
            printWriter = new PrintWriter(stream);
        }
        catch (Exception e) {
            System.err.println("Cannot use output file \"Lookup.dat\"");
        }
       
        // Write the contents of this HashTable to the file
        iterator = keys();
        while (iterator.hasNext()) {
            String key = (String)iterator.next();
            printWriter.println(key);
            String value = (String)get(key);
            value = removeNewlines(value);
            printWriter.println(value);
            printWriter.println();
        }
       
        // Close the file when we're done
        printWriter.close( );
    }
    
    /**
     * Replaces all line separator characters (which vary from one platform
     * to the next) with spaces.
     * 
     * @param value The input string, possibly containing line separators.
     * @return The input string with line separators replaced by spaces.
     */
    private String removeNewlines(String value) {
        return value.replaceAll("\r|\n", " ");
    }
}
