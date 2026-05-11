package controlAlarma;

import java.util.List;

import servicios.ServicioComLogisticaPrx;

public class ControlAlarma {

    private final ServicioComLogisticaPrx servicioLogistica;
    private final ParserAlarmaAsignada parser = new ParserAlarmaAsignada();

    public ControlAlarma(ServicioComLogisticaPrx servicioLogistica) {
        this.servicioLogistica = servicioLogistica;
    }

    public List<AlarmaAsignada> consultarAlarmas(int codigoTecnico) {
        List<String> registros = servicioLogistica.asignacionMaquinasDesabastecidas(codigoTecnico);
        return parser.parsear(registros);
    }

    public OrdenAtencion crearOrden(AlarmaAsignada alarma) {
        if (alarma == null || !alarma.isValida()) {
            return null;
        }
        return new OrdenAtencion(alarma);
    }

    public static String recursoParaAlarma(int idAlarma) {
        switch (idAlarma) {
            case 1:
                return "Kit de reparacion";
            case 2:
                return "Monedas de 100";
            case 4:
                return "Monedas de 200";
            case 6:
                return "Monedas de 500";
            case 8:
                return "Agua";
            case 9:
                return "Cafe";
            case 10:
                return "Azucar";
            case 11:
                return "Vaso";
            default:
                return "Tipo desconocido";
        }
    }
}
