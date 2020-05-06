package dds.monedero.model;

import java.time.LocalDate;

public abstract class Movimiento {
	private LocalDate fecha;
	// En ningún lenguaje de programación usen jamás doubles para modelar dinero en
	// el mundo real
	// siempre usen numeros de precision arbitraria, como BigDecimal en Java y
	// similares
	private double monto;

	public Movimiento(LocalDate fecha, double monto) {
		this.fecha = fecha;
		this.monto = monto;
	}

	public double getMonto() {
		return monto;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public boolean esDeLaFecha(LocalDate fecha) {
		return this.fecha.equals(fecha);
	}

	public abstract double calcularValor(Cuenta cuenta);
}
