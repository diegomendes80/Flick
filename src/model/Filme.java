package model;



public class Filme extends Midia {
    private String duracaoMinutos;
    private String diretor;


    public Filme(String titulo, String genero, String anoLancamento, String urlCapa, String sinopse, String duracaoMinutos, String diretor) {
        super(titulo, genero, anoLancamento, urlCapa, sinopse);
        this.duracaoMinutos = duracaoMinutos;
        this.diretor = diretor;
    }

    public String getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public String getDiretor() {
        return diretor;
    }

    @Override
    public String toString(){
        return "Filme: " + this.getTitulo() + "\n" +
                "Gênero: " + this.getGenero() + "\n" +
                "Duração: " + this.getDuracaoMinutos() + " min\n" +
                "Diretor: " + this.getDiretor() + "\n" +
                "Ano Lançamento: " + this.getAnoLancamento();
    }
}
