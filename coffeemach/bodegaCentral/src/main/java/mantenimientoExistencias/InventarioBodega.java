package mantenimientoExistencias;

import java.util.LinkedHashMap;
import java.util.Map;

public class InventarioBodega implements Inventario {

    private final Map<String, Existencia> existencias = new LinkedHashMap<String, Existencia>();

    public InventarioBodega() {
        existencias.put(TipoRecurso.MONEDAS_100, new Existencia(TipoRecurso.MONEDAS_100, 200));
        existencias.put(TipoRecurso.MONEDAS_200, new Existencia(TipoRecurso.MONEDAS_200, 200));
        existencias.put(TipoRecurso.MONEDAS_500, new Existencia(TipoRecurso.MONEDAS_500, 200));
        existencias.put(TipoRecurso.AGUA, new Existencia(TipoRecurso.AGUA, 20));
        existencias.put(TipoRecurso.CAFE, new Existencia(TipoRecurso.CAFE, 20));
        existencias.put(TipoRecurso.AZUCAR, new Existencia(TipoRecurso.AZUCAR, 20));
        existencias.put(TipoRecurso.VASO, new Existencia(TipoRecurso.VASO, 100));
        existencias.put(TipoRecurso.KIT_REPARACION, new Existencia(TipoRecurso.KIT_REPARACION, 10));
    }

    @Override
    public void abastecerSuministros() {
        abastecer(TipoRecurso.KIT_REPARACION, 5);
    }

    @Override
    public void abastecerMonedas() {
        abastecer(TipoRecurso.MONEDAS_100, 100);
        abastecer(TipoRecurso.MONEDAS_200, 100);
        abastecer(TipoRecurso.MONEDAS_500, 100);
    }

    @Override
    public void abastecerIngredientes() {
        abastecer(TipoRecurso.AGUA, 10);
        abastecer(TipoRecurso.CAFE, 10);
        abastecer(TipoRecurso.AZUCAR, 10);
        abastecer(TipoRecurso.VASO, 50);
    }

    @Override
    public boolean tieneExistencias(String recurso, int cantidad) {
        Existencia existencia = existencias.get(recurso);
        return existencia != null && existencia.getCantidad() >= cantidad;
    }

    @Override
    public boolean descontarExistencias(String recurso, int cantidad) {
        Existencia existencia = existencias.get(recurso);
        return existencia != null && existencia.descontar(cantidad);
    }

    @Override
    public String consultarResumen() {
        StringBuilder resumen = new StringBuilder();
        for (Existencia existencia : existencias.values()) {
            resumen.append(existencia.getRecurso())
                    .append(": ")
                    .append(existencia.getCantidad())
                    .append("\n");
        }
        return resumen.toString();
    }

    @Override
    public String recursoParaAlarma(int idAlarma) {
        switch (idAlarma) {
            case 1:
                return TipoRecurso.KIT_REPARACION;
            case 2:
                return TipoRecurso.MONEDAS_100;
            case 4:
                return TipoRecurso.MONEDAS_200;
            case 6:
                return TipoRecurso.MONEDAS_500;
            case 8:
                return TipoRecurso.AGUA;
            case 9:
                return TipoRecurso.CAFE;
            case 10:
                return TipoRecurso.AZUCAR;
            case 11:
                return TipoRecurso.VASO;
            default:
                return null;
        }
    }

    @Override
    public int cantidadRequeridaParaAlarma(int idAlarma) {
        switch (idAlarma) {
            case 2:
            case 4:
            case 6:
                return 20;
            case 11:
                return 50;
            case 1:
            case 8:
            case 9:
            case 10:
                return 1;
            default:
                return 0;
        }
    }

    private void abastecer(String recurso, int cantidad) {
        Existencia existencia = existencias.get(recurso);
        if (existencia != null) {
            existencia.abastecer(cantidad);
        }
    }
}
