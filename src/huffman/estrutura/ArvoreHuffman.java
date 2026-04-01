package huffman.estrutura;

/**
 * Constroi e percorre a Arvore de Huffman.
 * Usa o MinHeap para montar a arvore e gera a tabela de codigos.
 *
 * Responsavel: André
 */
public class ArvoreHuffman {

    private No raiz;

    /**
     * Constroi a arvore a partir do vetor de frequencias (tamanho 256).
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
     * Gera a tabela de codigos percorrendo a arvore recursivamente.
     * Esquerda = '0', Direita = '1'.
     *
     * @return vetor de Strings[256] com o codigo de cada caractere
     */
    public String[] gerarTabela() {
        String[] tabela = new String[256];
        gerarTabelaRecursivo(raiz, "", tabela);
        return tabela;
    }

    private void gerarTabelaRecursivo(No no, String codigo, String[] tabela) {
        if (no == null) {
            return;
        }

        if (no.eFolha()) {
            tabela[(int) no.caractere] = codigo.isEmpty() ? "0" : codigo;
            return;
        }

        gerarTabelaRecursivo(no.esquerda, codigo + "0", tabela);
        gerarTabelaRecursivo(no.direita, codigo + "1", tabela);
    }

    public No getRaiz() {
        return raiz;
    }

    /**
     * Imprime a arvore em pre-ordem para ETAPA 3 do console.
     */
    public void imprimir() {
        imprimirRecursivo(raiz, 0, new int[]{0});
    }

    private void imprimirRecursivo(No no, int nivel, int[] contadorInterno) {
        if (no == null) {
            return;
        }

        String indent = "  ".repeat(nivel);

        if (nivel == 0) {
            System.out.println("- (RAIZ, " + no.frequencia + ")");
        } else if (no.eFolha()) {
            System.out.println(indent + "- ('" + no.caractere + "', " + no.frequencia + ")");
        } else {
            contadorInterno[0]++;
            System.out.println(indent + "- (N" + contadorInterno[0] + ", " + no.frequencia + ")");
        }

        imprimirRecursivo(no.esquerda, nivel + 1, contadorInterno);
        imprimirRecursivo(no.direita, nivel + 1, contadorInterno);
    }

    /**
     * Retorna o estado inicial do heap para impressao na ETAPA 2.
     */
    public static String heapInicialToString(int[] frequencias) {
        MinHeap heap = new MinHeap();

        for (int i = 0; i < 256; i++) {
            if (frequencias[i] > 0) {
                heap.inserir(new No((char) i, frequencias[i]));
            }
        }

        return heap.toString();
    }
}
