# Huffman Compressor — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implementar compressão e descompressão de arquivos com o algoritmo de Huffman em Java, executável via `java -jar huffman.jar -c/-d`.

**Architecture:** Quatro pacotes isolados (`estrutura`, `compressao`, `descompressao`, `Main`) com dependência unidirecional. Cada componente tem seu arquivo de teste manual em `test/`. O arquivo `.huff` usa cabeçalho de 1024 bytes (256 ints de frequência) + 1 byte de padding + dados comprimidos.

**Tech Stack:** Java (sem build tool), compilação com `javac`, empacotamento com `jar`, testes manuais com classes `main` em `test/`.

---

## Estrutura de Arquivos

```
src/huffman/
├── estrutura/
│   ├── No.java              ← Rodrigo (Task 1)
│   ├── MinHeap.java         ← Rodrigo (Task 2)
│   └── ArvoreHuffman.java   ← André   (Task 3)
├── compressao/
│   └── Compressor.java      ← Nicolas (Task 4)
├── descompressao/
│   └── Descompressor.java   ← Julia   (Task 5)
└── Main.java                ← Julia   (Task 6)

test/
├── TesteNo.java             ← Task 1
├── TesteMinHeap.java        ← Task 2
├── TesteArvore.java         ← Task 3
└── TesteIntegracao.java     ← Task 7
```

---

## Task 1: Classe `No` (Rodrigo)

**Files:**
- Modify: `src/huffman/estrutura/No.java`
- Create: `test/TesteNo.java`

- [ ] **Step 1: Escrever o teste**

Crie o arquivo `test/TesteNo.java`:

```java
/**
 * Teste manual para a classe No.
 * Execute: javac -cp src test/TesteNo.java && java -cp src:test TesteNo
 */
public class TesteNo {
    public static void main(String[] args) {
        // Teste 1: nó folha
        huffman.estrutura.No folha = new huffman.estrutura.No('A', 3);
        assert folha.caractere == 'A' : "FALHOU: caractere errado";
        assert folha.frequencia == 3 : "FALHOU: frequencia errada";
        assert folha.eFolha() : "FALHOU: deveria ser folha";
        System.out.println("OK: No folha criado corretamente");

        // Teste 2: nó interno
        huffman.estrutura.No esq = new huffman.estrutura.No('B', 1);
        huffman.estrutura.No dir = new huffman.estrutura.No('N', 2);
        huffman.estrutura.No interno = new huffman.estrutura.No(3, esq, dir);
        assert !interno.eFolha() : "FALHOU: nao deveria ser folha";
        assert interno.frequencia == 3 : "FALHOU: frequencia do interno errada";
        System.out.println("OK: No interno criado corretamente");

        // Teste 3: compareTo
        huffman.estrutura.No a = new huffman.estrutura.No('A', 5);
        huffman.estrutura.No b = new huffman.estrutura.No('B', 2);
        assert a.compareTo(b) > 0 : "FALHOU: A(5) deveria ser maior que B(2)";
        assert b.compareTo(a) < 0 : "FALHOU: B(2) deveria ser menor que A(5)";
        assert a.compareTo(a) == 0 : "FALHOU: comparar consigo mesmo deveria ser 0";
        System.out.println("OK: compareTo funciona corretamente");

        System.out.println("\nTodos os testes de No passaram!");
    }
}
```

- [ ] **Step 2: Rodar o teste para confirmar que FALHA**

```bash
cd /home/odrigo_lucas/huffman-compressor
javac -cp src test/TesteNo.java 2>&1
java -ea -cp src:test TesteNo 2>&1
```

Esperado: erro de compilação ou falha de assertion (classe ainda incompleta).

- [ ] **Step 3: Implementar `No.java`**

Substitua o conteúdo de `src/huffman/estrutura/No.java`:

