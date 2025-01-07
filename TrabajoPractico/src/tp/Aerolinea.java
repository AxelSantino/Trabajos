package tp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class Aerolinea implements IAerolinea {
	private String NombreAerlolinea;
	private String CUIT;
///////////////////////////////////////////////////MAPS, LIST, ETC/////////////////////////////////////////////////////	
	public Map<String, Aeropuerto> aeropuertos; // clave:ORIGEN/DESTINO valor: AEROPUERTO
	private Map<Integer, Cliente> registroCliente; // clave:DNI valor:CLIENTE
	public Map<String, Vueloo> vuelos; // clave:CODIGOvuelo valor:VUELO
	private Map<String, Double> totalRecaudadoPorDestino; // clave:DESTINO valor:TOTALRECUADADO
	private Map<String, Integer> cantJets; // clave:CODvuelo valor:JETSnecesarios
	private int contadorPasajes;

	public Aerolinea(String nombre, String CUIT) {

		this.NombreAerlolinea = nombre;
		this.CUIT = CUIT;
		this.aeropuertos = new HashMap<>();
		this.registroCliente = new HashMap<>();
		this.vuelos = new HashMap<>();
		this.totalRecaudadoPorDestino = new HashMap<>();
		this.cantJets = new HashMap<>();
		this.contadorPasajes = 0;

	}

	@Override
	public void registrarCliente(int dni, String nombre, String telefono) {

		if (registroCliente.containsKey(dni)) { // verificamos si el dni del cliente ya está registrado
			throw new RuntimeException("El cliente con DNI " + dni + " ya está registrado.");
		}
		Cliente cliente = new Cliente(dni, nombre, telefono);
		registroCliente.put(cliente.dni, cliente); // si no lo está, lo registramos

	}

	@Override
	public void registrarAeropuerto(String nombre, String pais, String provincia, String direccion) {

		if (aeropuertos.containsKey(nombre)) { // verificamos si el nombre del aeropuerto está registrado
			throw new RuntimeException("El Aeropuerto : " + nombre + " ya está registrado.");
		}

		Aeropuerto aero = new Aeropuerto(nombre, pais, provincia, direccion);
		aeropuertos.put(nombre, aero); // si no lo están lo registramos
		totalRecaudadoPorDestino.put(nombre, 0.0);
	}

	@Override
	public String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes,
			double valorRefrigerio, double[] precios, int[] cantAsientos) {

		if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
			throw new RuntimeException("Origen o destino no registrado.");
		}

		Aeropuerto aeropuertoOrigen = new Aeropuerto(origen, "Argentina", null, null);
		Aeropuerto aeropuertoDestino = new Aeropuerto(destino, "Argentina", null, null);

		for (Aeropuerto Aero : aeropuertos.values()) {
			if (Aero.equals(aeropuertoOrigen) && !Aero.pais.equals("Argentina")) {
				throw new RuntimeException("El Aeropuerto Origen no es de Argentina.");
			}

			if (Aero.equals(aeropuertoDestino) && !Aero.pais.equals("Argentina")) {
				throw new RuntimeException("El Aeropuerto Destino no es de Argentina.");
			}
		}

		String codigo = generarCodigo("PUB");
		Nacional nacional = new Nacional(codigo, origen, destino, fecha, tripulantes, cantAsientos, precios,
				valorRefrigerio);

		vuelos.put(codigo, nacional); // Agregamos el vuelo si las condiciones se cumplen
		return codigo;
	}

	@Override
	public String registrarVueloPublicoInternacional(String origen, String destino, String fecha, int tripulantes,
			double valorRefrigerio, int cantRefrigerios, double[] precios, int[] cantAsientos, String[] escalas) {

		if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
			throw new RuntimeException("Origen o destino no registrado");
		}

		String codigo = generarCodigo("PUB");
		Internacional internacional = new Internacional(codigo, origen, destino, fecha, tripulantes, cantAsientos,
				valorRefrigerio, precios, cantRefrigerios, escalas);

		vuelos.put(codigo, internacional);
		return codigo;

	}

	@Override
	public String VenderVueloPrivado(String origen, String destino, String fecha, int tripulantes, double precio,
			int dniComprador, int[] acompaniantes) {

		// Verificar que los aeropuertos estén registrados
		if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) { // verificamos lo
																						// de siempre
			throw new RuntimeException("Origen o destino no registrado.");
		}

		String codigo = generarCodigo("PRI");
		Privado privado = new Privado(codigo, origen, destino, fecha, tripulantes, precio, dniComprador, acompaniantes);
		cantJets.put(codigo, privado.jets());
		vuelos.put(codigo, privado);
		// Actualizar el total recaudado para el destino
		double totalRecaudado = totalRecaudadoPorDestino.get(destino);
		double sumarRecaudacion = totalRecaudado + privado.precioVueloPrivado();
		totalRecaudadoPorDestino.put(destino, sumarRecaudacion);

		return codigo;
	}

	@Override
	public Map<Integer, String> asientosDisponibles(String codVuelo) {
		Map<Integer, String> asientosDisponibles = new HashMap<>();
		Publico vuelo = (Publico) vuelos.get(codVuelo);

		if (vuelo == null)
			throw new IllegalArgumentException("El vuelo con código " + codVuelo + " no existe.");

		int[] cantAsientos = vuelo.obtenerAsiento();
		Pasaje[] pasajes = vuelo.obtenerPasaje();

		int numAsiento = 0; // numero de asiento que le asigno

		for (int i = 0; i < cantAsientos.length; i++) { // recorro las secciones de los asientos [0]== turista [1] ==
														// ejecutivo [2] == primeraClase
			for (int j = 0; j < cantAsientos[i]; j++) {
				if (!pasajes[numAsiento].obtenerAsiento().Comprado()) { // verifico si el asietno no está ocupado
					// Asignar el asiento disponible con su tipo de clase
					asientosDisponibles.put(pasajes[numAsiento].numAsiento(), vuelo.tipoAsiento(numAsiento));
				}
				numAsiento++; // Incrementar el número del asiento
			}

		}
		return asientosDisponibles; // retorna el mapa de asientos disponibles
	}

	@Override
	public int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar) {

		if (!registroCliente.containsKey(dni)) {
			throw new RuntimeException("El cliente no está registrado");
		}

		Cliente cliente = registroCliente.get(dni);
		Publico publico = (Publico) vuelos.get(codVuelo);
		int codigoPasaje = publico.gestionarPasaje(nroAsiento - 1, cliente, aOcupar);
		String destino = vuelos.get(codVuelo).destino;
		double totalRecaudado = totalRecaudadoPorDestino.get(destino);
		double sumarRecaudacion = totalRecaudado + publico.recaudacion(nroAsiento);
		totalRecaudadoPorDestino.put(destino, sumarRecaudacion);

		if (codigoPasaje <= 0)
			throw new RuntimeException("Ocurrio un error y no se pudo realizar la compra");

		return codigoPasaje;
	}

	public List<String> consultarVuelosSimilares(String origen, String destino, String Fecha) {

		List<String> VuelosSimilares = new ArrayList<>();

		// Crear un objeto Fecha a partir de la fecha dada
		Fecha fechaConsulta = new Fecha(Fecha);
		Fecha fechaLimite = fechaConsulta.obtenerFechaSieteDiasDespues();

		// Recorrer el HashMap de vuelos
		for (Vueloo vuelo : vuelos.values()) {
			if (vuelo.origen().equals(origen) && vuelo.destino().equals(destino)) {
				Fecha fechaVuelo = new Fecha(vuelo.fecha());

				// Verificar si la fecha del vuelo está dentro del rango
				if (fechaVuelo.esMayorQue(fechaConsulta) && fechaVuelo.esMenorQue(fechaLimite)) {
					VuelosSimilares.add(vuelo.codigo); // Agregar el código del vuelo a la lista
				}
			}
		}

		return VuelosSimilares;
	}

	@Override
	public void cancelarPasaje(int dni, String codVuelo, int nroAsiento) {
		if (!vuelos.containsKey(codVuelo)) {
			throw new RuntimeException("Vuelo no encontrado."); // verifico si encontró el vuelo
		}

		Publico publico = (Publico) vuelos.get(codVuelo);

		publico.cancelarPasaje(nroAsiento - 1);

	}

	@Override
	public void cancelarPasaje(int dni, int codPasaje) {

		for (Map.Entry<String, Vueloo> entry : vuelos.entrySet()) {
			Publico publico = (Publico) entry.getValue();
			publico.cancelarPasajePorDni(dni);
		}
	}

	@Override
	public List<String> cancelarVuelo(String codVuelo) {
		List<String> pasajesSinProgramar = new ArrayList<>();

		Publico vuelo = (Publico) vuelos.get(codVuelo); // Obtenemos el vuelo
		if (vuelo == null)
			return pasajesSinProgramar;

		// Consultamos vuelos similares para el reenvío de los pasajes
		List<String> vuelosRemplazos = consultarVuelosSimilares(vuelo.origen(), vuelo.destino(), vuelo.fecha());

		// Obtenemos los pasajes del vuelo
		Pasaje[] pasajes = vuelo.obtenerPasaje();

		if (pasajes != null) {
			for (Pasaje pasaje : pasajes) {
				if (pasaje.verificarAsiento()) {
					// Intentamos reprogramar el pasaje en vuelos similares
					boolean reprogramado = false;
					for (String cod : vuelosRemplazos) {
						Publico vueloRemplazo = (Publico) vuelos.get(cod);
						if (vueloRemplazo != null && vueloRemplazo.reprogramarPasaje(pasaje)) {
							pasajesSinProgramar.add(pasaje.formatearPasaje(cod));
							reprogramado = true;
							break;
						}
					}

					// Si no se pudo reprogramar el pasaje, lo cancelamos
					if (!reprogramado) {
						pasajesSinProgramar.add(pasaje.formatearPasaje("CANCELADO"));
					}
				}
			}
		}

		return pasajesSinProgramar;
	}

	@Override
	public double totalRecaudado(String destino) {

		if (!totalRecaudadoPorDestino.containsKey(destino)) {
			return 0.0;
		}

		return totalRecaudadoPorDestino.get(destino);
	}

	@Override
	public String detalleDeVuelo(String codVuelo) {

		Vueloo vuelo = vuelos.get(codVuelo); // Obtiene el vuelo por código
		if (vuelo == null) {
			throw new RuntimeException("Vuelo no encontrado.");
		}

		return vuelo.toStringDetalles().toString();
	}

	// Metodo toString ()

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		// Mostrar aeropuertos
		result.append("Aeropuertos:\n");
		for (Aeropuerto aeropuerto : aeropuertos.values()) { // Iteramos sobre los valores del mapa de
																// aeropuertos
			result.append(aeropuerto).append("\n");
		}

		// Mostrar vuelos
		result.append("Vuelos:\n");
		for (Vueloo vuelo : vuelos.values()) {

			result.append(vuelo).append("\n");

		}

		for (Vueloo vuelo : vuelos.values()) {
			result.append(vuelo.toStringDetalles()).append("\n");

		}
		for (Vueloo vuelo : vuelos.values()) {
			if (vuelo instanceof Nacional) {
				Map<Integer, String> asientos = asientosDisponibles(vuelo.codigo);

				int turista = ((Nacional) vuelo).calculoCantTuristaDispo(asientos);
				int ejecutiva = ((Nacional) vuelo).calculoCantEjecutivaDispo(asientos);
				result.append("Asientos disponibles para el vuelo:").append(" " + vuelo.codigo())
						.append(" {Turista = " + turista).append(" Ejecutivo = " + ejecutiva).append("}");
			}
		}

		for (Vueloo vuelo : vuelos.values()) {
			if (!vuelo.TipoVuelo().equals("PRIVADO")) {
				result.append("\nTotal recaudado en vuelos a" + "'" + vuelo.destino + "':"
						+ totalRecaudado(vuelo.destino) + "");
			}

		}

		return result.toString();
	}

	// con esto genero el codigo para agregarle la etiqueta del PUB o PRIV
	private String generarCodigo(String codigo) {
		this.contadorPasajes++;
		return contadorPasajes + "-" + codigo;
	}

}
