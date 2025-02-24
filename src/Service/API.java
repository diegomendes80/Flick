package Service;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;

import model.Midia;
import org.json.JSONArray;
import org.json.JSONObject;


import javax.imageio.ImageIO;
import javax.swing.*;

public class API {
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkNTY4N2Q4MzY2NGFjM2E0MmVhYTE3NTg4YmMxZTNhZSIsIm5iZiI6MTczODg1Mzg0Ni43LCJzdWIiOiI2N2E0Y2RkNmRmMzM2ZjM4YWI4NWJmMTAiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.vMUzOdwEUnPajIGJqtthksIx4_cTN_5UykVgZpgLgRM";


    public static String buscarTitulo(String nome, String tipo) {
        try {
            if (!tipo.equals("movie") && !tipo.equals("tv")) {
                return "Tipo inválido. Use 'movie' para filmes ou 'tv' para séries.";
            }

            String nomeFormatado = URLEncoder.encode(nome, "UTF-8");
            String urlString = "https://api.themoviedb.org/3/search/" + tipo + "?query=" + nomeFormatado + "&language=pt-BR";

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray resultados = jsonResponse.getJSONArray("results");

            if (resultados.length() > 0) {
                JSONObject titulo = resultados.getJSONObject(0);
                return tipo.equals("tv") ? titulo.getString("name") : titulo.getString("title"); // "name" para séries, "title" para filmes
            } else {
                return "Nenhum resultado encontrado";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao buscar título";
        }
    }



    public static Object[] buscarMidia(String titulo, boolean isFilme) {
        try {
            // Codifica o título da mídia para a URL
            String tituloFormatado = URLEncoder.encode(titulo, "UTF-8");

            // URL dependendo do tipo de mídia (filme ou série)
            String urlString = isFilme ?
                    "https://api.themoviedb.org/3/search/movie?query=" + tituloFormatado + "&language=pt-BR" :
                    "https://api.themoviedb.org/3/search/tv?query=" + tituloFormatado + "&language=pt-BR";

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Configurações da requisição
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            conn.setRequestProperty("Accept", "application/json");

            // Lê a resposta da API
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Converte para JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray resultados = jsonResponse.getJSONArray("results");

            if (resultados.length() > 0) {
                JSONObject media = resultados.getJSONObject(0);
                String nome = isFilme ? media.getString("title") : media.getString("name");
                String sinopse = media.optString("overview", "Sinopse não disponível");
                String capa = "https://image.tmdb.org/t/p/w500" + media.optString("poster_path", "");
                String anoLancamento = isFilme ? media.optString("release_date", "0000").substring(0, 4) :
                        media.optString("first_air_date", "0000").substring(0, 4);
                int idMidia = media.getInt("id");

                // Busca informações adicionais
                String genero = buscarGenero(idMidia, isFilme);
                String diretor = isFilme ? buscarDiretor(idMidia, isFilme) : "Desconhecido";  // Diretores para séries não são buscados aqui
                String duracao = buscarDuracao(idMidia, isFilme);

                // Se for uma série, busca as temporadas e showrunners
                int numeroTemporadas = 0;
                List<String> showrunners = new ArrayList<>();
                if (!isFilme) {
                    numeroTemporadas = media.optInt("number_of_seasons", 0);
                    showrunners = buscarShowrunners(idMidia);
                }

                return new Object[]{nome, anoLancamento, genero, capa, diretor, duracao, numeroTemporadas, showrunners, sinopse};
            } else {
                System.out.println("Nenhuma mídia encontrada.");
                return new Object[]{"Nenhuma mídia encontrada", "", "", "", "", "", 0, new ArrayList<String>()};
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[]{"Erro ao buscar mídia", "", "", "", "", "", 0, new ArrayList<String>()};
        }
    }

    // Método para buscar showrunners de uma série
    public static List<String> buscarShowrunners(int idMidia) {
        List<String> showrunners = new ArrayList<>();
        try {
            String urlString = "https://api.themoviedb.org/3/tv/" + idMidia + "/credits?language=pt-BR";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray crew = jsonResponse.getJSONArray("crew");

            // Procura os showrunners na equipe de produção
            for (int i = 0; i < crew.length(); i++) {
                JSONObject pessoa = crew.getJSONObject(i);
                if (pessoa.getString("job").equals("Showrunner")) {
                    showrunners.add(pessoa.getString("name"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return showrunners;
    }

    // Método para buscar o gênero de um filme ou série
    public static String buscarGenero(int idMidia, boolean isFilme) {
        try {
            String urlString;
            if (isFilme) {
                urlString = "https://api.themoviedb.org/3/movie/" + idMidia + "?language=pt-BR";
            } else {
                urlString = "https://api.themoviedb.org/3/tv/" + idMidia + "?language=pt-BR";
            }

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray genres = jsonResponse.getJSONArray("genres");

            if (genres.length() > 0) {
                // Retorna o nome do primeiro gênero
                return genres.getJSONObject(0).getString("name");
            } else {
                return "Desconhecido";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao buscar gênero";
        }
    }

    // Método para buscar a duração de um filme ou série
    public static String buscarDuracao(int idMidia, boolean isFilme) {
        try {
            String urlString;
            if (isFilme) {
                urlString = "https://api.themoviedb.org/3/movie/" + idMidia + "?language=pt-BR";
            } else {
                // Para séries, não buscamos a duração do episódio
                return "Desconhecido"; // Ignora para séries
            }

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());

            if (isFilme) {
                int duracao = jsonResponse.getInt("runtime"); // Para filmes
                return duracao + " min";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Erro ao buscar duração";
    }

    // Método para buscar o diretor de um filme
    public static String buscarDiretor(int idMidia, boolean isFilme) {
        try {
            String urlString;
            if (isFilme) {
                urlString = "https://api.themoviedb.org/3/movie/" + idMidia + "/credits?language=pt-BR";
            } else {
                // Para séries, pode-se pegar o "diretor" como parte da equipe de produção
                urlString = "https://api.themoviedb.org/3/tv/" + idMidia + "/credits?language=pt-BR";
            }

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray crew = jsonResponse.getJSONArray("crew");

            // Procura pelo diretor na equipe de produção
            for (int i = 0; i < crew.length(); i++) {
                JSONObject pessoa = crew.getJSONObject(i);
                if (pessoa.getString("job").equals("Director")) {
                    return pessoa.getString("name");
                }
            }

            return "Desconhecido"; // Caso não encontre o diretor
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao buscar diretor";
        }
    }


    // Painel personalizado com imagem de fundo (método de corno)
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imageUrl) {
            try {
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    URL url = new URL(imageUrl); // Aqui a URL será criada se for válida
                    backgroundImage = ImageIO.read(url);
                } else {
                    throw new MalformedURLException("URL inválida ou vazia");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }


    //cria uma classe nova pra aplicar uma sombra gradiente la nos container de titulo dos filmes/series
    public class GradientShadowPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            int height = getHeight();
            int width = getWidth();

            // Gradiente que simula uma sombra no rodapé
            GradientPaint shadow = new GradientPaint(0, height, new Color(0, 0, 0, 210),
                    0, height - 80, new Color(0, 0, 0, 0));

            g2d.setPaint(shadow);
            g2d.fillRect(0, height - 80, width, 80);

            g2d.dispose();
        }
    }


    // Método para criar o elemento card de exibição do filme
    public JPanel criarCardMidia(Midia midia) {
        String nota = Double.toString(midia.getMediaNotas()); //converto a nota para string
        String nomeMidia = midia.getTitulo();
        String genero = midia.getGenero();
        String anoLancamento = midia.getAnoLancamento();
        String urlCapa = midia.getUrlCapa();

        JPanel card = new BackgroundPanel(urlCapa);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(220, 280));
        card.setMaximumSize(new Dimension(220, 280));
        // System.out.println(urlCapa);


        JPanel auxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));  // Alinha à direita
        auxPanel.setPreferredSize(new Dimension(200, 50));  // Largura máxima para o painel auxiliar
        auxPanel.setOpaque(false); //deixa transparente

        JPanel panelNota = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelNota.setPreferredSize(new Dimension(60, 30));
        panelNota.setMaximumSize(new Dimension(60, 30));
        panelNota.setBackground(new Color(15, 15, 26, 204));


        JLabel labelNota = new JLabel(nota + "/5");

        labelNota.setFont(new Font("Poppins", Font.BOLD, 15));
        labelNota.setForeground(Color.decode("#E4E5EC"));
        panelNota.add(labelNota);

        auxPanel.add(panelNota);
        card.add(auxPanel, BorderLayout.NORTH);

        JPanel panelInformation = new GradientShadowPanel();
        panelInformation.setLayout(new BoxLayout(panelInformation, BoxLayout.Y_AXIS));
        panelInformation.setPreferredSize(new Dimension(200, 80));
        panelInformation.setMaximumSize(new Dimension(200, 80));
        panelInformation.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panelInformation.setOpaque(false);


        JLabel midiaTitle = new JLabel(nomeMidia);
        midiaTitle.setFont(new Font("Poppins", Font.BOLD, 18));
        midiaTitle.setForeground(Color.decode("#E4E5EC"));

        JLabel midiaGenderYear = new JLabel(genero + " - " + anoLancamento);
        midiaGenderYear.setFont(new Font("Poppins", Font.BOLD, 15));
        midiaGenderYear.setForeground(Color.decode("#B5B6C9"));

        panelInformation.add(midiaTitle);
        panelInformation.add(midiaGenderYear);

        card.add(Box.createVerticalGlue());
        card.add(panelInformation, BorderLayout.SOUTH);


        return card;

    }


}

