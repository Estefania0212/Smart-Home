package com.estef.smarthome.voice;

import java.io.IOException;

public class TextToSpeech {
    private boolean available = false;
    private String voiceCommand;

    public TextToSpeech() {
        checkAvailability();
    }

    private void checkAvailability() {
        // Verificar diferentes comandos de voz según el SO
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows
            voiceCommand = "espeak";
            available = checkCommand(voiceCommand);
            if (!available) {
                // Intentar con PowerShell
                voiceCommand = "powershell -Command \"Add-Type -AssemblyName System.Speech; (New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('test')\"";
                available = checkCommand("powershell");
            }
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // Linux/Unix/macOS
            voiceCommand = "espeak";
            available = checkCommand(voiceCommand);
            if (!available) {
                voiceCommand = "say";
                available = checkCommand(voiceCommand);
            }
        }

        System.out.println("TTS disponible: " + available + ", comando: " + voiceCommand);
    }

    private boolean checkCommand(String cmd) {
        try {
            Process process;
            if (cmd.equals("espeak")) {
                process = Runtime.getRuntime().exec(new String[] { "espeak", "--version" });
            } else if (cmd.equals("say")) {
                process = Runtime.getRuntime().exec(new String[] { "say", "--version" });
            } else if (cmd.equals("powershell")) {
                process = Runtime.getRuntime().exec(new String[] { "powershell", "-Command", "exit" });
            } else {
                return false;
            }
            int exitCode = process.waitFor();
            return (exitCode == 0 || exitCode == 1); // Algunos comandos retornan 1 pero funcionan
        } catch (Exception e) {
            return false;
        }
    }

    public void speak(String text) {
        if (!available || text == null || text.trim().isEmpty()) {
            System.out.println("TTS (fallback): " + text);
            return;
        }

        try {
            String[] cmd;
            if (voiceCommand.equals("espeak")) {
                cmd = new String[] { "espeak", "-v", "es", "-s", "150", text };
            } else if (voiceCommand.equals("say")) {
                cmd = new String[] { "say", text };
            } else if (voiceCommand.startsWith("powershell")) {
                // Usar PowerShell en Windows
                String psCommand = String.format(
                        "Add-Type -AssemblyName System.Speech; $speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; $speak.Speak(\\\"%s\\\")",
                        text.replace("\"", "\\\""));
                cmd = new String[] { "powershell", "-Command", psCommand };
            } else {
                System.out.println("TTS (fallback): " + text);
                return;
            }

            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.start();
        } catch (IOException e) {
            System.err.println("Error en TTS: " + e.getMessage());
            System.out.println("TTS (fallback): " + text);
        }
    }

    public boolean isAvailable() {
        return available;
    }
}