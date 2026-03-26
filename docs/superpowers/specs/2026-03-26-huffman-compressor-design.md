# Design: Compressor de Arquivos Huffman

**Data:** 2026-03-26
**Disciplina:** Estrutura de Dados II
**Grupo:** Rodrigo, André, Nicolas, Julia
**Repositório:** https://github.com/Grupo-carro-chefe/huffman-compressor

---

## 1. Visão Geral

Implementação do algoritmo de Huffman em Java para compressão e descompressão de arquivos via linha de comando. O programa gera um arquivo `.huff` na compressão e restaura o arquivo original na descompressão, sem modificar o arquivo de entrada.

**Execução:**
```bash
java -jar huffman.jar -c <arquivo_original> <arquivo_comprimido>
java -jar huffman.jar -d <arquivo_comprimido> <arquivo_restaurado>
```

---

## 2. Arquitetura e Componentes

### Pacotes e responsáveis

```
src/huffman/
├── estrutura/
│   ├── No.java             # Rodrigo — nó da árvore e do heap
│   ├── MinHeap.java        # Rodrigo — fila de prioridades
│   └── ArvoreHuffman.java  # André   — construção e percurso da árvore
├── compressao/
│   └── Compressor.java     # Nicolas — codificação e escrita do .huff
├── descompressao/
│   └── Descompressor.java  # Julia   — leitura e decodificação do .huff
└── Main.java               # Julia   — ponto de entrada CLI
```

### Dependências entre componentes

```
No  ←  MinHeap  ←  ArvoreHuffman  ←  Compressor
                                   ←  Descompressor  ←  Main
```

---

## 3. Estruturas de Dados

### 3.1 Classe `No`

Representa tanto folhas (caracteres reais) quanto nós internos da árvore.

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `caractere` | `char` | Caractere ASCII (folhas); `'\0'` em nós internos |
| `frequencia` | `int` | Frequência do caractere ou soma dos filhos |
| `esquerda` | `No` | Filho esquerdo (código `0`) |
| `direita` | `No` | Filho direito (código `1`) |

Implementa `Comparable<No>` por frequência (menor = maior prioridade).

### 3.2 Classe `MinHeap`

Implementação de fila de prioridades com `ArrayList<No>`.

Invariante: `heap[i].frequencia <= heap[2i+1].frequencia` e `heap[i].frequencia <= heap[2i+2].frequencia`

| Operação | Complexidade |
|----------|-------------|
| `inserir` | O(log n) |
| `removerMinimo` | O(log n) |
| `tamanho` | O(1) |

### 3.3 Classe `ArvoreHuffman`

Constrói a árvore consumindo o MinHeap iterativamente. Gera a tabela de códigos com `String[256]`.

---

## 4. Formato do Arquivo `.huff`

```
[ CABEÇALHO: 256 × 4 bytes = 1024 bytes ]
  int[0]  → frequência do caractere ASCII 0
  int[1]  → frequência do caractere ASCII 1
  ...
  int[255]→ frequência do caractere ASCII 255

[ METADADO: 1 byte ]
  padding → quantos bits de preenchimento o último byte tem (0–7)

[ DADOS: bytes comprimidos ]
  bits agrupados em bytes; último byte pode ter padding de zeros
```

---

## 5. Processo de Compressão (Nicolas)

1. Ler arquivo de entrada → contar frequências (`int[256]`)
2. Construir MinHeap com os caracteres presentes
3. Construir Árvore de Huffman
4. Gerar tabela de códigos (`String[256]`)
5. Imprimir ETAPAS 1–5 no console
6. Escrever cabeçalho (256 ints) no `.huff`
7. Para cada byte do arquivo original: acumular bits do código; a cada 8 bits escrever 1 byte
8. Escrever byte de padding + último byte

---

## 6. Processo de Descompressão (Julia)

1. Ler cabeçalho (256 ints de frequência)
2. Reconstruir ArvoreHuffman com as mesmas frequências
3. Ler byte de padding
4. Percurso guiado: para cada bit lido, navegar na árvore (0=esquerda, 1=direita); ao chegar em folha, escrever caractere e voltar à raiz
5. Ignorar os últimos `padding` bits do arquivo

---

## 7. Saída Esperada no Console (compressão)

```
--------------------------------------------------
ETAPA 1: Tabela de Frequencia de Caracteres
--------------------------------------------------
Caractere 'A' (ASCII: 65): 3
...
--------------------------------------------------
ETAPA 2: Min-Heap Inicial (Vetor)
--------------------------------------------------
[ No('B',1), No('N',2), No('A',3) ]
--------------------------------------------------
ETAPA 3: Arvore de Huffman
--------------------------------------------------
- (RAIZ, 6)
  - ('A', 3)
  - (N1, 3)
    - ('B', 1)
    - ('N', 2)
--------------------------------------------------
ETAPA 4: Tabela de Codigos de Huffman
--------------------------------------------------
Caractere 'A': 0
Caractere 'B': 10
Caractere 'N': 11
--------------------------------------------------
ETAPA 5: Resumo da Compressao
--------------------------------------------------
Tamanho original....: 48 bits (6 bytes)
Tamanho comprimido..: 9 bits (2 bytes)
Taxa de compressao..: 81.25%
--------------------------------------------------
```

---

## 8. Geração do `huffman.jar`

Após todos implementarem, Rodrigo compila e empacota:

```bash
# Compilar
find src -name "*.java" > sources.txt
javac -d out @sources.txt

# Criar MANIFEST
echo "Main-Class: huffman.Main" > MANIFEST.MF

# Empacotar
jar cfm huffman.jar MANIFEST.MF -C out .
```

---

## 9. Fluxo de Trabalho Git

1. Clonar: `git clone https://github.com/Grupo-carro-chefe/huffman-compressor.git`
2. Criar branch: `git checkout -b feature/<nome>-<parte>`
3. Desenvolver e commitar: `git add <arquivo> && git commit -m "feat: ..."`
4. Enviar: `git push origin feature/<nome>-<parte>`
5. Abrir Pull Request no GitHub → aguardar 1 aprovação → merge

### Convenção de commits

| Prefixo | Quando usar |
|---------|-------------|
| `feat:` | nova funcionalidade |
| `fix:` | correção de bug |
| `docs:` | documentação |
| `test:` | arquivo de teste |

---

## 10. Ordem de Desenvolvimento

```
Semana 1: Rodrigo → No + MinHeap
Semana 1: André   → ArvoreHuffman (após No/MinHeap prontos)
Semana 2: Nicolas → Compressor
Semana 2: Julia   → Descompressor + Main
Semana 3: Integração, testes, geração do .jar, relatório
```
