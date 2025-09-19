package ps2.titular_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

import static ps2.titular_app.ES.*;

@SpringBootApplication
public class TitularAppApplication implements CommandLineRunner {

    @Autowired
    private TitularDao dao;

    public static void main(String[] args) {
        SpringApplication.run(TitularAppApplication.class, args);
    }

    @Override
    public void run(String... args) {
        print("=== GERENCIADOR DE TITULARES! ===");

        boolean sair = false;
        String menu = """
                
                (1) Listar todos os titulares
                (2) Buscar um titular específico pelo número (ID)
                (3) Criar um novo titular
                (4) Alterar os dados do titular
                (5) Apagar um titular
                (0) Sair
                
                Escolha uma opção: """;

        while (!sair) {
            String op = input(menu).trim();
            try {
                switch (op) {
                    case "1" -> listarTodos();
                    case "2" -> buscarPorId();
                    case "3" -> criar();
                    case "4" -> alterar();
                    case "5" -> apagar();
                    case "0" -> {
                        print("Saindo...");
                        sair = true;
                    }
                    default -> print("Opção inválida!");
                }
            } catch (Exception e) {
                print("Erro: " + e.getMessage());
            }
        }
    }

    private void listarTodos() {
        List<Titular> titulares = dao.listarTodos();
        if (titulares.isEmpty()) {
            print("Não há titulares cadastrados.");
            return;
        }
        print("\n--- Titulares ---");
        titulares.forEach(t -> print(t.toString()));
    }

    private void buscarPorId() {
        long id = lerId("Informe o ID do titular: ");
        Titular t = dao.buscarPorId(id);
        if (t == null) print("Titular não encontrado.");
        else print(t.toString());
    }

    private void criar() {
        String nome = input("Nome: ").trim();
        String cpf  = input("CPF: ").trim();

        if (nome.isBlank() || cpf.isBlank()) {
            print("Nome e CPF são obrigatórios.");
            return;
        }

        Titular novo = new Titular(0L, nome, cpf);
        Titular salvo = dao.criar(novo);
        print("Criado com sucesso: " + salvo);
    }

    private void alterar() {
        long id = lerId("ID do titular a alterar: ");
        Titular t = dao.buscarPorId(id);
        if (t == null) {
            print("Titular não encontrado.");
            return;
        }
        print("Atual: " + t);

        String nome = input("Novo nome (ENTER mantém): ");
        String cpf  = input("Novo CPF (ENTER mantém): ");

        if (!nome.isBlank()) t.setNome(nome.trim());
        if (!cpf.isBlank())  t.setCpf(cpf.trim());

        Titular atualizado = dao.atualizar(t);
        print("Atualizado: " + atualizado);
    }

    private void apagar() {
        long id = lerId("ID do titular a apagar: ");
        Titular t = dao.buscarPorId(id);
        if (t == null) {
            print("Titular não encontrado.");
            return;
        }
        String conf = input("Confirmar exclusão de " + t + " (s/n)? ").trim().toLowerCase();
        if (conf.equals("s") || conf.equals("sim")) {
            dao.apagar(id);
            print("Apagado com sucesso.");
        } else {
            print("Operação cancelada.");
        }
    }

    private long lerId(String prompt) {
        String idStr = input(prompt).trim();
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID inválido: " + idStr);
        }
    }
}