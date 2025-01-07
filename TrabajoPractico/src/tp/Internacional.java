package tp;

import java.util.HashMap;
import java.util.Map;

public class Internacional extends Publico {
	private String[] escalas;

	public Internacional(String codigo, String origen, String destino, String fecha, int tripulantes,
			int[] cantAsientos, double valorRefrigerios, double[] precio, int cantRefrigerios, String[] escalas) {
		super(codigo, origen, destino, fecha, tripulantes, cantAsientos, precio);
		this.valorRefrigerios = valorRefrigerios;
		this.cantRefrigerios = cantRefrigerios;
		this.escalas = escalas;
	}

	@Override
	int gestionarPasaje(int nroAsiento, Cliente cliente, boolean aOcupar) {
		cargarCliente(nroAsiento, cliente);

		return comprarPasaje(nroAsiento, aOcupar);
	}

	@Override
	void cancelarPasaje(int nroAsiento) {
		obtenerPasaje()[nroAsiento].asignarCliente(null);

	}

	@Override
	void cancelarPasajePorDni(int dni) {
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

	@Override
	 public StringBuilder toStringDetalles() {
        StringBuilder detalles = toStringBase();
        detalles.append("INTERNACIONAL");
        return detalles;
    }


	@Override
	protected String TipoVuelo() {
		return "INTERNACIONAL";
	}

	@Override
	public String toString() {
		return "Vuelo Publico Internacional " +"Registrado: " + codigo;
	}
	

}