```java
package huffman.estrutura;

/**
 * Representa um nó da Árvore de Huffman.
 * Usado tanto como folha (com caractere) quanto como nó interno.
 *
 * Grupo: Rodrigo, André, Nicolas, Julia
 * Responsável: Rodrigo
 */
public class No implements Comparable<No> {

    public char caractere;
    public int frequencia;
    public No esquerda;
    public No direita;

    /** Construtor para nó folha (caractere real) */
    public No(char caractere, int frequencia) {
        this.caractere = caractere;
        this.frequencia = frequencia;
        this.esquerda = null;
        this.direita = null;
    }

    /** Construtor para nó interno (sem caractere associado) */
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
```

- [ ] **Step 4: Rodar o teste para confirmar que PASSA**

```bash
javac -cp src src/huffman/estrutura/No.java
javac -cp src test/TesteNo.java
java -ea -cp src:test TesteNo
```

Esperado:
```
OK: No folha criado corretamente
OK: No interno criado corretamente
OK: compareTo funciona corretamente

Todos os testes de No passaram!
```

- [ ] **Step 5: Commit**

```bash
git add src/huffman/estrutura/No.java test/TesteNo.java
git commit -m "feat: implementa classe No com construtores, eFolha e compareTo"
```

---

## Task 2: Classe `MinHeap` (Rodrigo)

**Files:**
- Modify: `src/huffman/estrutura/MinHeap.java`
- Create: `test/TesteMinHeap.java`

- [ ] **Step 1: Escrever o teste**

Crie `test/TesteMinHeap.java`:

```java
/**
 * Teste manual para MinHeap.
 * Execute: javac -cp src test/TesteMinHeap.java && java -ea -cp src:test TesteMinHeap
 */
public class TesteMinHeap {
    public static void main(String[] args) {
        huffman.estrutura.MinHeap heap = new huffman.estrutura.MinHeap();

        // Teste 1: heap vazio
        assert heap.tamanho() == 0 : "FALHOU: heap deveria ter tamanho 0";
        assert heap.estaVazio() : "FALHOU: heap deveria estar vazio";
        System.out.println("OK: heap vazio");

        // Teste 2: inserção e remoção em ordem
        heap.inserir(new huffman.estrutura.No('A', 3));
        heap.inserir(new huffman.estrutura.No('B', 1));
        heap.inserir(new huffman.estrutura.No('N', 2));

        assert heap.tamanho() == 3 : "FALHOU: tamanho deveria ser 3";
        System.out.println("OK: inserção de 3 nós");

        huffman.estrutura.No min1 = heap.removerMinimo();
        assert min1.frequencia == 1 : "FALHOU: primeiro minimo deveria ser 1, foi " + min1.frequencia;
        assert min1.caractere == 'B' : "FALHOU: deveria ser B";
        System.out.println("OK: primeiro removerMinimo retornou B(1)");

        huffman.estrutura.No min2 = heap.removerMinimo();
        assert min2.frequencia == 2 : "FALHOU: segundo minimo deveria ser 2, foi " + min2.frequencia;
        assert min2.caractere == 'N' : "FALHOU: deveria ser N";
        System.out.println("OK: segundo removerMinimo retornou N(2)");

        huffman.estrutura.No min3 = heap.removerMinimo();
        assert min3.frequencia == 3 : "FALHOU: terceiro minimo deveria ser 3, foi " + min3.frequencia;
        assert min3.caractere == 'A' : "FALHOU: deveria ser A";
        System.out.println("OK: terceiro removerMinimo retornou A(3)");

        assert heap.estaVazio() : "FALHOU: heap deveria estar vazio após remover tudo";
        System.out.println("OK: heap vazio após remoções");

        // Teste 3: inserção em massa
        for (int i = 10; i >= 1; i--) {
            heap.inserir(new huffman.estrutura.No((char)('A' + i), i));
        }
        int anterior = -1;
        while (!heap.estaVazio()) {
            int freq = heap.removerMinimo().frequencia;
            assert freq >= anterior : "FALHOU: heap não está em ordem crescente: " + freq + " < " + anterior;
            anterior = freq;
        }
        System.out.println("OK: remoções em ordem crescente para 10 elementos");

        System.out.println("\nTodos os testes de MinHeap passaram!");
    }
}
```

- [ ] **Step 2: Rodar o teste para confirmar que FALHA**

