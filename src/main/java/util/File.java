package util;

import domain.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class File {

    public static String[] getConfig(String path, String id){
        try{
            String config = Arrays.stream(Files.readString(Paths.get(path)).split("\\r?\\n"))
                    .filter(e -> e.split(" ")[0].equals(id)).findFirst().orElse(null);
            if(config != null){
                return config.split(" ");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getAllConfigsFromFile(String path){
        try{
            return Files.readString(Paths.get(path)).split("\\r?\\n");
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Config getRandomConfigFromFile(List<Config> list, int id) {
        Random r = new Random();
        while (true) {
            int randomIndex = r.nextInt(list.size());
            Config randomConfig = list.get(randomIndex);
            if (randomConfig.getId() == id) continue;
            return randomConfig;
        }

    }

}
