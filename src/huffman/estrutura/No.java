package huffman.estrutura;

/**
 * Representa um nó da Árvore de Huffman.
 * Usado tanto como folha (com caractere) quanto como nó interno.
 *
 * Responsável: Rodrigo
 */
public class No implements Comparable<No> {

    public char caractere;
    public int frequencia;
    public No esquerda;
    public No direita;

    // Construtor para nó folha (caractere real)
    public No(char caractere, int frequencia) {
        this.caractere = caractere;
        this.frequencia = frequencia;
        this.esquerda = null;
        this.direita = null;
    }

    // Construtor para nó interno (sem caractere)
    public No(int frequencia, No esquerda, No direita) {
        this.caractere = '\0';
        this.frequencia = frequencia;
        this.esquerda = esquerda;
        this.direita = direita;
    }

    public boolean eFolha() {
        return esquerda == null && direita == null;
    }

    @Override
    public int compareTo(No outro) {
        return this.frequencia - outro.frequencia;
    }

    @Override
    public String toString() {
        if (eFolha()) {
            return "No('" + caractere + "'," + frequencia + ")";
        }
        return "No(interno," + frequencia + ")";
    }
}