```bash
javac -cp src test/TesteMinHeap.java
java -ea -cp src:test TesteMinHeap
```

Esperado: falha de assertion (métodos `subir`/`descer` ainda não implementados).

- [ ] **Step 3: Implementar `MinHeap.java`**

Substitua o conteúdo de `src/huffman/estrutura/MinHeap.java`:

```java
package huffman.estrutura;

import java.util.ArrayList;

/**
 * Fila de prioridades (Min-Heap) implementada com ArrayList.
 * Índice i → filho esquerdo: 2i+1, filho direito: 2i+2, pai: (i-1)/2
 *
 * Grupo: Rodrigo, André, Nicolas, Julia
 * Responsável: Rodrigo
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
```

- [ ] **Step 4: Rodar o teste para confirmar que PASSA**

```bash
javac -cp src src/huffman/estrutura/No.java src/huffman/estrutura/MinHeap.java
javac -cp src test/TesteMinHeap.java
java -ea -cp src:test TesteMinHeap
```

Esperado:
```
OK: heap vazio
OK: inserção de 3 nós
OK: primeiro removerMinimo retornou B(1)
OK: segundo removerMinimo retornou N(2)
OK: terceiro removerMinimo retornou A(3)
OK: heap vazio após remoções
OK: remoções em ordem crescente para 10 elementos

Todos os testes de MinHeap passaram!
```

- [ ] **Step 5: Commit e push para liberar para André**

```bash
git add src/huffman/estrutura/MinHeap.java test/TesteMinHeap.java
git commit -m "feat: implementa MinHeap com subir/descer logaritmicos"
git push origin feature/rodrigo-estrutura
```

Abrir PR no GitHub e avisar André.

---

## Task 3: Classe `ArvoreHuffman` (André)

> **Pré-requisito:** PR do Rodrigo (Task 1 + 2) deve estar na `main`.
> Antes de começar: `git checkout main && git pull origin main && git checkout feature/andre-arvore && git merge main`

**Files:**
- Modify: `src/huffman/estrutura/ArvoreHuffman.java`
- Create: `test/TesteArvore.java`

- [ ] **Step 1: Escrever o teste**

Crie `test/TesteArvore.java`:

```java
/**
 * Teste manual para ArvoreHuffman.
 * Execute: javac -cp src test/TesteArvore.java && java -ea -cp src:test TesteArvore
 */
public class TesteArvore {
    public static void main(String[] args) {
        // Monta frequências para "BANANA": B=1, A=3, N=2
        int[] frequencias = new int[256];
        frequencias['B'] = 1;
        frequencias['A'] = 3;
        frequencias['N'] = 2;

        huffman.estrutura.ArvoreHuffman arvore = new huffman.estrutura.ArvoreHuffman();
        arvore.construir(frequencias);

        // Teste 1: raiz não nula
        assert arvore.getRaiz() != null : "FALHOU: raiz nula";
        assert arvore.getRaiz().frequencia == 6 : "FALHOU: raiz deveria ter frequencia 6, teve " + arvore.getRaiz().frequencia;
        System.out.println("OK: raiz com frequencia 6");

        // Teste 2: tabela de códigos gerada
        String[] tabela = arvore.gerarTabela();
        assert tabela['A'] != null : "FALHOU: A sem código";
        assert tabela['B'] != null : "FALHOU: B sem código";
        assert tabela['N'] != null : "FALHOU: N sem código";
        System.out.println("OK: tabela gerada para A, B, N");

        // Teste 3: A deve ter código mais curto (maior frequência)
        assert tabela['A'].length() < tabela['B'].length() :
            "FALHOU: A (freq 3) deveria ter código mais curto que B (freq 1). A=" + tabela['A'] + " B=" + tabela['B'];
        System.out.println("OK: A tem código mais curto que B");

        // Teste 4: todos os códigos são diferentes (prefix-free)
        assert !tabela['A'].equals(tabela['B']) : "FALHOU: A e B têm o mesmo código";
        assert !tabela['A'].equals(tabela['N']) : "FALHOU: A e N têm o mesmo código";
        assert !tabela['B'].equals(tabela['N']) : "FALHOU: B e N têm o mesmo código";
        System.out.println("OK: todos os códigos são únicos");

        // Teste 5: codificação de BANANA = 9 bits
        String banana = "";
        for (char c : "BANANA".toCharArray()) banana += tabela[c];
        assert banana.length() == 9 : "FALHOU: BANANA deveria ter 9 bits, teve " + banana.length() + " (" + banana + ")";
        System.out.println("OK: BANANA codificado em 9 bits: " + banana);

        System.out.println("\nImpressão da árvore (ETAPA 3):");
        arvore.imprimir();

        System.out.println("\nTodos os testes de ArvoreHuffman passaram!");
    }
}
```

