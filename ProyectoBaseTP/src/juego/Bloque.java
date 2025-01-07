package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Bloque {
	double x;
	double y;
	double ancho;
	double alto;
	boolean destructible;
	Image bloqueA;
	Image bloqueB;

	public Bloque(double x, double y) {
		this.x = x;
		this.y = y;
		this.ancho = 50;
		this.alto = 45;
		this.destructible = false;
		this.bloqueA= Herramientas.cargarImagen("Imagenes/bloqueA.png");
		this.bloqueB= Herramientas.cargarImagen("Imagenes/bloqueB.png");
		if (Math.random()> 0.50) {
			destructible = true;
		}
	}


	public void dibujar(Entorno entorno) {
		if (destructible) { 
			entorno.dibujarImagen(bloqueB, this.x, this.y, 0, 1);
		}else {
			entorno.dibujarImagen(bloqueA, this.x, this.y, 0, 1);
		}
		
	}
	public double getAlto() {
		return this.alto;
	}
	public double getAncho() {
		return this.ancho;
	}

	public double getTecho() {
		return this.y-this.getAlto()/2;

	}
	public double getPiso() {
		return this.y+this.getAlto()/2;
	}

	public double getIzquierda() {
		return this.x-this.getAncho()/2;

	}
	public double getDerecha() {
		return this.x+this.getAncho()/2;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}


