public class Aluno {
    private int id;
    private String nome;
    private String curso;
    private int idade;

    // Construtor para NOVO aluno (sem ID, pois é auto-incremento)
    public Aluno(String nome, String curso, int idade) {
        this.nome = nome;
        this.curso = curso;
        this.idade = idade;
    }

    // Construtor COMPLETO (para dados vindos do banco)
    public Aluno(int id, String nome, String curso, int idade) {
        this.id = id;
        this.nome = nome;
        this.curso = curso;
        this.idade = idade;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome + ", Curso: " + curso + ", Idade: " + idade;
    }
}
