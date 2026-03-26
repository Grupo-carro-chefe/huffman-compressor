/**
 * Teste manual para a classe No.
 * Execute: javac -cp src test/TesteNo.java && java -ea -cp src:test TesteNo
 */
public class TesteNo {
    public static void main(String[] args) {
        // Teste 1: nó folha
        huffman.estrutura.No folha = new huffman.estrutura.No('A', 3);
        assert folha.caractere == 'A' : "FALHOU: caractere errado";
        assert folha.frequencia == 3 : "FALHOU: frequencia errada";
        assert folha.eFolha() : "FALHOU: deveria ser folha";
        System.out.println("OK: No folha criado corretamente");

        // Teste 2: nó interno
        huffman.estrutura.No esq = new huffman.estrutura.No('B', 1);
        huffman.estrutura.No dir = new huffman.estrutura.No('N', 2);
        huffman.estrutura.No interno = new huffman.estrutura.No(3, esq, dir);
        assert !interno.eFolha() : "FALHOU: nao deveria ser folha";
        assert interno.frequencia == 3 : "FALHOU: frequencia do interno errada";
        System.out.println("OK: No interno criado corretamente");

        // Teste 3: compareTo
        huffman.estrutura.No a = new huffman.estrutura.No('A', 5);
        huffman.estrutura.No b = new huffman.estrutura.No('B', 2);
        assert a.compareTo(b) > 0 : "FALHOU: A(5) deveria ser maior que B(2)";
        assert b.compareTo(a) < 0 : "FALHOU: B(2) deveria ser menor que A(5)";
        assert a.compareTo(a) == 0 : "FALHOU: comparar consigo mesmo deveria ser 0";
        System.out.println("OK: compareTo funciona corretamente");

        System.out.println("\nTodos os testes de No passaram!");
    }
}