- [ ] **Step 2: Rodar o teste para confirmar que FALHA**

```bash
javac -cp src src/huffman/estrutura/No.java src/huffman/estrutura/MinHeap.java src/huffman/estrutura/ArvoreHuffman.java
javac -cp src test/TesteArvore.java
java -ea -cp src:test TesteArvore
```

Esperado: falha de assertion (`construir` ainda não implementado).

- [ ] **Step 3: Implementar `ArvoreHuffman.java`**

Substitua o conteúdo de `src/huffman/estrutura/ArvoreHuffman.java`:

```java
package huffman.estrutura;

/**
 * Constrói e percorre a Árvore de Huffman.
 *
 * Grupo: Rodrigo, André, Nicolas, Julia
 * Responsável: André
 */
public class ArvoreHuffman {

    private No raiz;

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
            heap.inserir(new No(esq.frequencia + dir.frequencia, esq, dir));
        }
        raiz = heap.removerMinimo();
    }

    public String[] gerarTabela() {
        String[] tabela = new String[256];
        gerarRecursivo(raiz, "", tabela);
        return tabela;
    }

    private void gerarRecursivo(No no, String codigo, String[] tabela) {
        if (no == null) return;
        if (no.eFolha()) {
            // Caso especial: arquivo com apenas 1 caractere único
            tabela[(int) no.caractere] = codigo.isEmpty() ? "0" : codigo;
            return;
        }
        gerarRecursivo(no.esquerda, codigo + "0", tabela);
        gerarRecursivo(no.direita,  codigo + "1", tabela);
    }

    public No getRaiz() {
        return raiz;
    }

    /** Imprime a árvore em pré-ordem (formato ETAPA 3 do console) */
    public void imprimir() {
        imprimirRecursivo(raiz, 0, new int[]{0});
    }

    private void imprimirRecursivo(No no, int nivel, int[] contadorInterno) {
        if (no == null) return;
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
        imprimirRecursivo(no.direita,  nivel + 1, contadorInterno);
    }

    /** Retorna o estado inicial do heap para impressão na ETAPA 2 */
    public static String heapInicialToString(int[] frequencias) {
        MinHeap heap = new MinHeap();
        for (int i = 0; i < 256; i++) {
            if (frequencias[i] > 0) heap.inserir(new No((char) i, frequencias[i]));
        }
        return heap.toString();
    }
}
```

- [ ] **Step 4: Rodar o teste para confirmar que PASSA**

```bash
javac -cp src src/huffman/estrutura/No.java src/huffman/estrutura/MinHeap.java src/huffman/estrutura/ArvoreHuffman.java
javac -cp src test/TesteArvore.java
java -ea -cp src:test TesteArvore
```

Esperado:
```
OK: raiz com frequencia 6
OK: tabela gerada para A, B, N
OK: A tem código mais curto que B
OK: todos os códigos são únicos
OK: BANANA codificado em 9 bits: 100110110

Impressão da árvore (ETAPA 3):
- (RAIZ, 6)
  - ('A', 3)
  - (N1, 3)
    - ('B', 1)
    - ('N', 2)

Todos os testes de ArvoreHuffman passaram!
```

- [ ] **Step 5: Commit e push**

```bash
git add src/huffman/estrutura/ArvoreHuffman.java test/TesteArvore.java
git commit -m "feat: implementa ArvoreHuffman com construir, gerarTabela e imprimir"
git push origin feature/andre-arvore
```

