package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();
  public static final int DEPOSITOS_MAXIMOS = 3;
  private Limite limite;

  public Cuenta() {
    saldo = 0;
    limite = new Limite(1000);
  }
  public Cuenta(double montoInicial, Limite limite) {
    saldo = montoInicial;
    limite = limite;
  }

  public void poner(double cuanto) {
    validarMonto(cuanto);
    validarMovimiento();
    saldo += cuanto;
    agregarMovimiento(LocalDate.now(), cuanto, true);
  }

  private void validarMonto(double monto){
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }
  private void validarMovimiento(){
    if (movimientos.stream().filter(movimiento -> movimiento.isDeposito()).count() >= DEPOSITOS_MAXIMOS) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }
  public void sacar(double cuanto) {
    validarMonto(cuanto);
    validarSaldoNegativo(cuanto);
    limite.validarLimite(cuanto, this);
    saldo -= cuanto;
    agregarMovimiento(LocalDate.now(), cuanto, false);
  }
  private void validarSaldoNegativo(double cuanto){
    if (saldo - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + saldo + " $");
    }
  }
  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return movimientos.stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public double getSaldo() {
    return saldo;
  }
}
