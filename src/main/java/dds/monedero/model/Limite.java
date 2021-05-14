package dds.monedero.model;

import dds.monedero.exceptions.MaximoExtraccionDiarioException;

import java.time.LocalDate;

public class Limite {
  double limite;

  public Limite(double limite) {
    this.limite = limite;
  }

  public void validarLimite(double cuanto, Cuenta cuenta) {
    double limiteDisponible = limite - cuenta.getMontoExtraidoA(LocalDate.now());
    if (cuanto > limiteDisponible) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + limite
          + " diarios, límite: " + limite);
    }
  }
}
