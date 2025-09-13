import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        String url = "jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:6543/postgres?user=postgres.zuwmidevfxstazryemej&password=j37eg0qww2e";
        ContaDao dao = new ContaDao(ConnectionFactory.getConnection(url));

        while (true) {
            System.out.println("\n(1) Listar todas as contas");
            System.out.println("(2) Buscar uma conta específica pelo número");
            System.out.println("(3) Criar uma nova conta");
            System.out.println("(4) Alterar o saldo de uma conta");
            System.out.println("(5) Apagar uma conta");
            System.out.println("(0) Sair");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();

            try {
                switch (op) {
                    case "1": {
                        List<Conta> contas = dao.lerTodas();
                        if (contas.isEmpty()) System.out.println("Nenhuma conta encontrada.");
                        else contas.forEach(System.out::println);
                        break;
                    }
                    case "2": {
                        System.out.print("Número da conta: ");
                        long n = Long.parseLong(sc.nextLine());
                        Conta c = dao.buscarPeloNumero(n);
                        System.out.println(c == null ? "Conta não encontrada." : c);
                        break;
                    }
                    case "3": {
                        System.out.print("Número da conta (long): ");
                        long n = Long.parseLong(sc.nextLine());
                        System.out.print("Saldo inicial (ex: 1000.00): ");
                        BigDecimal s = new BigDecimal(sc.nextLine());
                        boolean ok = dao.criar(new Conta(n, s));
                        System.out.println(ok ? "Conta criada." : "Falha ao criar conta.");
                        break;
                    }
                    case "4": {
                        System.out.print("Número da conta: ");
                        long n = Long.parseLong(sc.nextLine());
                        System.out.print("Novo saldo: ");
                        BigDecimal s = new BigDecimal(sc.nextLine());
                        boolean ok = dao.atualizar(new Conta(n, s));
                        System.out.println(ok ? "Saldo atualizado." : "Falha ao atualizar saldo.");
                        break;
                    }
                    case "5": {
                        System.out.print("Número da conta: ");
                        long n = Long.parseLong(sc.nextLine());
                        boolean ok = dao.apagar(new Conta(n, null));
                        System.out.println(ok ? "Conta apagada." : "Falha ao apagar conta.");
                        break;
                    }
                    case "0":
                        System.out.println("Saindo...");
                        sc.close();
                        return;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}
