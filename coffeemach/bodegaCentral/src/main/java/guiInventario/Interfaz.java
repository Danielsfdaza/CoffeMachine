package guiInventario;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class Interfaz extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldCodMaquina;
    private JTextField textFieldIdAlarma;
    private JTextField textFieldUbicacion;
    private JTextField textFieldDescripcion;
    private JButton btnEjecutarAtencion;
    private JButton btnActualizarInventario;
    private JButton btnAbastecerMonedas;
    private JButton btnAbastecerIngredientes;
    private JButton btnAbastecerSuministros;
    private JTextArea textAreaInventario;
    private JTextArea textAreaResultado;

    public Interfaz() {
        setTitle("Bodega Central");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 820, 480);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Bodega Central");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(10, 5, 784, 20);
        contentPane.add(lblTitulo);

        JPanel panelOrden = new JPanel();
        panelOrden.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panelOrden.setLayout(null);
        panelOrden.setBounds(10, 35, 784, 100);
        contentPane.add(panelOrden);

        JLabel lblCodMaquina = new JLabel("Maquina");
        lblCodMaquina.setBounds(10, 12, 80, 14);
        panelOrden.add(lblCodMaquina);

        textFieldCodMaquina = new JTextField();
        textFieldCodMaquina.setBounds(90, 9, 80, 20);
        panelOrden.add(textFieldCodMaquina);

        JLabel lblIdAlarma = new JLabel("Alarma");
        lblIdAlarma.setBounds(190, 12, 80, 14);
        panelOrden.add(lblIdAlarma);

        textFieldIdAlarma = new JTextField();
        textFieldIdAlarma.setBounds(260, 9, 80, 20);
        panelOrden.add(textFieldIdAlarma);

        JLabel lblUbicacion = new JLabel("Ubicacion");
        lblUbicacion.setBounds(360, 12, 80, 14);
        panelOrden.add(lblUbicacion);

        textFieldUbicacion = new JTextField();
        textFieldUbicacion.setBounds(440, 9, 330, 20);
        panelOrden.add(textFieldUbicacion);

        JLabel lblDescripcion = new JLabel("Descripcion");
        lblDescripcion.setBounds(10, 44, 90, 14);
        panelOrden.add(lblDescripcion);

        textFieldDescripcion = new JTextField();
        textFieldDescripcion.setBounds(100, 41, 450, 20);
        panelOrden.add(textFieldDescripcion);

        btnEjecutarAtencion = new JButton("Ejecutar atencion");
        btnEjecutarAtencion.setBounds(570, 40, 200, 23);
        panelOrden.add(btnEjecutarAtencion);

        JPanel panelInventario = new JPanel();
        panelInventario.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panelInventario.setLayout(null);
        panelInventario.setBounds(10, 145, 380, 285);
        contentPane.add(panelInventario);

        JLabel lblInventario = new JLabel("Inventario");
        lblInventario.setHorizontalAlignment(SwingConstants.CENTER);
        lblInventario.setBounds(10, 8, 360, 14);
        panelInventario.add(lblInventario);

        JScrollPane scrollInventario = new JScrollPane();
        scrollInventario.setBounds(10, 30, 360, 180);
        panelInventario.add(scrollInventario);

        textAreaInventario = new JTextArea();
        textAreaInventario.setEditable(false);
        scrollInventario.setViewportView(textAreaInventario);

        btnActualizarInventario = new JButton("Actualizar");
        btnActualizarInventario.setBounds(10, 220, 110, 23);
        panelInventario.add(btnActualizarInventario);

        btnAbastecerMonedas = new JButton("Monedas");
        btnAbastecerMonedas.setBounds(130, 220, 110, 23);
        panelInventario.add(btnAbastecerMonedas);

        btnAbastecerIngredientes = new JButton("Ingredientes");
        btnAbastecerIngredientes.setBounds(250, 220, 120, 23);
        panelInventario.add(btnAbastecerIngredientes);

        btnAbastecerSuministros = new JButton("Suministros");
        btnAbastecerSuministros.setBounds(130, 250, 140, 23);
        panelInventario.add(btnAbastecerSuministros);

        JPanel panelResultado = new JPanel();
        panelResultado.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panelResultado.setLayout(null);
        panelResultado.setBounds(400, 145, 394, 285);
        contentPane.add(panelResultado);

        JLabel lblResultado = new JLabel("Resultado");
        lblResultado.setHorizontalAlignment(SwingConstants.CENTER);
        lblResultado.setBounds(10, 8, 374, 14);
        panelResultado.add(lblResultado);

        JScrollPane scrollResultado = new JScrollPane();
        scrollResultado.setBounds(10, 30, 374, 245);
        panelResultado.add(scrollResultado);

        textAreaResultado = new JTextArea();
        textAreaResultado.setEditable(false);
        scrollResultado.setViewportView(textAreaResultado);
    }

    public JTextField getTextFieldCodMaquina() {
        return textFieldCodMaquina;
    }

    public JTextField getTextFieldIdAlarma() {
        return textFieldIdAlarma;
    }

    public JTextField getTextFieldUbicacion() {
        return textFieldUbicacion;
    }

    public JTextField getTextFieldDescripcion() {
        return textFieldDescripcion;
    }

    public JButton getBtnEjecutarAtencion() {
        return btnEjecutarAtencion;
    }

    public JButton getBtnActualizarInventario() {
        return btnActualizarInventario;
    }

    public JButton getBtnAbastecerMonedas() {
        return btnAbastecerMonedas;
    }

    public JButton getBtnAbastecerIngredientes() {
        return btnAbastecerIngredientes;
    }

    public JButton getBtnAbastecerSuministros() {
        return btnAbastecerSuministros;
    }

    public JTextArea getTextAreaInventario() {
        return textAreaInventario;
    }

    public JTextArea getTextAreaResultado() {
        return textAreaResultado;
    }
}
