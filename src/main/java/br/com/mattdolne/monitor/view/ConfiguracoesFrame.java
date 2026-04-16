package br.com.mattdolne.monitor.view;

import br.com.mattdolne.monitor.model.MaquinaInfo;
import br.com.mattdolne.monitor.service.ColetorHardware;
import br.com.mattdolne.monitor.service.ConfiguracaoService;
import br.com.mattdolne.monitor.service.ExportarRelatorio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ConfiguracoesFrame extends JFrame {
    
    private ConfiguracaoService configService;
    private DashboardFrame dashboardPai;

    public ConfiguracoesFrame(DashboardFrame dashboardPai) {
        this.dashboardPai = dashboardPai;
        this.configService = new ConfiguracaoService();

        setTitle("Painel Administrativo - Monitor CORP");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(dashboardPai);

        montarTela();

    }

    private void montarTela() {
        JPanel painelPrincipal = new JPanel(new GridLayout(6, 1, 10, 10));
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        painelPrincipal.add(new JLabel("Horário de Fim de Expediente (HH:mm) :"));

        JTextField txtHorario = new JTextField(configService.carregarHorarioExpediente().toString());
        txtHorario.setFont(new Font("Consolas", Font.BOLD, 16));
        txtHorario.setHorizontalAlignment(JTextField.CENTER);
        painelPrincipal.add(txtHorario);

        JButton btnSalvarHorario = new JButton("Salvar");
        btnSalvarHorario.addActionListener(e -> {
            try {
                LocalTime novoHorario = LocalTime.parse(txtHorario.getText());
                configService.salvarHorarioExpediente(novoHorario);
                dashboardPai.setHorarioFimExpediente(novoHorario);
                JOptionPane.showMessageDialog(this, "Horário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato inválido! Use HH:mm (Exemplo: 18:00)", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });
        painelPrincipal.add(btnSalvarHorario);

        painelPrincipal.add(new JSeparator());
        painelPrincipal.add(new JLabel("Exportar Dados do Hardware Atual:"));

        JPanel painelExportacao = new JPanel(new GridLayout(1, 3, 10, 0));
        JButton btnTxt = new JButton("TXT");
        JButton btnCsv = new JButton("CSV");
        JButton btnJson = new JButton("JSON");

        btnTxt.addActionListener(e -> exportar("txt"));
        btnCsv.addActionListener(e -> exportar("csv"));
        btnJson.addActionListener(e -> exportar("json"));

        painelExportacao.add(btnTxt);
        painelExportacao.add(btnCsv);
        painelExportacao.add(btnJson);

        painelPrincipal.add(painelExportacao);

        add(painelPrincipal);
    }

    private void exportar(String formato) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório");
        fileChooser.setSelectedFile(new java.io.File("Relatorio_MonitorCORP"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String caminhoBase = fileChooser.getSelectedFile().getAbsolutePath();

            ColetorHardware coletor = new ColetorHardware();
            MaquinaInfo info = coletor.coletarDadosAtuais();
            ExportarRelatorio exportador = new ExportarRelatorio();

            try {
                if (formato.equals("txt")) exportador.exportarTxt(info, caminhoBase);
                else if (formato.equals("csv")) exportador.exportarCsv(info, caminhoBase);
                else if (formato.equals("json")) exportador.exportarJson(info, caminhoBase);

                JOptionPane.showMessageDialog(this, "Relatório " + formato.toUpperCase() + "Exportado com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro de I/O ao gravar o arquivo: " + ex.getMessage(), "Erro no Disco", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
