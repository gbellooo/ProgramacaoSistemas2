import java.sql.*;
import java.util.Scanner;

public class App2 {
    static String url = "jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:6543/postgres?user=postgres.zuwmidevfxstazryemej&password=j37eg0qww2e";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n--- MENU CONTAS ---");
            System.out.println("1 - Criar conta");
            System.out.println("2 - Listar contas");
            System.out.println("3 - Alterar saldo");
            System.out.println("4 - Remover conta");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1 -> {
                    System.out.print("Número da conta: ");
                    long nro = sc.nextLong();
                    System.out.print("Saldo inicial: ");
                    double saldo = sc.nextDouble();
                    criarConta(nro, saldo);
                }
                case 2 -> listarContas();
                case 3 -> {
                    System.out.print("Número da conta: ");
                    long nro = sc.nextLong();
                    System.out.print("Novo saldo: ");
                    double saldo = sc.nextDouble();
                    alterarSaldo(nro, saldo);
                }
                case 4 -> {
                    System.out.print("Número da conta: ");
                    long nro = sc.nextLong();
                    removerConta(nro);
                }
                case 0 -> System.out.println("\nSaindo...");
                default -> System.out.println("\nOpção inválida!");
            }

        } while (opcao != 0);

        sc.close();
    }

    public static void criarConta(long nroConta, double saldo) {
        try (Connection c = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO contas (nro_conta, saldo) VALUES (?, ?)";
            PreparedStatement stm = c.prepareStatement(sql);
            stm.setLong(1, nroConta);
            stm.setDouble(2, saldo);
            stm.executeUpdate();
            System.out.println("\nConta criada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listarContas() {
        try (Connection c = DriverManager.getConnection(url)) {
            String sql = "SELECT * FROM contas";
            PreparedStatement stm = c.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            System.out.println("\n--- LISTA DE CONTAS ---");
            while (rs.next()) {
                long nro = rs.getLong("nro_conta");
                double saldo = rs.getDouble("saldo");
                System.out.println("Número: " + nro + " - R$ " + saldo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void alterarSaldo(long nroConta, double novoSaldo) {
        try (Connection c = DriverManager.getConnection(url)) {
            String sql = "UPDATE contas SET saldo=? WHERE nro_conta=?";
            PreparedStatement stm = c.prepareStatement(sql);
            stm.setDouble(1, novoSaldo);
            stm.setLong(2, nroConta);
            int linhas = stm.executeUpdate();
            if (linhas > 0) {
                System.out.println("\nSaldo atualizado com sucesso!");
            } else {
                System.out.println("\nConta não encontrada!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removerConta(long nroConta) {
        try (Connection c = DriverManager.getConnection(url)) {
            String sql = "DELETE FROM contas WHERE nro_conta=?";
            PreparedStatement stm = c.prepareStatement(sql);
            stm.setLong(1, nroConta);
            int linhas = stm.executeUpdate();
            if (linhas > 0) {
                System.out.println("\nConta removida com sucesso!");
            } else {
                System.out.println("\nConta não encontrada!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}