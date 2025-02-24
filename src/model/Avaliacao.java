package model;



public class Avaliacao {
    private String usuario;
    private double nota;
    private String resenha;

    public Avaliacao(String usuario, double nota, String resenha) {
        this.usuario = usuario;
        this.nota = nota;
        this.resenha = resenha;
    }

    public String getUsuario() {
        return usuario;
    }

    public double getNota() {
        return nota;
    }

    public String getResenha() {
        return resenha;
    }


}
