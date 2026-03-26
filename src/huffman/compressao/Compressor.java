package huffman.compressao;

import huffman.estrutura.ArvoreHuffman;
import huffman.estrutura.MinHeap;
import huffman.estrutura.No;

import java.io.*;

/**
 * Responsável pela compressão de arquivos usando o algoritmo de Huffman.
 * Gera um arquivo .huff contendo o cabeçalho (frequências) e os bits comprimidos.
 *
 * Responsável: Julia
 */
public class Compressor {

    /**
     * Comprime o arquivo de entrada e salva no arquivo de saída.
     *
     * @param caminhoEntrada  caminho do arquivo original
     * @param caminhoSaida    caminho do arquivo .huff gerado
     */
    public void comprimir(String caminhoEntrada, String caminhoSaida) throws IOException {
        // Passo 1: Análise de frequência
        int[] frequencias = analisarFrequencias(caminhoEntrada);
        imprimirEtapa1(frequencias);

        // Passo 2: Construir MinHeap e Árvore
        ArvoreHuffman arvore = new ArvoreHuffman();
        arvore.construir(frequencias);

        // Passo 3: Gerar tabela de códigos
        String[] tabela = arvore.gerarTabela();
        imprimirEtapa3(arvore);
        imprimirEtapa4(tabela);

        // Passo 4: Codificar e gravar arquivo
        long bitsComprimidos = escreverArquivo(caminhoEntrada, caminhoSaida, frequencias, tabela);

        // Passo 5: Resumo
        long bytesOriginais = new File(caminhoEntrada).length();
        imprimirEtapa5(bytesOriginais, bitsComprimidos);
    }

    private int[] analisarFrequencias(String caminho) throws IOException {
        int[] frequencias = new int[256];
        try (FileInputStream fis = new FileInputStream(caminho)) {
            int b;
            while ((b = fis.read()) != -1) {
                frequencias[b]++;
            }
        }
        return frequencias;
    }

    private long escreverArquivo(String entrada, String saida, int[] frequencias, String[] tabela) throws IOException {
        // TODO: implementar escrita do cabeçalho + bits comprimidos no arquivo .huff
        // Sugestão: gravar os 256 inteiros de frequência como cabeçalho,
        // depois os bits agrupados em bytes com padding no último byte.
        return 0;
    }

    // ---- Métodos de impressão (ETAPAS do console) ----

    private void imprimirEtapa1(int[] frequencias) {
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 1: Tabela de Frequencia de Caracteres");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < 256; i++) {
            if (frequencias[i] > 0) {
                System.out.printf("Caractere '%c' (ASCII: %d): %d%n", (char) i, i, frequencias[i]);
            }
        }
    }

    private void imprimirEtapa3(ArvoreHuffman arvore) {
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 3: Arvore de Huffman");
        System.out.println("--------------------------------------------------");
        arvore.imprimir();
    }

    private void imprimirEtapa4(String[] tabela) {
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 4: Tabela de Codigos de Huffman");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < 256; i++) {
            if (tabela[i] != null) {
                System.out.printf("Caractere '%c': %s%n", (char) i, tabela[i]);
            }
        }
    }

    private void imprimirEtapa5(long bytesOriginais, long bitsComprimidos) {
        long bytesComprimidos = (bitsComprimidos + 7) / 8;
        double taxa = (1.0 - (double) bytesComprimidos / bytesOriginais) * 100;
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 5: Resumo da Compressao");
        System.out.println("--------------------------------------------------");
        System.out.printf("Tamanho original....: %d bits (%d bytes)%n", bytesOriginais * 8, bytesOriginais);
        System.out.printf("Tamanho comprimido..: %d bits (%d bytes)%n", bitsComprimidos, bytesComprimidos);
        System.out.printf("Taxa de compressao..: %.2f%%%n", taxa);
        System.out.println("--------------------------------------------------");
    }
}
