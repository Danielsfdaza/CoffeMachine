package bodega;

public class OrdenBodega {

    private final int codigoMaquina;
    private final int idAlarma;
    private final String ubicacion;
    private final String descripcion;

    public OrdenBodega(int codigoMaquina, int idAlarma, String ubicacion, String descripcion) {
        this.codigoMaquina = codigoMaquina;
        this.idAlarma = idAlarma;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }
}
