import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class App {
    private static BigDecimal lerBigDecimal(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim()
                .replace(".", "")      // permite digitar 1.000,50
                .replace(",", ".");    // vírgula como decimal
            try {
                return new BigDecimal(s);
            } catch (Exception e) {
                System.out.println("Valor inválido. Tente novamente.");
            }
        }
    }

    private static long lerLong(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Long.parseLong(s);
            } catch (Exception e) {
                System.out.println("Número inválido. Tente novamente.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // ==> Ajuste sua URL aqui (ideal: usar variável de ambiente DB_URL)
        String url = "jdbc:postgresql://aws-1-sa-east-1.pooler.supabase.com:6543/postgres?user=postgres.qvusurjoazclggvxrgvp&password=mackenzie@java";

        Connection conn = ConnectionFactory.getConnection(url);
        if (conn == null) {
            System.out.println("Não foi possível abrir conexão. Verifique a URL/credenciais.");
            return;
        }

        try (ContaDao dao = new ContaDao(conn); Scanner sc = new Scanner(System.in)) {
            int opcao;
            do {
                System.out.println("\n=== MENU CONTAS ===");
                System.out.println("(1) Listar todas as contas");
                System.out.println("(2) Buscar uma conta específica pelo número");
                System.out.println("(3) Criar uma nova conta");
                System.out.println("(4) Alterar o saldo de uma conta");
                System.out.println("(5) Apagar uma conta");
                System.out.println("(0) Sair");
                System.out.print("Escolha: ");

                String entrada = sc.nextLine().trim();
                if (entrada.isEmpty()) continue;
                try {
                    opcao = Integer.parseInt(entrada);
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida.");
                    continue;
                }

                switch (opcao) {
                    case 1: { // Listar todas
                        try {
                            List<Conta> contas = dao.lerTodas();
                            if (contas.isEmpty()) {
                                System.out.println("Nenhuma conta encontrada.");
                            } else {
                                contas.forEach(System.out::println);
                            }
                        } catch (Exception e) {
                            System.out.println("Erro ao listar contas: " + e.getMessage());
                        }
                        break;
                    }
                    case 2: { // Buscar por número
                        long numero = lerLong(sc, "Número da conta: ");
                        Conta c = dao.buscarPeloNumero(numero);
                        if (c == null) {
                            System.out.println("Conta não encontrada.");
                        } else {
                            System.out.println("Encontrada: " + c);
                        }
                        break;
                    }
                    case 3: { // Criar
                        long numero = lerLong(sc, "Número da nova conta: ");
                        BigDecimal saldo = lerBigDecimal(sc, "Saldo inicial: ");
                        boolean ok = dao.criar(new Conta(numero, saldo));
                        System.out.println(ok ? "Conta criada com sucesso." : "Falha ao criar conta (talvez já exista?).");
                        break;
                    }
                    case 4: { // Alterar saldo
                        long numero = lerLong(sc, "Número da conta: ");
                        Conta c = dao.buscarPeloNumero(numero);
                        if (c == null) {
                            System.out.println("Conta não encontrada.");
                            break;
                        }
                        System.out.println("Saldo atual: " + c.getSaldo());
                        BigDecimal novoSaldo = lerBigDecimal(sc, "Novo saldo: ");
                        c.setSaldo(novoSaldo);
                        boolean ok = dao.atualizar(c);
                        System.out.println(ok ? "Saldo atualizado." : "Falha ao atualizar saldo.");
                        break;
                    }
                    case 5: { // Apagar
                        long numero = lerLong(sc, "Número da conta a apagar: ");
                        boolean ok = dao.apagar(new Conta(numero, BigDecimal.ZERO));
                        System.out.println(ok ? "Conta apagada." : "Falha ao apagar (talvez não exista).");
                        break;
                    }
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }

            } while (true && (/*opcao != 0 handled inside*/ true)); // laço controlado por '0' no switch

        } // fecha DAO e Scanner (e statements). Feche a Connection se não fechar no DAO:
        // conn.close();
    }
}
