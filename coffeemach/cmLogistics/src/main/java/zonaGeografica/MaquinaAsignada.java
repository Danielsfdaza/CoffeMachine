package zonaGeografica;

public class MaquinaAsignada {

    private final int idMaquina;
    private final String ubicacion;
    private final String zona;
    private final boolean valida;
    private final String error;

    public MaquinaAsignada(int idMaquina, String ubicacion) {
        this.idMaquina = idMaquina;
        this.ubicacion = ubicacion;
        this.zona = derivarZona(ubicacion);
        this.valida = true;
        this.error = "";
    }

    private MaquinaAsignada(String error) {
        this.idMaquina = 0;
        this.ubicacion = "";
        this.zona = "";
        this.valida = false;
        this.error = error;
    }

    public static MaquinaAsignada invalida(String error) {
        return new MaquinaAsignada(error);
    }

    public static MaquinaAsignada desdeServidor(String registro) {
        if (registro == null || registro.trim().isEmpty()) {
            return invalida("Registro vacio");
        }

        String[] partes = registro.split("-", 2);
        if (partes.length != 2) {
            return invalida("Formato invalido: " + registro);
        }

        try {
            int idMaquina = Integer.parseInt(partes[0].trim());
            String ubicacion = partes[1].trim();
            if (idMaquina <= 0 || ubicacion.isEmpty()) {
                return invalida("Datos incompletos: " + registro);
            }
            return new MaquinaAsignada(idMaquina, ubicacion);
        } catch (NumberFormatException e) {
            return invalida("Codigo de maquina invalido: " + registro);
        }
    }

    private String derivarZona(String ubicacion) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            return "Sin zona";
        }

        String[] partes = ubicacion.split("-");
        return partes[0].trim();
    }

    public int getIdMaquina() {
        return idMaquina;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getZona() {
        return zona;
    }

    public boolean isValida() {
        return valida;
    }

    public String getError() {
        return error;
    }

    public String toDisplayString() {
        if (!valida) {
            return "Registro invalido - " + error;
        }
        return idMaquina + " - " + ubicacion + " (" + zona + ")";
    }
}
