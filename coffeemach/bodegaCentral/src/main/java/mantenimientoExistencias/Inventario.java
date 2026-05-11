package mantenimientoExistencias;

public interface Inventario {
    public void abastecerSuministros();

    public void abastecerMonedas();

    public void abastecerIngredientes();

    public boolean tieneExistencias(String recurso, int cantidad);

    public boolean descontarExistencias(String recurso, int cantidad);

    public String consultarResumen();

    public String recursoParaAlarma(int idAlarma);

    public int cantidadRequeridaParaAlarma(int idAlarma);

}
