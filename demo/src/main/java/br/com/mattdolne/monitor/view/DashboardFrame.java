package br.com.mattdolne.monitor.view;

import br.com.mattdolne.monitor.model.MaquinaInfo;
import br.com.mattdolne.monitor.service.ColetorHardware;
import br.com.mattdolne.monitor.service.RegrasConformidade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        // Configurações básicas da janela
        setTitle("Monitor Corp");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o programa quando clicar no X
        setLocationRelativeTo(null); // Abre a janela centralizada na tela

        ColetorHardware coletor = new ColetorHardware();
        MaquinaInfo info = coletor.coletarDadosAtuais();

        RegrasConformidade regras = new RegrasConformidade();
        List<String> alertas = regras.verificarAlertas(info);

        JPanel painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Resumo do Sistema: " + info.getHostname(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        //Formatar grid 
        JPanel painelHardware = new JPanel(new GridLayout(8, 1, 5, 5));
        painelHardware.setBorder(BorderFactory.createTitledBorder("Especificações Técnicas"));

        painelHardware.add(new JLabel(" Domínio: " + info.getDominio()));
        painelHardware.add(new JLabel(" SO: " + info.getSistemaOperacional()));
        painelHardware.add(new JLabel(" Data de Formatação: " + info.getDataInstalacaoOS()));
        painelHardware.add(new JLabel(" Memória RAM: " + String.format("%.2f GB", info.getMemoriaRamGB())));
        painelHardware.add(new JLabel(" SSD Instalado: " + (info.getDiscoSSD() ? "Sim" : "Não")));
        painelHardware.add(new JLabel(" Espaço em Disco: " + String.format("%.1f%%", info.getEspacoDiscoLivrePorcentagem())));
        painelHardware.add(new JLabel(" Endereço IP: " + info.getIP()));
        painelHardware.add(new JLabel(" Tempo Ligado: " + info.getUptimeHoras() + "horas"));

        painelPrincipal.add(painelHardware, BorderLayout.WEST);

        JPanel painelAuditoria = new JPanel(new BorderLayout());
        painelAuditoria.setBorder(BorderFactory.createTitledBorder("Status de Conformidade"));
        
        JTextArea txtAlertas = new JTextArea();
        txtAlertas.setEditable(false); 
        txtAlertas.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtAlertas.setMargin(new Insets(10, 10, 10, 10));

        txtAlertas.setLineWrap(true);
        txtAlertas.setWrapStyleWord(true);
        
        if (alertas.isEmpty()) {
            txtAlertas.setForeground(new Color(46, 204, 113));
            txtAlertas.setText("\n\n [OK] Máquina está operando adequadamente!");
        } else {
            txtAlertas.setForeground(new Color(255, 107, 107));
            StringBuilder sb = new StringBuilder();
            sb.append("Foram encontrados ").append(alertas.size()).append(" problema(s): \n\n");
            for (String alerta : alertas) {
                sb.append("/// ").append(alerta).append("\n\n");
            }
            txtAlertas.setText(sb.toString());
        }
        
        painelAuditoria.add(new JScrollPane(txtAlertas), BorderLayout.CENTER);
        painelPrincipal.add(painelAuditoria, BorderLayout.CENTER);


        add(painelPrincipal);

    }
    
}
