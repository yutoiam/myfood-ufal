import entidades.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioController {
    private List<Usuario> usuarios;

    public UsuarioController() {
        this.usuarios = new ArrayList<>();
    }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }

    public void criarUsuario(String nome, String email, String senha, String endereco) throws MyFoodException {
        validarDadosComuns(nome, email, senha, endereco);
        verificarEmailDuplicado(email);
        int novoId = usuarios.size() + 1;
        usuarios.add(new Usuario(novoId, nome, email, senha, endereco, null));
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws MyFoodException {
        validarDadosComuns(nome, email, senha, endereco);
        if (cpf == null || cpf.trim().isEmpty() || cpf.length() != 14) throw new MyFoodException("CPF invalido");
        verificarEmailDuplicado(email);
        int novoId = usuarios.size() + 1;
        usuarios.add(new Usuario(novoId, nome, email, senha, endereco, cpf));
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa) throws MyFoodException {
        validarDadosComuns(nome, email, senha, endereco);
        if (veiculo == null || veiculo.trim().isEmpty()) throw new MyFoodException("Veiculo invalido");
        if (placa == null || placa.trim().isEmpty()) throw new MyFoodException("Placa invalido");

        for (Usuario u : usuarios) {
            if (u.getPlaca() != null && u.getPlaca().equals(placa)) throw new MyFoodException("Placa invalido");
        }
        verificarEmailDuplicado(email);
        int novoId = usuarios.size() + 1;
        usuarios.add(new Usuario(novoId, nome, email, senha, endereco, veiculo, placa));
    }

    private void validarDadosComuns(String nome, String email, String senha, String endereco) throws MyFoodException {
        if (nome == null || nome.trim().isEmpty()) throw new MyFoodException("Nome invalido");
        if (email == null || email.trim().isEmpty() || !email.contains("@")) throw new MyFoodException("Email invalido");
        if (senha == null || senha.trim().isEmpty()) throw new MyFoodException("Senha invalido");
        if (endereco == null || endereco.trim().isEmpty()) throw new MyFoodException("Endereco invalido");
    }

    private void verificarEmailDuplicado(String email) throws MyFoodException {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) throw new MyFoodException("Conta com esse email ja existe");
        }
    }

    public int login(String email, String senha) throws MyFoodException {
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            throw new MyFoodException("Login ou senha invalidos");
        }
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) return u.getId();
        }
        throw new MyFoodException("Login ou senha invalidos");
    }

    public String getAtributoUsuario(int id, String atributo) throws MyFoodException {
        Usuario u = buscarUsuarioPorId(id);
        switch (atributo) {
            case "nome": return u.getNome();
            case "email": return u.getEmail();
            case "senha": return u.getSenha();
            case "endereco": return u.getEndereco();
            case "veiculo": return u.getVeiculo();
            case "placa": return u.getPlaca();
            case "cpf":
                if (u.getCpf() == null) throw new MyFoodException("Usuario nao possui cpf");
                return u.getCpf();
            default:
                return "Atributo não mapeado";
        }
    }

    public Usuario buscarUsuarioPorId(int id) throws MyFoodException {
        for (Usuario u : usuarios) {
            if (u.getId() == id) return u;
        }
        throw new MyFoodException("Usuario nao cadastrado.");
    }
}