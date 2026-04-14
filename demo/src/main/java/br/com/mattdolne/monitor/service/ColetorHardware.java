package br.com.mattdolne.monitor.service;

import br.com.mattdolne.monitor.model.MaquinaInfo;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.io.File;
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
        /**
         * O próximo método cria uma caixa (MaquinaInfo) vazia,
         * preenche com os dados reais do PC e devolve a caixa cheia.
         */
    }
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
    
            // 5. Coletar Informações do Disco - Nativo + OSHI
            // Usamos File.listRoots() para pegar a raiz do disco automaticamente.
            File[] discos = File.listRoots();
            if (discos != null && discos.length > 0) {
                File discoPrincipal = discos[0]; //Pega o disco principal do sistema
                long espacoTotal = discoPrincipal.getTotalSpace();
                long espacoLivre = discoPrincipal.getUsableSpace();
    
                // Regra de 3 para descobrir a porcentagem livre
                double porcentagemLivre = ((double) espacoLivre / espacoTotal) * 100;
                info.setEspacoDiscoLivrePorcentagem(porcentagemLivre);
            }
    
            // Descobrir se unidade é SSD via OSHI
            /*
            boolean temSSD = false;
            for (oshi.hardware.HWDiskStore disco : hardware.getDiskStores()) {
                // Se a unidade contiver "SSD" ou "NVMe", será marcado como true
                String modelo = disco.getModel().toUpperCase();
                if (modelo.contains("SSD") || modelo.contains("NVME")) {
                    temSSD = true;
                    break; 
                }
            }
            info.setDiscoSSD(temSSD);
            */
            // Verificar SSD consultando o tipo de mídia física no Windows
            String comandoSSD = "Get-PhysicalDisk | Select-Object -ExpandProperty MediaType";
            String tiposMidia = executarComandoPowerShell(comandoSSD);
            info.setDiscoSSD(tiposMidia.toUpperCase().contains("SSD"));
            
            
            // 6. Coletar Endereço IP e MAC com OSHI
            for (oshi.hardware.NetworkIF net : hardware.getNetworkIFs()) {
                // Ignorar interfaces de loopback ou desativadas
                if (!net.getIfOperStatus().name().equals("UP") || net.getIPv4addr().length == 0) {
                    continue;
                }
                info.setIP(net.getIPv4addr()[0]);
                info.setMacAddress(net.getMacaddr());
                break;
                
            }

            // 7. Consultas de Domínio e última formatação com PowerShell
            // Pegar domínio
            String comandoDominio = "(Get-WmiObject Win32_ComputerSystem).Domain";
            String dominioLocal = executarComandoPowerShell(comandoDominio);
            info.setDominio(dominioLocal);

            // Pegar data da última formatação - instalação de SO
            String comandoData = "([WMI]'').ConvertToDateTime((Get-WmiObject Win32_OperatingSystem).InstallDate).ToString('dd/MM/yyyy')";
            String dataInstalacao = executarComandoPowerShell(comandoData);
            info.setDataIntalacaoOS(dataInstalacao);

            return info;
        }

            private String executarComandoPowerShell(String comando) {
                try {
                    ProcessBuilder builder = new ProcessBuilder("powershell.exe", "-NoProfile", "-Command", comando);
                    builder.redirectErrorStream(true);
                    Process processo = builder.start();

                    java.io.BufferedReader leitor = new java.io.BufferedReader(
                        new java.io.InputStreamReader(processo.getInputStream(), "CP850")
                    );

                    StringBuilder saida = new StringBuilder();
                    String linha;
                    while ((linha = leitor.readLine()) != null) {
                        saida.append(linha);                   
                } 
                return saida.toString().trim();

                } catch (Exception e) {
                    return "Erro ao executar o comando";
                }
            }
    }
        

