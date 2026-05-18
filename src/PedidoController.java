import entidades.Empresa;
import entidades.Pedido;
import entidades.Produto;
import entidades.Usuario;
import entidades.Entrega;
import java.util.ArrayList;
import java.util.List;

public class PedidoController {
    private List<Pedido> pedidos;
    private List<Entrega> entregas;
    private UsuarioController uc;
    private EmpresaController ec;

    public PedidoController(UsuarioController uc, EmpresaController ec) {
        this.pedidos = new ArrayList<>();
        this.entregas = new ArrayList<>();
        this.uc = uc;
        this.ec = ec;
    }

    public List<Pedido> getPedidosList() { return pedidos; }
    public void setPedidosList(List<Pedido> pedidos) { this.pedidos = pedidos; }
    public List<Entrega> getEntregasList() { return entregas; }
    public void setEntregasList(List<Entrega> entregas) { this.entregas = entregas; }

    public Pedido buscarPedidoPorId(int numero) {
        for (Pedido p : pedidos) {
            if (p.getNumero() == numero) return p;
        }
        return null;
    }

    public int criarPedido(int clienteId, int empresaId) throws MyFoodException {
        Usuario u = uc.buscarUsuarioPorId(clienteId);
        if (u.getCpf() != null) throw new MyFoodException("Dono de empresa nao pode fazer um pedido");

        for (Pedido p : pedidos) {
            if (p.getClienteId() == clienteId && p.getEmpresaId() == empresaId && p.getEstado().equals("aberto")) {
                throw new MyFoodException("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
            }
        }
        int novoId = pedidos.size() + 1;
        pedidos.add(new Pedido(novoId, clienteId, empresaId));
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

    public void adicionarProduto(int numeroPedido, int produtoId) throws MyFoodException {
        Pedido pedido = buscarPedidoPorId(numeroPedido);
        if (pedido == null) throw new MyFoodException("Nao existe pedido em aberto");
        if (!pedido.getEstado().equals("aberto")) throw new MyFoodException("Nao e possivel adcionar produtos a um pedido fechado");

        Produto produtoEncontrado = null;
        Empresa empresaDonaDoProduto = null;
        for (Empresa e : ec.getEmpresas()) {
            for (Produto p : e.getProdutos()) {
                if (p.getId() == produtoId) {
                    produtoEncontrado = p;
                    empresaDonaDoProduto = e;
                    break;
                }
            }
            if (produtoEncontrado != null) break;
        }

        if (produtoEncontrado == null) throw new MyFoodException("Produto nao cadastrado");
        if (empresaDonaDoProduto.getId() != pedido.getEmpresaId()) throw new MyFoodException("O produto nao pertence a essa empresa");

        pedido.adicionarProduto(produtoEncontrado);
    }

    public String getPedidos(int numero, String atributo) throws MyFoodException {
        if (atributo == null || atributo.trim().isEmpty()) throw new MyFoodException("Atributo invalido");
        Pedido p = buscarPedidoPorId(numero);
        if (p == null) throw new MyFoodException("Pedido nao encontrado");

        switch (atributo) {
            case "cliente": return uc.buscarUsuarioPorId(p.getClienteId()).getNome();
            case "empresa": return ec.buscarEmpresaPorId(p.getEmpresaId()).getNome();
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
            default: throw new MyFoodException("Atributo nao existe");
        }
    }

    public void fecharPedido(int numero) throws MyFoodException {
        Pedido p = buscarPedidoPorId(numero);
        if (p == null) throw new MyFoodException("Pedido nao encontrado");
        p.setEstado("preparando");
    }

    public void removerProduto(int numeroPedido, String nomeProduto) throws MyFoodException {
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) throw new MyFoodException("Produto invalido");
        Pedido p = buscarPedidoPorId(numeroPedido);
        if (p == null) throw new MyFoodException("Pedido nao encontrado");
        if (!p.getEstado().equals("aberto")) throw new MyFoodException("Nao e possivel remover produtos de um pedido fechado");

        boolean removido = p.removerProduto(nomeProduto);
        if (!removido) throw new MyFoodException("Produto nao encontrado");
    }

    public void liberarPedido(int numero) throws MyFoodException {
        Pedido p = buscarPedidoPorId(numero);
        if (p == null) throw new MyFoodException("Pedido nao encontrado");
        if (p.getEstado().equals("pronto")) throw new MyFoodException("Pedido ja liberado");
        if (!p.getEstado().equals("preparando")) throw new MyFoodException("Nao e possivel liberar um produto que nao esta sendo preparado");
        p.setEstado("pronto");
    }

    public int obterPedido(int entregadorId) throws MyFoodException {
        Usuario entregador = uc.buscarUsuarioPorId(entregadorId);
        if (entregador.getVeiculo() == null || entregador.getVeiculo().trim().isEmpty()) {
            throw new MyFoodException("Usuario nao e um entregador");
        }

        List<Integer> empresasDoEntregador = new ArrayList<>();
        for (Empresa emp : ec.getEmpresas()) {
            if (emp.getEntregadores() != null && emp.getEntregadores().contains(entregadorId)) {
                empresasDoEntregador.add(emp.getId());
            }
        }

        if (empresasDoEntregador.isEmpty()) throw new MyFoodException("Entregador nao estar em nenhuma empresa.");

        Pedido melhorPedido = null;
        boolean temPrioridadeFarmacia = false;

        for (Pedido p : pedidos) {
            if (p.getEstado().equals("pronto") && empresasDoEntregador.contains(p.getEmpresaId())) {
                Empresa emp = ec.buscarEmpresaPorId(p.getEmpresaId());
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

        if (melhorPedido == null) throw new MyFoodException("Nao existe pedido para entrega");
        return melhorPedido.getNumero();
    }

    private Entrega buscarEntregaPorId(int id) {
        for (Entrega e : entregas) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public int criarEntrega(int pedidoId, int entregadorId, String destino) throws MyFoodException {
        Pedido p = buscarPedidoPorId(pedidoId);
        if (p == null || !p.getEstado().equals("pronto")) throw new MyFoodException("Pedido nao esta pronto para entrega");

        Usuario entregador = uc.buscarUsuarioPorId(entregadorId);
        if (entregador.getVeiculo() == null || entregador.getVeiculo().trim().isEmpty()) {
            throw new MyFoodException("Nao e um entregador valido");
        }

        for (Entrega ent : entregas) {
            if (ent.getEntregadorId() == entregadorId) {
                Pedido pedidoDaEntrega = buscarPedidoPorId(ent.getPedidoId());
                if (pedidoDaEntrega != null && pedidoDaEntrega.getEstado().equals("entregando")) {
                    throw new MyFoodException("Entregador ainda em entrega");
                }
            }
        }

        String destinoFinal = destino;
        if (destinoFinal == null || destinoFinal.trim().isEmpty()) {
            Usuario cliente = uc.buscarUsuarioPorId(p.getClienteId());
            destinoFinal = cliente.getEndereco();
        }

        int novoId = entregas.size() + 1;
        entregas.add(new Entrega(novoId, pedidoId, entregadorId, destinoFinal));
        p.setEstado("entregando");
        return novoId;
    }

    public String getEntrega(int id, String atributo) throws MyFoodException {
        if (atributo == null || atributo.trim().isEmpty()) throw new MyFoodException("Atributo invalido");
        Entrega e = buscarEntregaPorId(id);
        if (e == null) throw new MyFoodException("Nao existe nada para ser entregue com esse id");

        Pedido p = buscarPedidoPorId(e.getPedidoId());
        switch (atributo) {
            case "cliente": return uc.buscarUsuarioPorId(p.getClienteId()).getNome();
            case "empresa": return ec.buscarEmpresaPorId(p.getEmpresaId()).getNome();
            case "pedido": return String.valueOf(p.getNumero());
            case "entregador": return uc.buscarUsuarioPorId(e.getEntregadorId()).getNome();
            case "destino": return e.getDestino();
            case "produtos": return getPedidos(p.getNumero(), "produtos");
            default: throw new MyFoodException("Atributo nao existe");
        }
    }

    public int getIdEntrega(int pedidoId) throws MyFoodException {
        for (Entrega e : entregas) {
            if (e.getPedidoId() == pedidoId) return e.getId();
        }
        throw new MyFoodException("Nao existe entrega com esse id");
    }

    public void entregar(int entregaId) throws MyFoodException {
        Entrega e = buscarEntregaPorId(entregaId);
        if (e == null) throw new MyFoodException("Nao existe nada para ser entregue com esse id");
        Pedido p = buscarPedidoPorId(e.getPedidoId());
        if (p != null) p.setEstado("entregue");
    }
}