Abrir PR no GitHub e avisar Nicolas e Julia.

---

## Task 4: Classe `Compressor` (Nicolas)

> **Pré-requisito:** PR do André (Task 3) deve estar na `main`.
> Antes de começar: `git checkout main && git pull origin main && git checkout feature/nicolas-compressor && git merge main`

**Files:**
- Modify: `src/huffman/compressao/Compressor.java`

- [ ] **Step 1: Criar arquivo de teste manual**

Crie `test/banana.txt` com apenas o conteúdo:
```
BANANA
```
(sem quebra de linha — use: `printf 'BANANA' > test/banana.txt`)

```bash
printf 'BANANA' > test/banana.txt
```

- [ ] **Step 2: Implementar `Compressor.java`**

Substitua o conteúdo de `src/huffman/compressao/Compressor.java`:

```java
package huffman.compressao;

import huffman.estrutura.ArvoreHuffman;
import huffman.estrutura.No;

import java.io.*;

/**
 * Comprime arquivos usando o algoritmo de Huffman.
 * Formato do .huff: [256 ints de frequência][1 byte padding][dados comprimidos]
 *
 * Grupo: Rodrigo, André, Nicolas, Julia
 * Responsável: Nicolas
 */
public class Compressor {

    public long comprimir(String caminhoEntrada, String caminhoSaida) throws IOException {
        int[] frequencias = analisarFrequencias(caminhoEntrada);
        imprimirEtapa1(frequencias);

        ArvoreHuffman arvore = new ArvoreHuffman();
        arvore.construir(frequencias);

        imprimirEtapa2(frequencias);
        imprimirEtapa3(arvore);

        String[] tabela = arvore.gerarTabela();
        imprimirEtapa4(tabela);

        long bitsComprimidos = escreverArquivo(caminhoEntrada, caminhoSaida, frequencias, tabela);
        long bytesOriginais = new File(caminhoEntrada).length();
        imprimirEtapa5(bytesOriginais, bitsComprimidos);

        return bitsComprimidos;
    }

    private int[] analisarFrequencias(String caminho) throws IOException {
        int[] frequencias = new int[256];
        try (FileInputStream fis = new FileInputStream(caminho)) {
            int b;
            while ((b = fis.read()) != -1) frequencias[b]++;
        }
        return frequencias;
    }

    private long escreverArquivo(String entrada, String saida, int[] frequencias, String[] tabela) throws IOException {
        long totalBits = 0;

        // Calcula o total de bits para saber o padding antes de escrever
        try (FileInputStream fis = new FileInputStream(entrada)) {
            int b;
            while ((b = fis.read()) != -1) {
                if (tabela[b] != null) totalBits += tabela[b].length();
            }
        }

        int padding = (int) ((8 - (totalBits % 8)) % 8);

        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saida)))) {
            // Cabeçalho: 256 inteiros de frequência
            for (int freq : frequencias) dos.writeInt(freq);

            // Padding: quantos bits de preenchimento o último byte tem
            dos.writeByte(padding);

            // Dados: agrupa bits em bytes
            int buffer = 0;
            int bitsNoBuffer = 0;

            try (FileInputStream fis = new FileInputStream(entrada)) {
                int b;
                while ((b = fis.read()) != -1) {
                    String codigo = tabela[b];
                    for (char bit : codigo.toCharArray()) {
                        buffer = (buffer << 1) | (bit == '1' ? 1 : 0);
                        bitsNoBuffer++;
                        if (bitsNoBuffer == 8) {
                            dos.writeByte(buffer);
                            buffer = 0;
                            bitsNoBuffer = 0;
                        }
                    }
                }
            }

            // Último byte com padding
            if (bitsNoBuffer > 0) {
                buffer = buffer << (8 - bitsNoBuffer);
                dos.writeByte(buffer);
            }
        }

        return totalBits;
    }

    private void imprimirEtapa1(int[] frequencias) {
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 1: Tabela de Frequencia de Caracteres");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < 256; i++) {
            if (frequencias[i] > 0)
                System.out.printf("Caractere '%c' (ASCII: %d): %d%n", (char) i, i, frequencias[i]);
        }
    }

    private void imprimirEtapa2(int[] frequencias) {
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 2: Min-Heap Inicial (Vetor)");
        System.out.println("--------------------------------------------------");
        System.out.println(ArvoreHuffman.heapInicialToString(frequencias));
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
            if (tabela[i] != null)
                System.out.printf("Caractere '%c': %s%n", (char) i, tabela[i]);
        }
    }

    private void imprimirEtapa5(long bytesOriginais, long bitsComprimidos) {
        long bytesComprimidos = (bitsComprimidos + 7) / 8;
        double taxa = (1.0 - (double) bytesComprimidos / bytesOriginais) * 100;
        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 5: Resumo da Compressao");
        System.out.println("--------------------------------------------------");
        System.out.printf("Tamanho original....: %d bits (%d bytes)%n", bytesOriginais * 8, bytesOriginais);
        System.out.printf("Tamanho comprimido..: %d bits (%d bytes)%n", bitsComprimidos, (bitsComprimidos + 7) / 8);
        System.out.printf("Taxa de compressao..: %.2f%%%n", taxa);
        System.out.println("--------------------------------------------------");
    }
}
```

