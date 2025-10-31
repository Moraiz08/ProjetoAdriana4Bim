
import java.util.List;
import java.util.Scanner;

public class App {
    private static AlunoDAO dao = new AlunoDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;

        do {
            exibirMenu(); // Exibe o menu 
            opcao = lerOpcao();

            try {
                switch (opcao) {
                    case 1: // 1- Inserir aluno
                        inserir();
                        break;
                    case 2: // 2- Listar alunos
                        listar();
                        break;
                    case 3: // 3- Atualizar aluno
                        atualizar();
                        break;
                    case 4: // 4- Excluir aluno
                        excluir();
                        break;
                    case 0: // 0 - Sair
                        System.out.println("Encerrando a aplicação. Adeus!");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (Exception e) {
                System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
                scanner.nextLine(); // Limpa o buffer em caso de erro
            }
        } while (opcao != 0);
    }

    private static void exibirMenu() {
        System.out.println("\n--- Cadastro de Alunos (CRUD JDBC) ---");
        System.out.println("1- Inserir aluno"); 
        System.out.println("2- Listar alunos"); 
        System.out.println("3- Atualizar aluno"); 
        System.out.println("4- Excluir aluno"); 
        System.out.println("0 - Sair"); 
        System.out.print("Escolha uma opção: ");
    }
    
    private static int lerOpcao() {
        // Inclui tratamento de exceções (try-catch)  para entrada do usuário
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next(); // consome a entrada inválida
        }
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consome a quebra de linha
        return opcao;
    }

    private static void inserir() {
        System.out.println("\n--- INSERIR ALUNO ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Curso: ");
        String curso = scanner.nextLine();
        
        int idade = -1;
        while (idade <= 0) {
            System.out.print("Idade: ");
            if (scanner.hasNextInt()) {
                idade = scanner.nextInt();
            }
            scanner.nextLine(); // Consome a quebra de linha
            if (idade <= 0) {
                System.out.println("Idade deve ser um número positivo.");
            }
        }

        Aluno novoAluno = new Aluno(nome, curso, idade);
        dao.inserirAluno(novoAluno);
    }

