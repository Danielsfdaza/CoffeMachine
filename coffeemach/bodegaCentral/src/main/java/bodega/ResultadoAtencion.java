package bodega;

public class ResultadoAtencion {

    private final boolean exitoso;
    private final String mensaje;
    private final String recurso;
    private final int cantidad;

    public ResultadoAtencion(boolean exitoso, String mensaje, String recurso, int cantidad) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.recurso = recurso;
        this.cantidad = cantidad;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getRecurso() {
        return recurso;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String toDisplayString() {
        String estado = exitoso ? "Atencion ejecutada" : "Atencion rechazada";
        return estado + "\n"
                + "Mensaje: " + mensaje + "\n"
                + "Recurso: " + recurso + "\n"
                + "Cantidad: " + cantidad;
    }
}
