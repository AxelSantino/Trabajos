package tp;

public abstract class Publico extends Vueloo {
	protected double valorRefrigerios;
	protected int cantRefrigerios;
	protected double[] precio;
	protected int[] cantAsientos;
	protected Pasaje[] pasajes;

	public Publico(String codigo, String origen, String destino, String fecha, int tripulantes, int[] cantAsientos,
			double[] precio) {
		super(codigo, origen, destino, fecha, tripulantes);

		this.cantAsientos = cantAsientos;
		this.precio = precio;
		this.pasajes = cargarAsientos();
	}

	 public abstract boolean reprogramarPasaje(Pasaje pasaje);
	
	abstract int gestionarPasaje(int nroAsiento, Cliente cliente, boolean aOcupar);

	abstract void cancelarPasaje(int nroAsiento);

	abstract void cancelarPasajePorDni(int dni);

	public double recaudacion(int numAsiento) {
		double gananciaAsientos = 0;
		double costoRefrigerios = valorRefrigerios * cantRefrigerios;

		if (cantAsientos.length == 3) {
			int totalAsientos = cantAsientos[0] + cantAsientos[1] + cantAsientos[2];
			if (numAsiento <= cantAsientos[0]) {
				gananciaAsientos = (precio[0] + costoRefrigerios) * 1.2;
			} else if (numAsiento <= cantAsientos[0] + cantAsientos[1]) {
				gananciaAsientos = (precio[1] + costoRefrigerios) * 1.2;
			} else {
				gananciaAsientos = (precio[2] + costoRefrigerios) * 1.2;
			}
		} else if (cantAsientos.length == 2) {
			if (numAsiento <= cantAsientos[0]) {
				gananciaAsientos = (precio[0] + costoRefrigerios) * 1.2;
			} else {
				gananciaAsientos = (precio[1] + costoRefrigerios) * 1.2;
			}
		}
		return gananciaAsientos;
	}

	private Pasaje[] cargarAsientos() {
		int totalAsientos = 0;
		for (int cant : cantAsientos) {
			totalAsientos += cant;
		}

		Pasaje[] pasajes = new Pasaje[totalAsientos];
		int num = 1;
		for (int i = 0; i < cantAsientos.length; i++) {
			for (int j = 0; j < cantAsientos[i]; j++) {
				pasajes[num - 1] = new Pasaje(num);
				num++;
			}
		}
		return pasajes;
	}

	public int comprarPasaje(int numAsiento, boolean ocupar) {
		if (numAsiento >= 0 && numAsiento < pasajes.length) {
			pasajes[numAsiento].comprar(ocupar);
			return pasajes[numAsiento].dniPasajero() / 2 + numAsiento;
		}
		return -1; // Devuelve negativo si el número de asiento no es válido
	}

	public void cargarCliente(int numAsiento, Cliente cliente) {
		if (numAsiento >= 0 && numAsiento < pasajes.length) {
			pasajes[numAsiento].asignarCliente(cliente);
		}
	}

	public String tipoAsiento(int num) {
		if (num <= cantAsientos[0]) {
			return "Clase Turista";
		} else if (num <= cantAsientos[0] + cantAsientos[1]) {
			return "Clase Ejecutiva";
		} else {
			return "Primera clase";
		}
	}

	public int[] obtenerAsiento() {
		return this.cantAsientos;
	}

	public Pasaje[] obtenerPasaje() {
		return this.pasajes;
	}
}