    private static void listar() {
        System.out.println("\n--- LISTA DE ALUNOS ---");
        
        // Assume que dao.listarAlunos() retorna List<Aluno>
        List<Aluno> alunos = dao.listarAlunos(); 
        
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
        } else {
            // --- 1. CABEÇALHO DA TABELA ---
            
            // Define as larguras das colunas: 
            // ID: 4 caracteres | NOME: 15 caracteres | CURSO: 8 caracteres | IDADE: 5 caracteres
            String formatoCabecalho = "| %-4s | %-15s | %-8s | %-5s |%n";
            String separador = "+------+-----------------+----------+-------+%n";

            // Imprime o separador superior
            System.out.printf(separador);
            
            // Imprime o cabeçalho usando o formato
            System.out.printf(formatoCabecalho, "ID", "NOME", "CURSO", "IDADE");
            
            // Imprime a linha de separação
            System.out.printf(separador);

            // --- 2. LISTAGEM DOS DADOS ---
            String formatoDados = "| %-4d | %-15s | %-8s | %-5d |%n";
            
            for (Aluno aluno : alunos) {
                // Imprime cada linha, usando os getters da classe Aluno
                System.out.printf(
                    formatoDados, 
                    aluno.getId(), 
                    aluno.getNome(), 
                    aluno.getCurso(), 
                    aluno.getIdade()
                );
            }

            // --- 3. RODAPÉ DA TABELA ---
            System.out.printf(separador);
            
        }
    }

    private static void atualizar() {
        System.out.println("\n--- ATUALIZAR ALUNO ---");
        System.out.print("ID do aluno a ser atualizado: ");
        
        // 1. Validação de ID
        if (!scanner.hasNextInt()) {
            System.out.println("ID inválido. Operação cancelada.");
            scanner.nextLine();
            return;
        }
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        // 2. BUSCAR ALUNO EXISTENTE
        // Presume-se que o AlunoDAO tem um método que retorna Aluno ou null
        Aluno alunoExistente = dao.SelectById(id); 
        
        if (alunoExistente == null) {
            System.out.println("❌ Erro: Aluno " + id + " não encontrado no banco de dados.");
            return;
        }

        // 3. EXIBIR DADOS ATUAIS E COLETAR NOVOS DADOS

        System.out.println("\n--- DADOS ATUAIS ---");
        System.out.println("ID:    " + alunoExistente.getId());
        System.out.println("Nome:  " + alunoExistente.getNome());
        System.out.println("Curso: " + alunoExistente.getCurso());
        System.out.println("Idade: " + alunoExistente.getIdade());
        System.out.println("--------------------");

        String novoNome = alunoExistente.getNome();
        String novoCurso = alunoExistente.getCurso();
        int novaIdade = alunoExistente.getIdade();
        
        String input;

        // A. Atualizar Nome
        System.out.print("Novo Nome (Atual: " + novoNome + "). Deixe em branco para manter: ");
        input = scanner.nextLine().trim();
        if (!input.isEmpty()) {
            novoNome = input;
        }

        // B. Atualizar Curso
        System.out.print("Novo Curso (Atual: " + novoCurso + "). Deixe em branco para manter: ");
        input = scanner.nextLine().trim();
        if (!input.isEmpty()) {
            novoCurso = input;
        }

        // C. Atualizar Idade (Com Validação)
        while (true) {
            System.out.print("Nova Idade (Atual: " + novaIdade + "). Digite um novo valor, ou 'n' para manter: ");
            input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("n") || input.isEmpty()) {
                break; // Mantém a idade atual e sai do loop
            }
            
            try {
                int idadeDigitada = Integer.parseInt(input);
                if (idadeDigitada > 0) {
                    novaIdade = idadeDigitada;
                    break; // Idade válida, sai do loop
                } else {
                    System.out.println("Idade deve ser um número positivo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número ou 'n' para manter.");
            }
        }

        // 4. CHAMAR A FUNÇÃO DE ATUALIZAÇÃO NO DAO
        // Criamos um novo objeto Aluno com o ID existente e os novos (ou antigos) dados
        Aluno alunoAtualizado = new Aluno(id, novoNome, novoCurso, novaIdade);
        dao.atualizarAluno(alunoAtualizado);

        System.out.println("✅ Aluno com ID " + id + " enviado para atualização.");
    }

    private static void excluir() {
        System.out.println("\n--- EXCLUIR ALUNO ---");
        System.out.print("ID do aluno a ser excluído: ");
        
        // 1. Validação de ID
        if (!scanner.hasNextInt()) {
            System.out.println("ID inválido. Operação cancelada.");
            scanner.nextLine();
            return;
        }
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        // 2. BUSCAR ALUNO EXISTENTE
        Aluno alunoParaExcluir = dao.SelectById(id); 
        
        if (alunoParaExcluir == null) {
            System.out.println("❌ Erro: Aluno com ID " + id + " não encontrado.");
            return;
        }

        // 3. EXIBIR DADOS E PEDIR CONFIRMAÇÃO
        System.out.println("\n--- ALUNO ENCONTRADO ---");
        System.out.println("ID:    " + alunoParaExcluir.getId());
        System.out.println("Nome:  " + alunoParaExcluir.getNome());
        System.out.println("Curso: " + alunoParaExcluir.getCurso());
        System.out.println("Idade: " + alunoParaExcluir.getIdade());
        System.out.println("-------------------------");
        
        System.out.print("Tem certeza que deseja EXCLUIR este aluno? (S/N): ");
        String confirmacao = scanner.nextLine().trim().toLowerCase();

        // 4. PROCESSAR CONFIRMAÇÃO
        if (confirmacao.equals("s")) {
            dao.excluirAluno(id);
            // Assumindo que o método dao.excluirAluno() exibe uma mensagem de sucesso
            // Se não exibir, adicione aqui: System.out.println("✅ Aluno excluído com sucesso!");
        } else {
            System.out.println("🚫 Exclusão cancelada pelo usuário.");
        }
    }
    
   
}
