import java.util.*;
import com.zeroc.Ice.*;
import gui.ControladorLogistica;
import servicios.ServicioComLogisticaPrx;

public class CmLogistics {
    public static void main(String[] args) {
        List<String> extArgs = new ArrayList<>();
        try (Communicator communicator = Util.initialize(args, "CmLogistic.cfg", extArgs)) {
            ServicioComLogisticaPrx serverCentral = null;

            try {
                ObjectPrx proxy = communicator.propertyToProxy("ServerCentral");
                serverCentral = ServicioComLogisticaPrx.checkedCast(proxy);
                if (serverCentral != null) {
                    serverCentral = serverCentral.ice_twoway();
                }
            } catch (java.lang.Exception e) {
                System.out.println("No fue posible conectar con ServerCentral: " + e.getMessage());
                return;
            }

            if (serverCentral == null) {
                System.out.println("No fue posible conectar con ServerCentral.");
                return;
            }

            ControladorLogistica controlador = new ControladorLogistica(serverCentral, communicator);
            controlador.run();
            communicator.waitForShutdown();
        }
    }
}
