import java.util.ArrayList;
import java.util.List;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import bodega.Bodega;
import bodega.BodegaCentralService;
import guiInventario.ControladorInventario;
import mantenimientoExistencias.Inventario;
import mantenimientoExistencias.InventarioBodega;
import servicios.ServicioAbastecimientoPrx;

public class BodegaCentral {

    public static void main(String[] args) {
        List<String> extArgs = new ArrayList<String>();
        try (Communicator communicator = Util.initialize(args, "BodegaCentral.cfg", extArgs)) {
            ServicioAbastecimientoPrx maquinaCafe = crearProxyMaquina(communicator);
            Inventario inventario = new InventarioBodega();
            Bodega bodega = new BodegaCentralService(inventario, maquinaCafe);
            ControladorInventario controlador = new ControladorInventario(inventario, bodega, communicator);

            controlador.run();
            communicator.waitForShutdown();
        }
    }

    private static ServicioAbastecimientoPrx crearProxyMaquina(Communicator communicator) {
        try {
            ObjectPrx proxy = communicator.propertyToProxy("MaquinaCafe");
            ServicioAbastecimientoPrx maquinaCafe = ServicioAbastecimientoPrx.checkedCast(proxy);
            if (maquinaCafe != null) {
                return maquinaCafe.ice_twoway();
            }
        } catch (java.lang.Exception e) {
            System.out.println("MaquinaCafe no disponible: " + e.getMessage());
        }
        return null;
    }
}
