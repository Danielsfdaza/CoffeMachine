package controlAlarma;

public class OrdenAtencion {

    private final int codigoMaquina;
    private final int idAlarma;
    private final String ubicacion;
    private final String fechaInicial;
    private final String descripcion;
    private final String recursoEsperado;

    public OrdenAtencion(AlarmaAsignada alarma) {
        this.codigoMaquina = alarma.getIdMaquina();
        this.idAlarma = alarma.getIdAlarma();
        this.ubicacion = alarma.getUbicacion();
        this.fechaInicial = alarma.getFechaInicial();
        this.descripcion = alarma.getDescripcion();
        this.recursoEsperado = alarma.getRecursoEsperado();
    }

    public int getCodigoMaquina() {
        return codigoMaquina;
    }

    public int getIdAlarma() {
        return idAlarma;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRecursoEsperado() {
        return recursoEsperado;
    }

    public String toDisplayString() {
        return "ORDEN DE ATENCION\n"
                + "Maquina: " + codigoMaquina + "\n"
                + "Alarma: " + idAlarma + "\n"
                + "Ubicacion: " + ubicacion + "\n"
                + "Fecha inicial: " + fechaInicial + "\n"
                + "Descripcion: " + descripcion + "\n"
                + "Recurso esperado: " + recursoEsperado;
    }
}
