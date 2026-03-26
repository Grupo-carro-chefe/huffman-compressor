/**
 * Teste manual para MinHeap.
 * Execute: javac -cp src test/TesteMinHeap.java && java -ea -cp src:test TesteMinHeap
 */
public class TesteMinHeap {
    public static void main(String[] args) {
        huffman.estrutura.MinHeap heap = new huffman.estrutura.MinHeap();

        // Teste 1: heap vazio
        assert heap.tamanho() == 0 : "FALHOU: heap deveria ter tamanho 0";
        assert heap.estaVazio() : "FALHOU: heap deveria estar vazio";
        System.out.println("OK: heap vazio");

        // Teste 2: inserção e remoção em ordem
        heap.inserir(new huffman.estrutura.No('A', 3));
        heap.inserir(new huffman.estrutura.No('B', 1));
        heap.inserir(new huffman.estrutura.No('N', 2));

        assert heap.tamanho() == 3 : "FALHOU: tamanho deveria ser 3";
        System.out.println("OK: inserção de 3 nós");

        huffman.estrutura.No min1 = heap.removerMinimo();
        assert min1.frequencia == 1 : "FALHOU: primeiro minimo deveria ser 1, foi " + min1.frequencia;
        assert min1.caractere == 'B' : "FALHOU: deveria ser B";
        System.out.println("OK: primeiro removerMinimo retornou B(1)");

        huffman.estrutura.No min2 = heap.removerMinimo();
        assert min2.frequencia == 2 : "FALHOU: segundo minimo deveria ser 2, foi " + min2.frequencia;
        assert min2.caractere == 'N' : "FALHOU: deveria ser N";
        System.out.println("OK: segundo removerMinimo retornou N(2)");

        huffman.estrutura.No min3 = heap.removerMinimo();
        assert min3.frequencia == 3 : "FALHOU: terceiro minimo deveria ser 3, foi " + min3.frequencia;
        assert min3.caractere == 'A' : "FALHOU: deveria ser A";
        System.out.println("OK: terceiro removerMinimo retornou A(3)");

        assert heap.estaVazio() : "FALHOU: heap deveria estar vazio após remover tudo";
        System.out.println("OK: heap vazio após remoções");

        // Teste 3: inserção em massa
        for (int i = 10; i >= 1; i--) {
            heap.inserir(new huffman.estrutura.No((char)('A' + i), i));
        }
        int anterior = -1;
        while (!heap.estaVazio()) {
            int freq = heap.removerMinimo().frequencia;
            assert freq >= anterior : "FALHOU: heap não está em ordem crescente: " + freq + " < " + anterior;
            anterior = freq;
        }
        System.out.println("OK: remoções em ordem crescente para 10 elementos");

        System.out.println("\nTodos os testes de MinHeap passaram!");
    }
}