- [ ] **Step 3: Compilar e testar manualmente**

```bash
javac -cp src src/huffman/estrutura/No.java src/huffman/estrutura/MinHeap.java \
      src/huffman/estrutura/ArvoreHuffman.java src/huffman/compressao/Compressor.java

# Teste rápido via main temporário
java -ea -cp src huffman.compressao.Compressor 2>&1 || true
```

Crie um main temporário `test/TesteCompressor.java`:

```java
public class TesteCompressor {
    public static void main(String[] args) throws Exception {
        huffman.compressao.Compressor c = new huffman.compressao.Compressor();
        c.comprimir("test/banana.txt", "test/banana.huff");

        // Verificar que o arquivo .huff foi criado
        java.io.File f = new java.io.File("test/banana.huff");
        assert f.exists() : "FALHOU: arquivo .huff não foi criado";
        assert f.length() > 1024 : "FALHOU: arquivo .huff muito pequeno (sem dados?)";
        System.out.println("OK: arquivo .huff criado com " + f.length() + " bytes");
        System.out.println("OK: tamanho original = 6 bytes, comprimido = " + (f.length() - 1025) + " bytes de dados");
    }
}
```

```bash
javac -cp src test/TesteCompressor.java
java -ea -cp src:test TesteCompressor
```

Esperado: saída das 5 ETAPAs no console + `OK: arquivo .huff criado`.

- [ ] **Step 4: Commit e push**

```bash
git add src/huffman/compressao/Compressor.java test/TesteCompressor.java test/banana.txt
git commit -m "feat: implementa Compressor com escrita de cabecalho, padding e dados"
git push origin feature/nicolas-compressor
```

Abrir PR e avisar Julia.

---

## Task 5: Classe `Descompressor` (Julia)

> **Pré-requisito:** PR do André (Task 3) deve estar na `main`. Não precisa esperar Nicolas — pode desenvolver em paralelo. Para testar integrado, precisa do PR do Nicolas também.
> Antes de começar: `git checkout main && git pull origin main && git checkout feature/julia-descompressor && git merge main`

**Files:**
- Modify: `src/huffman/descompressao/Descompressor.java`

- [ ] **Step 1: Implementar `Descompressor.java`**

Substitua o conteúdo de `src/huffman/descompressao/Descompressor.java`:

