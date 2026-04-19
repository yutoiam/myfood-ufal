import entidades.Empresa;
import entidades.Pedido;
import entidades.Produto;
import entidades.Usuario;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Facade {
    private List<Usuario> usuarios;
    private List<Empresa> empresas;
    private List<Pedido> pedidos;

    public Facade() {
        this.usuarios = new ArrayList<>();
        this.empresas = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        carregarDados();
    }

    public void zerarSistema() {
        this.usuarios.clear();
        this.empresas.clear();
        this.pedidos.clear();
        new File("usuarios.xml").delete();
        new File("empresas.xml").delete();
        new File("pedidos.xml").delete();
    }

    public void encerrarSistema() {
        salvarDados();
        System.out.println("Sistema encerrado.");
    }

    private void salvarDados() {
        try {
            XMLEncoder encoderU = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("usuarios.xml")));
            encoderU.writeObject(this.usuarios);
            encoderU.close();

            XMLEncoder encoderE = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("empresas.xml")));
            encoderE.writeObject(this.empresas);
            encoderE.close();

            XMLEncoder encoderP = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("pedidos.xml")));
            encoderP.writeObject(this.pedidos);
            encoderP.close();
        } catch (Exception e) {
            System.out.println("Erro ao salvar dados no XML: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        try {
            File fUsuarios = new File("usuarios.xml");
            if (fUsuarios.exists()) {
                XMLDecoder decoderU = new XMLDecoder(new BufferedInputStream(new FileInputStream("usuarios.xml")));
                this.usuarios = (List<Usuario>) decoderU.readObject();
                decoderU.close();
            }

            File fEmpresas = new File("empresas.xml");
            if (fEmpresas.exists()) {
                XMLDecoder decoderE = new XMLDecoder(new BufferedInputStream(new FileInputStream("empresas.xml")));
                this.empresas = (List<Empresa>) decoderE.readObject();
                decoderE.close();
            }

            File fPedidos = new File("pedidos.xml");
            if (fPedidos.exists()) {
                XMLDecoder decoderP = new XMLDecoder(new BufferedInputStream(new FileInputStream("pedidos.xml")));
                this.pedidos = (List<Pedido>) decoderP.readObject();
                decoderP.close();
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados do XML: " + e.getMessage());
        }
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
        Usuario u = buscarUsuarioPorId(id);
        if (atributo.equals("nome")) return u.getNome();
        if (atributo.equals("email")) return u.getEmail();
        if (atributo.equals("senha")) return u.getSenha();
        if (atributo.equals("endereco")) return u.getEndereco();
        if (atributo.equals("cpf")) {
            if (u.getCpf() == null) throw new Exception("Usuario nao possui cpf");
            return u.getCpf();
        }
        return "Atributo não mapeado";
    }

    private Usuario buscarUsuarioPorId(int id) throws Exception {
        for (Usuario u : usuarios) {
            if (u.getId() == id) return u;
        }
        throw new Exception("Usuario nao cadastrado.");
    }

    private Empresa buscarEmpresaPorId(int id) {
        for (Empresa e : empresas) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, String tipoCozinha) throws Exception {
        if (tipoEmpresa == null || tipoEmpresa.trim().isEmpty()) throw new Exception("Tipo de empresa invalido");
        if (nome == null || nome.trim().isEmpty()) throw new Exception("Nome invalido");
        if (endereco == null || endereco.trim().isEmpty()) throw new Exception("Endereco invalido");
        if (tipoCozinha == null || tipoCozinha.trim().isEmpty()) throw new Exception("Tipo de cozinha invalido");

        Usuario dono = buscarUsuarioPorId(donoId);
        if (dono.getCpf() == null || dono.getCpf().trim().isEmpty()) {
            throw new Exception("Usuario nao pode criar uma empresa");
        }

        for (Empresa emp : empresas) {
            if (emp.getNome().equals(nome)) {
                if (emp.getDonoId() != donoId) {
                    throw new Exception("Empresa com esse nome ja existe");
                } else {
                    if (emp.getEndereco().equals(endereco)) {
                        throw new Exception("Proibido cadastrar duas empresas com o mesmo nome e local");
                    }
                }
            }
        }

        int novoId = empresas.size() + 1;
        Empresa novaEmpresa = new Empresa(novoId, donoId, nome, tipoCozinha, endereco);
        empresas.add(novaEmpresa);
        return novoId;
    }

    public String getAtributoEmpresa(int empresaId, String atributo) throws Exception {
        Empresa e = buscarEmpresaPorId(empresaId);
        if (e == null) throw new Exception("Empresa nao cadastrada");
        if (atributo == null || atributo.trim().isEmpty()) throw new Exception("Atributo invalido");

        if (atributo.equals("nome")) return e.getNome();
        if (atributo.equals("tipoCozinha")) return e.getTipoCozinha();
        if (atributo.equals("endereco")) return e.getEndereco();
        if (atributo.equals("dono")) return buscarUsuarioPorId(e.getDonoId()).getNome();
        throw new Exception("Atributo invalido");
    }

    public String getEmpresasDoUsuario(int idDono) throws Exception {
        Usuario dono = buscarUsuarioPorId(idDono);
        if (dono.getCpf() == null || dono.getCpf().trim().isEmpty()) {
            throw new Exception("Usuario nao pode criar uma empresa");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{[");
        boolean primeira = true;
        for (Empresa emp : empresas) {
            if (emp.getDonoId() == idDono) {
                if (!primeira) sb.append(", ");
                sb.append("[").append(emp.getNome()).append(", ").append(emp.getEndereco()).append("]");
                primeira = false;
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    public int getIdEmpresa(int idDono, String nome, int indice) throws Exception {
        if (nome == null || nome.trim().isEmpty()) throw new Exception("Nome invalido");
        if (indice < 0) throw new Exception("Indice invalido");

        int count = 0;
        boolean existeNome = false;

        for (Empresa emp : empresas) {
            if (emp.getDonoId() == idDono && emp.getNome().equals(nome)) {
                existeNome = true;
                if (count == indice) return emp.getId();
                count++;
            }
        }

        if (!existeNome) throw new Exception("Nao existe empresa com esse nome");
        throw new Exception("Indice maior que o esperado");
    }

    public int criarProduto(int empresaId, String nome, double valor, String categoria) throws Exception {
        if (nome == null || nome.trim().isEmpty()) throw new Exception("Nome invalido");
        if (valor < 0) throw new Exception("Valor invalido");
        if (categoria == null || categoria.trim().isEmpty()) throw new Exception("Categoria invalido");

        Empresa emp = buscarEmpresaPorId(empresaId);
        if (emp == null) throw new Exception("Empresa nao encontrada");

        for (Produto p : emp.getProdutos()) {
            if (p.getNome().equals(nome)) {
                throw new Exception("Ja existe um produto com esse nome para essa empresa");
            }
        }

        int novoId = 1;
        for (Empresa e : empresas) novoId += e.getProdutos().size();

        Produto novoProduto = new Produto(novoId, nome, valor, categoria);
        emp.getProdutos().add(novoProduto);
        return novoId;
    }

    public void editarProduto(int produtoId, String nome, double valor, String categoria) throws Exception {
        if (nome == null || nome.trim().isEmpty()) throw new Exception("Nome invalido");
        if (valor < 0) throw new Exception("Valor invalido");
        if (categoria == null || categoria.trim().isEmpty()) throw new Exception("Categoria invalido");

        Produto alvo = null;
        for (Empresa e : empresas) {
            for (Produto p : e.getProdutos()) {
                if (p.getId() == produtoId) {
                    alvo = p;
                    break;
                }
            }
            if (alvo != null) break;
        }

        if (alvo == null) throw new Exception("Produto nao cadastrado");

        alvo.setNome(nome);
        alvo.setValor(valor);
        alvo.setCategoria(categoria);
    }

    public String getProduto(String nome, int empresaId, String atributo) throws Exception {
        Empresa emp = buscarEmpresaPorId(empresaId);
        if (emp == null) throw new Exception("Empresa nao encontrada");

        Produto alvo = null;
        for (Produto p : emp.getProdutos()) {
            if (p.getNome().equals(nome)) {
                alvo = p;
                break;
            }
        }

        if (alvo == null) throw new Exception("Produto nao encontrado");

        if (atributo.equals("valor")) return String.format(java.util.Locale.US, "%.2f", alvo.getValor());
        if (atributo.equals("categoria")) return alvo.getCategoria();
        if (atributo.equals("empresa")) return emp.getNome();
        throw new Exception("Atributo nao existe");
    }

    public String listarProdutos(int empresaId) throws Exception {
        Empresa emp = buscarEmpresaPorId(empresaId);
        if (emp == null) throw new Exception("Empresa nao encontrada");

        StringBuilder sb = new StringBuilder();
        sb.append("{[");
        boolean primeira = true;
        for (Produto p : emp.getProdutos()) {
            if (!primeira) sb.append(", ");
            sb.append(p.getNome());
            primeira = false;
        }
        sb.append("]}");
        return sb.toString();
    }

    private Pedido buscarPedidoPorId(int numero) {
        for (Pedido p : pedidos) {
            if (p.getNumero() == numero) return p;
        }
        return null;
    }

    public int criarPedido(int clienteId, int empresaId) throws Exception {
        Usuario u = buscarUsuarioPorId(clienteId);
        if (u.getCpf() != null) {
            throw new Exception("Dono de empresa nao pode fazer um pedido");
        }

        for (Pedido p : pedidos) {
            if (p.getClienteId() == clienteId && p.getEmpresaId() == empresaId && p.getEstado().equals("aberto")) {
                throw new Exception("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
            }
        }

        int novoId = pedidos.size() + 1;
        Pedido novoPedido = new Pedido(novoId, clienteId, empresaId);
        pedidos.add(novoPedido);
        return novoId;
    }

    public int getNumeroPedido(int clienteId, int empresaId, int indice) {
        int count = 0;
        for (Pedido p : pedidos) {
            if (p.getClienteId() == clienteId && p.getEmpresaId() == empresaId) {
                if (count == indice) return p.getNumero();
                count++;
            }
        }
        return -1;
    }

    public void adicionarProduto(int numeroPedido, int produtoId) throws Exception {
        Pedido pedido = buscarPedidoPorId(numeroPedido);
        if (pedido == null) throw new Exception("Nao existe pedido em aberto");
        if (!pedido.getEstado().equals("aberto")) throw new Exception("Nao e possivel adcionar produtos a um pedido fechado");

        Produto produtoEncontrado = null;
        Empresa empresaDonaDoProduto = null;

        for (Empresa e : empresas) {
            for (Produto p : e.getProdutos()) {
                if (p.getId() == produtoId) {
                    produtoEncontrado = p;
                    empresaDonaDoProduto = e;
                    break;
                }
            }
            if (produtoEncontrado != null) break;
        }

        if (produtoEncontrado == null) throw new Exception("Produto nao cadastrado");

        if (empresaDonaDoProduto.getId() != pedido.getEmpresaId()) {
            throw new Exception("O produto nao pertence a essa empresa");
        }

        pedido.adicionarProduto(produtoEncontrado);
    }

    public String getPedidos(int numero, String atributo) throws Exception {
        if (atributo == null || atributo.trim().isEmpty()) throw new Exception("Atributo invalido");

        Pedido p = buscarPedidoPorId(numero);
        if (p == null) throw new Exception("Pedido nao encontrado");

        if (atributo.equals("cliente")) return buscarUsuarioPorId(p.getClienteId()).getNome();
        if (atributo.equals("empresa")) return buscarEmpresaPorId(p.getEmpresaId()).getNome();
        if (atributo.equals("estado")) return p.getEstado();
        if (atributo.equals("valor")) return String.format(java.util.Locale.US, "%.2f", p.getValorTotal());
        if (atributo.equals("produtos")) {
            StringBuilder sb = new StringBuilder();
            sb.append("{[");
            boolean primeiro = true;
            for (Produto prod : p.getProdutos()) {
                if (!primeiro) sb.append(", ");
                sb.append(prod.getNome());
                primeiro = false;
            }
            sb.append("]}");
            return sb.toString();
        }
        throw new Exception("Atributo nao existe");
    }

    public void fecharPedido(int numero) throws Exception {
        Pedido p = buscarPedidoPorId(numero);
        if (p == null) throw new Exception("Pedido nao encontrado");
        p.setEstado("preparando");
    }

    public void removerProduto(int numeroPedido, String nomeProduto) throws Exception {
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) throw new Exception("Produto invalido");

        Pedido p = buscarPedidoPorId(numeroPedido);
        if (p == null) throw new Exception("Pedido nao encontrado");

        if (!p.getEstado().equals("aberto")) {
            throw new Exception("Nao e possivel remover produtos de um pedido fechado");
        }

        boolean removido = p.removerProduto(nomeProduto);
        if (!removido) {
            throw new Exception("Produto nao encontrado");
        }
    }
}