import entidades.Empresa;
import entidades.Entrega;
import entidades.Pedido;
import entidades.Usuario;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.List;

public class Facade {
    private UsuarioController usuarioController;
    private EmpresaController empresaController;
    private PedidoController pedidoController;

    public Facade() {
        this.usuarioController = new UsuarioController();
        this.empresaController = new EmpresaController(this.usuarioController);
        this.pedidoController = new PedidoController(this.usuarioController, this.empresaController);
        carregarDados();
    }

    public void zerarSistema() {
        usuarioController.getUsuarios().clear();
        empresaController.getEmpresas().clear();
        pedidoController.getPedidosList().clear();
        pedidoController.getEntregasList().clear();
        new File("usuarios.xml").delete();
        new File("empresas.xml").delete();
        new File("pedidos.xml").delete();
        new File("entregas.xml").delete();
    }

    public void encerrarSistema() {
        salvarDados();
        System.out.println("Sistema encerrado.");
    }

    private void salvarDados() {
        try {
            XMLEncoder encoderU = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("usuarios.xml")));
            encoderU.writeObject(usuarioController.getUsuarios());
            encoderU.close();

            XMLEncoder encoderE = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("empresas.xml")));
            encoderE.writeObject(empresaController.getEmpresas());
            encoderE.close();

            XMLEncoder encoderP = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("pedidos.xml")));
            encoderP.writeObject(pedidoController.getPedidosList());
            encoderP.close();

            XMLEncoder encoderEn = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("entregas.xml")));
            encoderEn.writeObject(pedidoController.getEntregasList());
            encoderEn.close();
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
                usuarioController.setUsuarios((List<Usuario>) decoderU.readObject());
                decoderU.close();
            }

            File fEmpresas = new File("empresas.xml");
            if (fEmpresas.exists()) {
                XMLDecoder decoderE = new XMLDecoder(new BufferedInputStream(new FileInputStream("empresas.xml")));
                empresaController.setEmpresas((List<Empresa>) decoderE.readObject());
                decoderE.close();
            }

            File fPedidos = new File("pedidos.xml");
            if (fPedidos.exists()) {
                XMLDecoder decoderP = new XMLDecoder(new BufferedInputStream(new FileInputStream("pedidos.xml")));
                pedidoController.setPedidosList((List<Pedido>) decoderP.readObject());
                decoderP.close();
            }

            File fEntregas = new File("entregas.xml");
            if (fEntregas.exists()) {
                XMLDecoder decoderEn = new XMLDecoder(new BufferedInputStream(new FileInputStream("entregas.xml")));
                pedidoController.setEntregasList((List<Entrega>) decoderEn.readObject());
                decoderEn.close();
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados do XML: " + e.getMessage());
        }
    }

    // Delegação de métodos do Usuario
    public void criarUsuario(String nome, String email, String senha, String endereco) throws MyFoodException {
        usuarioController.criarUsuario(nome, email, senha, endereco);
    }
    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws MyFoodException {
        usuarioController.criarUsuario(nome, email, senha, endereco, cpf);
    }
    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa) throws MyFoodException {
        usuarioController.criarUsuario(nome, email, senha, endereco, veiculo, placa);
    }
    public int login(String email, String senha) throws MyFoodException {
        return usuarioController.login(email, senha);
    }
    public String getAtributoUsuario(int id, String atributo) throws MyFoodException {
        return usuarioController.getAtributoUsuario(id, atributo);
    }

    // Delegação de métodos da Empresa e Produtos
    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, String tipoCozinha) throws MyFoodException {
        return empresaController.criarEmpresa(tipoEmpresa, donoId, nome, endereco, tipoCozinha);
    }
    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, String abre, String fecha, String tipoMercado) throws MyFoodException {
        return empresaController.criarEmpresa(tipoEmpresa, donoId, nome, endereco, abre, fecha, tipoMercado);
    }
    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios) throws MyFoodException {
        return empresaController.criarEmpresa(tipoEmpresa, donoId, nome, endereco, aberto24Horas, numeroFuncionarios);
    }
    public void alterarFuncionamento(int mercadoId, String abre, String fecha) throws MyFoodException {
        empresaController.alterarFuncionamento(mercadoId, abre, fecha);
    }
    public String getAtributoEmpresa(int empresaId, String atributo) throws MyFoodException {
        return empresaController.getAtributoEmpresa(empresaId, atributo);
    }
    public String getEmpresasDoUsuario(int idDono) throws MyFoodException {
        return empresaController.getEmpresasDoUsuario(idDono);
    }
    public int getIdEmpresa(int idDono, String nome, int indice) throws MyFoodException {
        return empresaController.getIdEmpresa(idDono, nome, indice);
    }
    public int criarProduto(int empresaId, String nome, double valor, String categoria) throws MyFoodException {
        return empresaController.criarProduto(empresaId, nome, valor, categoria);
    }
    public void editarProduto(int produtoId, String nome, double valor, String categoria) throws MyFoodException {
        empresaController.editarProduto(produtoId, nome, valor, categoria);
    }
    public String getProduto(String nome, int empresaId, String atributo) throws MyFoodException {
        return empresaController.getProduto(nome, empresaId, atributo);
    }
    public String listarProdutos(int empresaId) throws MyFoodException {
        return empresaController.listarProdutos(empresaId);
    }

    // Delegação de métodos de Pedidos e Entregas
    public int criarPedido(int clienteId, int empresaId) throws MyFoodException {
        return pedidoController.criarPedido(clienteId, empresaId);
    }
    public int getNumeroPedido(int clienteId, int empresaId, int indice) {
        return pedidoController.getNumeroPedido(clienteId, empresaId, indice);
    }
    public void adicionarProduto(int numeroPedido, int produtoId) throws MyFoodException {
        pedidoController.adicionarProduto(numeroPedido, produtoId);
    }
    public String getPedidos(int numero, String atributo) throws MyFoodException {
        return pedidoController.getPedidos(numero, atributo);
    }
    public void fecharPedido(int numero) throws MyFoodException {
        pedidoController.fecharPedido(numero);
    }
    public void removerProduto(int numeroPedido, String nomeProduto) throws MyFoodException {
        pedidoController.removerProduto(numeroPedido, nomeProduto);
    }
    public void liberarPedido(int numero) throws MyFoodException {
        pedidoController.liberarPedido(numero);
    }
    public void cadastrarEntregador(int empresaId, int entregadorId) throws MyFoodException {
        empresaController.cadastrarEntregador(empresaId, entregadorId);
    }
    public String getEntregadores(int empresaId) throws MyFoodException {
        return empresaController.getEntregadores(empresaId);
    }
    public String getEmpresas(int entregadorId) throws MyFoodException {
        return empresaController.getEmpresas(entregadorId);
    }
    public int obterPedido(int entregadorId) throws MyFoodException {
        return pedidoController.obterPedido(entregadorId);
    }
    public int criarEntrega(int pedidoId, int entregadorId, String destino) throws MyFoodException {
        return pedidoController.criarEntrega(pedidoId, entregadorId, destino);
    }
    public String getEntrega(int id, String atributo) throws MyFoodException {
        return pedidoController.getEntrega(id, atributo);
    }
    public int getIdEntrega(int pedidoId) throws MyFoodException {
        return pedidoController.getIdEntrega(pedidoId);
    }
    public void entregar(int entregaId) throws MyFoodException {
        pedidoController.entregar(entregaId);
    }
}