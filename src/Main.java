import easyaccept.EasyAccept;

public class Main {
    public static void main(String[] args) {
        String[] testes = {
                "testes/us1_1.txt", "testes/us1_2.txt",
                "testes/us2_1.txt", "testes/us2_2.txt",
                "testes/us3_1.txt", "testes/us3_2.txt",
                "testes/us4_1.txt", "testes/us4_2.txt",
                "testes/us5_1.txt", "testes/us5_2.txt",
                "testes/us6_1.txt", "testes/us6_2.txt",
                "testes/us7_1.txt", "testes/us7_2.txt",
                "testes/us8_1.txt", "testes/us8_2.txt"
        };

        for (String teste : testes) {
            System.out.println("--------------------------------------------------");
            String[] parametros = {"Facade", teste};
            EasyAccept.main(parametros);
        }
    }
}