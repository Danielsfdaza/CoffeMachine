package guiInventario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import bodega.Bodega;
import bodega.OrdenBodega;
import bodega.ResultadoAtencion;
import com.zeroc.Ice.Communicator;
import mantenimientoExistencias.Inventario;

public class ControladorInventario implements Runnable {

    private final Inventario inventario;
    private final Bodega bodega;
    private final Communicator communicator;
    private Interfaz interfaz;

    public ControladorInventario(Inventario inventario, Bodega bodega, Communicator communicator) {
        this.inventario = inventario;
        this.bodega = bodega;
        this.communicator = communicator;
    }

    @Override
    public void run() {
        try {
            interfaz = new Interfaz();
            interfaz.setLocationRelativeTo(null);
            interfaz.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            interfaz.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    communicator.shutdown();
                }
            });
            interfaz.setVisible(true);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }

        actualizarVista();
        eventos();
    }

    public void eventos() {
        interfaz.getBtnEjecutarAtencion().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ejecutarAtencion();
            }
        });

        interfaz.getBtnActualizarInventario().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarVista();
            }
        });

        interfaz.getBtnAbastecerMonedas().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inventario.abastecerMonedas();
                escribirResultado("Inventario de monedas abastecido.");
                actualizarVista();
            }
        });

        interfaz.getBtnAbastecerIngredientes().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inventario.abastecerIngredientes();
                escribirResultado("Inventario de ingredientes abastecido.");
                actualizarVista();
            }
        });

        interfaz.getBtnAbastecerSuministros().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inventario.abastecerSuministros();
                escribirResultado("Inventario de suministros abastecido.");
                actualizarVista();
            }
        });
    }

    public void actualizarVista() {
        if (interfaz != null) {
            interfaz.getTextAreaInventario().setText(inventario.consultarResumen());
        }
    }

    private void ejecutarAtencion() {
        int codMaquina = leerEntero(interfaz.getTextFieldCodMaquina().getText());
        int idAlarma = leerEntero(interfaz.getTextFieldIdAlarma().getText());

        if (codMaquina <= 0) {
            escribirResultado("El codigo de maquina debe ser numerico y mayor que cero.");
            return;
        }

        if (idAlarma <= 0) {
            escribirResultado("El ID de alarma debe ser numerico y mayor que cero.");
            return;
        }

        OrdenBodega orden = new OrdenBodega(codMaquina, idAlarma,
                interfaz.getTextFieldUbicacion().getText(),
                interfaz.getTextFieldDescripcion().getText());
        ResultadoAtencion resultado = bodega.atenderOrden(orden);
        escribirResultado(resultado.toDisplayString());
        actualizarVista();
    }

    private int leerEntero(String valor) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void escribirResultado(String mensaje) {
        if (interfaz != null) {
            interfaz.getTextAreaResultado().setText(mensaje);
        }
    }
}
