package app;

import co.edu.unicartagena.mqtt.Sensor;
import co.edu.unicartagena.mqtt.ServidorMQTT;

import java.util.List;
import java.util.Scanner;

public class SimuladorMQTT {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		ServidorMQTT servidor = new ServidorMQTT();

		servidor.registrarSensor(new Sensor("S01", "Temperatura", "iot/sensor01/temperatura"));
		servidor.registrarSensor(new Sensor("S02", "Humedad", "iot/sensor02/humedad"));
		servidor.registrarSensor(new Sensor("S03", "Nivel de agua", "iot/sensor03/nivel"));

		int opciones = -1;

		while (opciones != 0) {
			mostrarMenu();
			opciones = leerOpcion(in);

			switch (opciones) {
				case 1:
					publicarMensajeInteractivo(in, servidor);
					break;
				case 2:
					servidor.procesarMensaje();
					break;
				case 3:
					ejecutarSecuenciaDePrueba(servidor);
					break;
				case 4:
					mostrarSensores(servidor);
					break;
				case 0:
					System.out.println("Saliendo del simulador...");
					break;
				default:
					System.out.println("Opción inválida.");
			}
			System.out.println();
		}
		in.close();
	}

	private static void mostrarMenu() {

		System.out.println("*******Simulador MQTT*******");
		System.out.println("1. Publicar mensaje ");
		System.out.println("2. Procesar siguiente mensaje");
		System.out.println("3. Ejecutar secuencia");
		System.out.println("4. Ver sensores registrados");
		System.out.println("0. Salir");
		System.out.print("Seleccione una opción: ");

	}

	private static int leerOpcion(Scanner in) {

		while (!in.hasNextInt()) {
			System.out.print("Ingrese un número válido: ");
			in.next();
		}

		int opcion = in.nextInt();

		in.nextLine();

		return opcion;
	}

	private static void publicarMensajeInteractivo(Scanner in, ServidorMQTT servidor) {
		List<Sensor> sensores = servidor.getSensoresRegistrados();

		System.out.println("Sensores disponibles:");

		for (int i = 0; i < sensores.size(); i++)
		{
			System.out.println((i + 1) + ". " + sensores.get(i));
		}

		System.out.print("Seleccione el número del sensor: ");

		int seleccion = leerOpcion(in);

		if (seleccion < 1 || seleccion > sensores.size()) {

			System.out.println("Sensor inválido.");

			return;
		}

		Sensor sensor = sensores.get(seleccion - 1);

		servidor.publicarMensaje(sensor);

	}

	private static void mostrarSensores(ServidorMQTT servidor){

		for (Sensor sensor : servidor.getSensoresRegistrados()) {
			System.out.println(sensor);
		}

	}

	private static void ejecutarSecuenciaDePrueba(ServidorMQTT servidor) {
		servidor.publicarMensaje("S01", "iot/sensor01/temperatura", "28.5 °C", "10:00:01");
		servidor.publicarMensaje("S02", "iot/sensor02/humedad", "76 %", "10:00:05");
		servidor.publicarMensaje("S03", "iot/sensor03/nivel", "45 cm", "10:00:09");

		servidor.procesarMensaje();

		servidor.publicarMensaje("S01", "iot/sensor01/temperatura", "29.1 °C", "10:00:15");

		servidor.procesarMensaje();
		servidor.procesarMensaje();
		servidor.procesarMensaje();

		servidor.procesarMensaje();
	}
}