package co.edu.unicartagena.mqtt;

public class Sensor {
    private String id;
    private String tipoMedicion;
    private String topic;
    //constructor
    public Sensor(String id, String tipoMedicion, String topic) {
        this.id = id;
        this.tipoMedicion = tipoMedicion;
        this.topic = topic;
    }
    //getters
    public String getId() {return id;}
    public String getTipoMedicion() {return tipoMedicion;}
    public String getTopic() {return topic;}

    //mostrar sensor
    @Override
    public String toString() {
        return id + " (" + tipoMedicion + ") -> " + topic;
    }
}