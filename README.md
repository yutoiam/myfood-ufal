# MyFood - Sistema de Delivery (UFAL)

Este projeto consiste no desenvolvimento de um sistema de delivery robusto, focado na gestão de usuários (clientes e donos de restaurantes), empresas, cardápios e pedidos. Desenvolvido como parte da disciplina de Programação 2 na UFAL.

## Funcionalidades (Milestone 1)

O sistema atualmente suporta as seguintes operações:
- **Gestão de Usuários:** Cadastro, login e consulta de atributos.
- **Gestão de Empresas:** Cadastro de restaurantes vinculados a donos e listagem por proprietário.
- **Catálogo de Produtos:** Criação, edição e listagem de itens de cardápio por empresa.
- **Sistema de Pedidos:** Carrinho de compras dinâmico, adição/remoção de itens e fechamento de pedidos.
- **Persistência de Dados:** Salvamento automático de todo o estado do sistema em arquivos **XML**.

## Arquitetura e Design

O projeto foi construído seguindo princípios de **Clean Code** e padrões de projeto para garantir manutenibilidade:

- **Facade Pattern:** Centraliza toda a lógica do sistema em uma única interface (`Facade.java`), facilitando a integração com scripts de testes automatizados.
- **Persistência com XMLEncoder/Decoder:** Implementação de serialização para garantir que os dados não sejam perdidos entre execuções.
- **Design Orientado a Objetos:** Divisão clara de responsabilidades entre as entidades `Usuario`, `Empresa`, `Produto` e `Pedido`.

## Tecnologias Utilizadas

- **Java 17+**
- **EasyAccept:** Framework para testes de aceitação.
- **XML:** Para persistência de dados.
- **IntelliJ IDEA:** Ambiente de desenvolvimento.

## Como Rodar os Testes

Para validar as funcionalidades através dos testes de aceitação:

1. Certifique-se de ter o Java instalado e o arquivo `easyaccept.jar` no seu classpath.
2. Compile o projeto.
3. Execute a classe `Main.java`.
    - O sistema executará automaticamente a bateria de testes de aceitação (US1 a US4), incluindo os testes de persistência.
    - O resultado detalhado de cada suite aparecerá no console.

## 📝 Autor

Desenvolvido por Yuri Mota.