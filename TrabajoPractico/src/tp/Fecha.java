package tp;

public class Fecha {
	private int dia;
	private int mes;
	private int año;

	public Fecha(String fecha) {
		String[] partes = fecha.split("/");
		this.dia = Integer.parseInt(partes[0]);
		this.mes = Integer.parseInt(partes[1]);
		this.año = Integer.parseInt(partes[2]);
	}

	// Método para calcular la fecha de una semana después
	public Fecha obtenerFechaSieteDiasDespues() {
		int diaFinal = dia + 7;
		int mesFinal = mes;
		int añoFinal = año;

		// Ajustar el mes y año si el día final excede el número de días del mes
		if (mes == 2) { // Febrero
			if (esBisiesto(año)) {
				if (diaFinal > 29) {
					diaFinal -= 29;
					mesFinal++;
				}
			} else {
				if (diaFinal > 28) {
					diaFinal -= 28;
					mesFinal++;
				}
			}
		} else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) { // Meses de 30 días
			if (diaFinal > 30) {
				diaFinal -= 30;
				mesFinal++;
			}
		} else { // Meses de 31 días
			if (diaFinal > 31) {
				diaFinal -= 31;
				mesFinal++;
			}
		}

		// Asegurarse de que el mes no exceda 12
		if (mesFinal > 12) {
			mesFinal = 1;
			añoFinal++;
		}

		return new Fecha(diaFinal + "/" + mesFinal + "/" + añoFinal);
	}

	
	// Método auxiliar para verificar si un año es bisiesto
	private boolean esBisiesto(int año) {
		return (año % 4 == 0 && año % 100 != 0) || (año % 400 == 0);
	}

	// Método para comparar si esta fecha es después de otra fecha
	public boolean esMayorQue(Fecha otraFecha) {
		if (this.año != otraFecha.año) {
			return this.año > otraFecha.año;
		} else if (this.mes != otraFecha.mes) {
			return this.mes > otraFecha.mes;
		} else {
			return this.dia > otraFecha.dia;
		}
	}

	// Método para comparar si esta fecha es antes de otra fecha
	public boolean esMenorQue(Fecha otraFecha) {
		if (this.año != otraFecha.año) {
			return this.año < otraFecha.año;
		} else if (this.mes != otraFecha.mes) {
			return this.mes < otraFecha.mes;
		} else {
			return this.dia < otraFecha.dia;
		}
	}
}
