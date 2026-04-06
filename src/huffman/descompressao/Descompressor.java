package huffman.descompressao;

import huffman.estrutura.ArvoreHuffman;
import huffman.estrutura.No;

import java.io.*;

/**
 * Responsavel pela descompressao de arquivos .huff.
 * Reconstrói a arvore a partir do cabecalho e decodifica os bits.
 *
 * Responsavel: Rodrigo
 */
public class Descompressor {

    /**
     * Descomprime o arquivo .huff e restaura o arquivo original.
     *
     * @param caminhoEntrada  caminho do arquivo .huff
     * @param caminhoSaida    caminho do arquivo restaurado
     */
    public void descomprimir(String caminhoEntrada, String caminhoSaida) throws IOException {
        try (DataInputStream entrada = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoEntrada)));
             BufferedOutputStream saida = new BufferedOutputStream(new FileOutputStream(caminhoSaida))) {

            int[] frequencias = lerCabecalho(entrada);
            int totalCaracteres = somarFrequencias(frequencias);

            if (totalCaracteres == 0) {
                return;
            }

            ArvoreHuffman arvore = new ArvoreHuffman();
            arvore.construir(frequencias);
            No raiz = arvore.getRaiz();

            if (raiz == null) {
                return;
            }

            if (raiz.eFolha()) {
                for (int i = 0; i < totalCaracteres; i++) {
                    saida.write((byte) raiz.caractere);
                }
                return;
            }

            BitInputStream streamDeBits = new BitInputStream(entrada);
            for (int i = 0; i < totalCaracteres; i++) {
                char caractere = decodificarProximoCaractere(raiz, streamDeBits);
                saida.write((byte) caractere);
            }
        }
    }

    /**
     * Percurso guiado: navega da raiz ate uma folha seguindo os bits lidos.
     * Retorna o caractere decodificado.
     */
    private char decodificarProximoCaractere(No raiz, BitInputStream streamDeBits) throws IOException {
        No atual = raiz;

        while (!atual.eFolha()) {
            int bit = streamDeBits.readBit();

            if (bit == -1) {
                throw new EOFException("Fim inesperado dos dados comprimidos.");
            }

            atual = (bit == 0) ? atual.esquerda : atual.direita;

            if (atual == null) {
                throw new IOException("Caminho invalido na arvore de Huffman.");
            }
        }

        return atual.caractere;
    }

    private int[] lerCabecalho(DataInputStream entrada) throws IOException {
        int[] frequencias = new int[256];

        for (int i = 0; i < frequencias.length; i++) {
            frequencias[i] = entrada.readInt();
        }

        return frequencias;
    }

    private int somarFrequencias(int[] frequencias) {
        int total = 0;

        for (int frequencia : frequencias) {
            total += frequencia;
        }

        return total;
    }

    private static class BitInputStream {
        private final InputStream input;
        private int buffer;
        private int bitsRestantes;

        BitInputStream(InputStream input) {
            this.input = input;
        }

        int readBit() throws IOException {
            if (bitsRestantes == 0) {
                buffer = input.read();
                if (buffer == -1) {
                    return -1;
                }
                bitsRestantes = 8;
            }

            bitsRestantes--;
            return (buffer >> bitsRestantes) & 1;
        }
    }
}
