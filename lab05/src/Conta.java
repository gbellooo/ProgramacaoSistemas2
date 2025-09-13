import java.math.BigDecimal;

public class Conta {
    private long numero;
    private BigDecimal saldo;

    public Conta(long n, BigDecimal s) {
        numero = n;
        saldo = s;
    }

    public long getNumero() { return numero; }
    public void setNumero(long n) { numero = n; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal s) { saldo = s; }

    @Override
    public String toString() {
        return "Conta [numero=" + numero + ", saldo=" + saldo + "]";
    }
}
