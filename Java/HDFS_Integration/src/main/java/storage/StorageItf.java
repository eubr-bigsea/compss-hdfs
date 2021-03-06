package storage;

import integration.BlockLocality;
import integration.HDFS;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Representation of the Storage ITF
 *
 */
public final class StorageItf {

    static ArrayList<BlockLocality> HDFS_SPLITS_LIST =  new ArrayList<BlockLocality>();
    static Map<Integer, Integer[]> mapper = new HashMap<Integer, Integer[]>();

    static HDFS dfs;

    private static final String STORAGE_NOT_FOUND_MESSAGE = "You are trying to start a run with "
            + "persistent object storage but any back-end client is loaded in the classpath.";


    /**
     * Constructor
     * 
     */
    public StorageItf() {
    }

    /**
     * Initializes the persistent storage
     *
     * @exclude
     * @param storageConf
     * @throws StorageException
     *
     * @deprecated 
     */

    public static void init(String storageConf) throws StorageException {
        BufferedReader br   = null;
        String defaultfs    = null;
        int number_block    = 0;
        int file_id = 0;
        String filename     = "";

        try {
            br = new BufferedReader(new FileReader(storageConf));
            defaultfs   = br.readLine();
            System.out.println("[INFO] - HDFS MASTER NODE:"+defaultfs);
            dfs =  new HDFS(defaultfs);
            while ((filename= br.readLine()) != null){
                System.out.println("[INFO] - Creating HDFS's Blocks of "+filename);
                HDFS_SPLITS_LIST.addAll(dfs.findALLBlocksByStorageAPI(filename,number_block));
                mapper.put(file_id, new Integer[] {number_block, HDFS_SPLITS_LIST.size()-1});
               // System.out.println("START:"+number_block+" END:"+(HDFS_SPLITS_LIST.size()-1));
                number_block=HDFS_SPLITS_LIST.size();
                file_id++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Returns a list of blocks of a file in the HDFS
     *
     * @param id_file   id of the hdfs's file
     * @return          List of blocks splitted by number of the real blocks in the HDFS
     */
    public static ArrayList<BlockLocality> getBlocks(int id_file) throws StorageException {
        if (id_file <= mapper.size()) {
            Integer[] pos = mapper.get(id_file);
            //System.out.println(pos[0]+"---"+(pos[1]+1));
            return new ArrayList<BlockLocality> (HDFS_SPLITS_LIST.subList(pos[0],pos[1]+1));
        }else
            return null;
    }

    /**
     * Stops the persistent storage
     *
     * @throws StorageException
     */
    public static void finish() throws StorageException {
        //throw new StorageException(STORAGE_NOT_FOUND_MESSAGE);
        dfs.closeConection();
    }

    /**
     * Returns all the valid locations of a given id
     *
     * @exclude
     * @param pscoId
     * @return
     * @throws StorageException
     *
     * @deprecated 
     */
    public static List<String> getLocations(String pscoId) throws StorageException {
        //throw new StorageException(STORAGE_NOT_FOUND_MESSAGE);

        List<String> locations = new ArrayList<String>();
        for(BlockLocality b: HDFS_SPLITS_LIST){
            if (b.getID().equals(pscoId)) {
                locations = new ArrayList<String>( Arrays.asList(b.getLocations()));
            }
        }


        return  locations;
    }

    /**
     * Creates a new replica of PSCO id @id in host @hostname
     *
     * @param id
     * @param hostName
     * @throws StorageException
     *
     * @deprecated 
     */
    public static void newReplica(String id, String hostName) throws StorageException {
        //throw new StorageException(STORAGE_NOT_FOUND_MESSAGE);
    }

    /**
     * Create a new version of the PSCO id @id in the host @hostname Returns the id of the new version
     *
     * @param id
     * @param hostName
     * @return
     * @throws StorageException
     *
     * @deprecated 
     */
    public static String newVersion(String id, String hostName) throws StorageException {
        //throw new StorageException(STORAGE_NOT_FOUND_MESSAGE);
        return null;
    }

    /**
     * Returns the object with id @id This function retrieves the object from any location
     *
     * @param id
     * @return
     * @throws StorageException
     *
     * @deprecated 
     */
    public static Object getByID(String id) throws StorageException {
        //throw new StorageException(STORAGE_NOT_FOUND_MESSAGE);
        BlockLocality c = new BlockLocality();
        for(BlockLocality b: HDFS_SPLITS_LIST){
            if (b.getID().equals(id))
                c = b;
        }
        return c;
    }

    /**
     * Executes the task into persistent storage
     *
     * @param id
     * @param descriptor
     * @param values
     * @param hostName
     * @param callback
     * @return
     * @throws StorageException
     *
     * @deprecated 
     */
    public static String executeTask(String id, String descriptor, Object[] values, String hostName, CallbackHandler callback)
            throws StorageException {
        throw new StorageException(STORAGE_NOT_FOUND_MESSAGE);
    }

    /**
     * Retrieves the result of persistent storage execution
     *
     * @param event
     * @return
     *
     * @deprecated 
     */
    public static Object getResult(CallbackEvent event) throws StorageException {
        // Nothing to do
        return null;
    }

    /**
     * Consolidates all intermediate versions to the final id
     *
     * @param idFinal
     * @throws StorageException
     *
     * @deprecated 
     */
    public static void consolidateVersion(String idFinal) throws StorageException {
        throw new StorageException(STORAGE_NOT_FOUND_MESSAGE);
    }

}
