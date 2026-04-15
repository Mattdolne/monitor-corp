package br.com.mattdolne.monitor;

import br.com.mattdolne.monitor.view.DashboardFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // 1. Aplica o tema
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Falha ao inicializar o tema FlatLaf.");
        }
        
        // 2. Inicia a inteface gráfica na Thread correta para que a tela não trave
        SwingUtilities.invokeLater(() -> {
            DashboardFrame telaPrincipal = new DashboardFrame();
            telaPrincipal.setVisible(true);
        });
        
    }
}