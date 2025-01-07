package tp;

import java.util.Objects;

public class Cliente {
	public int dni;
	public String nombre;
    public String telefono;

	public Cliente(int dni, String nombre ,String telefono) {
		this.dni=dni;
		this.nombre=nombre;
		this.telefono=telefono; 
	}
	
	
	public int darDni() {
		return dni;
	}

	public String telefonoCliente() {
		return telefono;
	}
	
	public String nombreCliente() {
		return nombre;
	}
	

	@Override
	public String toString() {
		return "Cliente [nombre=" + nombre + ", dni=" + dni + ", telefono=" + telefono + "]";
	}



}