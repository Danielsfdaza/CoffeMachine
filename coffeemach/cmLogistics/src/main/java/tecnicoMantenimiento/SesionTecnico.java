package tecnicoMantenimiento;

import java.util.ArrayList;
import java.util.List;

import zonaGeografica.MaquinaAsignada;

public class SesionTecnico {

    private TecnicoMantenimiento tecnico;
    private boolean activa;
    private List<MaquinaAsignada> maquinasAsignadas = new ArrayList<>();

    public void iniciar(TecnicoMantenimiento tecnico) {
        this.tecnico = tecnico;
        this.activa = true;
    }

    public void cerrar() {
        this.tecnico = null;
        this.activa = false;
        this.maquinasAsignadas.clear();
    }

    public boolean isActiva() {
        return activa;
    }

    public TecnicoMantenimiento getTecnico() {
        return tecnico;
    }

    public int getCodigoTecnico() {
        if (tecnico == null) {
            return 0;
        }
        return tecnico.getCodigo();
    }

    public List<MaquinaAsignada> getMaquinasAsignadas() {
        return maquinasAsignadas;
    }

    public void setMaquinasAsignadas(List<MaquinaAsignada> maquinasAsignadas) {
        this.maquinasAsignadas = maquinasAsignadas;
    }
}
