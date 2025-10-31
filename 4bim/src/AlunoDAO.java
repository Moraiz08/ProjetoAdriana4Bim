import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    // ⚠️ ATUALIZE COM SUAS CREDENCIAIS DO MYSQL
    private static final String URL = "jdbc:mysql://localhost:3306/alunos";
    private static final String USER = "root"; 
    private static final String PASSWORD = "root";

    /**
     * Estabelece a conexão com o banco de dados.
     * Garante o uso de DriverManager e Connection[cite: 24, 27].
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 1. Inserir aluno [cite: 17]
    public void inserirAluno(Aluno aluno) { // Função inserir Aluno() [cite: 34]
        String sql = "INSERT INTO aluno (nome, curso, idade) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql); // Uso de PreparedStatement [cite: 28]

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCurso());
            stmt.setInt(3, aluno.getIdade());

            stmt.executeUpdate();
            System.out.println("✅ Aluno inserido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir aluno: " + e.getMessage());
        } finally {
            // Garante o fechamento da conexão 
            fecharRecursos(conn, stmt, null); 
        }
    }

    // 2. Listar todos os alunos [cite: 18]
    public List<Aluno> listarAlunos() { // Função listar Alunos() [cite: 35]
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM aluno";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; // Uso de ResultSet [cite: 32]

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String curso = rs.getString("curso");
                int idade = rs.getInt("idade");
                
                Aluno aluno = new Aluno(id, nome, curso, idade);
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar alunos: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs); // Garante o fechamento da conexão 
        }
        return alunos;
    }

    // 3. Atualizar dados de um aluno [cite: 19]
    public void atualizarAluno(Aluno aluno) { // Função atualizar Aluno() [cite: 37]
        String sql = "UPDATE aluno SET nome = ?, curso = ?, idade = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCurso());
            stmt.setInt(3, aluno.getIdade());
            stmt.setInt(4, aluno.getId()); // Usa o ID para WHERE

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("✅ Aluno ID " + aluno.getId() + " atualizado com sucesso!");
            } else {
                System.out.println("❌ Aluno ID " + aluno.getId() + " não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar aluno: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null); // Garante o fechamento da conexão 
        }
    }

    // 4. Excluir aluno pelo ID [cite: 20]
    public void excluirAluno(int id) { // Função excluir Aluno() [cite: 38]
        String sql = "DELETE FROM aluno WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("✅ Aluno ID " + id + " excluído com sucesso!");
            } else {
                System.out.println("❌ Aluno ID " + id + " não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir aluno: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, null); // Garante o fechamento da conexão 
        }
    }
    
    // Método auxiliar para fechamento de recursos 
    private void fecharRecursos(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }

    public Aluno SelectById(int id) {
        // A lista sempre será criada, mesmo que vazia em caso de erro.
        Aluno alunos = null;
        
        // SQL com '?' para o parâmetro dinâmico
        String sql = "SELECT id, nome, curso, idade FROM aluno WHERE id = ?"; 
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; 

        try {
            // 1. Obter a Conexão
            conn = getConnection();
            
            // 2. Preparar o Statement
            stmt = conn.prepareStatement(sql);
            
            // 🔑 CORREÇÃO CHAVE: Injetar o valor do 'id' no primeiro '?' (índice 1)
            stmt.setInt(1, id); // O primeiro '?' é o índice 1

            // 3. Executar a Query
            rs = stmt.executeQuery();

            // 4. Iterar sobre os resultados
            // Embora seja esperado apenas 1 resultado (já que 'id' é Primary Key),
            // o loop 'while' é o padrão para processar um ResultSet.
            while (rs.next()) {
                // Reutilizamos a variável 'id' do método (parâmetro)
                // ou pegamos diretamente do ResultSet, como você fez.
                int alunoId = rs.getInt("id"); 
                String nome = rs.getString("nome");
                String curso = rs.getString("curso");
                int idade = rs.getInt("idade");
                
                // Criação e adição do objeto Aluno à lista
                Aluno aluno = new Aluno(alunoId, nome, curso, idade);
                alunos = aluno;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar alunos pelo ID: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs); // Garante o fechamento da conexão 
        }
        
        return alunos;
    }
}
