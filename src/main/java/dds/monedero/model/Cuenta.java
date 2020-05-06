package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {

	private double saldo = 0;
	private List<Movimiento> movimientosExtracciones = new ArrayList<>();
	private List<Movimiento> movimientosDepositos = new ArrayList<>();
	
	public Cuenta() {
	}

	public Cuenta(double montoInicial) {
		saldo = montoInicial;
	}
	
	public List<Movimiento> getMovimientosExtracciones() {
		return movimientosExtracciones;
	}

	public void setMovimientosExtracciones(List<Movimiento> movimientosExtracciones) {
		this.movimientosExtracciones = movimientosExtracciones;
	}
	
	public List<Movimiento> getMovimientosDepositos() {
		return movimientosDepositos;
	}

	public void controlarMonto(double monto) {
		if(monto <= 0) {
			throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
		}
	}

	public void poner(double cuanto) {
		controlarMonto(cuanto);
		if (getMovimientosDepositos().stream().count() >= 3) {
			throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
		}
		Movimiento nuevo = new MovimientoDeposito(LocalDate.now(), cuanto);
		efectuarMovimiento(nuevo);
		movimientosDepositos.add(nuevo);
	}

	public void sacar(double cuanto) {
		controlarMonto(cuanto);
		if (getSaldo() - cuanto < 0) {
			throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
		}
		double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
		double limite = 1000 - montoExtraidoHoy;
		if (cuanto > limite) {
			throw new MaximoExtraccionDiarioException(
					"No puede extraer mas de $ " + 1000 + " diarios, límite: " + limite);
		}
		Movimiento nuevo = new MovimientoExtraccion(LocalDate.now(), cuanto);
		efectuarMovimiento(nuevo);
		movimientosExtracciones.add(nuevo);
	}

	public void efectuarMovimiento(Movimiento movimiento) {
		setSaldo(movimiento.calcularValor(this));
	}

	public double getMontoExtraidoA(LocalDate fecha) {
		return getMovimientosExtracciones().stream()
				.filter(movimiento -> movimiento.getFecha().equals(fecha))
				.mapToDouble(Movimiento::getMonto).sum();
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

}
