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
	private List<Movimiento> movimientos = new ArrayList<>();

	public Cuenta() {
	}

	public Cuenta(double montoInicial) {
		saldo = montoInicial;
	}

	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}
	
	public void controlarMonto(double monto) {
		if(monto <= 0) {
			throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
		}
	}

	public void poner(double cuanto) {
		controlarMonto(cuanto);
		if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
			throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
		}

		new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
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
		new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
	}

	public void agregarMovimiento(Movimiento movimiento) {
		movimientos.add(movimiento);
	}

	public double getMontoExtraidoA(LocalDate fecha) {
		return getMovimientos().stream()
				.filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
				.mapToDouble(Movimiento::getMonto).sum();
	}

	public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

}
