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
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Nome invalido");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Email invalido");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new Exception("Senha invalido");
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new Exception("Endereco invalido");
        }
        // TODO: Validar se o email já existe na nossa lista de usuários
        int novoId = usuarios.size() + 1;
        Usuario novoUsuario = new Usuario(novoId, nome, email, senha, endereco);
        usuarios.add(novoUsuario);
    }

    public String getAtributoUsuario(int id, String atributo) {
        return "Atributo não encontrado";
    }
}