package juego;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	Entorno entorno;
	Protagonista prota;
	Bloques[] bloques;
	Enemigo[] enemigos;
	Magma magma;
	Settings settings;
	List<Boomerang> boom;
	List<Fuego> fuego;

	Image fondo;
	Image fondoDerrota;
	Image fondoVictoria;
	Image fondoDerrota2;
	Image texto1;
	Image menu;
	Image letramenu1;
	Image letramenu2;
	Image letramenu3;
	


	// VARIABLES CREADAS PARA LA FUNCIONALIDAD DEL JUEGO
	int enemigosEliminados;
	int score;
	boolean PrincepuedeDisparar;
	boolean GameOver;
	boolean Victoria;
	boolean GameMagma;
	boolean inicio;
	int Iniciojuego;
	

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	Juego() {

		this.entorno = new Entorno(this, "Super Elizabeth - Grupo 3 - v1 ", 800, 600);
		// BLOQUES
		bloques = new Bloques[5];
		// PROTAGONISTA
		prota = new Protagonista(376, 545, 30, 50, 0);
		// FONDOS	
		menu = Herramientas.cargarImagen("Imagenes/menu.gif");
		letramenu1 = Herramientas.cargarImagen("Imagenes/LetraMenu1.png");
		letramenu2 = Herramientas.cargarImagen("Imagenes/LetraMenu2.png");
		letramenu3 = Herramientas.cargarImagen("Imagenes/LetraMenu3.png");

		fondo = Herramientas.cargarImagen("Imagenes/fondoo.png");
		fondoDerrota = Herramientas.cargarImagen("Imagenes/derrota2.png");
		fondoVictoria = Herramientas.cargarImagen("Imagenes/Victoria.png");
		texto1 = Herramientas.cargarImagen("Imagenes/textoMuerteMagma.png");
		fondoDerrota2 = Herramientas.cargarImagen("Imagenes/magmaP.gif");
		
		// MUSICA
		Herramientas.loop("Musica/Juego.wav");
		// ENEMIGOS
		enemigos = new Enemigo[8];
		// BOOMERANG
		boom = new ArrayList<>();
		// FUEGO
		fuego = new ArrayList<>();
		// MAGMA
		magma = new Magma(376, 610, 10000, 50);

		// LLAMADO A VARIABLES CREADAS PARA LA FUNCIONALIDAD DEL JUEGO
		this.PrincepuedeDisparar = true;
		this.settings = new Settings();
		this.Iniciojuego = 0;

		// Inicializar los enemigos
		double velocidad = 0.9;
		for (int i = 0; i < enemigos.length; i++) {
			double y = 63.5 + (i / 2) * 120; // Posición Y basada en la fila
			enemigos[i] = new Enemigo(valorAleatorio(20, 780), y, 30, 50, velocidad);
			velocidad *= -1;
		}

		// Inicializar a los bloques
		for (int i = 0; i < bloques.length; i++) {
			bloques[i] = new Bloques(120 * (i + 0.5) + 50);

		}
		// Inicia el juego!
		this.entorno.iniciar();
	}
