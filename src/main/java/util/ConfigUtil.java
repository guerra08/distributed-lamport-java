package util;

import java.util.List;
import java.util.Random;

public class ConfigUtil {

    public static domain.Config getRandomConfigFromList(List<domain.Config> list, int id) {
        Random r = new Random();
        while (true) {
            int randomIndex = r.nextInt(list.size());
            domain.Config randomConfig = list.get(randomIndex);
            if (randomConfig.getId() == id) continue;
            return randomConfig;
        }
    }
}
