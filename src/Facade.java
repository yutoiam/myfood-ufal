import entidades.Usuario;
import java.util.ArrayList;
import java.util.List;

public class Facade {
    private List<Usuario> usuarios;

    public Facade() {
        this.usuarios = new ArrayList<>();
    }

    public void zerarSistema() {
        this.usuarios.clear();
    }

    public void encerrarSistema() {
        System.out.println("Sistema encerrado.");
    }

    public void criarUsuario(String nome, String email, String senha, String endereco) throws Exception {
        validarDadosComuns(nome, email, senha, endereco);
        verificarEmailDuplicado(email);

        int novoId = usuarios.size() + 1;
        Usuario novoUsuario = new Usuario(novoId, nome, email, senha, endereco, null);
        usuarios.add(novoUsuario);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws Exception {
        validarDadosComuns(nome, email, senha, endereco);

        if (cpf == null || cpf.trim().isEmpty() || cpf.length() != 14) {
            throw new Exception("CPF invalido");
        }

        verificarEmailDuplicado(email);

        int novoId = usuarios.size() + 1;
        Usuario novoUsuario = new Usuario(novoId, nome, email, senha, endereco, cpf);
        usuarios.add(novoUsuario);
    }

    private void validarDadosComuns(String nome, String email, String senha, String endereco) throws Exception {
        if (nome == null || nome.trim().isEmpty()) throw new Exception("Nome invalido");
        if (email == null || email.trim().isEmpty() || !email.contains("@")) throw new Exception("Email invalido");
        if (senha == null || senha.trim().isEmpty()) throw new Exception("Senha invalido");
        if (endereco == null || endereco.trim().isEmpty()) throw new Exception("Endereco invalido");
    }

    private void verificarEmailDuplicado(String email) throws Exception {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) {
                throw new Exception("Conta com esse email ja existe");
            }
        }
    }

    public int login(String email, String senha) throws Exception {
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            throw new Exception("Login ou senha invalidos");
        }

        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                return u.getId();
            }
        }
        throw new Exception("Login ou senha invalidos");
    }

    public String getAtributoUsuario(int id, String atributo) throws Exception {
        Usuario usuarioEncontrado = null;

        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                usuarioEncontrado = u;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            throw new Exception("Usuario nao cadastrado.");
        }

        if (atributo.equals("nome")) return usuarioEncontrado.getNome();
        if (atributo.equals("email")) return usuarioEncontrado.getEmail();
        if (atributo.equals("senha")) return usuarioEncontrado.getSenha();
        if (atributo.equals("endereco")) return usuarioEncontrado.getEndereco();
        if (atributo.equals("cpf")) {
            if (usuarioEncontrado.getCpf() == null) throw new Exception("Usuario nao possui cpf");
            return usuarioEncontrado.getCpf();
        }

        return "Atributo não mapeado";
    }
}