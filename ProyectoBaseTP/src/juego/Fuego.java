package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;

public class Fuego {

	private double x, y;
	private double velocidad;
	private boolean direccion;
	private Image imagenDisparoD;
	private Image imagenDisparoI;
	int anchoDeseado;
	int altoDeseado;

	public Fuego(double x, double y, boolean direccion) {
		this.x = x;
		this.y = y;
		this.velocidad = 5;
		this.imagenDisparoI = Herramientas.cargarImagen("Imagenes/FuegoI2.png");
		this.imagenDisparoD = Herramientas.cargarImagen("Imagenes/FuegoD2.png");
		this.direccion = direccion;

	}

	public void mover() {
		if (direccion) {
			x += velocidad;
		} else {
			x -= velocidad;
		}
	}

	public void dibujarse(Entorno entorno) {
		if (direccion) {
			entorno.dibujarImagen(imagenDisparoD, x, y, 0, 1);
		}else {
			entorno.dibujarImagen(imagenDisparoI, x, y, 0, 1);
		}

	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getAncho() {
		return anchoDeseado;
	}

	public int getAlto() {
		return altoDeseado;
	}

}
