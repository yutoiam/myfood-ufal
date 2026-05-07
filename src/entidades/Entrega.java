package entidades;

public class Entrega {
    private int id;
    private int pedidoId;
    private int entregadorId;
    private String destino;

    public Entrega() {
    }

    public Entrega(int id, int pedidoId, int entregadorId, String destino) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.entregadorId = entregadorId;
        this.destino = destino;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPedidoId() { return pedidoId; }
    public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }
    public int getEntregadorId() { return entregadorId; }
    public void setEntregadorId(int entregadorId) { this.entregadorId = entregadorId; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
}