# huffman-compressor

Projeto 1 de Estrutura de Dados II — Compressão de Arquivos com o Algoritmo de Huffman

**Grupo Carro Chefe:** Rodrigo, André, Nicolas, Julia

---

## Como usar

```bash
# Comprimir
java -jar huffman.jar -c <arquivo_original> <arquivo_comprimido>

# Descomprimir
java -jar huffman.jar -d <arquivo_comprimido> <arquivo_restaurado>
```

## Divisão de Responsabilidades

| Pessoa  | Arquivos |
|---------|----------|
| Rodrigo | `No.java`, `MinHeap.java` |
| André   | `ArvoreHuffman.java` |
| Nicolas | `Compressor.java` |
| Julia   | `Descompressor.java`, `Main.java` |

## Estrutura do Projeto

```
src/huffman/
├── estrutura/
│   ├── No.java
│   ├── MinHeap.java
│   └── ArvoreHuffman.java
├── compressao/
│   └── Compressor.java
├── descompressao/
│   └── Descompressor.java
└── Main.java
```

## Guia completo para o grupo

Veja o arquivo [docs/GUIA-DO-GRUPO.md](docs/GUIA-DO-GRUPO.md) para instruções detalhadas de:
- Como configurar o Git
- Como clonar, criar branch, commitar e abrir PR
- O que cada pessoa precisa implementar passo a passo
- Como gerar o `huffman.jar` no final

## Fluxo de trabalho Git

```
main (protegida — não commitar direto)
 └── feature/rodrigo-estrutura   → PR → merge
 └── feature/andre-arvore        → PR → merge
 └── feature/nicolas-compressor  → PR → merge
 └── feature/julia-descompressor → PR → merge
```
