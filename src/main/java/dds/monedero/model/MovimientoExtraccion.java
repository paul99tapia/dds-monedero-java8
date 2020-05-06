package dds.monedero.model;

import java.time.LocalDate;

public class MovimientoExtraccion extends Movimiento{

	public MovimientoExtraccion(LocalDate fecha, double monto) {
		super(fecha, monto);
	}
	
	public double calcularValor(Cuenta cuenta) {
		return cuenta.getSaldo() - getMonto();
	}
}
