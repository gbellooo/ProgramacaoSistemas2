public class CriterioPrecoMaximo implements CriterioBusca {
    public boolean testar(Produto produto, String valor) {
        try {
            double precoMaximo = Double.parseDouble(valor);
            return produto.getPreco() <= precoMaximo;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}