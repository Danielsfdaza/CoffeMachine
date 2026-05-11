package tecnicoMantenimiento;

public class TecnicoMantenimiento {

    private final int codigo;
    private final String password;

    public TecnicoMantenimiento(int codigo, String password) {
        this.codigo = codigo;
        this.password = password;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getPassword() {
        return password;
    }
}
