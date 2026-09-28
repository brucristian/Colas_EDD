package co.edu.unicartagena.mqtt;

import co.edu.unicartagena.queue.Cola;
import co.edu.unicartagena.queue.Nodo;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServidorMQTT {
    private Cola<MensajeMQTT> cola;
    private List<Sensor> sensoresRegistrados;
    private int contadorId;
    private int mensajesEnCola;
    private Random random;
    private LocalTime relojSimulado;
    private DateTimeFormatter formatoHora;

    //constructor

    public ServidorMQTT() {

        this.cola = new Cola<>();
        this.sensoresRegistrados = new ArrayList<>();
        this.contadorId = 1;
        this.mensajesEnCola = 0;
        this.random = new Random();
        this.relojSimulado = LocalTime.of(10, 0, 0);
        this.formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");

    }


    //registrar sensores en lista de sensores que se estaran usando
    public void registrarSensor(Sensor sensor) { sensoresRegistrados.add(sensor); }

    public List<Sensor> getSensoresRegistrados() { return sensoresRegistrados; }

    // publicarMensaje: mensaje crea un objeto mensajeMQTT y lo encola a la estructura
    public void publicarMensaje(String dispositivoId, String topic, String payload,
                                String timestamp) {
        MensajeMQTT mensaje = new MensajeMQTT(contadorId, dispositivoId, topic,
                payload, timestamp);

        contadorId++;
        cola.encolar(new Nodo<>(mensaje));
        mensajesEnCola++;
        System.out.println("Publicado -> " + mensaje);
    }
    // publicar se utiliza para generar el mensaje con valores random(); en payload y timestamp
    public void publicarMensaje(Sensor sensor) {

        String payload = generarPayload(sensor);
        String timestamp = generarTimestamp();
        publicarMensaje(sensor.getId(), sensor.getTopic(), payload, timestamp);
    }

    // aqui se procesa el mensaje, se decola de la estructura y se muestra en pantalla
    public void procesarMensaje() {

        if (cola.estaVacia()) {
            System.out.println("No hay mensajes en la cola para procesar.");
            return;
        }

        MensajeMQTT mensaje = cola.decolar();
        mensajesEnCola--;
        System.out.println("Procesado -> " + mensaje);
        System.out.println("Mensajes restantes en la cola: " + mensajesEnCola);
    }
    // aqui uso un menu para generar el random(); de payload dependiendo de medicion
    private String generarPayload(Sensor sensor) {
        switch (sensor.getTipoMedicion()) {
            case "Temperatura":
                double temperatura = 15 + random.nextDouble() * 20;
                return String.format("%.1f °C", temperatura);
            case "Humedad":
                int humedad = 30 + random.nextInt(61);
                return humedad + " %";
            case "Nivel de agua":
                int nivel = 10 + random.nextInt(91);
                return nivel + " cm";
            default:
                return String.valueOf(random.nextInt(100));
        }
    }

    // aqui utilizo otro generador de timestamp para que la hora sea de forma aleatoria
    private String generarTimestamp() {

        relojSimulado = relojSimulado.plusSeconds(1 + random.nextInt(10));
        return relojSimulado.format(formatoHora);

    }
}