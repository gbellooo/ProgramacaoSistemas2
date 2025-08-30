public class CriterioPrecoMinimo implements CriterioBusca {
    public boolean testar(Produto produto, String valor) {
        try {
            double precoMinimo = Double.parseDouble(valor);
            return produto.getPreco() >= precoMinimo;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}