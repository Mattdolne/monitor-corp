package br.com.mattdolne.monitor;

import br.com.mattdolne.monitor.model.MaquinaInfo;
import br.com.mattdolne.monitor.service.ColetorHardware;

public class Main {
    
    public static void main(String[] args) {
        
        // 1. Instanciamos o nosso coletor
        ColetorHardware coletor = new ColetorHardware();
        
        // 2. Pedimos para ele coletar os dados e guardar na "caixa" (minhaMaquina)
        MaquinaInfo minhaMaquina = coletor.coletarDadosAtuais();

        // 3. Imprimimos no console
        System.out.println("=== TESTE DO COLETOR ===");
        System.out.println("Hostname: " + minhaMaquina.getHostname());
        System.out.println("Memória RAM (GB): " + minhaMaquina.getMemoriaRamGB());
        System.out.println("SSD Instalado: " + minhaMaquina.getDiscoSSD());
        System.out.println("Espaço Livre (%): " + minhaMaquina.getEspacoDiscoLivrePorcentagem());
        System.out.println("Endereço IP ataual: " + minhaMaquina.getIP());
        System.out.println("Endereço MAC: " + minhaMaquina.getMacAddress());
        System.out.println("Domínio da Máquina: " + minhaMaquina.getDominio());
        System.out.println("Data da última formatação: " + minhaMaquina.getDataInstalacaoOS());
    }
}