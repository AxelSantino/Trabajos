package juego;

import java.util.Random;


public class Settings {

	private double valorAleatorio(double min, double max) {
		Random r = new Random();
		return r.nextDouble((max - min) + 1) + min;
	}


	public void reiniciarJuego(Juego juego) {
		juego.prota = new Protagonista(376, 545, 30, 50, 0);
		juego.magma = new Magma (376, 610, 10000, 50);
		juego.score = 0;
		juego.enemigosEliminados = 0;
		juego.PrincepuedeDisparar = true;
		juego.boom.clear();
		juego.fuego.clear();
		juego.enemigos = new Enemigo[8];
		double velocidad = 0.9;
		for (int i = 0; i < juego.enemigos.length; i++) {
			double y = 63.5 + (i / 2) * 120;
			juego.enemigos[i] = new Enemigo(valorAleatorio(20, 780), y, 30, 50, velocidad);
			velocidad *= -1;
		}
		 juego.bloques = new Bloques[5];
	        for (int i = 0; i < juego.bloques.length; i++) {
	            juego.bloques[i] = new Bloques(120 * (i + 0.5) + 50);
	        }
		
		juego.GameOver = false;
		juego.Victoria = false;
		juego.GameMagma = false;
	}
	
}




