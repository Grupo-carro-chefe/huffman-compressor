package huffman.estrutura;

/**
 * Constrói e percorre a Árvore de Huffman.
 * Usa o MinHeap para montar a árvore e gera a tabela de códigos.
 *
 * Responsável: Nicolas
 */
public class ArvoreHuffman {

    private No raiz;

    /**
     * Constrói a árvore a partir do vetor de frequências (tamanho 256).
     */
    public void construir(int[] frequencias) {
        MinHeap heap = new MinHeap();

        for (int i = 0; i < 256; i++) {
            if (frequencias[i] > 0) {
                heap.inserir(new No((char) i, frequencias[i]));
            }
        }

        while (heap.tamanho() > 1) {
            No esq = heap.removerMinimo();
            No dir = heap.removerMinimo();
            No interno = new No(esq.frequencia + dir.frequencia, esq, dir);
            heap.inserir(interno);
        }

        raiz = heap.removerMinimo();
    }

    /**
     * Gera a tabela de códigos percorrendo a árvore recursivamente.
     * Esquerda = '0', Direita = '1'.
     *
     * @return vetor de Strings[256] com o código de cada caractere
     */
    public String[] gerarTabela() {
        String[] tabela = new String[256];
        gerarTabelaRecursivo(raiz, "", tabela);
        return tabela;
    }

    private void gerarTabelaRecursivo(No no, String codigo, String[] tabela) {
        if (no == null) return;
        if (no.eFolha()) {
            tabela[(int) no.caractere] = codigo;
            return;
        }
        gerarTabelaRecursivo(no.esquerda, codigo + "0", tabela);
        gerarTabelaRecursivo(no.direita, codigo + "1", tabela);
    }

    public No getRaiz() {
        return raiz;
    }

    /**
     * Imprime a árvore em pré-ordem para ETAPA 3 do console.
     */
    public void imprimir() {
        imprimirRecursivo(raiz, 0);
    }

    private void imprimirRecursivo(No no, int nivel) {
        if (no == null) return;
        String indent = "  ".repeat(nivel);
        if (no == raiz) {
            System.out.println("- (RAIZ, " + no.frequencia + ")");
        } else if (no.eFolha()) {
            System.out.println(indent + "- ('" + no.caractere + "', " + no.frequencia + ")");
        } else {
            System.out.println(indent + "- (N" + nivel + ", " + no.frequencia + ")");
        }
        imprimirRecursivo(no.esquerda, nivel + 1);
        imprimirRecursivo(no.direita, nivel + 1);
    }
}
