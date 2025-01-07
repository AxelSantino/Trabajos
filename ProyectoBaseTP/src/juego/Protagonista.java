package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Protagonista {

	// valores configurables para optimizar el salto de la princesa y el piso donde
	// se para.
	static final double tramoLento = 25; // tramo de velocidad lenta en el que la princesa desacelera velocidad al subir
	static final double fuerzaSalto = 4.5; // para ganar altura al saltar
	double incrSalto = 3.5; // valor que hace reducir y aumentar la velocidad
	static final double pulsoSalto = 0.32; // para sumar o restar al contador de efecto gravedad (sube y baja lentamente
											// en // el 'tramoLento')
	static final int pisoActual[] = { 545, 423, 303, 183, 63, 0 }; // stablece el piso donde// se para la princesa
	static final double escala = 0.9; // tamaño de la imagen de la princesa
	static final byte velNormal = 3;
	static final byte velBaja = 1;
	static final double velMin = 0.5;

	// variables princesa ordenada por tipo de datos
	public static final int subeRapido = 1;
	public static final int subeLento = 2;
	public static final int bajaLento = 3;
	public static final int bajaRapido = 4;

	
	boolean estaApoyado = false;
	boolean banderaSalto;
	boolean sentidoProta;
	boolean izq = false;
	boolean der = true;
	boolean chocaBloque = false;
	boolean agachada;
	boolean saltando;
	boolean protamuerta;

	int angulo;
	int piso = 0;
	int progresoSalto = subeRapido; // variable que contiene el progreso del salto de la princesa

	double alto;
	double ancho;
	double velocidad;
	double movX;
	double movY;

	Image princeIzq;
	Image princeDer;
	Image agaIzq;
	Image agaDer;

	// VARIABLES DE LAS VIDAS DE LA PRINCESA (EXTRA)
	private int cooldown;
	private int tiempoVul;
	int vidas;
	static final double escalaVida = 0.09;
	Image Ivida;

	public Protagonista(int x, int y, int ancho, int alto, int angulo) {
		this.movX = x;
		this.movY = y;
		this.ancho = ancho;
		this.alto = alto;
		this.angulo = 0;
		this.agachada = false;
		this.protamuerta=true;
		princeIzq = Herramientas.cargarImagen("Imagenes/princeIzq.png");
		princeDer = Herramientas.cargarImagen("Imagenes/princeDer.png");
		agaIzq = Herramientas.cargarImagen("Imagenes/agaIzq.png");
		agaDer = Herramientas.cargarImagen("Imagenes/agaDer.png");

		this.cooldown = 100;
		this.tiempoVul = 0;
		this.vidas = 3;
		this.Ivida = Herramientas.cargarImagen("Imagenes/Corazon.png");
	}

	public void dibujarse(Entorno entorno) {
		movX += velocidad;
		if (sentidoProta == der) {

			if (agachada) { // Dibujar protagonista agachado
				entorno.dibujarImagen(agaDer, movX, movY + 15, angulo, Protagonista.escala);
			} else {
				entorno.dibujarImagen(princeDer, movX - 10, movY, angulo, Protagonista.escala);
			}
		} else {

			if (agachada) { // Dibujar protagonista agachado
				entorno.dibujarImagen(agaIzq, movX, movY + 15, 0, Protagonista.escala);
			} else {
				entorno.dibujarImagen(princeIzq, movX, movY, 0, Protagonista.escala);
			}
		}
	}

	public Boomerang disparar() {
		return new Boomerang(movX, movY, 5, sentidoProta); // Velocidad del disparo 5

	}

	public int getVidas() {
		return vidas;
	}

	public void reducirVida() {
		if (tiempoVul == 0) {
			if (vidas > 0) {
				vidas--;
				Herramientas.play("Musica/PrincesaM.wav");
			}
			tiempoVul = cooldown; // Reinicia el contador
		}	
	}
	
	 public void actualizarTiempoInvulnerabilidad() {
	        if (tiempoVul > 0) {
	            tiempoVul--; // Decrementa el contador de tiempo
	        }
	    }
	public void dibujarVidas(Entorno entorno) {
		for (int i = 0; i < vidas; i++) {
			entorno.dibujarImagen(Ivida, 720 + (i * 30), 15, 0, Protagonista.escalaVida);
		}
	}

	/////////////////// RELACIONADO AL SALTO DEL PERSONAJE//////////////////////////

	public void iniciaSalto(Entorno s) {// la princesa comienza a saltar

		saltando = true;
		if (movY >= pisoActual[piso + 1] + tramoLento) {// salta hasta un limite
			movY -= fuerzaSalto;
			if (chocaBloque) {// si en el salto se choca con un bloque en el techo comienza a descender
				chocaBloque = false;
				progresoSalto = bajaLento;

			}
		} else {
			progresoSalto = subeLento;
		}
	}

	public void continuaSalto(Entorno b) {// al llegar arriba realiza un efecto gravedad y luego baja

		switch (progresoSalto) {

		case subeRapido:// mientras sube rapido se verifica si llega a otro piso o solo hace un salto
						// leve
			if (movY < pisoActual[piso]) {
				progresoSalto = subeLento;
				break;
			}
			if (!estaApoyado && piso > 0) { // se verifica que la princesa tenga piso firme
				progresoSalto = bajaLento;
				piso -= 1;
			}
			break;

		case subeLento:
			incrSalto -= pulsoSalto;
			movY -= incrSalto;// reduce la velocidad de subida
			if (incrSalto <= 0 || chocaBloque) {
				progresoSalto = bajaLento;
				chocaBloque = false;
			}
			break;

		case bajaLento: // comienza la bajada suave
			incrSalto += pulsoSalto;
			movY += incrSalto;
			if (incrSalto >= fuerzaSalto || movY >= pisoActual[piso]) {
				incrSalto = fuerzaSalto;
				progresoSalto = bajaRapido;
			}
			if (movY <= pisoActual[piso + 1] && piso < 4) {// si la princesa toca un piso mas arriba se queda ahi
				piso += 1;
				saltando = false;
			}
			break;

		case bajaRapido:// la princesa termina de bajar a piso firme
			movY += incrSalto;
			if (movY >= pisoActual[piso]) {
				movY = pisoActual[piso];
				incrSalto = fuerzaSalto;
				progresoSalto = subeRapido;
				banderaSalto = false;
				saltando = false;
			}
			break;
		}
	}

	public double getAlto() {
		return this.alto;
	}

	public double getAncho() {
		return this.ancho;
	}

	public double getTecho() {
		return this.movY - this.getAlto() / 2;

	}

	public double getPiso() {
		return this.movY + this.getAlto() / 2;
	}

	public double getIzquierda() {
		return this.movX - this.getAncho() / 2;

	}

	public double getDerecha() {
		return this.movX + this.getAncho() / 2;
	}

	public double getX() {
		return movX;
	}

	public double getY() {
		return movY;
	}
}
