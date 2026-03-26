package huffman.estrutura;

import java.util.ArrayList;

/**
 * Fila de prioridades (Min-Heap) implementada com ArrayList.
 * Para um nó no índice i:
 *   - Filho esquerdo: 2*i + 1
 *   - Filho direito:  2*i + 2
 *   - Pai:            (i - 1) / 2
 *
 * Responsável: André
 */
public class MinHeap {

    private ArrayList<No> heap;

    public MinHeap() {
        this.heap = new ArrayList<>();
    }

    public void inserir(No no) {
        heap.add(no);
        subir(heap.size() - 1);
    }

    public No removerMinimo() {
        if (heap.isEmpty()) return null;
        No minimo = heap.get(0);
        No ultimo = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, ultimo);
            descer(0);
        }
        return minimo;
    }

    public int tamanho() {
        return heap.size();
    }

    public boolean estaVazio() {
        return heap.isEmpty();
    }

    private void subir(int i) {
        while (i > 0) {
            int pai = (i - 1) / 2;
            if (heap.get(i).compareTo(heap.get(pai)) < 0) {
                trocar(i, pai);
                i = pai;
            } else {
                break;
            }
        }
    }

    private void descer(int i) {
        int tamanho = heap.size();
        while (true) {
            int menor = i;
            int esq = 2 * i + 1;
            int dir = 2 * i + 2;
            if (esq < tamanho && heap.get(esq).compareTo(heap.get(menor)) < 0) menor = esq;
            if (dir < tamanho && heap.get(dir).compareTo(heap.get(menor)) < 0) menor = dir;
            if (menor != i) {
                trocar(i, menor);
                i = menor;
            } else {
                break;
            }
        }
    }

    private void trocar(int i, int j) {
        No temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /** Retorna representação do vetor interno para impressão (ETAPA 2) */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");
        for (int i = 0; i < heap.size(); i++) {
            sb.append(heap.get(i));
            if (i < heap.size() - 1) sb.append(", ");
        }
        sb.append(" ]");
        return sb.toString();
    }
}
