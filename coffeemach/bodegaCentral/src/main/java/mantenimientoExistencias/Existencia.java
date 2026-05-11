package mantenimientoExistencias;

public class Existencia {

    private final String recurso;
    private int cantidad;

    public Existencia(String recurso, int cantidad) {
        this.recurso = recurso;
        this.cantidad = cantidad;
    }

    public String getRecurso() {
        return recurso;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void abastecer(int cantidad) {
        if (cantidad > 0) {
            this.cantidad += cantidad;
        }
    }

    public boolean descontar(int cantidad) {
        if (cantidad <= 0 || this.cantidad < cantidad) {
            return false;
        }
        this.cantidad -= cantidad;
        return true;
    }
}
