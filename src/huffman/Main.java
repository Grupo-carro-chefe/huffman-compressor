package huffman;

import huffman.compressao.Compressor;
import huffman.descompressao.Descompressor;

/**
 * Ponto de entrada do programa.
 * Uso:
 *   java -jar huffman.jar -c <arquivo_original> <arquivo_comprimido>
 *   java -jar huffman.jar -d <arquivo_comprimido> <arquivo_restaurado>
 *
 * Grupo: Rodrigo, André, Nicolas, Julia
 * Disciplina: Estrutura de Dados II
 * Responsável: Rodrigo
 */
public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Uso:");
            System.err.println("  Comprimir:    java -jar huffman.jar -c <original> <comprimido>");
            System.err.println("  Descomprimir: java -jar huffman.jar -d <comprimido> <restaurado>");
            System.exit(1);
        }

        String modo = args[0];
        String arquivoEntrada = args[1];
        String arquivoSaida = args[2];

        try {
            if (modo.equals("-c")) {
                Compressor compressor = new Compressor();
                compressor.comprimir(arquivoEntrada, arquivoSaida);
            } else if (modo.equals("-d")) {
                Descompressor descompressor = new Descompressor();
                descompressor.descomprimir(arquivoEntrada, arquivoSaida);
            } else {
                System.err.println("Modo inválido: use -c para comprimir ou -d para descomprimir.");
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.exit(1);
        }
    }
}
