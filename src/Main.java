import easyaccept.EasyAccept;

public class Main {
    public static void main(String[] args) {
        String[] arquivosDeTeste = new String[] {
                "Facade",
                "testes/us1_1.txt",
                "testes/us1_2.txt",
                "testes/us2_1.txt",
                "testes/us2_2.txt",
                "testes/us3_1.txt",
                "testes/us3_2.txt",
                "testes/us4_1.txt",
                "testes/us4_2.txt"
        };
        EasyAccept.main(arquivosDeTeste);
    }
}