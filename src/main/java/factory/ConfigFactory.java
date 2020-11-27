package factory;

import domain.Config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigFactory {

    public static List<Config> buildOtherConfigsList(String[] configs, String id){
        return Arrays.stream(configs).filter(e -> e.split(" ")[0].equals(id)).map(e -> {
            String[] split = e.split(" ");
            return new Config(split);
        }).collect(Collectors.toList());
    }

}
