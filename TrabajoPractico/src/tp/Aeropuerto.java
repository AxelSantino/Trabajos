package tp;

import java.util.Objects;

public class Aeropuerto {


	public String nombre;
	public String pais;
	public String provincia;
	public String direccion;


	public Aeropuerto(String nombre, String pais, String provincia, String direccion) {
		super();
		this.nombre = nombre;
		this.pais = pais;
		this.provincia = provincia;
		this.direccion = direccion;
	}
	@Override
	public String toString() {
		return "Aeropuerto " + nombre +" pais: "+ pais  ;
	}
	@Override
	public int hashCode() {
		return Objects.hash(nombre, pais, provincia);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aeropuerto other = (Aeropuerto) obj;
		return Objects.equals(nombre, other.nombre) && Objects.equals(pais, other.pais)
				&& Objects.equals(provincia, other.provincia);
	}

	
}
