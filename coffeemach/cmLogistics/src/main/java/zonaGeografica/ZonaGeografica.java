package zonaGeografica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ZonaGeografica {

    public List<MaquinaAsignada> convertirAsignaciones(List<String> registros) {
        List<MaquinaAsignada> maquinas = new ArrayList<>();
        if (registros == null) {
            return maquinas;
        }

        for (String registro : registros) {
            maquinas.add(MaquinaAsignada.desdeServidor(registro));
        }

        maquinas.sort(new Comparator<MaquinaAsignada>() {
            @Override
            public int compare(MaquinaAsignada a, MaquinaAsignada b) {
                return a.toDisplayString().compareToIgnoreCase(b.toDisplayString());
            }
        });
        return maquinas;
    }
}
