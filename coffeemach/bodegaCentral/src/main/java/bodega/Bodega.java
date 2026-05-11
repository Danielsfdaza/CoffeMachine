package bodega;

public interface Bodega {
    public void consultarMonedas();

    public void consultarIngredientes();

    public void consultarSuministros();

    public void entregaKitReparacion();

    public void retirarExistencias();

    public void abastecerExistencia();

    public void separarExistencias();

    public ResultadoAtencion atenderOrden(OrdenBodega orden);

    public String recursoParaAlarma(int idAlarma);

    public boolean soportaAlarma(int idAlarma);

}
