package me.moshe.dropstack.file;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class DroppedItemData {
    public static HashMap<UUID, Integer> droppedItems = new HashMap<UUID, Integer>();
    public static String Path = "plugins/DropsStack" + File.separator + "data.dat";

    public static void setup(){
        File file = new File(Path);
        new File("plugins/DropsStack").mkdir();
        if(file.exists()){
            droppedItems = load();
        }
        if(droppedItems == null){
            droppedItems = new HashMap<UUID, Integer>();
        }
    }

    public static void save(){
        File file = new File(Path);
        new File("plugins/DropsStack").mkdir();
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Path));
            oos.writeObject(droppedItems);
            oos.flush();
            oos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static HashMap<UUID, Integer> load(){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Path));
            Object result = ois.readObject();
            ois.close();
            return (HashMap<UUID,Integer>)result;
        }catch(Exception e){
            return null;
        }
    }
}
