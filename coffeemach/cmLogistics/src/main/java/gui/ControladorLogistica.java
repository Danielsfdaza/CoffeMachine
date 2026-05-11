package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.zeroc.Ice.Communicator;

import controlAlarma.AlarmaAsignada;
import controlAlarma.ControlAlarma;
import controlAlarma.OrdenAtencion;
import servicios.ServicioComLogisticaPrx;
import tecnicoMantenimiento.SesionTecnico;
import tecnicoMantenimiento.TecnicoMantenimiento;
import zonaGeografica.MaquinaAsignada;
import zonaGeografica.ZonaGeografica;

public class ControladorLogistica implements Runnable {

    private final ServicioComLogisticaPrx servicioLogistica;
    private final Communicator communicator;
    private final ControlAlarma controlAlarma;
    private final ZonaGeografica zonaGeografica = new ZonaGeografica();
    private final SesionTecnico sesion = new SesionTecnico();
    private final List<AlarmaAsignada> alarmasActuales = new ArrayList<>();
    private InterfazLogistica interfaz;

    public ControladorLogistica(ServicioComLogisticaPrx servicioLogistica, Communicator communicator) {
        this.servicioLogistica = servicioLogistica;
        this.communicator = communicator;
        this.controlAlarma = new ControlAlarma(servicioLogistica);
    }

    @Override
    public void run() {
        try {
            interfaz = new InterfazLogistica();
            interfaz.setLocationRelativeTo(null);
            interfaz.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            interfaz.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    communicator.shutdown();
                }
            });
            interfaz.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        actualizarVista();
        eventos();
    }

    public void eventos() {
        interfaz.getBtnIniciarSesion().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        interfaz.getBtnRefrescar().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refrescarDatos();
            }
        });

        interfaz.getBtnGenerarOrden().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarOrden();
            }
        });

        interfaz.getBtnCerrarSesion().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
    }

    public void actualizarVista() {
        if (interfaz == null) {
            return;
        }

        if (!sesion.isActiva()) {
            interfaz.getTextAreaMaquinas().setText("");
            interfaz.getTextAreaAlarmas().setText("");
            interfaz.getTextAreaOrden().setText("");
            escribirMensaje("Ingrese codigo y password para iniciar sesion.");
            return;
        }

        actualizarMaquinas();
        actualizarAlarmas();
    }

    private void iniciarSesion() {
        int codigo = leerCodigoTecnico();
        if (codigo <= 0) {
            escribirMensaje("El codigo debe ser numerico y mayor que cero.");
            return;
        }

        String password = new String(interfaz.getPasswordField().getPassword());
        if (password.trim().isEmpty()) {
            escribirMensaje("El password no puede estar vacio.");
            return;
        }

        try {
            boolean loginValido = servicioLogistica.inicioSesion(codigo, password);
            if (!loginValido) {
                escribirMensaje("Credenciales invalidas.");
                return;
            }

            sesion.iniciar(new TecnicoMantenimiento(codigo, password));
            escribirMensaje("Sesion iniciada para tecnico " + codigo + ".");
            refrescarDatos();
        } catch (Exception e) {
            escribirMensaje("No fue posible iniciar sesion: " + e.getMessage());
        }
    }

    private void refrescarDatos() {
        if (!sesion.isActiva()) {
            escribirMensaje("Debe iniciar sesion antes de consultar.");
            return;
        }

        try {
            List<String> asignaciones = servicioLogistica.asignacionMaquina(sesion.getCodigoTecnico());
            sesion.setMaquinasAsignadas(zonaGeografica.convertirAsignaciones(asignaciones));

            alarmasActuales.clear();
            alarmasActuales.addAll(controlAlarma.consultarAlarmas(sesion.getCodigoTecnico()));

            actualizarVista();
            escribirMensaje("Informacion actualizada.");
        } catch (Exception e) {
            escribirMensaje("No fue posible consultar el servidor central: " + e.getMessage());
        }
    }

    private void actualizarMaquinas() {
        StringBuilder texto = new StringBuilder();
        List<MaquinaAsignada> maquinas = sesion.getMaquinasAsignadas();
        if (maquinas.isEmpty()) {
            texto.append("Sin maquinas asignadas");
        } else {
            for (MaquinaAsignada maquina : maquinas) {
                texto.append(maquina.toDisplayString()).append("\n");
            }
        }
        interfaz.getTextAreaMaquinas().setText(texto.toString());
    }

    private void actualizarAlarmas() {
        StringBuilder texto = new StringBuilder();
        if (alarmasActuales.isEmpty()) {
            texto.append("Sin alarmas asignadas");
        } else {
            for (int i = 0; i < alarmasActuales.size(); i++) {
                texto.append(alarmasActuales.get(i).toDisplayString(i + 1)).append("\n");
            }
        }
        interfaz.getTextAreaAlarmas().setText(texto.toString());
    }

    private void generarOrden() {
        if (!sesion.isActiva()) {
            escribirMensaje("Debe iniciar sesion antes de generar orden.");
            return;
        }

        int seleccion = leerSeleccionAlarma();
        if (seleccion <= 0 || seleccion > alarmasActuales.size()) {
            escribirMensaje("Seleccione un numero de alarma valido.");
            return;
        }

        AlarmaAsignada alarma = alarmasActuales.get(seleccion - 1);
        if (!alarma.isValida()) {
            escribirMensaje("La alarma seleccionada no tiene un formato valido.");
            return;
        }

        OrdenAtencion orden = controlAlarma.crearOrden(alarma);
        if (orden == null) {
            escribirMensaje("No fue posible generar la orden.");
            return;
        }

        interfaz.getTextAreaOrden().setText(orden.toDisplayString());
        escribirMensaje("Orden generada. Entregar estos datos a bodega.");
    }

    private void cerrarSesion() {
        sesion.cerrar();
        alarmasActuales.clear();
        interfaz.getTextFieldCodigoTecnico().setText("");
        interfaz.getPasswordField().setText("");
        interfaz.getTextFieldSeleccionAlarma().setText("");
        actualizarVista();
    }

    private int leerCodigoTecnico() {
        try {
            return Integer.parseInt(interfaz.getTextFieldCodigoTecnico().getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int leerSeleccionAlarma() {
        try {
            return Integer.parseInt(interfaz.getTextFieldSeleccionAlarma().getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void escribirMensaje(String mensaje) {
        if (interfaz != null) {
            interfaz.getTextAreaMensajes().setText(mensaje);
        }
    }
}
