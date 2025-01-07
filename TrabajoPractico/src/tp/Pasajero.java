package tp;

public class Pasajero extends Cliente {


	public Pasajero(int dni, String nombre, String telefono) {
		super(dni, nombre, telefono);
		
	}
	@Override
	public String toString() {
		return "Pasajero [nombre=" + nombre + ", dni=" + dni  + ", telefono=" + telefono + "]";
	} 


}
