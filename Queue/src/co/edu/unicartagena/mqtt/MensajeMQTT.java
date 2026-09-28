package co.edu.unicartagena.mqtt;

public class MensajeMQTT{
    private final int id;
    private final String dispositivoId;
    private final String topic;
    private final String payload;
    private final String timestamp;

    public MensajeMQTT(int id, String dispositivoId, String topic, String payload, String timestamp) {
        this.id = id;
        this.dispositivoId = dispositivoId;
        this.topic = topic;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getDispositivoId() {
        return dispositivoId;
    }

    public String getTopic() {
        return topic;
    }

    public String getPayload() {
        return payload;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        String separador = "\n----------------------------------------";
        return separador +
                "\nID: " + id +
                "\nDispositivo: " + dispositivoId +
                "\nTopic: " + topic +
                "\nPayload: " + payload +
                "\nTimestamp: " + timestamp +
                "\n" + separador;
    }
}
