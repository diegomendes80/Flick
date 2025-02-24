package view;

import model.Midia;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    public static boolean ListaIsUpdate = false;

    private JPanel contentPanel = new JPanel(new BorderLayout());
    telaInicial telaInicial = new telaInicial(this);
    TelaListas telaListas = new TelaListas(this);
    private boolean isUpdate;


    public MainFrame() {
        setTitle("Flick");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#0F0F1A"));

        contentPanel.setBackground(Color.decode("#0F0F1A"));
        add(contentPanel, BorderLayout.CENTER);


        isUpdate=false;
        mostrarTelaInicial(isUpdate);

        setVisible(true);
    }

    public void mostrarTelaInicial(boolean isUpdate) {

        JPanel novaTela = telaInicial.criaTela(isUpdate);


        for (int i = contentPanel.getComponentCount() - 1; i >= 0; i--) {
            contentPanel.remove(i);

        }

        // Revalidate e Repaint após a remoção
        contentPanel.revalidate();
        contentPanel.repaint();


        contentPanel.add(novaTela, BorderLayout.CENTER);

    }

    public void mostrarTelaAvaliacao(Midia midia, String tipo) {
        TelaAvaliacao telaAvaliacao = new TelaAvaliacao(this);
        String urlCapa = midia.getUrlCapa();
        String sinopse = midia.getSinopse();
        JPanel novaTela =  telaAvaliacao.criaTela(midia, tipo, urlCapa, sinopse);


        for (int i = contentPanel.getComponentCount() - 1; i >= 0; i--) {
            contentPanel.remove(i);

        }

        // Revalidate e Repaint após a remoção
        contentPanel.revalidate();
        contentPanel.repaint();


        contentPanel.add(novaTela, BorderLayout.CENTER);

    }

    public void mostrarTelaListas(){
        JPanel novaTela = telaListas.criaTela(ListaIsUpdate);

        for (int i = contentPanel.getComponentCount() - 1; i >= 0; i--) {
            contentPanel.remove(i);

        }

        // Revalidate e Repaint após a remoção
        contentPanel.revalidate();
        contentPanel.repaint();


        contentPanel.add(novaTela, BorderLayout.CENTER);

        ListaIsUpdate = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);  // Inicia o MainFrame
    }
}
