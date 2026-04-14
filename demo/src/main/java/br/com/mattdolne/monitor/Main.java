package br.com.mattdolne.monitor;

import br.com.mattdolne.monitor.model.MaquinaInfo;
import br.com.mattdolne.monitor.service.ColetorHardware;
import br.com.mattdolne.monitor.service.RegrasConformidade;

import java.util.List;

public class Main {
    
    public static void main(String[] args) {

        System.out.println("Iniciando coleta de dados da máquina...");
        
        // 1. Instanciamos o nosso coletor
        ColetorHardware coletor = new ColetorHardware();
        
        // 2. Pedimos para ele coletar os dados e guardar na "caixa" (minhaMaquina)
        MaquinaInfo minhaMaquina = coletor.coletarDadosAtuais();

        // 3. Imprimimos no console
        System.out.println("=== RELATÓRIO TÉCNICO ===");
        System.out.println("Hostname: " + minhaMaquina.getHostname());
        System.out.println("Memória RAM (GB): " + String.format("%.2f", minhaMaquina.getMemoriaRamGB()) + " GB");
        System.out.println("SSD Instalado: " + minhaMaquina.getDiscoSSD());
        System.out.println("Espaço Livre (%): " + String.format("%.1f", minhaMaquina.getEspacoDiscoLivrePorcentagem()) + "%");
        System.out.println("Endereço IP ataual: " + minhaMaquina.getIP());
        System.out.println("Endereço MAC: " + minhaMaquina.getMacAddress());
        System.out.println("Domínio da Máquina: " + minhaMaquina.getDominio());
        System.out.println("Data da última formatação: " + minhaMaquina.getDataInstalacaoOS());
        System.out.println("Tempo desde a última inicialização: " + minhaMaquina.getUptimeHoras() + " horas");

        // 4. Auditoria
        System.out.println("\n==== AUDITORIA DE CONFORMIDADE ===");
        RegrasConformidade auditor = new RegrasConformidade();

        // Passa a máquina para o auditor e ele nos devolve a lista de alertas
        List<String> alertas = auditor.verificarAlertas(minhaMaquina);

        // Se a lista estiver vazia, não há alertas
        if (alertas.isEmpty()) {
            System.out.println("OK! Máquina 100% dentro dos padrões de integridade");
        } else {
            // se houver alertas...
            System.out.println("Atenção! Foram encontrados " + alertas.size() + " problema(s):");
            for (String alerta : alertas) {
                System.out.println(" -> " + alerta);
            }
        }


    }
}