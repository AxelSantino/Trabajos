package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;

public class Boomerang {

	private double x, y;
	private double velocidad;
	private boolean direccionDerecha;
	private Image imagenDisparoD;
	private Image imagenDisparoI;
	int anchoDeseado;
	int altoDeseado;
	
	public Boomerang(double x, double y, double velocidad, boolean direccionDerecha) {
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		this.direccionDerecha = direccionDerecha;
		this.imagenDisparoD = Herramientas.cargarImagen("Imagenes/boomD.gif");
		this.imagenDisparoI = Herramientas.cargarImagen("Imagenes/boomI.gif");	

	}

	public void mover() {
		if (direccionDerecha) {
			x += velocidad;
		} else {
			x -= velocidad;
		}
	}

	public void dibujarse(Entorno entorno) {
		if (direccionDerecha) {
			entorno.dibujarImagen(imagenDisparoD, x, y, 0, 0.09);
		}else {
			entorno.dibujarImagen(imagenDisparoI, x, y, 0, 0.09);
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
	public int  getAlto() {
		return altoDeseado;
	}
	
}

