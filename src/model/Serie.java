package model;


import java.util.List;

public class Serie extends Midia{
    private int qtdTemporadas;
    List<String> showrunners;

    public Serie(String titulo, String genero, String anoLancamento, String urlCapa, String sinopse, int qtdTemporadas, List<String> showrunners) {
        super(titulo, genero, anoLancamento, urlCapa, sinopse);
        this.qtdTemporadas = qtdTemporadas;
        this.showrunners = showrunners;
    }

    public List<String> getShowrunners() {
        return showrunners;
    }

    public int getQtdTemporadas() {
        return qtdTemporadas;
    }

    @Override
    public String toString(){
        return "Nome: " + this.getTitulo() + "\n" +
                "Gênero: " + this.getGenero() + "\n" +
                "Temporadas: " + this.getQtdTemporadas() + "\n" +
                "Showrunners: " + this.getShowrunners() + "\n" +
                "Ano Lançamento: " + this.getAnoLancamento();
    }

}
