package bodega;

import mantenimientoExistencias.Inventario;
import servicios.ServicioAbastecimientoPrx;

public class BodegaCentralService implements Bodega {

    private final Inventario inventario;
    private final ServicioAbastecimientoPrx maquinaCafe;

    public BodegaCentralService(Inventario inventario, ServicioAbastecimientoPrx maquinaCafe) {
        this.inventario = inventario;
        this.maquinaCafe = maquinaCafe;
    }

    @Override
    public void consultarMonedas() {
        System.out.println(inventario.consultarResumen());
    }

    @Override
    public void consultarIngredientes() {
        System.out.println(inventario.consultarResumen());
    }

    @Override
    public void consultarSuministros() {
        System.out.println(inventario.consultarResumen());
    }

    @Override
    public void entregaKitReparacion() {
        inventario.descontarExistencias(recursoParaAlarma(1), 1);
    }

    @Override
    public void retirarExistencias() {
        System.out.println("Use atenderOrden para retirar existencias de una alarma concreta.");
    }

    @Override
    public void abastecerExistencia() {
        System.out.println("Use atenderOrden para abastecer una maquina concreta.");
    }

    @Override
    public void separarExistencias() {
        System.out.println("Use atenderOrden para separar existencias de una orden concreta.");
    }

    @Override
    public ResultadoAtencion atenderOrden(OrdenBodega orden) {
        if (orden == null || orden.getCodigoMaquina() <= 0) {
            return new ResultadoAtencion(false, "Orden invalida.", "N/A", 0);
        }

        if (!soportaAlarma(orden.getIdAlarma())) {
            return new ResultadoAtencion(false, "ID de alarma no soportado.", "N/A", 0);
        }

        String recurso = recursoParaAlarma(orden.getIdAlarma());
        int cantidad = inventario.cantidadRequeridaParaAlarma(orden.getIdAlarma());

        if (!inventario.tieneExistencias(recurso, cantidad)) {
            return new ResultadoAtencion(false, "No hay existencias suficientes.", recurso, cantidad);
        }

        if (maquinaCafe != null) {
            try {
                maquinaCafe.abastecer(orden.getCodigoMaquina(), orden.getIdAlarma());
            } catch (java.lang.Exception e) {
                return new ResultadoAtencion(false,
                        "La maquina no respondio. Orden queda pendiente: " + e.getMessage(),
                        recurso, cantidad);
            }
        }

        if (!inventario.descontarExistencias(recurso, cantidad)) {
            return new ResultadoAtencion(false, "No fue posible descontar inventario.", recurso, cantidad);
        }

        String mensaje = maquinaCafe == null
                ? "Existencias separadas. Maquina no conectada."
                : "Abastecimiento enviado a la maquina.";
        return new ResultadoAtencion(true, mensaje, recurso, cantidad);
    }

    @Override
    public String recursoParaAlarma(int idAlarma) {
        return inventario.recursoParaAlarma(idAlarma);
    }

    @Override
    public boolean soportaAlarma(int idAlarma) {
        return recursoParaAlarma(idAlarma) != null;
    }
}
