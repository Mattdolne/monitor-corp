package br.com.mattdolne.monitor.service;

import br.com.mattdolne.monitor.model.MaquinaInfo;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ColetorHardware {

    // Instanciar o motor principal do OSHI
    private final SystemInfo systemInfo;
    private final HardwareAbstractionLayer hardware;
    private final OperatingSystem os;

    public ColetorHardware() {
        this.systemInfo = new SystemInfo();
        this.hardware = systemInfo.getHardware();
        this.os = systemInfo.getOperatingSystem();
    }

    /**
     * O próximo método cria uma caixa (MaquinaInfo) vazia,
     * preenche com os dados reais do PC e devolve a caixa cheia.
     */

    public MaquinaInfo coletarDadosAtuais() {
        MaquinaInfo info = new MaquinaInfo();
    
        // 1. Coletar informações básicas com Java Nativo
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            info.setHostname(localHost.getHostName());

        } catch (UnknownHostException e) {
            info.setHostname("Desconhecido");
        }

        // 2. Coletar Sistema Operacional com OSHI
        // Pega a família (Windows) e a versão (11)
        String nomeOS = os.getFamily() + " " + os.getVersionInfo().getVersion();
        info.setSistemaOperacional(nomeOS);

        // 3. Coletar Memória com OSHI
        long ramBytes = hardware.getMemory().getTotal();
        //OSHI devolve em Bytes - para transformar em GB:
        double ramGB = ramBytes / (1024.0 * 1024.0 * 1024.0);
        info.setMemoriaRamGB(ramGB);

        // 4. Coletar Uptime para alerta de 24h com OSHI
        long uptimeSegundos = os.getSystemUptime();
        long uptimeHoras = uptimeSegundos / 3600; //converte segundos para horas
        info.setUptimeHoras(uptimeHoras);


        return info;
    }
}
