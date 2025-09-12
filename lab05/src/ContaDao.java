import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ContaDao implements IContaDao, AutoCloseable {
    private final Connection conn;
    private final PreparedStatement pstmCreate;
    private final PreparedStatement pstmRead;
    private final PreparedStatement pstmReadByNumber;
    private final PreparedStatement pstmUpdate;
    private final PreparedStatement pstmDelete;

    public ContaDao(Connection c) throws SQLException {
        this.conn = c;
        this.pstmCreate        = conn.prepareStatement("INSERT INTO contas (nro_conta, saldo) VALUES (?, ?)");
        this.pstmRead          = conn.prepareStatement("SELECT nro_conta, saldo FROM contas ORDER BY nro_conta");
        this.pstmReadByNumber  = conn.prepareStatement("SELECT nro_conta, saldo FROM contas WHERE nro_conta = ?");
        this.pstmUpdate        = conn.prepareStatement("UPDATE contas SET saldo = ? WHERE nro_conta = ?");
        this.pstmDelete        = conn.prepareStatement("DELETE FROM contas WHERE nro_conta = ?");
    }

    @Override
    public boolean criar(Conta c) {
        try {
            pstmCreate.clearParameters();
            pstmCreate.setLong(1, c.getNumero());
            pstmCreate.setBigDecimal(2, c.getSaldo());
            return pstmCreate.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Erro ao criar conta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Conta> lerTodas() throws Exception {
        List<Conta> contas = new ArrayList<>();
        try (ResultSet rs = pstmRead.executeQuery()) {
            while (rs.next()) {
                long n = rs.getLong("nro_conta");
                BigDecimal s = rs.getBigDecimal("saldo");
                contas.add(new Conta(n, s));
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao ler contas: " + e.getMessage(), e);
        }
        return contas;
    }

    @Override
    public Conta buscarPeloNumero(long numero) {
        try {
            pstmReadByNumber.clearParameters();
            pstmReadByNumber.setLong(1, numero);
            try (ResultSet rs = pstmReadByNumber.executeQuery()) {
                if (rs.next()) {
                    BigDecimal s = rs.getBigDecimal("saldo");
                    return new Conta(numero, s);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar conta: " + e.getMessage());
        }
        return null; // não encontrada ou erro
    }

    @Override
    public boolean atualizar(Conta c) {
        try {
            pstmUpdate.clearParameters();
            pstmUpdate.setBigDecimal(1, c.getSaldo());
            pstmUpdate.setLong(2, c.getNumero());
            return pstmUpdate.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar conta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean apagar(Conta c) {
        try {
            pstmDelete.clearParameters();
            pstmDelete.setLong(1, c.getNumero());
            return pstmDelete.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Erro ao apagar conta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void close() {
        try { if (pstmCreate != null) pstmCreate.close(); } catch (Exception ignored) {}
        try { if (pstmRead != null) pstmRead.close(); } catch (Exception ignored) {}
        try { if (pstmReadByNumber != null) pstmReadByNumber.close(); } catch (Exception ignored) {}
        try { if (pstmUpdate != null) pstmUpdate.close(); } catch (Exception ignored) {}
        try { if (pstmDelete != null) pstmDelete.close(); } catch (Exception ignored) {}
        // Feche a Connection aqui se desejar centralizar:
        // try { if (conn != null) conn.close(); } catch (Exception ignored) {}
    }
}
