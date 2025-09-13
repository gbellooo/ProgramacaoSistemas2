import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ContaDao implements IContaDao {
    private PreparedStatement pstmCreate;
    private PreparedStatement pstmRead;
    private PreparedStatement pstmReadByNumber;
    private PreparedStatement pstmUpdate;
    private PreparedStatement pstmDelete;

    public ContaDao(Connection c) throws Exception {
        if (c == null) throw new Exception("Conexão nula na ContaDao.");
        // Melhor explicitar as colunas para evitar problemas de ordem
        pstmCreate       = c.prepareStatement("INSERT INTO CONTAS (NRO_CONTA, SALDO) VALUES (?, ?)");
        pstmRead         = c.prepareStatement("SELECT NRO_CONTA, SALDO FROM CONTAS ORDER BY NRO_CONTA");
        pstmReadByNumber = c.prepareStatement("SELECT NRO_CONTA, SALDO FROM CONTAS WHERE NRO_CONTA = ?");
        pstmUpdate       = c.prepareStatement("UPDATE CONTAS SET SALDO = ? WHERE NRO_CONTA = ?");
        pstmDelete       = c.prepareStatement("DELETE FROM CONTAS WHERE NRO_CONTA = ?");
    }
    
    @Override
    public boolean criar(Conta c) {
        try {
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
        try (ResultSet resultados = pstmRead.executeQuery()) {
            while (resultados.next()){
                long n = resultados.getLong("nro_conta");
                BigDecimal s = resultados.getBigDecimal("saldo");
                contas.add(new Conta(n, s));
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao ler contas: " + e.getMessage(), e);
        }
        return contas;
    }
    
    @Override
    public Conta buscarPeloNumero(long id) {
        try {
            pstmReadByNumber.setLong(1, id);
            try (ResultSet rs = pstmReadByNumber.executeQuery()) {
                if (rs.next()) {
                    long n = rs.getLong("nro_conta");
                    BigDecimal s = rs.getBigDecimal("saldo");
                    return new Conta(n, s);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar conta: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean atualizar(Conta c) {
        try {
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
            pstmDelete.setLong(1, c.getNumero());
            return pstmDelete.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Erro ao apagar conta: " + e.getMessage());
            return false;
        }
    }
}
