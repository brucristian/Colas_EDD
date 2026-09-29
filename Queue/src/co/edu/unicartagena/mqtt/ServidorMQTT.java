package co.edu.unicartagena.mqtt;

import co.edu.unicartagena.queue.Cola;
import co.edu.unicartagena.queue.Nodo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServidorMQTT {
    private Cola<MensajeMQTT> cola;
    private List<Sensor> sensoresRegistrados;
    private int contadorId;
    private int mensajesEnCola;
    private Random random;

    //constructor

    public ServidorMQTT() {

        this.cola = new Cola<>();
        this.sensoresRegistrados = new ArrayList<>();
        this.contadorId = 1;
        this.mensajesEnCola = 0;
        this.random = new Random();

    }


    //registrar sensores en lista de sensores que se estaran usando
    public void registrarSensor(Sensor sensor) { sensoresRegistrados.add(sensor); }

    public List<Sensor> getSensoresRegistrados() { return sensoresRegistrados; }

    // publicarMensaje: mensaje crea un objeto mensajeMQTT y lo encola a la estructura
    public void publicarMensaje(String dispositivoId, String topic, String payload) {
        MensajeMQTT mensaje = new MensajeMQTT(contadorId, dispositivoId, topic,
                payload);

        contadorId++;
        cola.encolar(new Nodo<>(mensaje));
        mensajesEnCola++;
        System.out.println("Publicado -> " + mensaje);
        System.out.println("Estado de la cola (Frente -> Final): " + estadoDeLaCola());
    }
    // publicar se utiliza para generar el mensaje con valores random(); en payload
    public void publicarMensaje(Sensor sensor) {

        String payload = generarPayload(sensor);
        publicarMensaje(sensor.getId(), sensor.getTopic(), payload);
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
        System.out.println("Estado de la cola (Frente -> Final): " + estadoDeLaCola());
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

    //metodo para mostrar el estado de la cola en ejecucion de la secuencia prueba
    private String estadoDeLaCola(){
        String estado = "";

        for(int i = 0; i < mensajesEnCola; i++){
            MensajeMQTT mensaje = cola.decolar();

            if(i > 0){
                estado += " -> ";
            }
            estado += mensaje.getDispositivoId();

            cola.encolar(new Nodo<>(mensaje));
        }

        if(estado.equals("")){
            return "(vacia)";
        }
        return estado;
    }
}