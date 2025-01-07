package juego;

import entorno.Entorno;

public class Bloques {
	public Bloque[] bloques;

	public Bloques(double y) { // Arreglo encargado de crear los bloques individuales de la clase Bloque. este
								// arreglo se llama al main
		bloques = new Bloque[16];
		Bloque aux = new Bloque(0.0, 0.0);
		double ancho = aux.getAncho();
		for (int i = 0; i < bloques.length; i++) {
		
			bloques[i] = new Bloque((i + 0.5) * ancho, y);
		}
	}

	public void dibujar(Entorno e) {
		for (int i = 0; i < bloques.length; i++) {
			if (bloques[i] != null) {
				bloques[i].dibujar(e);
			}
		}
	}
}
