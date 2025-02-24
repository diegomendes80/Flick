package model;

import java.util.ArrayList;
import java.util.List;

public class Midia {
    private String titulo;
    private String genero;
    private String anoLancamento;
    private String urlCapa;
    private String sinopse;
    //private String codigo;
    List<Avaliacao> avaliacoes = new ArrayList<>();

    public Midia(String titulo, String genero, String anoLancamento, String urlCapa, String sinopse) {
        this.titulo = titulo;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
        this.urlCapa = urlCapa;
        this.sinopse = sinopse;

    }



    public String getSinopse() {
        return sinopse;
    }

    public String getUrlCapa() {
        return urlCapa;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public String getAnoLancamento() {
        return anoLancamento;
    }

    public double getMediaNotas(){
        List<Double> notas = new ArrayList<>();

        if(avaliacoes.size() == 0) return 0;

        for(int i=0; i < avaliacoes.size(); i++){
            notas.add(avaliacoes.get(i).getNota());
        }

        double somaNotas = 0;

        for(int i=0; i<notas.size(); i++){
            somaNotas += notas.get(i);
        }

        double media = somaNotas / avaliacoes.size();
        //arredonda pra duas casas decimais
        return Math.round(media * 10.0) / 10.0;
    }



    public void setAvaliacao(Avaliacao avaliacao){
        avaliacoes.add(avaliacao);
    }

    public List<Avaliacao> getAvaliacoes(){
        return avaliacoes;
    }



}

