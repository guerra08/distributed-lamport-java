package domain;

public class Config {

    private Integer id;
    private String host;
    private Integer port;
    private Double chance;

    public Config(Integer id, String host, Integer port, Double chance) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.chance = chance;
    }

    public Config(String[] configArray){
        this(
                Integer.parseInt(configArray[0]),
                configArray[1],
                Integer.parseInt(configArray[2]),
                Double.parseDouble(configArray[3])
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Double getChance() {
        return chance;
    }

    public void setChance(Double chance) {
        this.chance = chance;
    }
}
