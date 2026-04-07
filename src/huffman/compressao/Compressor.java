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
        // Passo 1: Ler o arquivo e contar quantas vezes cada caractere aparece
        int[] frequencias = analisarFrequencias(caminhoEntrada);
        imprimirEtapa1(frequencias);

        // Passo 2 e 3: Cria a árvore de Huffman com base nas frequências  
        ArvoreHuffman arvore = new ArvoreHuffman();
        arvore.construir(frequencias);

        // Passo 4: Gera os códigos da árvore (esquerda = 0 e direita = 1)
        String[] tabela = arvore.gerarTabela();
        imprimirEtapa3(arvore);
        imprimirEtapa4(tabela);

        // Passo 5: Compressão do arquivo - Lê o arquivo novamente, substitui os caracteres pelo código e grava em binário
        long bitsComprimidos = escreverArquivo(caminhoEntrada, caminhoSaida, frequencias, tabela);


        // Passo 6: Calcula a taxa de compressão
        long bytesOriginais = new File(caminhoEntrada).length();
        imprimirEtapa5(bytesOriginais, bitsComprimidos);
    }

    // Lê o arquivo e conta quantas vezes cada byte aparece
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

    // Método responsável por escrever o arquivo comprimido (.huff)
    private long escreverArquivo(String entrada, String saida, int[] frequencias, String[] tabela) throws IOException {

        try (
            // DataOutputStream permite escrever dados binários (int, byte, etc)
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(saida));

            // reabre o arquivo original para ler novamente
            FileInputStream fis = new FileInputStream(entrada)
        ) {

            // CABEÇALHO
            // Salva as frequências dos caracteres
            for (int i = 0; i < 256; i++) {
                dos.writeInt(frequencias[i]);
            }

            // PARTE 2: CODIFICAÇÃO DOS DADOs

            int b;

            int buffer = 0;        // guarda bits temporariamente
            int bitsNoBuffer = 0;  // quantos bits já foram adicionados
            long totalBits = 0;    // total de bits gerados

            // lê novamente o arquivo original
            while ((b = fis.read()) != -1) {

                // pega o código Huffman do caractere
                String codigo = tabela[b];

                // percorre cada bit do código
                for (char bit : codigo.toCharArray()) {

                    // desloca o buffer para a esquerda 
                    buffer <<= 1;

                    // se o bit for 1, adiciona 1 no final
                    if (bit == '1') {
                        buffer |= 1;
                    }

                    bitsNoBuffer++;
                    totalBits++;

                    // quando tiver 8 bits, escreve um byte no arquivo
                    if (bitsNoBuffer == 8) {
                        dos.writeByte(buffer);
                        buffer = 0;
                        bitsNoBuffer = 0;
                    }
                }
            }

            // PADDING
            // Se sobrar bits, completa com zeros
            if (bitsNoBuffer > 0) {
                buffer <<= (8 - bitsNoBuffer);
                dos.writeByte(buffer);
            }

            return totalBits;
        }
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