```java
package huffman.descompressao;

import huffman.estrutura.ArvoreHuffman;
import huffman.estrutura.No;

import java.io.*;

/**
 * Descomprime arquivos .huff gerados pelo Compressor.
 * Lê o cabeçalho, reconstrói a árvore e decodifica os bits.
 *
 * Grupo: Rodrigo, André, Nicolas, Julia
 * Responsável: Julia
 */
public class Descompressor {

    public void descomprimir(String caminhoEntrada, String caminhoSaida) throws IOException {
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoEntrada)));
             FileOutputStream fos = new FileOutputStream(caminhoSaida)) {

            // Passo 1: ler cabeçalho (256 ints de frequência)
            int[] frequencias = new int[256];
            long totalCaracteres = 0;
            for (int i = 0; i < 256; i++) {
                frequencias[i] = dis.readInt();
                totalCaracteres += frequencias[i];
            }

            // Passo 2: reconstruir a árvore
            ArvoreHuffman arvore = new ArvoreHuffman();
            arvore.construir(frequencias);
            No raiz = arvore.getRaiz();

            // Passo 3: ler o padding
            int padding = dis.readByte() & 0xFF;

            // Passo 4: ler todos os bytes restantes (dados comprimidos)
            byte[] dados = dis.readAllBytes();

            // Passo 5: percurso guiado pelos dados
            No atual = raiz;
            long caracteresEscritos = 0;

            for (int byteIdx = 0; byteIdx < dados.length; byteIdx++) {
                int byteLido = dados[byteIdx] & 0xFF;
                int bitsNesteByte = (byteIdx == dados.length - 1) ? (8 - padding) : 8;

                for (int bitIdx = 7; bitIdx >= (8 - bitsNesteByte); bitIdx--) {
                    int bit = (byteLido >> bitIdx) & 1;
                    atual = (bit == 0) ? atual.esquerda : atual.direita;

                    if (atual.eFolha()) {
                        fos.write(atual.caractere);
                        caracteresEscritos++;
                        atual = raiz;

                        if (caracteresEscritos == totalCaracteres) break;
                    }
                }
                if (caracteresEscritos == totalCaracteres) break;
            }
        }
    }
}
```

- [ ] **Step 2: Compilar**

```bash
javac -cp src src/huffman/estrutura/No.java src/huffman/estrutura/MinHeap.java \
      src/huffman/estrutura/ArvoreHuffman.java \
      src/huffman/descompressao/Descompressor.java
```

Esperado: sem erros de compilação.

- [ ] **Step 3: Commit (teste de integração vem na Task 7)**

```bash
git add src/huffman/descompressao/Descompressor.java
git commit -m "feat: implementa Descompressor com percurso guiado pelos dados"
git push origin feature/julia-descompressor
```

---

## Task 6: Classe `Main` (Julia)

**Files:**
- Modify: `src/huffman/Main.java`

- [ ] **Step 1: Implementar `Main.java`**

Substitua o conteúdo de `src/huffman/Main.java`:

```java
package huffman;

import huffman.compressao.Compressor;
import huffman.descompressao.Descompressor;

import java.io.File;

/**
 * Ponto de entrada — CLI do compressor Huffman.
 * Uso:
 *   java -jar huffman.jar -c <original> <comprimido>
 *   java -jar huffman.jar -d <comprimido> <restaurado>
 *
 * Grupo: Rodrigo, André, Nicolas, Julia
 * Disciplina: Estrutura de Dados II
 * Responsável: Julia
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

        File entrada = new File(arquivoEntrada);
        if (!entrada.exists() || !entrada.isFile()) {
            System.err.println("Erro: arquivo de entrada nao encontrado: " + arquivoEntrada);
            System.exit(1);
        }

        try {
            if (modo.equals("-c")) {
                new Compressor().comprimir(arquivoEntrada, arquivoSaida);
                System.out.println("Arquivo comprimido salvo em: " + arquivoSaida);
            } else if (modo.equals("-d")) {
                new Descompressor().descomprimir(arquivoEntrada, arquivoSaida);
                System.out.println("Arquivo restaurado salvo em: " + arquivoSaida);
            } else {
                System.err.println("Modo invalido: use -c para comprimir ou -d para descomprimir.");
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Erro durante a execucao: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
```

- [ ] **Step 2: Commit e push**

```bash
git add src/huffman/Main.java
git commit -m "feat: implementa Main com validacao de argumentos e tratamento de erros"
git push origin feature/julia-descompressor
```