//////////////////////////////////////////////////////////ACÁ SE COCINA EL JUEGO///////////////////////////////////////////////////////////////////////////////////////

	public void tick() {

		if (GameOver || Victoria || GameMagma) {
			mostrarPantallaFinal();
			return;
		}

		///// MENÚ///////

		if (prota != null && Iniciojuego == 0) {

			inicio = true;

			if (inicio) {
				entorno.dibujarImagen(menu, 400, 300, 0);
				entorno.dibujarImagen(letramenu1, 200, 180, 0);
				entorno.dibujarImagen(letramenu2, 190, 450, 0);
				entorno.dibujarImagen(letramenu3, 178, 550, 0);

				if (entorno.sePresiono(entorno.TECLA_ENTER)) {
					Iniciojuego = 1;
					inicio = false;

				} else if (entorno.sePresiono('s')) { // SI TOCAS LA S SALIS DEL JUEGO
					System.exit(0); // Cerrar la aplicación
				}

			}
		}
		if (Iniciojuego == 1) {

			//// Dibujamos todo lo que necesitamos ////

			// DIBUJAMOS EL ENTORNO //
			entorno.dibujarImagen(fondo, 400, 300, 0, 1);

			// DIBUJAMOS AL PROTAGONISTA
			prota.dibujarse(entorno);

			// DIBUJAMOS LAS VIDAS
			prota.dibujarVidas(entorno);

			// Dibuja a los enemigos y genera su movimiento
			for (Enemigo enemigos : this.enemigos) {
				enemigos.dibujarse(entorno);
				enemigos.mover();

				if (enemigos.getDerecha() > entorno.ancho() || enemigos.getIzquierda() < 0) { // No permite que los
																								// enemigos
					// se salgan de la pantalla
					enemigos.DameVelocidad(enemigos.getVelocidad() * -1);

				}
			}
			// DIBUJA BLOQUE POR BLOQUE EN EL ENTORNO 
			for (int i = 0; i < bloques.length; i++) {
				bloques[i].dibujar(entorno);
			}
			// DIBUJO LA MAGMA
			magma.dibujarMagma(entorno);
			magma.comienzo += 1; // lo que tarda la magma para poder comenzar a subir

			magma.cont += 7; // hace como una especie de reloj para q la magma suba lento o rapido (en este
								// caso la idea es lento)

			if (magma.cont >= 15 && magma.comienzo > 300) {
				magma.actualizar();
				magma.cont = 0;
				for (int i = 0; i < bloques.length; i++) {

					if (detectarColisionMAGMABLOQUES(bloques)) {

						if (bloques[1] == null) {
							magma.banderaMagma = false; // Lo que hace esto es que si el piso de arriba de
														// todo
														// es borrado no aparecen mas los enemigos
						}
					}
				}
				if (colisionPRINCEmagma1(prota) && prota.protamuerta) {
					Herramientas.play("Musica/princesaM.wav");
					prota.vidas = 0;
					prota.protamuerta = false;

					GameMagma = true;

				}
			}

			///// ACTUALIZACIONES DE PANTALLA/////

			prota.actualizarTiempoInvulnerabilidad();

			// MOVIMIENTO DEL PROTAGONISTA //

			if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && prota.movX > 20 && prota.progresoSalto < 3) {
				prota.sentidoProta = false;
				prota.velocidad = -1 * Protagonista.velNormal; // Velocidad normal
				if (prota.agachada) {
					prota.velocidad = -1 * Protagonista.velMin; // Velocidad reducida pq está agachado
				}
				if (prota.saltando) {
					prota.velocidad = -1 * Protagonista.velBaja; // Velocidad cuando salta
				}
			} else if (entorno.estaPresionada(entorno.TECLA_DERECHA) && prota.movX < 780 && prota.progresoSalto < 3) {
				prota.sentidoProta = true;
				prota.velocidad = Protagonista.velNormal; // Velocidad normal
				if (prota.agachada) {
					prota.velocidad = Protagonista.velMin; // Velocidad reducida pq está agachado
				}
				if (prota.saltando) {
					prota.velocidad = Protagonista.velBaja; // Velocidad cuando salta
				}
			} else {
				prota.velocidad = 0;
			}

			// ***********************************************************************************************************
			// Presienando la tecla 'x' la princesa comienza a saltar, cuando se deja de
			// presionar sigue un efecto de gravedad
			// que luego de unos instantes comienza a bajar. Al descender lo hace primero
			// despacio y luego rapido.
			//

			if (!prota.agachada && entorno.estaPresionada(entorno.TECLA_ARRIBA) && prota.progresoSalto == Protagonista.subeRapido
					&& prota.banderaSalto)  { // EL SALTO DE LA
											// PRINCESA
				// SUBIENDO rapido
				prota.iniciaSalto(entorno);// se inicia el salto
					
			} else {
				prota.banderaSalto = true;
				prota.continuaSalto(entorno); // esta funcion solo se activa con la banderaSalto=true (cuando no se
												// presiona
												// 'x')
			}
			
			
			// *************************************************************************************************************

			// Manejar estado de agachada
			if (!prota.saltando && entorno.estaPresionada(entorno.TECLA_ABAJO)) {
				prota.agachada = true;

			} else {
				prota.agachada = false;
			}

			// Verificar si el protagonista ha tocado la parte superior de la pantalla
			if (prota.movY <= 0) {
				Victoria = true;
				return;
			}

			// DISPARO DEL PROTAGONISTA
			// Presionando la tecla 'c' la princesa puede disparar

			if (entorno.sePresiono(entorno.TECLA_CTRL) && PrincepuedeDisparar) {
				Herramientas.play("Musica/Disparo.wav");
				boom.add(prota.disparar());
				PrincepuedeDisparar = false;
			}

			// COLISION DE LOS PROYECTILES DEL PROTA CON LOS ENEMIGOS

			List<Boomerang> disparosARemover = new ArrayList<>();

			for (Boomerang boome : boom) {
				boome.mover();
				boome.dibujarse(entorno);
				if (boome.getX() < 0 || boome.getX() > entorno.ancho()) {
					disparosARemover.add(boome);
					PrincepuedeDisparar = true;

				} else {
					for (int i = 0; i < enemigos.length; i++) {
						if (enemigos[i] != null && disparoAEnemigo(boome, enemigos[i])) {
							disparosARemover.add(boome);
							enemigos[i] = null; // Eliminar enemigo del arreglo
							PrincepuedeDisparar = true;
							score += 2;
							enemigosEliminados += 1;
							Herramientas.play("Musica/Muerte.wav");
							break; // Cuando el disparo colisiona con el enemigo, el disparo desaparece

						}
					}
				}
			}

			boom.removeAll(disparosARemover);

			// AÑADE A LOS ENEMIGOS EN UNA LISTA
			List<Enemigo> listaEnemigos = new ArrayList<>();
			for (Enemigo enemigo : enemigos) {
				if (enemigo != null) {

					listaEnemigos.add(enemigo);
				}

			}
			// Asegura que siempre haya al menos dos enemigos en la pantalla

			if (magma.getY() > 300) { // Cuando la lava llega a la mitad de la pantalla dejan de aparecer enemigos

				while (listaEnemigos.size() < 2) { // Lee listaEnemigos y se asegura que siempre haya minimo 2
					listaEnemigos.add(generarEnemigoAleatorio());
				}
			}
			enemigos = listaEnemigos.toArray(new Enemigo[0]); // Convierte la listaEnemigos en un arreglo como lo era
																// originalmente Enemigo[]

			// COLISION DE LA PRINCESA CON LOS ENEMIGOS
			for (int i = 0; i < enemigos.length; i++) {
				if (colisionPrinceEnemigo(prota, enemigos[i])) {
					prota.reducirVida();
					if (prota.getVidas() <= 0) {
						GameOver = true;
						break;
					}
				}
			}

			// GESTIONAR DISPAROS DE ENEMIGOS
			for (Enemigo enemigo : enemigos) {
				enemigo.actualizar(); // Actualizar el estado del enemigo
				Fuego nuevoDisparo = enemigo.disparar();
				if (nuevoDisparo != null) {
					fuego.add(nuevoDisparo);
				}
			}

			// COLISION DE LOS PROYECTILES DE LOS ENEMIGOS CON EL PROTA

			List<Fuego> disparoEnemigo = new ArrayList<>();
			for (Fuego f : fuego) {
				f.mover();
				f.dibujarse(entorno);
				if (f.getX() < 0 || f.getX() > entorno.ancho()) {
					disparoEnemigo.add(f);

				}

				if (!prota.agachada && disparoAprince(prota, f)) { /// QUE LOS DISPAROS MATEN A LA PRINCESA:

					prota.reducirVida(); // Cada disparo le saca un punto de vida al prota
			
					disparoEnemigo.add(f); // Añadir el disparo que impacta a la lista para ser eliminado

				}
				if (prota.getVidas() <= 0) {
					GameOver = true;
				}

			}
			fuego.removeAll(disparoEnemigo);

			// DETECTAR APOYO DEL PROTA CON LOS BLOQUES
			if (detectarApoyo(prota, bloques)) {
				prota.estaApoyado = true;
			} else {
				prota.estaApoyado = false;
			}
			if (detectarColision(prota, bloques)) {
				prota.chocaBloque = true;
			}

			// DETECTAR APOYO DE LOS ENEMIGOS CON LOS BLOQUES
			ArrayList<Enemigo> enemigosList = new ArrayList<>(Arrays.asList(enemigos)); // El arreglo se convierte en
																						// una
																						// lista

			// DETECTAR APOYO DE ENEMIGOS CON LOS BLOQUES

			for (int j = 0; j < enemigosList.size(); j++) {
				Enemigo enemigo = enemigosList.get(j);
				if (tocaSuelo(enemigo, bloques)) {
					enemigo.apoyado = true;
				} else {
					enemigo.apoyado = false;
					enemigo.caer(entorno);
				}

				if (tocaSuelo(enemigo, bloques[4])) {

					// Eliminar el enemigo de la lista
					enemigosList.remove(j);
					j--; // Ajustar el índice después de eliminar un enemigo
				}
				/////// aca cuando la magma toca a los enemigos los mata
				if (enemigomagmaa(enemigo)) {
					// Eliminar el enemigo de la lista
					enemigosList.remove(j);
					j--; // Ajustar el índice después de eliminar un enemigo

				}
			}

			// Convertir a arreglo de nuevo
			enemigos = enemigosList.toArray(new Enemigo[0]);

			// COLORINCHE VISUAL

			entorno.cambiarFont("", 17, Color.WHITE);
			entorno.escribirTexto("" + enemigosEliminados, 255, 20);

			entorno.cambiarFont("", 17, Color.WHITE);
			entorno.escribirTexto("SCORE:", 5, 20);
			entorno.escribirTexto("ELIMINACIONES:", 120, 20);

			entorno.cambiarFont("", 17, Color.WHITE);
			entorno.escribirTexto("" + score, 72, 20);

		}
	}

	///////////////////////// FUNCIONALIDADES CREADAS PARA EL FUNCIONAMIENTO DEL
	/////////////////////////	JUEGO///////////////////////////////

	private double valorAleatorio(double min, double max) { // funcion para crear un numero aleatorio en un determinado
		// rango (min y max)

		Random r = new Random();
		return r.nextDouble((max - min) + 1) + min;
	}

	private Enemigo generarEnemigoAleatorio() {
		Random rand = new Random();
		double x = 70 * (rand.nextInt(8) + 1) + (rand.nextInt(2) * 50);
		double y = 63.5 + (rand.nextInt(4) * 120);
		double velocidad = rand.nextBoolean() ? 1 : -1;
		return new Enemigo(x, y, 30, 50, velocidad);
	}

	// DERROTA Y/O VICTORIA
	public void mostrarPantallaFinal() {
		if (GameOver) {
			entorno.dibujarImagen(fondoDerrota, 400, 300, 0, 1);
			if (entorno.sePresiono(entorno.TECLA_ENTER)) {
				settings.reiniciarJuego(this);
			} else if (entorno.sePresiono('s')) { // SI TOCAS LA S SALIS DEL JUEGO
				System.exit(0); // Cerrar la aplicación
			}

		}

		else if (GameMagma) {
			entorno.dibujarImagen(fondoDerrota2, 400, 300, 0, 1);
			entorno.dibujarImagen(texto1, 390, 200, 0, 2);
			if (entorno.sePresiono(entorno.TECLA_ENTER)) {
				GameMagma = false;
				settings.reiniciarJuego(this);

			} else if (entorno.sePresiono('s')) { // SI TOCAS LA S SALIS DEL JUEGO
				System.exit(0); // Cerrar la aplicación
				GameMagma = false;
				
			}

		}

		else if (Victoria) {
			entorno.dibujarImagen(fondoVictoria, 400, 300, 0, 1);
			entorno.cambiarFont("", 20, Color.black);
			entorno.escribirTexto("" + enemigosEliminados, 285, 20);
			entorno.cambiarFont("", 20, Color.black);
			entorno.escribirTexto("SCORE:", 5, 20);
			entorno.escribirTexto("ELIMINACIONES:", 120, 20);
			entorno.cambiarFont("", 20, Color.black);
			entorno.escribirTexto("" + score, 90, 20);
			if (entorno.sePresiono(entorno.TECLA_ENTER)) {
				settings.reiniciarJuego(this);
			} else if (entorno.sePresiono('s')) { // SI TOCAS LAS S SALIS DEL JUEGO
				System.exit(0); // Cerrar la aplicación
			}

		}
	}

	/////////////////////////////////////// COLISIONES///////////////////////////////////////////////////////////////////////
	///////////////////////// PORFAVOR ACÁ LAS COLISIONES////////

	public boolean disparoAprince(Protagonista p, Fuego f) { //// CON ESTO LOS DISPAROS DE LOS ENEMIGOS MATAN A LA
																//// PRICESA/////
		// Obtener las coordenadas del disparo de fuego
		double pIzquierda = p.getX() - p.getAncho() / 2;
		double pDerecha = p.getX() + p.getAncho() / 2;
		double pArriba = p.getY() - p.getAlto() / 2;
		double pAbajo = p.getY() + p.getAlto() / 2;

		// Obtener las coordenadas de la princesa
		double fIzquierda = f.getX() - f.getAncho() / 2;
		double fDerecha = f.getX() + f.getAncho() / 2;
		double fArriba = f.getY() - f.getAlto() / 2;
		double fAbajo = f.getY() + f.getAlto() / 2;

		// Comprobar si colisionan
		boolean colisionX = (fIzquierda < pDerecha) && (fDerecha > pIzquierda);
		boolean colisionY = (fArriba < pAbajo) && (fAbajo > pArriba);

		return colisionX && colisionY;
	}

	public boolean colisionPrinceEnemigo(Protagonista p, Enemigo e) { // COLISIONES DE LOS ENEMIGOS CON LA PRINCESA
		// Obtener las coordenadas del boomerang
		double bIzquierda = p.getX() - p.getAncho() / 2;
		double bDerecha = p.getX() + p.getAncho() / 2;
		double bArriba = p.getY() - p.getAlto() / 2;
		double bAbajo = p.getY() + p.getAlto() / 2;

		// Obtener las coordenadas del enemigo
		double eIzquierda = e.getX() - e.getAncho() / 2;
		double eDerecha = e.getX() + e.getAncho() / 2;
		double eArriba = e.getY() - e.getAlto() / 2;
		double eAbajo = e.getY() + e.getAlto() / 2;

		// Comprobar si colisionan
		boolean colisionX = (bIzquierda < eDerecha) && (bDerecha > eIzquierda);
		boolean colisionY = (bArriba < eAbajo) && (bAbajo > eArriba);

		return colisionX && colisionY;

	}

	public boolean disparoAEnemigo(Boomerang b, Enemigo e) { // COLISION DE LOS DISPAROS A LOS ENEMIGOS
		// Obtener las coordenadas del boomerang
		double bIzquierda = b.getX() - b.getAncho() / 2;
		double bDerecha = b.getX() + b.getAncho() / 2;
		double bArriba = b.getY() - b.getAlto() / 2;
		double bAbajo = b.getY() + b.getAlto() / 2;

		// Obtener las coordenadas del enemigo
		double eIzquierda = e.getX() - e.getAncho() / 2;
		double eDerecha = e.getX() + e.getAncho() / 2;
		double eArriba = e.getY() - e.getAlto() / 2;
		double eAbajo = e.getY() + e.getAlto() / 2;

		// Comprobar si colisionan
		boolean colisionX = (bIzquierda < eDerecha) && (bDerecha > eIzquierda);
		boolean colisionY = (bArriba < eAbajo) && (bAbajo > eArriba);

		return colisionX && colisionY;
	}

