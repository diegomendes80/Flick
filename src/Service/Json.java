package Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Midia;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Json {


    String caminhoFilmes = System.getProperty("user.dir") + "/src/Service/todosFilmes.json";
    String caminhoSeries = System.getProperty("user.dir") + "/src/Service/todasSeries.json";

    String caminhoFilmesFavoritos = System.getProperty("user.dir") + "/src/Service/filmesFavoritos.json";
    String caminhoSeriesFavoritas = System.getProperty("user.dir") + "/src/Service/seriesFavoritas.json";

    Gson gson = new Gson();
    Type listType = new TypeToken<List<Midia>>() {}.getType(); //avisa pro json que ele sera convertido em um list



    List<Midia> filmesFavoritos = JSONtoLIST(caminhoFilmesFavoritos);
    List<Midia> seriesFavorias = JSONtoLIST(caminhoSeriesFavoritas);


    public List<Midia> getFilmes() {
        return JSONtoLIST(caminhoFilmes);
    }

    public List<Midia> getSeries() {
        return JSONtoLIST(caminhoSeries);
    }

    public List<Midia> getFilmesFavoritos() {
        return filmesFavoritos;
    }

    public List<Midia> getSeriesFavorias() {
        return seriesFavorias;
    }

    //carrega os elementos do json na lista java
    private List<Midia> JSONtoLIST(String caminhoArquivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            List<Midia> lista = gson.fromJson(reader, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Midia[] JSONtoOBJECT(String caminhoArquivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            return gson.fromJson(reader, Midia[].class); // Converte diretamente para array
        } catch (FileNotFoundException e) {
            return new Midia[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //converte de uma lista pro arquivo json
    private void LISTtoJSON(List<Midia> lista, String caminhoArquivo) {
        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            gsonPretty.toJson(lista, writer); // Salva a lista no arquivo com formatação
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





    public void salvaMidia(Midia midia, String tipo, String caminhoarquivo){
        AtomicReference<Boolean> jaExiste = new AtomicReference<>(false);

        if(tipo.equals("Filmes")){

            List<Midia> filmes = JSONtoLIST(caminhoarquivo);

            filmes.forEach(e -> {
                if(e.getTitulo().equals(midia.getTitulo())) {
                    jaExiste.set(true);
                }
            });

            if(!jaExiste.get()){
                filmes.add(midia);
                //atualiza o arquivo JSON com a nova midia
                LISTtoJSON(filmes, caminhoarquivo);
            }
        }
        else if(tipo.equals("Séries")){
            List<Midia> series = JSONtoLIST(caminhoarquivo);

            series.forEach(e -> {
                if(e.getTitulo().equals(midia.getTitulo())) jaExiste.set(true);
            });

            if(!jaExiste.get()){
                series.add(midia);
                //atualiza o arquivo JSON com a nova midia
                LISTtoJSON(series, caminhoarquivo);

            }
            }


    }

    public void removeMidia(Midia midia, String tipo, String caminhoarquivo) {
        if (tipo.equals("Filmes")) {
            List<Midia> filmes = getFilmesFavoritos();


            filmes.removeIf(e -> e.getTitulo().equals(midia.getTitulo()));



            LISTtoJSON(filmes, caminhoarquivo);
            this.filmesFavoritos = JSONtoLIST(caminhoFilmesFavoritos);
            System.out.println(filmesFavoritos);
        }
        else if (tipo.equals("Séries")) {
            List<Midia> series = getSeriesFavorias();


            series.removeIf(e -> e.getTitulo().equals(midia.getTitulo()));


            LISTtoJSON(series, caminhoarquivo);
            this.seriesFavorias = JSONtoLIST(caminhoSeriesFavoritas);
        }
    }

    public void atualizaMidia(Midia midia, String lista, String caminhoarquivo){
        if(lista.equals("Filmes")){
            List<Midia> filmes = JSONtoLIST(caminhoarquivo);

            for (int i = 0; i < filmes.size(); i++) {
                if (filmes.get(i).getTitulo().equals(midia.getTitulo())) {
                    // Atualiza o objeto na lista
                    filmes.set(i, midia);
                    // Salva a lista atualizada no arquivo JSON
                    LISTtoJSON(filmes, caminhoFilmes);
                    break;
                }
            }
        } else if (lista.equals("Séries")) {

            List<Midia> series = JSONtoLIST(caminhoarquivo);
            for (int i = 0; i < series.size(); i++) {
                if (series.get(i).getTitulo().equals(midia.getTitulo())) {
                    // Atualiza o objeto na lista
                    series.set(i, midia);
                    // Salva a lista atualizada no arquivo JSON
                    LISTtoJSON(series, caminhoSeries);
                    break;
                }
            }
        }
    }





        public  String gerarCodigoUnico() {
            // Gera um UUID aleatório e pega os primeiros 10 caracteres numéricos
            String codigo = UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 10);
            return codigo;
        }
}