Abrir PR e avisar Rodrigo para integração final.

---

## Task 7: Integração, Testes Finais e Geração do `huffman.jar` (Rodrigo)

> **Pré-requisito:** todos os PRs anteriores mesclados na `main`.
> `git checkout main && git pull origin main`

- [ ] **Step 1: Compilar tudo junto**

```bash
cd /home/odrigo_lucas/huffman-compressor
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt
```

Esperado: sem erros. Se houver, corrigir antes de continuar.

- [ ] **Step 2: Criar o `huffman.jar`**

```bash
echo "Main-Class: huffman.Main" > MANIFEST.MF
jar cfm huffman.jar MANIFEST.MF -C out .
```

- [ ] **Step 3: Teste de compressão com BANANA**

```bash
printf 'BANANA' > test/banana.txt
java -jar huffman.jar -c test/banana.txt test/banana.huff
```

Esperado no console:
```
--------------------------------------------------
ETAPA 1: Tabela de Frequencia de Caracteres
--------------------------------------------------
Caractere 'A' (ASCII: 65): 3
Caractere 'B' (ASCII: 66): 1
Caractere 'N' (ASCII: 78): 2
--------------------------------------------------
ETAPA 2: Min-Heap Inicial (Vetor)
...
ETAPA 5: Resumo da Compressao
--------------------------------------------------
Tamanho original....: 48 bits (6 bytes)
Tamanho comprimido..: 9 bits (2 bytes)
Taxa de compressao..: ...%
```

- [ ] **Step 4: Teste de descompressão (ida e volta)**

```bash
java -jar huffman.jar -d test/banana.huff test/banana-restaurado.txt
diff test/banana.txt test/banana-restaurado.txt
```

Esperado: `diff` sem saída (arquivos idênticos).

- [ ] **Step 5: Teste de integração completo**

Crie `test/TesteIntegracao.java`:

```java
import java.io.*;
import java.nio.file.*;

public class TesteIntegracao {
    public static void main(String[] args) throws Exception {
        // Teste 1: BANANA
        testar("BANANA", "test/t1.txt", "test/t1.huff", "test/t1-restaurado.txt");

        // Teste 2: texto repetitivo
        testar("AAAAAAAAAAAAAAAA", "test/t2.txt", "test/t2.huff", "test/t2-restaurado.txt");

        // Teste 3: texto com múltiplos caracteres
        testar("Hello World! Este e um texto de teste.", "test/t3.txt", "test/t3.huff", "test/t3-restaurado.txt");

        System.out.println("\nTodos os testes de integracao passaram!");
    }

    static void testar(String conteudo, String original, String comprimido, String restaurado) throws Exception {
        Files.writeString(Path.of(original), conteudo);

        huffman.compressao.Compressor c = new huffman.compressao.Compressor();
        c.comprimir(original, comprimido);

        huffman.descompressao.Descompressor d = new huffman.descompressao.Descompressor();
        d.descomprimir(comprimido, restaurado);

        String original_str = Files.readString(Path.of(original));
        String restaurado_str = Files.readString(Path.of(restaurado));

        assert original_str.equals(restaurado_str) :
            "FALHOU para '" + conteudo + "': original != restaurado\n" +
            "Original:   '" + original_str + "'\nRestaurado: '" + restaurado_str + "'";

        System.out.println("OK: '" + conteudo.substring(0, Math.min(20, conteudo.length())) + "...' comprimido e restaurado corretamente");
    }
}
```

```bash
javac -cp src:out test/TesteIntegracao.java -d out
java -ea -cp out TesteIntegracao
```

Esperado:
```
OK: 'BANANA...' comprimido e restaurado corretamente
OK: 'AAAAAAAAAAAAAAAA...' comprimido e restaurado corretamente
OK: 'Hello World! Este e um...' comprimido e restaurado corretamente

Todos os testes de integracao passaram!
```

- [ ] **Step 6: Commit final**

```bash
git add huffman.jar MANIFEST.MF test/TesteIntegracao.java
git commit -m "feat: gera huffman.jar e adiciona testes de integracao"
git push origin main
```
