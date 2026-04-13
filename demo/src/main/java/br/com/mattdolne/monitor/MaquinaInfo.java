package br.com.mattdolne.monitor;

public class MaquinaInfo {
    
    //Informações Básicas
    private String hostname;
    private String ip;
    private String macAddress;
    
    //Regras de Conformidade
    private String sistemaOperacional;
    private double memoriaRamGB;
    private double espacoDisco;
    private boolean discoSSD;
    private boolean entrouNoDominio;
    private long uptimeHoras;

    //Construtor vazio
    public MaquinaInfo() {
    }

    // --- GETTERS E SETTERS ---
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIP() {
        return ip;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public void setSistemaOperacional(String sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    public double getMemoriaRamGB() {
        return memoriaRamGB;
    }

    public void setMemoriaRamGB(double memoriaRamGB) {
        this.memoriaRamGB = memoriaRamGB;
    }

    public double getEspacoDisco() {
        return espacoDisco;
    }

    public void setEspacoDisco(double espacoDisco) {
        this.espacoDisco = espacoDisco;
    }

    public boolean getDiscoSSD() {
        return discoSSD;
    }

    public void setDiscoSSD(boolean discoSSD) {
        this.discoSSD = discoSSD;
    }

    public boolean getEntrouNoDominio() {
        return entrouNoDominio;
    }

    public void setEntrouNoDominio(boolean entrouNoDominio) {
        this.entrouNoDominio = entrouNoDominio;
    }

    public long getUptimeHoras() {
        return uptimeHoras;
    }

    public void setUptimeHoras(long uptimeHoras) {
        this.uptimeHoras = uptimeHoras;
    }

}
