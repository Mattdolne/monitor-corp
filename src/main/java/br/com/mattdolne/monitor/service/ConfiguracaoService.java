package br.com.mattdolne.monitor.service;

import java.io.*;
import java.time.LocalTime;
import java.util.Properties;

public class ConfiguracaoService {
    
    private static final String FILE_PATH = "config.properties";
    private Properties props = new Properties();

    public ConfiguracaoService() {
        carregarArquivo();
    }

    private void carregarArquivo() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                props.load(in);

            } catch (IOException e) {
                System.err.println("Erro ao carregar configurações: " + e.getMessage());
            }
        }
    }

    public void salvarHorarioExpediente(LocalTime hora) {
        props.setProperty("horario.fim.expediente", hora.toString());
        try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
            props.store(out, "Configurações do Monitor CORP");
        } catch (IOException e) {
            System.err.println("Erro ao salvar configurações: " + e.getMessage());
        }
    }

    public LocalTime carregarHorarioExpediente() {
        String horaStr = props.getProperty("horario.fim.expediente", "18:00");
        return LocalTime.parse(horaStr);
    }

}
