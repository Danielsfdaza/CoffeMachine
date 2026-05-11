package controlAlarma;

public class AlarmaAsignada {

    private final int idMaquina;
    private final String ubicacion;
    private final String fechaInicial;
    private final int idAlarma;
    private final String descripcion;
    private final boolean valida;
    private final String error;

    public AlarmaAsignada(int idMaquina, String ubicacion, String fechaInicial,
            int idAlarma, String descripcion) {
        this.idMaquina = idMaquina;
        this.ubicacion = ubicacion;
        this.fechaInicial = fechaInicial;
        this.idAlarma = idAlarma;
        this.descripcion = descripcion;
        this.valida = true;
        this.error = "";
    }

    private AlarmaAsignada(String error) {
        this.idMaquina = 0;
        this.ubicacion = "";
        this.fechaInicial = "";
        this.idAlarma = 0;
        this.descripcion = "";
        this.valida = false;
        this.error = error;
    }

    public static AlarmaAsignada invalida(String error) {
        return new AlarmaAsignada(error);
    }

    public int getIdMaquina() {
        return idMaquina;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public int getIdAlarma() {
        return idAlarma;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isValida() {
        return valida;
    }

    public String getError() {
        return error;
    }

    public String getRecursoEsperado() {
        return ControlAlarma.recursoParaAlarma(idAlarma);
    }

    public String toDisplayString(int indice) {
        if (!valida) {
            return indice + ". Registro invalido - " + error;
        }

        return indice + ". Maquina " + idMaquina + " | " + ubicacion
                + " | " + fechaInicial + " | alarma " + idAlarma
                + " | " + descripcion + " | recurso: " + getRecursoEsperado();
    }
}
