package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class InterfazLogistica extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldCodigoTecnico;
    private JPasswordField passwordField;
    private JTextField textFieldSeleccionAlarma;
    private JButton btnIniciarSesion;
    private JButton btnRefrescar;
    private JButton btnGenerarOrden;
    private JButton btnCerrarSesion;
    private JTextArea textAreaMaquinas;
    private JTextArea textAreaAlarmas;
    private JTextArea textAreaOrden;
    private JTextArea textAreaMensajes;

    public InterfazLogistica() {
        setTitle("Logistica de Mantenimiento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 520);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Logistica de Mantenimiento");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(10, 5, 864, 20);
        contentPane.add(lblTitulo);

        JPanel panelSesion = new JPanel();
        panelSesion.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panelSesion.setBounds(10, 35, 864, 72);
        panelSesion.setLayout(null);
        contentPane.add(panelSesion);

        JLabel lblCodigo = new JLabel("Codigo");
        lblCodigo.setBounds(10, 12, 70, 14);
        panelSesion.add(lblCodigo);

        textFieldCodigoTecnico = new JTextField();
        textFieldCodigoTecnico.setBounds(80, 9, 120, 20);
        panelSesion.add(textFieldCodigoTecnico);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(220, 12, 80, 14);
        panelSesion.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(300, 9, 130, 20);
        panelSesion.add(passwordField);

        btnIniciarSesion = new JButton("Iniciar sesion");
        btnIniciarSesion.setBounds(450, 8, 130, 23);
        panelSesion.add(btnIniciarSesion);

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBounds(590, 8, 120, 23);
        panelSesion.add(btnRefrescar);

        btnCerrarSesion = new JButton("Cerrar sesion");
        btnCerrarSesion.setBounds(720, 8, 130, 23);
        panelSesion.add(btnCerrarSesion);

        JLabel lblSeleccion = new JLabel("Alarma #");
        lblSeleccion.setBounds(10, 43, 70, 14);
        panelSesion.add(lblSeleccion);

        textFieldSeleccionAlarma = new JTextField();
        textFieldSeleccionAlarma.setBounds(80, 40, 70, 20);
        panelSesion.add(textFieldSeleccionAlarma);

        btnGenerarOrden = new JButton("Generar orden");
        btnGenerarOrden.setBounds(165, 39, 140, 23);
        panelSesion.add(btnGenerarOrden);

        JPanel panelMaquinas = new JPanel();
        panelMaquinas.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panelMaquinas.setBounds(10, 118, 300, 220);
        panelMaquinas.setLayout(null);
        contentPane.add(panelMaquinas);

        JLabel lblMaquinas = new JLabel("Maquinas asignadas");
        lblMaquinas.setHorizontalAlignment(SwingConstants.CENTER);
        lblMaquinas.setBounds(10, 8, 280, 14);
        panelMaquinas.add(lblMaquinas);

        JScrollPane scrollMaquinas = new JScrollPane();
        scrollMaquinas.setBounds(10, 30, 280, 180);
        panelMaquinas.add(scrollMaquinas);

        textAreaMaquinas = new JTextArea();
        textAreaMaquinas.setEditable(false);
        scrollMaquinas.setViewportView(textAreaMaquinas);

        JPanel panelAlarmas = new JPanel();
        panelAlarmas.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panelAlarmas.setBounds(320, 118, 554, 220);
        panelAlarmas.setLayout(null);
        contentPane.add(panelAlarmas);

        JLabel lblAlarmas = new JLabel("Alarmas activas");
        lblAlarmas.setHorizontalAlignment(SwingConstants.CENTER);
        lblAlarmas.setBounds(10, 8, 534, 14);
        panelAlarmas.add(lblAlarmas);

        JScrollPane scrollAlarmas = new JScrollPane();
        scrollAlarmas.setBounds(10, 30, 534, 180);
        panelAlarmas.add(scrollAlarmas);

        textAreaAlarmas = new JTextArea();
        textAreaAlarmas.setEditable(false);
        scrollAlarmas.setViewportView(textAreaAlarmas);

        JPanel panelOrden = new JPanel();
        panelOrden.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panelOrden.setBounds(10, 350, 430, 120);
        panelOrden.setLayout(null);
        contentPane.add(panelOrden);

        JLabel lblOrden = new JLabel("Orden de atencion");
        lblOrden.setHorizontalAlignment(SwingConstants.CENTER);
        lblOrden.setBounds(10, 8, 410, 14);
        panelOrden.add(lblOrden);

        JScrollPane scrollOrden = new JScrollPane();
        scrollOrden.setBounds(10, 30, 410, 80);
        panelOrden.add(scrollOrden);

        textAreaOrden = new JTextArea();
        textAreaOrden.setEditable(false);
        scrollOrden.setViewportView(textAreaOrden);

        JPanel panelMensajes = new JPanel();
        panelMensajes.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panelMensajes.setBounds(450, 350, 424, 120);
        panelMensajes.setLayout(null);
        contentPane.add(panelMensajes);

        JLabel lblMensajes = new JLabel("Mensajes");
        lblMensajes.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensajes.setBounds(10, 8, 404, 14);
        panelMensajes.add(lblMensajes);

        JScrollPane scrollMensajes = new JScrollPane();
        scrollMensajes.setBounds(10, 30, 404, 80);
        panelMensajes.add(scrollMensajes);

        textAreaMensajes = new JTextArea();
        textAreaMensajes.setEditable(false);
        scrollMensajes.setViewportView(textAreaMensajes);
    }

    public JTextField getTextFieldCodigoTecnico() {
        return textFieldCodigoTecnico;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JTextField getTextFieldSeleccionAlarma() {
        return textFieldSeleccionAlarma;
    }

    public JButton getBtnIniciarSesion() {
        return btnIniciarSesion;
    }

    public JButton getBtnRefrescar() {
        return btnRefrescar;
    }

    public JButton getBtnGenerarOrden() {
        return btnGenerarOrden;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

    public JTextArea getTextAreaMaquinas() {
        return textAreaMaquinas;
    }

    public JTextArea getTextAreaAlarmas() {
        return textAreaAlarmas;
    }

    public JTextArea getTextAreaOrden() {
        return textAreaOrden;
    }

    public JTextArea getTextAreaMensajes() {
        return textAreaMensajes;
    }
}
