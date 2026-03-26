package huffman.descompressao;

import huffman.estrutura.ArvoreHuffman;
import huffman.estrutura.No;

import java.io.*;

/**
 * Responsável pela descompressão de arquivos .huff.
 * Reconstrói a árvore a partir do cabeçalho e decodifica os bits.
 *
 * Responsável: Rodrigo
 */
public class Descompressor {

    /**
     * Descomprime o arquivo .huff e restaura o arquivo original.
     *
     * @param caminhoEntrada  caminho do arquivo .huff
     * @param caminhoSaida    caminho do arquivo restaurado
     */
    public void descomprimir(String caminhoEntrada, String caminhoSaida) throws IOException {
        // TODO: implementar
        // 1. Ler cabeçalho (256 frequências) do arquivo .huff
        // 2. Reconstruir a árvore de Huffman com ArvoreHuffman.construir()
        // 3. Percorrer a árvore guiado pelos bits (percurso guiado pelos dados)
        // 4. A cada folha encontrada, escrever o caractere no arquivo de saída
    }

    /**
     * Percurso guiado: navega da raiz até uma folha seguindo os bits lidos.
     * Retorna o caractere decodificado.
     */
    private char decodificarProximoCaractere(No raiz, /* stream de bits */ Object streamDeBits) {
        // TODO: implementar percurso guiado pelos dados
        // while (nó atual não é folha):
        //   lê próximo bit
        //   bit == '0' -> vai para esquerda
        //   bit == '1' -> vai para direita
        // retorna nó.caractere
        return '\0';
    }
}
