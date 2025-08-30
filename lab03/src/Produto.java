public class Produto {
    private String descricao;
    private double preco;
    private String marca;
    
    public Produto(String descricao, double preco, String marca) {
        this.descricao = descricao;
        this.preco = preco;
        this.marca = marca;
    }

    @Override
    public String toString() {
        return "Produto [descricao=" + descricao + ", preco=" + preco + ", marca=" + marca + "]";
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public String getMarca() {
        return marca;
    }



}
