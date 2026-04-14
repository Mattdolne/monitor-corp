package br.com.mattdolne.monitor.service;

import br.com.mattdolne.monitor.model.MaquinaInfo;
import java.util.ArrayList;
import java.util.List;

public class RegrasConformidade {
    
    public List<String> verificarAlertas(MaquinaInfo info) {
        List<String> alertas = new ArrayList<>();

        // Regra 1: Uptime 24h
        if (info.getUptimeHoras() >= 24) {
            alertas.add("ALERTA: Sua máquina está há mais de 24h ligada. Reinicie para aplicar correções de segurança e limpar processos.");
        }

        // Regra 2: RAM < 8GB
        if (info.getMemoriaRamGB() < 7.5) {
            alertas.add("ALERTA: memória RAM instalada no dispositivo é inferior a 8GB. Máquina não está apta para executar Windows 11");
        }

        // Regra 3: Windows Antigo
        if (info.getSistemaOperacional().contains("10")) {
            alertas.add("ALERTA: esta máquina está operando com Windows Antigo. Solicite a migração e adequação com o Suporte Técnico responsável.");
        }

        // Regra 4: Disco quase cheio (<15% livre)
        if (info.getEspacoDiscoLivrePorcentagem() < 15) {
            alertas.add("ALERTA: espaço em disco inferior a 15%! Sobrecarga de armazenamento pode ser danosa ao dispositivo. Solicite uma limpeza ao Suporte Técnico responsável.");
        }

        return alertas;
    }

}
