import entidades.Empresa;
import entidades.Produto;
import entidades.Usuario;
import java.util.ArrayList;
import java.util.List;

public class EmpresaController {
    private List<Empresa> empresas;
    private UsuarioController uc;

    public EmpresaController(UsuarioController uc) {
        this.empresas = new ArrayList<>();
        this.uc = uc;
    }

    public List<Empresa> getEmpresas() { return empresas; }
    public void setEmpresas(List<Empresa> empresas) { this.empresas = empresas; }

    public Empresa buscarEmpresaPorId(int id) {
        for (Empresa e : empresas) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    private void validarDadosBasicosEmpresa(String tipoEmpresa, int donoId, String nome, String endereco) throws MyFoodException {
        if (tipoEmpresa == null || tipoEmpresa.trim().isEmpty()) throw new MyFoodException("Tipo de empresa invalido");
        if (nome == null || nome.trim().isEmpty()) throw new MyFoodException("Nome invalido");
        if (endereco == null || endereco.trim().isEmpty()) throw new MyFoodException("Endereco da empresa invalido");
    }

    private void validarDuplicatasEDono(int donoId, String nome, String endereco) throws MyFoodException {
        Usuario dono = uc.buscarUsuarioPorId(donoId);
        if (dono.getCpf() == null || dono.getCpf().trim().isEmpty()) {
            throw new MyFoodException("Usuario nao pode criar uma empresa");
        }

        for (Empresa emp : empresas) {
            if (emp.getNome().equals(nome)) {
                if (emp.getDonoId() != donoId) {
                    throw new MyFoodException("Empresa com esse nome ja existe");
                } else if (emp.getEndereco().equals(endereco)) {
                    throw new MyFoodException("Proibido cadastrar duas empresas com o mesmo nome e local");
                }
            }
        }
    }

    private void validarHorario(String abre, String fecha) throws MyFoodException {
        if (abre == null || fecha == null) throw new MyFoodException("Horario invalido");
        if (abre.length() != 5 || abre.indexOf(':') != 2) throw new MyFoodException("Formato de hora invalido");
        if (fecha.length() != 5 || fecha.indexOf(':') != 2) throw new MyFoodException("Formato de hora invalido");

        try {
            int hA = Integer.parseInt(abre.substring(0, 2));
            int mA = Integer.parseInt(abre.substring(3, 5));
            int hF = Integer.parseInt(fecha.substring(0, 2));
            int mF = Integer.parseInt(fecha.substring(3, 5));

            if (hA < 0 || hA > 23 || mA < 0 || mA > 59) throw new MyFoodException("Horario invalido");
            if (hF < 0 || hF > 23 || mF < 0 || mF > 59) throw new MyFoodException("Horario invalido");
            if ((hA * 60 + mA) >= (hF * 60 + mF)) throw new MyFoodException("Horario invalido");
        } catch (NumberFormatException e) {
            throw new MyFoodException("Formato de hora invalido");
        }
    }

    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, String tipoCozinha) throws MyFoodException {
        validarDadosBasicosEmpresa(tipoEmpresa, donoId, nome, endereco);
        if (tipoCozinha == null || tipoCozinha.trim().isEmpty()) throw new MyFoodException("Tipo de cozinha invalido");
        validarDuplicatasEDono(donoId, nome, endereco);
        int novoId = empresas.size() + 1;
        Empresa novaEmpresa = new Empresa(novoId, donoId, nome, tipoCozinha, endereco);
        novaEmpresa.setTipoEmpresa(tipoEmpresa);
        empresas.add(novaEmpresa);
        return novoId;
    }

    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, String abre, String fecha, String tipoMercado) throws MyFoodException {
        validarDadosBasicosEmpresa(tipoEmpresa, donoId, nome, endereco);
        validarHorario(abre, fecha);
        if (tipoMercado == null || tipoMercado.trim().isEmpty()) throw new MyFoodException("Tipo de mercado invalido");
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

    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios) throws MyFoodException {
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

    public void alterarFuncionamento(int mercadoId, String abre, String fecha) throws MyFoodException {
        Empresa e = buscarEmpresaPorId(mercadoId);
        if (e == null || !"mercado".equals(e.getTipoEmpresa())) throw new MyFoodException("Nao e um mercado valido");
        validarHorario(abre, fecha);
        e.setAbre(abre);
        e.setFecha(fecha);
    }

    public String getAtributoEmpresa(int empresaId, String atributo) throws MyFoodException {
        Empresa e = buscarEmpresaPorId(empresaId);
        if (e == null) throw new MyFoodException("Empresa nao cadastrada");
        if (atributo == null || atributo.trim().isEmpty()) throw new MyFoodException("Atributo invalido");

        switch (atributo) {
            case "nome": return e.getNome();
            case "tipoCozinha": return e.getTipoCozinha();
            case "endereco": return e.getEndereco();
            case "dono": return uc.buscarUsuarioPorId(e.getDonoId()).getNome();
            case "abre": return e.getAbre();
            case "fecha": return e.getFecha();
            case "tipoMercado": return e.getTipoMercado();
            case "aberto24Horas": return String.valueOf(e.isAberto24Horas());
            case "numeroFuncionarios": return String.valueOf(e.getNumeroFuncionarios());
            default: throw new MyFoodException("Atributo invalido");
        }
    }

    public String getEmpresasDoUsuario(int idDono) throws MyFoodException {
        Usuario dono = uc.buscarUsuarioPorId(idDono);
        if (dono.getCpf() == null || dono.getCpf().trim().isEmpty()) throw new MyFoodException("Usuario nao pode criar uma empresa");

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

    public int getIdEmpresa(int idDono, String nome, int indice) throws MyFoodException {
        if (nome == null || nome.trim().isEmpty()) throw new MyFoodException("Nome invalido");
        if (indice < 0) throw new MyFoodException("Indice invalido");

        int count = 0;
        boolean existeNome = false;
        for (Empresa emp : empresas) {
            if (emp.getDonoId() == idDono && emp.getNome().equals(nome)) {
                existeNome = true;
                if (count == indice) return emp.getId();
                count++;
            }
        }
        if (!existeNome) throw new MyFoodException("Nao existe empresa com esse nome");
        throw new MyFoodException("Indice maior que o esperado");
    }

    public int criarProduto(int empresaId, String nome, double valor, String categoria) throws MyFoodException {
        if (nome == null || nome.trim().isEmpty()) throw new MyFoodException("Nome invalido");
        if (valor < 0) throw new MyFoodException("Valor invalido");
        if (categoria == null || categoria.trim().isEmpty()) throw new MyFoodException("Categoria invalido");

        Empresa emp = buscarEmpresaPorId(empresaId);
        if (emp == null) throw new MyFoodException("Empresa nao encontrada");

        for (Produto p : emp.getProdutos()) {
            if (p.getNome().equals(nome)) throw new MyFoodException("Ja existe um produto com esse nome para essa empresa");
        }

        int novoId = 1;
        for (Empresa e : empresas) novoId += e.getProdutos().size();
        emp.getProdutos().add(new Produto(novoId, nome, valor, categoria));
        return novoId;
    }

    public void editarProduto(int produtoId, String nome, double valor, String categoria) throws MyFoodException {
        if (nome == null || nome.trim().isEmpty()) throw new MyFoodException("Nome invalido");
        if (valor < 0) throw new MyFoodException("Valor invalido");
        if (categoria == null || categoria.trim().isEmpty()) throw new MyFoodException("Categoria invalido");

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
        if (alvo == null) throw new MyFoodException("Produto nao cadastrado");
        alvo.setNome(nome);
        alvo.setValor(valor);
        alvo.setCategoria(categoria);
    }

    public String getProduto(String nome, int empresaId, String atributo) throws MyFoodException {
        Empresa emp = buscarEmpresaPorId(empresaId);
        if (emp == null) throw new MyFoodException("Empresa nao encontrada");

        Produto alvo = null;
        for (Produto p : emp.getProdutos()) {
            if (p.getNome().equals(nome)) {
                alvo = p;
                break;
            }
        }
        if (alvo == null) throw new MyFoodException("Produto nao encontrado");

        switch (atributo) {
            case "valor": return String.format(java.util.Locale.US, "%.2f", alvo.getValor());
            case "categoria": return alvo.getCategoria();
            case "empresa": return emp.getNome();
            default: throw new MyFoodException("Atributo nao existe");
        }
    }

    public String listarProdutos(int empresaId) throws MyFoodException {
        Empresa emp = buscarEmpresaPorId(empresaId);
        if (emp == null) throw new MyFoodException("Empresa nao encontrada");

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

    public void cadastrarEntregador(int empresaId, int entregadorId) throws MyFoodException {
        Usuario entregador = uc.buscarUsuarioPorId(entregadorId);
        if (entregador.getVeiculo() == null || entregador.getVeiculo().trim().isEmpty()) {
            throw new MyFoodException("Usuario nao e um entregador");
        }
        Empresa empresa = buscarEmpresaPorId(empresaId);
        if (empresa != null) {
            if (!empresa.getEntregadores().contains(entregadorId)) {
                empresa.getEntregadores().add(entregadorId);
            }
        }
    }

    public String getEntregadores(int empresaId) throws MyFoodException {
        Empresa empresa = buscarEmpresaPorId(empresaId);
        if (empresa == null) throw new MyFoodException("Empresa nao encontrada");

        StringBuilder sb = new StringBuilder();
        sb.append("{[");
        boolean primeiro = true;
        for (int idEntregador : empresa.getEntregadores()) {
            if (!primeiro) sb.append(", ");
            Usuario u = uc.buscarUsuarioPorId(idEntregador);
            sb.append(u.getEmail());
            primeiro = false;
        }
        sb.append("]}");
        return sb.toString();
    }

    public String getEmpresas(int entregadorId) throws MyFoodException {
        Usuario entregador = uc.buscarUsuarioPorId(entregadorId);
        if (entregador.getVeiculo() == null || entregador.getVeiculo().trim().isEmpty()) {
            throw new MyFoodException("Usuario nao e um entregador");
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
}