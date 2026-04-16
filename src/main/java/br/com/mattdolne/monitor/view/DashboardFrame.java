package br.com.mattdolne.monitor.view;

import br.com.mattdolne.monitor.model.MaquinaInfo;
import br.com.mattdolne.monitor.service.ColetorHardware;
import br.com.mattdolne.monitor.service.ConfiguracaoService;
import br.com.mattdolne.monitor.service.RegrasConformidade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.time.LocalTime;

public class DashboardFrame extends JFrame {

    private TrayIcon trayIcon;
    private ConfiguracaoService configService = new ConfiguracaoService();

    private LocalTime horarioFimExpediente = configService.carregarHorarioExpediente();
    private boolean expedienteNotificadoHoje = false;

    //===========================================================
    //  CONSTRUTOR
    //===========================================================
    public DashboardFrame() {
        // Configurações básicas da janela
        setTitle("Monitor Corp");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); 
        setLocationRelativeTo(null); 

        atualizarDadosDaTela();

        if (SystemTray.isSupported()) {
            configurarBandejaDoSistema();
            iniciarMonitoramentoSilencioso();
        } else {
            System.out.println("Bandeja do sistema não suportada.");
        }
    }

    //===========================================================
    //  MÉTODO DE ATUALIZAÇÃO DE TELA
    //===========================================================
    private void atualizarDadosDaTela() {

        getContentPane().removeAll(); //aqui a tela será limpa quando atualizar


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

        //PAINEL AUDITORIA
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
    
            JButton btnAdmin = new JButton("Configurações");
            btnAdmin.addActionListener(e -> abrirLoginAdmin());
            painelPrincipal.add(btnAdmin, BorderLayout.SOUTH);

            
            add(painelPrincipal);
            
            revalidate();
            repaint();
            
            
        }   
        // <--- FIM DO MÉTODO atualizarDadosDaTela

//===========================================================
//  MÉTODO LOGIN ADMIN
//===========================================================

        private void abrirLoginAdmin() {
            JPasswordField pf = new JPasswordField();
            int okPF = JOptionPane.showConfirmDialog(null, pf, "Insira a senha de Administrador: ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (okPF == JOptionPane.OK_OPTION) {
                String password = new String(pf.getPassword());
                if (password.equals("admin@123")) {
                    abrirTelaConfiguracoes();
                } else {
                    JOptionPane.showMessageDialog(this, "Senha Incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        // <--- FIM DO MÉTODO abrirLoginAdmin
        
        private void abrirTelaConfiguracoes() {
            ConfiguracoesFrame telaAdmin = new ConfiguracoesFrame(this);
            telaAdmin.setVisible(true);
        }

        public void setHorarioFimExpediente(LocalTime novoHorario) {
            this.horarioFimExpediente = novoHorario;
            this.expedienteNotificadoHoje = false;
        }

//===========================================================
//  MÉTODOS DA BANDEJA DO SISTEMA
//===========================================================
private void configurarBandejaDoSistema() {
    SystemTray tray = SystemTray.getSystemTray();

    // Criar ícone
    BufferedImage imagemIcone = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = imagemIcone.createGraphics();
    g.setColor(new Color(41, 128, 185));
    g.fillOval(1, 1, 14, 14);
    g.dispose();

    PopupMenu menuBandeja = new PopupMenu();

    MenuItem itemAbrir = new MenuItem("Abrir Dashboard");
    itemAbrir.addActionListener(e -> {
        atualizarDadosDaTela();
        setVisible(true);
        setExtendedState(JFrame.NORMAL);
    });

    MenuItem itemSair = new MenuItem("Encerrar Agente");
    itemSair.addActionListener(e -> System.exit(0));

    menuBandeja.add(itemAbrir);
    menuBandeja.addSeparator();
    menuBandeja.add(itemSair);

    trayIcon = new TrayIcon(imagemIcone, "Monitor CORP", menuBandeja);
    trayIcon.setImageAutoSize(true);

    trayIcon.addActionListener( e -> {
        atualizarDadosDaTela();
        setVisible(true);
        setExtendedState(JFrame.NORMAL);
    });

    try {
        tray.add(trayIcon);
    } catch (AWTException e) {
        System.err.println("Erro ao adicionar ícone na bandeja.");
    }
} // <---------- FIM DO MÉTODO configurarBandejaDoSistema    

private void iniciarMonitoramentoSilencioso() {
    int intervaloDeChecagem = 3 * 60 * 60 * 1000; //notificação a cada 3h

    Timer timerSegundoPlano = new Timer(intervaloDeChecagem, e -> {
        ColetorHardware coletor = new ColetorHardware();
        MaquinaInfo info = coletor.coletarDadosAtuais();
        RegrasConformidade auditor = new RegrasConformidade();
        List<String> alertas = auditor.verificarAlertas(info);

        if (!alertas.isEmpty()) {
            trayIcon.displayMessage(
                "Alerta de Segurança",
                "A máquina apresenta " + alertas.size() + " violação(ões) de conformidade! Clique para verificar.",
                TrayIcon.MessageType.WARNING
            );
        }
    });

    timerSegundoPlano.setCoalesce(true);
    timerSegundoPlano.start();

    Timer timerExpediente = new Timer(60000, e -> {
        LocalTime agora = LocalTime.now();

        LocalTime avisoPrevio = horarioFimExpediente.minusMinutes(2);

        if (agora.getHour() == 0 && agora.getMinute() == 0) {
            expedienteNotificadoHoje = false;
        }

        if (!expedienteNotificadoHoje && (agora.isAfter(avisoPrevio) || agora.getMinute() == avisoPrevio.getMinute())) {

            trayIcon.displayMessage(
                "Monitor CORP: Seu expediente está chegando ao fim.",
                "Lembre-se de desligar o computador antes de sair. Bom descanso!",
                TrayIcon.MessageType.WARNING
            );

            expedienteNotificadoHoje = true;

        }
    });
    timerExpediente.start();

} // <--- FIM DO MÉTODO iniciarMonitoramentoSilencioso

}

