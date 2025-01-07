package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;

public class Magma {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private double velocidadY; // Velocidad a la que el lago de magma sube
	double cont;
	double comienzo;
	int respaldo;
	boolean banderaMagma;
	Image lava;

	public Magma(int x, int y, int ancho, int alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidadY = 0.01;
		this.cont = 0;
		this.comienzo = 0;
		this.respaldo = y;
		this.banderaMagma = true;
		lava = Herramientas.cargarImagen("Imagenes/LAVAS.gif");
	}

	public void actualizar() {
		// Actualiza la posición del lago de magma para que suba con el tiempo
		y -= velocidadY;

	}

	public void dibujarMagma(Entorno entorno) {;
		entorno.dibujarImagen(lava, x, y + 930, 0, 8);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}

	public double getVelocidadY() {
		return velocidadY;
	}

	public double getTecho() {
		return this.y - this.getAlto() / 2;
	}
}
