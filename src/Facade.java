import entidades.Empresa;
import entidades.Pedido;
import entidades.Produto;
import entidades.Usuario;
import entidades.Entrega;

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
    private List<Entrega> entregas;

    public Facade() {
        this.usuarios = new ArrayList<>();
        this.empresas = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.entregas = new ArrayList<>();
        carregarDados();
    }

    public void zerarSistema() {
        this.usuarios.clear();
        this.empresas.clear();
        this.pedidos.clear();
        this.entregas.clear();
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
            encoderU.writeObject(this.usuarios);
            encoderU.close();

            XMLEncoder encoderE = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("empresas.xml")));
            encoderE.writeObject(this.empresas);
            encoderE.close();

            XMLEncoder encoderP = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("pedidos.xml")));
            encoderP.writeObject(this.pedidos);
            encoderP.close();

            XMLEncoder encoderEn = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("entregas.xml")));
            encoderEn.writeObject(this.entregas);
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

            File fEntregas = new File("entregas.xml");
            if (fEntregas.exists()) {
                XMLDecoder decoderEn = new XMLDecoder(new BufferedInputStream(new FileInputStream("entregas.xml")));
                this.entregas = (List<Entrega>) decoderEn.readObject();
                decoderEn.close();
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

    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa) throws Exception {
        validarDadosComuns(nome, email, senha, endereco);

        if (veiculo == null || veiculo.trim().isEmpty()) throw new Exception("Veiculo invalido");
        if (placa == null || placa.trim().isEmpty()) throw new Exception("Placa invalido");

        for (Usuario u : usuarios) {
            if (u.getPlaca() != null && u.getPlaca().equals(placa)) {
                throw new Exception("Placa invalido");
            }
        }

        verificarEmailDuplicado(email);

        int novoId = usuarios.size() + 1;
        Usuario novoUsuario = new Usuario(novoId, nome, email, senha, endereco, veiculo, placa);
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
        switch (atributo) {
            case "nome": return u.getNome();
            case "email": return u.getEmail();
            case "senha": return u.getSenha();
            case "endereco": return u.getEndereco();
            case "veiculo": return u.getVeiculo();
            case "placa": return u.getPlaca();
            case "cpf":
                if (u.getCpf() == null) throw new Exception("Usuario nao possui cpf");
                return u.getCpf();
            default:
                return "Atributo não mapeado";
        }
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

    private void validarDadosBasicosEmpresa(String tipoEmpresa, int donoId, String nome, String endereco) throws Exception {
        if (tipoEmpresa == null || tipoEmpresa.trim().isEmpty()) throw new Exception("Tipo de empresa invalido");
        if (nome == null || nome.trim().isEmpty()) throw new Exception("Nome invalido");
        if (endereco == null || endereco.trim().isEmpty()) throw new Exception("Endereco da empresa invalido");
    }

    private void validarDuplicatasEDono(int donoId, String nome, String endereco) throws Exception {
        Usuario dono = buscarUsuarioPorId(donoId);
        if (dono.getCpf() == null || dono.getCpf().trim().isEmpty()) {
            throw new Exception("Usuario nao pode criar uma empresa");
        }

        for (Empresa emp : empresas) {
            if (emp.getNome().equals(nome)) {
                if (emp.getDonoId() != donoId) {
                    throw new Exception("Empresa com esse nome ja existe");
                } else if (emp.getEndereco().equals(endereco)) {
                    throw new Exception("Proibido cadastrar duas empresas com o mesmo nome e local");
                }
            }
        }
    }

    private void validarHorario(String abre, String fecha) throws Exception {
        if (abre == null || fecha == null) throw new Exception("Horario invalido");
        if (abre.length() != 5 || abre.indexOf(':') != 2) throw new Exception("Formato de hora invalido");
        if (fecha.length() != 5 || fecha.indexOf(':') != 2) throw new Exception("Formato de hora invalido");

        try {
            int hA = Integer.parseInt(abre.substring(0, 2));
            int mA = Integer.parseInt(abre.substring(3, 5));
            int hF = Integer.parseInt(fecha.substring(0, 2));
            int mF = Integer.parseInt(fecha.substring(3, 5));

            if (hA < 0 || hA > 23 || mA < 0 || mA > 59) throw new Exception("Horario invalido");
            if (hF < 0 || hF > 23 || mF < 0 || mF > 59) throw new Exception("Horario invalido");
            if ((hA * 60 + mA) >= (hF * 60 + mF)) throw new Exception("Horario invalido");
        } catch (NumberFormatException e) {
            throw new Exception("Formato de hora invalido");
        }
    }

    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, String tipoCozinha) throws Exception {
        validarDadosBasicosEmpresa(tipoEmpresa, donoId, nome, endereco);
        if (tipoCozinha == null || tipoCozinha.trim().isEmpty()) throw new Exception("Tipo de cozinha invalido");

        validarDuplicatasEDono(donoId, nome, endereco);

        int novoId = empresas.size() + 1;
        Empresa novaEmpresa = new Empresa(novoId, donoId, nome, tipoCozinha, endereco);
        novaEmpresa.setTipoEmpresa(tipoEmpresa);
        empresas.add(novaEmpresa);
        return novoId;
    }

    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, String abre, String fecha, String tipoMercado) throws Exception {
        validarDadosBasicosEmpresa(tipoEmpresa, donoId, nome, endereco);
        validarHorario(abre, fecha);
        if (tipoMercado == null || tipoMercado.trim().isEmpty()) throw new Exception("Tipo de mercado invalido");

        validarDuplicatasEDono(donoId, nome, endereco);

        int novoId = empresas.size() + 1;
        Empresa novaEmpresa = new Empresa(novoId, donoId, nome, null, endereco);
        novaEmpresa.setTipoEmpresa(tipoEmpresa);
        novaEmpresa.setAbre(abre);
        novaEmpresa.setFecha(fecha);
        novaEmpresa.setTipoMercado(tipoMercado);
        empresas.add(novaEmpresa);
        return novoId;
    }

    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios) throws Exception {
        validarDadosBasicosEmpresa(tipoEmpresa, donoId, nome, endereco);
        validarDuplicatasEDono(donoId, nome, endereco);

        int novoId = empresas.size() + 1;
        Empresa novaEmpresa = new Empresa(novoId, donoId, nome, null, endereco);
        novaEmpresa.setTipoEmpresa(tipoEmpresa);
        novaEmpresa.setAberto24Horas(aberto24Horas);
        novaEmpresa.setNumeroFuncionarios(numeroFuncionarios);
        empresas.add(novaEmpresa);
        return novoId;
    }

    public void alterarFuncionamento(int mercadoId, String abre, String fecha) throws Exception {
        Empresa e = buscarEmpresaPorId(mercadoId);
        if (e == null || !"mercado".equals(e.getTipoEmpresa())) {
            throw new Exception("Nao e um mercado valido");
        }
        validarHorario(abre, fecha);
        e.setAbre(abre);
        e.setFecha(fecha);
    }

    public String getAtributoEmpresa(int empresaId, String atributo) throws Exception {
        Empresa e = buscarEmpresaPorId(empresaId);
        if (e == null) throw new Exception("Empresa nao cadastrada");
        if (atributo == null || atributo.trim().isEmpty()) throw new Exception("Atributo invalido");

        switch (atributo) {
            case "nome": return e.getNome();
            case "tipoCozinha": return e.getTipoCozinha();
            case "endereco": return e.getEndereco();
            case "dono": return buscarUsuarioPorId(e.getDonoId()).getNome();
            case "abre": return e.getAbre();
            case "fecha": return e.getFecha();
            case "tipoMercado": return e.getTipoMercado();
            case "aberto24Horas": return String.valueOf(e.isAberto24Horas());
            case "numeroFuncionarios": return String.valueOf(e.getNumeroFuncionarios());
            default: throw new Exception("Atributo invalido");
        }
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

        switch (atributo) {
            case "valor": return String.format(java.util.Locale.US, "%.2f", alvo.getValor());
            case "categoria": return alvo.getCategoria();
            case "empresa": return emp.getNome();
            default: throw new Exception("Atributo nao existe");
        }
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

        switch (atributo) {
            case "cliente": return buscarUsuarioPorId(p.getClienteId()).getNome();
            case "empresa": return buscarEmpresaPorId(p.getEmpresaId()).getNome();
            case "estado": return p.getEstado();
            case "valor": return String.format(java.util.Locale.US, "%.2f", p.getValorTotal());
            case "produtos":
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
            default: throw new Exception("Atributo nao existe");
        }
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

    public void cadastrarEntregador(int empresaId, int entregadorId) throws Exception {
        Usuario entregador = buscarUsuarioPorId(entregadorId);

        if (entregador.getVeiculo() == null || entregador.getVeiculo().trim().isEmpty()) {
            throw new Exception("Usuario nao e um entregador");
        }

        Empresa empresa = buscarEmpresaPorId(empresaId);
        if (empresa != null) {
            if (!empresa.getEntregadores().contains(entregadorId)) {
                empresa.getEntregadores().add(entregadorId);
            }
        }
    }

    public String getEntregadores(int empresaId) throws Exception {
        Empresa empresa = buscarEmpresaPorId(empresaId);
        if (empresa == null) throw new Exception("Empresa nao encontrada");

        StringBuilder sb = new StringBuilder();
        sb.append("{[");
        boolean primeiro = true;

        for (int idEntregador : empresa.getEntregadores()) {
            if (!primeiro) sb.append(", ");
            Usuario u = buscarUsuarioPorId(idEntregador);
            sb.append(u.getEmail());
            primeiro = false;
        }
        sb.append("]}");
        return sb.toString();
    }

    public String getEmpresas(int entregadorId) throws Exception {
        Usuario entregador = buscarUsuarioPorId(entregadorId);

        if (entregador.getVeiculo() == null || entregador.getVeiculo().trim().isEmpty()) {
            throw new Exception("Usuario nao e um entregador");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{[");
        boolean primeiro = true;

        for (Empresa emp : empresas) {
            if (emp.getEntregadores() != null && emp.getEntregadores().contains(entregadorId)) {
                if (!primeiro) sb.append(", ");
                sb.append("[").append(emp.getNome()).append(", ").append(emp.getEndereco()).append("]");
                primeiro = false;
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    public void liberarPedido(int numero) throws Exception {
        Pedido p = buscarPedidoPorId(numero);
        if (p == null) throw new Exception("Pedido nao encontrado");

        if (p.getEstado().equals("pronto")) {
            throw new Exception("Pedido ja liberado");
        }
        if (!p.getEstado().equals("preparando")) {
            throw new Exception("Nao e possivel liberar um produto que nao esta sendo preparado");
        }

        p.setEstado("pronto");
    }

    public int obterPedido(int entregadorId) throws Exception {
        Usuario entregador = buscarUsuarioPorId(entregadorId);
        if (entregador.getVeiculo() == null || entregador.getVeiculo().trim().isEmpty()) {
            throw new Exception("Usuario nao e um entregador");
        }

        List<Integer> empresasDoEntregador = new ArrayList<>();
        for (Empresa emp : empresas) {
            if (emp.getEntregadores() != null && emp.getEntregadores().contains(entregadorId)) {
                empresasDoEntregador.add(emp.getId());
            }
        }

        if (empresasDoEntregador.isEmpty()) {
            throw new Exception("Entregador nao estar em nenhuma empresa.");
        }

        Pedido melhorPedido = null;
        boolean temPrioridadeFarmacia = false;

        for (Pedido p : pedidos) {
            if (p.getEstado().equals("pronto") && empresasDoEntregador.contains(p.getEmpresaId())) {
                Empresa emp = buscarEmpresaPorId(p.getEmpresaId());
                boolean ehFarmacia = "farmacia".equals(emp.getTipoEmpresa());

                if (ehFarmacia) {
                    if (!temPrioridadeFarmacia) {
                        melhorPedido = p;
                        temPrioridadeFarmacia = true;
                    } else if (p.getNumero() < melhorPedido.getNumero()) {
                        melhorPedido = p;
                    }
                } else if (!temPrioridadeFarmacia) {
                    if (melhorPedido == null || p.getNumero() < melhorPedido.getNumero()) {
                        melhorPedido = p;
                    }
                }
            }
        }

        if (melhorPedido == null) {
            throw new Exception("Nao existe pedido para entrega");
        }

        return melhorPedido.getNumero();
    }

    private Entrega buscarEntregaPorId(int id) {
        for (Entrega e : entregas) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public int criarEntrega(int pedidoId, int entregadorId, String destino) throws Exception {
        Pedido p = buscarPedidoPorId(pedidoId);
        if (p == null || !p.getEstado().equals("pronto")) {
            throw new Exception("Pedido nao esta pronto para entrega");
        }

        Usuario entregador = buscarUsuarioPorId(entregadorId);
        if (entregador.getVeiculo() == null || entregador.getVeiculo().trim().isEmpty()) {
            throw new Exception("Nao e um entregador valido");
        }

        for (Entrega ent : entregas) {
            if (ent.getEntregadorId() == entregadorId) {
                Pedido pedidoDaEntrega = buscarPedidoPorId(ent.getPedidoId());
                if (pedidoDaEntrega != null && pedidoDaEntrega.getEstado().equals("entregando")) {
                    throw new Exception("Entregador ainda em entrega");
                }
            }
        }

        String destinoFinal = destino;
        if (destinoFinal == null || destinoFinal.trim().isEmpty()) {
            Usuario cliente = buscarUsuarioPorId(p.getClienteId());
            destinoFinal = cliente.getEndereco();
        }

        int novoId = entregas.size() + 1;
        Entrega novaEntrega = new Entrega(novoId, pedidoId, entregadorId, destinoFinal);
        entregas.add(novaEntrega);

        p.setEstado("entregando");

        return novoId;
    }

    public String getEntrega(int id, String atributo) throws Exception {
        if (atributo == null || atributo.trim().isEmpty()) throw new Exception("Atributo invalido");

        Entrega e = buscarEntregaPorId(id);
        if (e == null) throw new Exception("Nao existe nada para ser entregue com esse id");

        Pedido p = buscarPedidoPorId(e.getPedidoId());

        switch (atributo) {
            case "cliente": return buscarUsuarioPorId(p.getClienteId()).getNome();
            case "empresa": return buscarEmpresaPorId(p.getEmpresaId()).getNome();
            case "pedido": return String.valueOf(p.getNumero());
            case "entregador": return buscarUsuarioPorId(e.getEntregadorId()).getNome();
            case "destino": return e.getDestino();
            case "produtos": return getPedidos(p.getNumero(), "produtos");
            default: throw new Exception("Atributo nao existe");
        }
    }

    public int getIdEntrega(int pedidoId) throws Exception {
        for (Entrega e : entregas) {
            if (e.getPedidoId() == pedidoId) {
                return e.getId();
            }
        }
        throw new Exception("Nao existe entrega com esse id");
    }

    public void entregar(int entregaId) throws Exception {
        Entrega e = buscarEntregaPorId(entregaId);
        if (e == null) {
            throw new Exception("Nao existe nada para ser entregue com esse id");
        }

        Pedido p = buscarPedidoPorId(e.getPedidoId());
        if (p != null) {
            p.setEstado("entregue");
        }
    }
}