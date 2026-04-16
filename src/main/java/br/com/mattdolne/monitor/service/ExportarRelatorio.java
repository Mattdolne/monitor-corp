package br.com.mattdolne.monitor.service;

import br.com.mattdolne.monitor.model.MaquinaInfo;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class ExportarRelatorio {

    public void exportarTxt(MaquinaInfo info, String caminho) throws IOException {
        try (PrintWriter out = new PrintWriter(new PrintWriter(caminho + ".txt"))) {
            out.println("=== RELATÓRIO DE ESTAÇÃO DE TRABALHO ===");
            out.println("Hostname: " + info.getHostname());
            out.println("Domínio: " + info.getDominio());
            out.println("Sistema Operacional: " + info.getSistemaOperacional());
            out.println("Endereço IP: " + info.getIP());
            out.println("Endereço MAC: " + info.getMacAddress());
            out.println("Armazenamento Livre: " + String.format("%.1f%%", info.getEspacoDiscoLivrePorcentagem()));
            out.println("Memória RAM: " + String.format("%.2f GB", info.getMemoriaRamGB()));
        }
    }

    public void exportarCsv(MaquinaInfo info, String caminho) throws IOException {
        try (PrintWriter out = new PrintWriter(new PrintWriter(caminho + ".csv"))) {
            out.println("Hostname;Domínio;Sistema Operacional;Endereço IP;Endereço MAC;Armazenamento Livre;Memória RAM");
            out.println(info.getHostname() + ";" + info.getDominio() + ";" + info.getSistemaOperacional() + 
                        ";" + info.getIP() + ";" + info.getMacAddress() + ";" + info.getEspacoDiscoLivrePorcentagem() + ";" + info.getMemoriaRamGB()); 
        }
    }

    public void exportarJson(MaquinaInfo info, String caminho) throws IOException {
        String json = "{\n" +
                "  \"hostname\": \"" + info.getHostname() + "\",\n" +
                "  \"ip\": \"" + info.getIP() + "\",\n" +
                "  \"ram_gb\": " + info.getMemoriaRamGB() + "\n" +
                "}";
       try (FileWriter file = new FileWriter(caminho + "json")) {
            file.write(json);
        }
    }
    
}
