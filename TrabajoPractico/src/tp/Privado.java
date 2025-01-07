package tp;

public class Privado extends Vueloo {

	private double precioPorJet;
	private int dni;
	private int[] acompaniantes;
	private int jetsNecesarios;

	private int capacidadJet = 15;

	public Privado(String codigo, String origen, String destino, String fecha, int tripulantes, double precio, int dni,
			int[] acompaniantes) {
		super(codigo, origen, destino, fecha, tripulantes);

		this.precioPorJet = precio;
		this.dni = dni;
		this.acompaniantes = acompaniantes;
		this.jetsNecesarios = (acompaniantes.length + 1 + capacidadJet - 1) / capacidadJet;
	}

	public int jets() {
		return jetsNecesarios;
	}

	public double precioVueloPrivado() {
		return jetsNecesarios * precioPorJet * 1.30;
	}

	@Override
	public StringBuilder toStringDetalles() {
		StringBuilder detalles = toStringBase(); // Asume que toStringBase() devuelve "3-PRI - Aeroparque - Bariloche -
													// 07/01/2025 - "
		detalles.append("PRIVADO (").append(jetsNecesarios).append(")");
		return detalles;
	}

	@Override
	protected String TipoVuelo() {
		return "PRIVADO";
	}

	@Override
	public String toString() {
		return "Vuelo Privado " +"Registrado: " + codigo;
	}
	
	
}
