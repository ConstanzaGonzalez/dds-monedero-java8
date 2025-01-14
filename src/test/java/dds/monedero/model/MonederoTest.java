package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void ponerDineroEnCuentaGuardaSaldo() {
    cuenta.poner(1500);
    assertEquals(cuenta.getSaldo(), 1500);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void realizarDepositosIncrementaElSaldo() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(cuenta.getSaldo(), 3856);
  }

  @Test
  void realizarMasDeTresDepositosNoEsPosible() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void extraerMasQueElSaldoNoEsPosible() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.poner(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void extraerMasDe1000EnUnDiaNoEsPosible() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.poner(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void extraerMontoNegativoNoEsPosible() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }
  @Test
  void realizarExtraccionesDisminuyeElSaldo() {
    cuenta.poner(1500);
    cuenta.sacar(400);
    cuenta.sacar(100);
    assertEquals(cuenta.getSaldo(), 1000);
  }

}