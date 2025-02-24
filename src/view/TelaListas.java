package view;

import Service.API;
import Service.Json;
import model.Midia;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TelaListas extends JPanel{
    private MainFrame mainFrame;
    //public static boolean isUpdate = false;
    JPanel container = new JPanel();
    JScrollPane cardsFilmes;
    JScrollPane cardsSeries;

    Json json = new Json();
    List<Midia> filmesFavoritos;
    List<Midia> seriesFavoritas;


    public class BotaoPersonalizado extends JRadioButton {
        public BotaoPersonalizado(String texto) {
            super(texto);
            setIcon(new ImageIcon(new byte[0]));
            setSelectedIcon(new ImageIcon(new byte[0]));
            setBackground(Color.decode("#131320"));
            setForeground(Color.decode("#7A7B9F"));
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("Poppins", Font.BOLD, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    public static void estilizarBarraRolagem(JScrollPane scrollPane) {
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        JScrollBar horizontal = scrollPane.getHorizontalScrollBar();

        // Estilizando a barra de rolagem vertical
        vertical.setUI(new BasicScrollBarUI() {
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                // Pintando a faixa de rolagem (track)
                g.setColor(Color.decode("#0F0F1A"));
                g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                // Pintando o thumb (parte deslizante)
                g.setColor(Color.decode("#1A1B2D"));
                g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setMinimumSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setMaximumSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setVisible(false); // Tornar invisível
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setMinimumSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setMaximumSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setVisible(false); // Tornar invisível
                return button;
            }
        });

        // Estilizando a barra de rolagem horizontal
        horizontal.setUI(new BasicScrollBarUI() {
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                // Pintando a faixa de rolagem (track)
                g.setColor(Color.decode("#0F0F1A"));
                g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                // Pintando o thumb (parte deslizante)
                g.setColor(Color.decode("#1A1B2D"));
                g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setMinimumSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setMaximumSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setVisible(false); // Tornar invisível
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setMinimumSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setMaximumSize(new Dimension(0, 0)); // Tamanho zero para remover
                button.setVisible(false); // Tornar invisível
                return button;
            }
        });

        // Estilizando o fundo do JScrollPane
        scrollPane.setBackground(Color.decode("#131320"));
    }

    public TelaListas(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        this.filmesFavoritos = json.getFilmesFavoritos();
        this.seriesFavoritas = json.getSeriesFavorias();

        cardsFilmes = gerarCards(filmesFavoritos, "Filmes");
        cardsSeries = gerarCards(seriesFavoritas, "Séries");


    }

    public JPanel criaTela(boolean isUpdate) {

        container.removeAll();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.decode("#0F0F1A"));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        header.setMaximumSize(new Dimension(1920, 80));
        header.setBackground(Color.decode("#0F0F1A"));

        BotaoPersonalizado botaoVoltar = new BotaoPersonalizado("Voltar");
        botaoVoltar.setPreferredSize(new Dimension(100, 30));
        botaoVoltar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        botaoVoltar.addActionListener(e -> mainFrame.mostrarTelaInicial(false));

        header.add(botaoVoltar);
        container.add(header);

        //-----------------------------------------------------------------------------------------


        JPanel painelTituloFilmes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTituloFilmes.setBackground(Color.decode("#0F0F1A"));
        painelTituloFilmes.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        JLabel tituloFilmes = new JLabel("Filmes Favoritos");
        tituloFilmes.setFont(new Font("Poppins", Font.BOLD, 20));
        tituloFilmes.setForeground(Color.decode("#E5E2E9"));

        painelTituloFilmes.setPreferredSize(new Dimension(tituloFilmes.getPreferredSize().width, tituloFilmes.getPreferredSize().height));
        painelTituloFilmes.add(tituloFilmes);
        container.add(painelTituloFilmes);

        if (isUpdate) {
            Json json = new Json();
            List<Midia> filmesFavoritosAtualizados = json.getFilmesFavoritos();
            container.add(gerarCards(filmesFavoritosAtualizados, "Filmes"));
            //container.add(Box.createVerticalStrut(20));
            MainFrame.ListaIsUpdate = false;

        } else {
            container.add(cardsFilmes);
            //container.add(Box.createVerticalStrut(20));
        }

        JPanel painelTituloSeries = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTituloSeries.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 0));
        painelTituloSeries.setBackground(Color.decode("#0F0F1A"));
        JLabel tituloSeries = new JLabel("Séries Favoritas");
        tituloSeries.setFont(new Font("Poppins", Font.BOLD, 20));
        tituloSeries.setForeground(Color.decode("#E5E2E9"));

        painelTituloSeries.setPreferredSize(new Dimension(tituloSeries.getPreferredSize().width, tituloSeries.getPreferredSize().height));
        painelTituloSeries.add(tituloSeries);
        container.add(painelTituloSeries);

        if (isUpdate) {
            Json json = new Json();
            List<Midia> seriesFavoritosAtualizados = json.getSeriesFavorias();
            container.add(gerarCards(seriesFavoritosAtualizados, "Séries"));
            MainFrame.ListaIsUpdate = false;

        } else {
            container.add(cardsSeries);
        }


        return container;
    }

    

    private JScrollPane gerarCards(List<Midia> midias, String tipo) {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painel.setBackground(Color.decode("#0F0F1A"));

        API api = new API();
        for (Midia midia : midias) {
            JPanel card = api.criarCardMidia(midia);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(card);
                    mainFrame.mostrarTelaAvaliacao(midia, tipo);
                }

            });
            painel.add(card);
        }



        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.decode("#0F0F1A"));

        container.add(Box.createVerticalGlue());
        container.add(painel);
        container.add(Box.createVerticalGlue());


        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.decode("#0F0F1A"));
        scrollPane.setPreferredSize(new Dimension(1300, 300));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        estilizarBarraRolagem(scrollPane);

        return scrollPane;
    }

}
