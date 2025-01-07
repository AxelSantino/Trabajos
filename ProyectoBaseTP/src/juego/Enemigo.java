package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Enemigo {

	private double x;
	private double y;
	private double ancho;
	private double alto;
	private double cooldownDisparo;
	private int tiempoDesdeUltimoDisparo;

	boolean sentidoTirano;
	Image tiranoDer;
	Image tiranoIzq;
	boolean disparoDerecha;
	boolean apoyado = false;

	double getVelocidad() {
		return velocidad;
	}

	public void DameVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}

	private double velocidad;

	public Enemigo(double x, double y, double ancho, double alto, double velocidad) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.cooldownDisparo = Math.random() * (200) + 200;  //TIEMPO QUE TARDA EL ENEMIGO EN VOLVER A DISPARAR
		this.tiempoDesdeUltimoDisparo = 0;
		this.sentidoTirano = true;
		this.tiranoIzq = Herramientas.cargarImagen("Imagenes/BowserI.gif");
		this.tiranoDer = Herramientas.cargarImagen("Imagenes/BowserD.gif");

	}

	public void dibujarse(Entorno entorno) {
		if (sentidoTirano) {
			entorno.dibujarImagen(tiranoDer, this.x, this.y, 0, 0.19);

		} else {
			entorno.dibujarImagen(tiranoIzq, this.x, this.y, 0, 0.19);
		}
	}

	public Fuego disparar() {
		double yNuevo = this.y - 15; /// Ajusta el disparo del enemigo un poco mas arriba
		boolean direccionDisparo = velocidad > 0;
		if (tiempoDesdeUltimoDisparo >= cooldownDisparo) {
			tiempoDesdeUltimoDisparo = 0; // Reinicia el contador
			return new Fuego(this.x, yNuevo, direccionDisparo); // Crea el proyectil nuevo
		}
		return null; // Si no pasó suficiente tiempo no dispara
	}

	public void caer(Entorno e) {
		if (!this.apoyado) {
			this.y += 6;
		}

	}

	public void actualizar() {
		tiempoDesdeUltimoDisparo++;
	}

	public void mover() {
		this.x += this.velocidad;
		if (this.velocidad > 0) {
			this.sentidoTirano = true;
		} else {
			this.sentidoTirano = false;
		}
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getAncho() {
		return ancho;
	}

	public double getTecho() {
		return this.y - this.getAlto() / 2;

	}

	public double getPiso() {
		return this.y + this.getAlto() / 2;

	}

	public double getAlto() {
		return alto;
	}

	public double getDerecha() {
		return this.x + getAncho() / 2;
	}

	public double getIzquierda() {
		return this.x - getAncho() / 2;
	}

}