/////////////////////////////////////////////////APOYO SUELO ENEMIGO///////////////////////////////////////////////////////	

	public boolean tocaSuelo(Enemigo e, Bloque b) {
		return (Math.abs(e.getPiso() - b.getTecho()) <= 4) && (e.getDerecha() > b.getIzquierda())
				&& (e.getIzquierda() < b.getDerecha());
	}

	public boolean tocaSuelo(Enemigo e, Bloques bl) {
		for (int i = 0; i < bl.bloques.length; i++) {
			if (bl.bloques[i] != null && tocaSuelo(e, bl.bloques[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean tocaSuelo(Enemigo e, Bloques[] l) {
		// CON UN BLOQUE DE UNA FILA MAS ARRIBA
		for (int i = 0; i < l.length; i++) {
			if (tocaSuelo(e, l[i])) {
				return true;
			}
		}
		return false;
	}

////////////////////////////////////////////APOYO SUELO PERSONAJE/////////////////////////////////////////////////////////

	public boolean detectarApoyo(Protagonista p, Bloque b) { // DETECTA EL APOYO DEL PROTAGONISTA CON UN BLOQUE
																// INDIVIDUAL
		return (Math.abs(p.getPiso() - b.getTecho()) < 2) && (p.getDerecha() > b.getIzquierda())
				&& (p.getIzquierda() < b.getDerecha());
	}

	public boolean detectarApoyo(Protagonista p, Bloques bl) { // RECORRE EL ARREGLO DE BLOQUES EN BUSCA DEL APOYO CON
																// UN BLOQUE INDIVIDUAL
		for (int i = 0; i < bl.bloques.length; i++) {
			if (bl.bloques[i] != null && detectarApoyo(p, bl.bloques[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean detectarApoyo(Protagonista p, Bloques[] l) { // RECORRE EL ARREGLO EN BUSCA DEL APOYO DEL PERSONAJE
																// CON UN BLOQUE DE UNA FILA MAS ARRIBA
		for (int i = 0; i < l.length; i++) {
			if (detectarApoyo(p, l[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean detectarColision(Protagonista p, Bloque b) {

		return (Math.abs(p.getTecho() - b.getPiso()) < 2) && (p.getDerecha() > b.getIzquierda())
				&& (p.getIzquierda() < b.getDerecha());
	}

	public boolean detectarColision(Protagonista p, Bloques bl) { // RECORRE EL ARREGLO BLOQUES EN BUSCA DE UNO
																	// DESTRUCTIBLE, SI SE ROMPE DESAPARECE, SINO NO
		for (int i = 0; i < bl.bloques.length; i++) {
			if (bl.bloques[i] != null && detectarColision(p, bl.bloques[i])) {
				if (bl.bloques[i].destructible) {
					bl.bloques[i] = null;
				}
				return true;
			}
		}
		return false;
	}

	public boolean detectarColision(Protagonista p, Bloques[] l) { // Evita que la protagonista sobre pase la base del
																	// bloque fijo
		for (int i = 0; i < l.length; i++) {
			if (detectarColision(p, l[i])) {
				return true;
			}
		}
		return false;
	}

	////////////////////////////// COLISIONES DE MAGMA///////////////////////
	////////////////////////////// ///////////////////////////////////////

	public boolean colisionMagmaBLOQUES(Bloque l) {            //COLISION DE LA MAGMA CON EL BLOQUE
		if (Math.abs(l.getTecho() - magma.getTecho()) < 2) {
			return true;
		}
		return false;
	}

	public boolean detectarColisionN(Bloques bl) {    //SI HAY COLISION, BORRA EL BLOQUE
		for (int i = 0; i < bl.bloques.length; i++) {
			if (bl.bloques[i] != null && colisionMagmaBLOQUES(bl.bloques[i])) {

				bl.bloques[i] = null;
				return true;
			}
		}
		return false;
	}

	public boolean detectarColisionMAGMABLOQUES(Bloques[] l) {   //BORRA TODOS LOS BLOQUES DE LA FILA SI LA MAGMA LOS TOCA

		for (int i = 0; i < l.length; i++) {
			if (detectarColisionN(l[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean colisionenemigomagma1(Enemigo e) {   //DETECTA LA COLISION DEL ENEMIGO CON EL MAGMA
		return magma.getTecho() < e.getTecho();
	}

	public boolean colisionenemigomagma(Enemigo e) {  //SI HAY COLISION, DEVUELVE VERDADERO, SINO FALSO

		if (e != null && colisionenemigomagma1(e)) {
			return true;
		}

		return false;
	}

	public boolean enemigomagmaa(Enemigo e) {  //SE LLAMA AL TICK,Y BORRA AL ENEMIGO SI LO TOCA LA LAVA

	
		if (colisionenemigomagma(e)) {
			return true;
		}

		return false;

	}

	public boolean colisionPRINCEmagma1(Protagonista p) { //DETECTA LA COLISION DE LA PRINCESA CON EL MAGMA
		return magma.getTecho() < p.getY();
	}

///////////////////////////////////////////////	FIN DE COLISIONES///////////////////////////////////////////////////////////

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
