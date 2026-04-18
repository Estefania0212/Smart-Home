package com.estef.smarthome;

import javax.swing.*;
import java.awt.*;
import com.estef.smarthome.factory.CommandFactory;
import com.estef.smarthome.commands.Command;
import com.estef.smarthome.state.*;
import com.estef.smarthome.strategy.*;
import com.estef.smarthome.voice.TextToSpeech;

public class MainGUI {
    private JFrame frame;
    private JTextArea log;
    private SerialManager serial;
    private DoorState doorState = new DoorClosed();
    private long lightOnTimestamp = -1;
    private ThresholdStrategy threshold = new FixedMinutesStrategy(1); // 1 min

    private TextToSpeech tts = new TextToSpeech();

    public MainGUI() {
        serial = new SerialManager();
        initUI();
        initSerialListener();
        startLightChecker();

        // Verificar estado del TTS
        if (tts.isAvailable()) {
            append("Sistema de voz inicializado correctamente");
        } else {
            append("Sistema de voz no disponible - usando modo consola");
        }
    }

    private void initUI() {
        frame = new JFrame("SmartHome - Panel de Control");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(780, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        // ---- ENCABEZADO ----
        JLabel title = new JLabel("Smart Home - Sistema de Automatización", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 90, 150));
        frame.add(title, BorderLayout.NORTH);

        // ---- PANEL DE CONEXIÓN ----
        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        top.setBackground(new Color(240, 248, 255));
        top.setBorder(BorderFactory.createTitledBorder("Conexión"));

        String[] ports = SerialManager.listPorts();
        JComboBox<String> cbPorts = new JComboBox<>(ports);
        JButton btnConnect = createButton("Conectar", "icons/connect.png");

        top.add(new JLabel("Puerto disponible:"));
        top.add(cbPorts);
        top.add(btnConnect);

        // ---- PANEL DE CONTROLES ----
        JPanel center = new JPanel(new GridLayout(2, 2, 15, 15));
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createTitledBorder("Controles"));

        JButton bLightOn = createButton("Encender Luz", "icons/light_on.png");
        JButton bLightOff = createButton("Apagar Luz", "icons/light_off.png");
        JButton bDoorOpen = createButton("Abrir Puerta", "icons/door_open.png");
        JButton bDoorClose = createButton("Cerrar Puerta", "icons/door_close.png");

        center.add(bLightOn);
        center.add(bLightOff);
        center.add(bDoorOpen);
        center.add(bDoorClose);

        // ---- PANEL DE REGISTRO (CONSOLA) ----
        log = new JTextArea();
        log.setFont(new Font("Consolas", Font.PLAIN, 13));
        log.setEditable(false);
        log.setBackground(new Color(245, 245, 245));
        log.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JScrollPane scrollPane = new JScrollPane(log);
        scrollPane.setPreferredSize(new Dimension(740, 140));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registro de Actividades"));

        // ---- PANEL DE PIE DE PÁGINA ----
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel footer = new JLabel(
                "<html><center>" +
                        "Proyecto A198 - Patrones de Software<br>" +
                        "Integrantes: Iván Darío Hernández, Estefanía Moreno Reyes, Edson Jaime Pinto Abreo, " +
                        "Daniel José Suárez Gonzales, Jesús David Arias Estupiñán<br>" +
                        "Empresa: Soluciones Digitales Daniel Tomas | Sector: Tecnología - Domótica" +
                        "</center></html>",
                SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(Color.DARK_GRAY);
        footerPanel.add(footer, BorderLayout.CENTER);

        // ---- ENSAMBLE ----
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(top, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        // ---- ACCIONES ----
        btnConnect.addActionListener(e -> {
            String p = (String) cbPorts.getSelectedItem();
            if (p == null) {
                append("No se detectaron puertos disponibles.");
                return;
            }
            if (serial.open(p, 9600)) {
                append("Conexión establecida correctamente con el puerto " + p);
                tts.speak("Sistema conectado");
            } else {
                append("Error: no fue posible conectar con el puerto " + p);
                tts.speak("Error de conexión");
            }
        });

        bLightOn.addActionListener(e -> {
            Command c = CommandFactory.create("LIGHT_ON", serial);
            if (c != null) {
                c.execute();
                lightOnTimestamp = System.currentTimeMillis();
                append("Comando enviado: encender luz.");
                tts.speak("Encendiendo luz");
            }
        });

        bLightOff.addActionListener(e -> {
            Command c = CommandFactory.create("LIGHT_OFF", serial);
            if (c != null) {
                c.execute();
                append("Comando enviado: apagar luz.");
                lightOnTimestamp = -1;
                tts.speak("Apagando luz");
            }
        });

        bDoorOpen.addActionListener(e -> {
            Command c = CommandFactory.create("DOOR_OPEN", serial);
            if (c != null) {
                c.execute();
                append("Comando enviado: abrir puerta.");
                doorState = new DoorOpen();
                append("Estado local puerta: " + doorState.name());
                tts.speak("Abriendo puerta");
            }
        });

        bDoorClose.addActionListener(e -> {
            Command c = CommandFactory.create("DOOR_CLOSE", serial);
            if (c != null) {
                c.execute();
                append("Comando enviado: cerrar puerta.");
                doorState = new DoorClosed();
                append("Estado local puerta: " + doorState.name());
                tts.speak("Cerrando puerta");
            }
        });
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(225, 235, 250));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(190, 200, 230)));
        button.setIcon(loadIcon(iconPath));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        return button;
    }

    private ImageIcon loadIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    private void startLightChecker() {
        Timer t = new Timer(30_000, e -> {
            if (lightOnTimestamp > 0) {
                long elapsed = System.currentTimeMillis() - lightOnTimestamp;
                if (threshold.shouldWarn(elapsed)) {
                    String mensaje = "Aviso: la luz ha permanecido encendida por un tiempo prolongado.";
                    append(mensaje);

                    // AVISO POR VOZ
                    tts.speak("Atención. Luz encendida por mucho tiempo.Por favor, apague la luz . Gracias");

                    JOptionPane.showMessageDialog(
                            frame,
                            mensaje,
                            "Advertencia de Luz Encendida",
                            JOptionPane.WARNING_MESSAGE);
                    lightOnTimestamp = -1;
                }
            }
        });
        t.start();
    }

    private void initSerialListener() {
        serial.addListener(msg -> SwingUtilities.invokeLater(() -> {
            append("Arduino -> " + msg);
            if (msg.startsWith("LIGHT:ON:OK")) {
                append("Confirmado: luz encendida");
                tts.speak("Luz encendida confirmada");
            } else if (msg.startsWith("LIGHT:OFF:OK")) {
                append("Confirmado: luz apagada");
                tts.speak("Luz apagada confirmada");
            } else if (msg.startsWith("DOOR:OPEN:OK")) {
                append("Confirmado: puerta abierta");
                tts.speak("Puerta abierta confirmada");
            } else if (msg.startsWith("DOOR:CLOSE:OK")) {
                append("Confirmado: puerta cerrada");
                tts.speak("Puerta cerrada confirmada");
            } else if (msg.startsWith("ERR:")) {
                append("Error del sistema: " + msg);
                tts.speak("Error en el sistema");
            }
        }));
    }

    private void append(String s) {
        log.append(s + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}