package tp;

import java.util.Map;
import java.util.Objects;

public abstract class Vueloo {

	protected String codigo;
	protected String origen;
	protected String destino;
	protected String fecha;
	protected int tripulantes;

	public Vueloo(String codigo, String origen, String destino, String fecha, int tripulantes) {
		this.codigo = codigo;
		this.origen = origen;
		this.destino = destino;
		this.fecha = fecha;
		this.tripulantes = tripulantes;
	}

	public abstract StringBuilder toStringDetalles();

	protected abstract String TipoVuelo();

	public StringBuilder toStringBase() {
		StringBuilder base = new StringBuilder();
		base.append(codigo).append(" - ");
		base.append(origen).append(" - ");
		base.append(destino).append(" - ");
		base.append(fecha).append(" - ");
		return base;
	}

	public String fecha() {
		return fecha;
	}

	public String origen() {
		return origen;
	}

	public String destino() {
		return destino;
	}

	public String codigo() {
		return codigo;
	}

}
