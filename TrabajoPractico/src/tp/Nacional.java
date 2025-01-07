package tp;

import java.util.HashMap;
import java.util.Map;

public class Nacional extends Publico {

	public Nacional(String codigo, String origen, String destino, String fecha, int tripulantes, int[] cantAsientos,
			double[] precio, double valorRefrigerio) {
		super(codigo, origen, destino, fecha, tripulantes, cantAsientos, precio);
		this.valorRefrigerios = valorRefrigerio;
	}

	@Override
	int gestionarPasaje(int nroAsiento, Cliente cliente, boolean aOcupar) {
		cargarCliente(nroAsiento, cliente);

		return comprarPasaje(nroAsiento, aOcupar);
	}

	@Override
	void cancelarPasaje(int nroAsiento) { // ponemos el pasaje que queremos cancelar en null
		obtenerPasaje()[nroAsiento].asignarCliente(null);

	}

	@Override
	void cancelarPasajePorDni(int dni) { // ponemos el pasaje que queremos cancelar en null
		for (Pasaje pasaje : obtenerPasaje()) {
			if (pasaje.dniPasajero() == dni) {
				pasaje.asignarCliente(null);
			}
		}

	}

	@Override
	public boolean reprogramarPasaje(Pasaje pasaje) {
		for (int i = pasaje.numAsiento() - 1; i < pasajes.length; i++) {
			if (!pasajes[i].verificarAsiento()) {
				pasajes[i].comprar(pasaje.obtenerAsiento().Ocupado());
				return true;
			}
		}
		return false;
	}
public int calculoCantTuristaDispo(Map <Integer ,String> Asientos) {
	int cont=0;
	for  (String asiento : Asientos.values()) {
		
		if (asiento.equals("Clase Turista")) {
			cont++;
		}
		
	}
	return cont;
	
}
	
public int calculoCantEjecutivaDispo(Map <Integer ,String> Asientos) {
	int cont=0;
	for  (String asiento : Asientos.values()) {
		
		if (asiento.equals("Clase Ejecutiva")) {
			cont++;
		}
		
	}
	return cont;
	
}
	@Override
	public StringBuilder toStringDetalles() {
		 StringBuilder detalles = toStringBase();
	        detalles.append("NACIONAL");
	        return detalles;
	    }

	@Override
	protected String TipoVuelo() {
		return "NACIONAL";
	}

	@Override
	public String toString() {
		return "Vuelo Publico Nacional " +"Registrado: " + codigo;
	}
	

	
}
