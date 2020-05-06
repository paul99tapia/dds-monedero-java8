package dds.monedero.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class MonederoTest {
	private Cuenta cuenta;

	@Before
	public void init() {
		cuenta = new Cuenta();
		cuenta.setSaldo(0);
	}

	@Test
	public void ponerMonto() {
		cuenta.poner(1500);
		Assert.assertEquals(1500, cuenta.getSaldo(), 0.001);
	}

	@Test
	public void ponerTresDepositos() {
		cuenta.poner(1500);
		Assert.assertEquals(1500, cuenta.getSaldo(), 0.001);
		cuenta.poner(456);
		Assert.assertEquals(1956, cuenta.getSaldo(), 0.001);
		cuenta.poner(1900);
		Assert.assertEquals(3856, cuenta.getSaldo(), 0.001);
	}

	@Test(expected = MaximaCantidadDepositosException.class)
	public void noDeberiaRealizarMasDeTresDepositos() {
		cuenta.poner(1500);
		cuenta.poner(456);
		cuenta.poner(1900);
		cuenta.poner(245);
	}
	
	@Test(expected = MontoNegativoException.class)
	public void noPuedoRealizarUnDepositoNegativo() {
		cuenta.poner(-1500);
	}

	@Test
	public void extraerMonto() {
		cuenta.setSaldo(500);
		cuenta.sacar(300);
		Assert.assertEquals(200, cuenta.getSaldo(), 0.001);
	}
	
	@Test
	public void realizarVariasExtraccionesSinSuperarLimite() {
		cuenta.setSaldo(1000);
		cuenta.sacar(300);
		cuenta.sacar(300);
		cuenta.sacar(200);
		cuenta.sacar(100);
		Assert.assertEquals(100, cuenta.getSaldo(), 0.001);
	}

	@Test(expected = SaldoMenorException.class)
	public void noPuedoExtraerMasQueElSaldo() {
		cuenta.setSaldo(90);
		cuenta.sacar(100);
	}

	@Test(expected = MaximoExtraccionDiarioException.class)
	public void noDeberiaPoderSuperarElLimiteDeExtraccion() {
		cuenta.setSaldo(5000);
		cuenta.sacar(1001);
	}

	@Test(expected = MontoNegativoException.class)
	public void noDeberiaPoderExtraerMontoNegativo() {
		cuenta.sacar(-500);
	}

}