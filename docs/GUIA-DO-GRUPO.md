# Guia do Grupo — Carro Chefe
## Projeto Huffman — Estrutura de Dados II

---

## Divisão de Responsabilidades

| Pessoa  | Arquivos                                          | Resumo                                      |
|---------|---------------------------------------------------|---------------------------------------------|
| Rodrigo | `src/huffman/estrutura/No.java` e `MinHeap.java`  | Estrutura base — os outros dependem disso   |
| André   | `src/huffman/estrutura/ArvoreHuffman.java`        | Constrói a árvore usando o MinHeap          |
| Nicolas | `src/huffman/compressao/Compressor.java`          | Comprime arquivos usando a árvore           |
| Julia   | `src/huffman/descompressao/Descompressor.java` e `Main.java` | Descomprime e faz a CLI funcionar |

---

## Passo a Passo para TODOS (GitHub do zero)

### Passo 1 — Instalar o Git
Baixe em **https://git-scm.com** e instale normalmente.

Após instalar, abra o terminal (cmd no Windows ou terminal no Mac/Linux) e configure seu nome:
```bash
git config --global user.name "Seu Nome Aqui"
git config --global user.email "seu@email.com"
```

---

### Passo 2 — Aceitar o convite da organização
Rodrigo vai te convidar pelo GitHub. Acesse seu email, clique no link do convite e aceite. Depois você verá o repositório em:
```
https://github.com/Grupo-carro-chefe/huffman-compressor
```

---

### Passo 3 — Clonar o repositório (fazer isso UMA VEZ)
"Clonar" significa baixar o projeto para o seu computador.

```bash
git clone https://github.com/Grupo-carro-chefe/huffman-compressor.git
cd huffman-compressor
```

Agora você tem a pasta `huffman-compressor` no seu computador com todos os arquivos.

---

### Passo 4 — Criar sua branch antes de começar
**Nunca escreva código direto na branch `main`.** Crie uma branch com seu nome:

```bash
# Exemplo para cada pessoa:
git checkout -b feature/rodrigo-estrutura   # Rodrigo
git checkout -b feature/andre-arvore        # André
git checkout -b feature/nicolas-compressor  # Nicolas
git checkout -b feature/julia-descompressor # Julia
```

Isso cria uma "cópia paralela" do projeto só sua, onde você pode trabalhar sem afetar os outros.

---

### Passo 5 — Abrir o projeto no VS Code
```bash
code .
```
Ou abra o VS Code manualmente e abra a pasta `huffman-compressor`.

---

### Passo 6 — Editar seus arquivos
Abra apenas os arquivos que são seus (veja a tabela de divisão acima).
Escreva o código conforme descrito na seção "O que cada um implementa" abaixo.

---

### Passo 7 — Salvar seu progresso (commit)
Sempre que terminar uma parte do código, salve no Git:

```bash
# Adicionar o arquivo que você editou
git add src/huffman/estrutura/No.java

# Criar o commit com uma mensagem descritiva
git commit -m "feat: implementa classe No com construtor e compareTo"
```

Faça isso quantas vezes quiser — quanto mais commits, melhor.

---

### Passo 8 — Enviar para o GitHub (push)
Para que os outros vejam seu código:

```bash
git push origin feature/rodrigo-estrutura
# substitua pelo nome da sua branch
```

---

### Passo 9 — Abrir um Pull Request (PR)
Quando terminar sua parte:

1. Acesse `https://github.com/Grupo-carro-chefe/huffman-compressor`
2. Vai aparecer um botão amarelo **"Compare & pull request"** — clique nele
3. No campo de descrição, escreva o que você implementou
4. Clique em **"Create pull request"**
5. Avise um colega no WhatsApp para revisar

---

### Passo 10 — Revisar o PR de um colega
1. Acesse o repositório no GitHub
2. Clique na aba **"Pull requests"**
3. Abra o PR do colega, leia o código
4. Clique em **"Review changes"** → **"Approve"** → **"Submit review"**

---

### Passo 11 — Fazer o merge (após aprovação)
Após 1 aprovação, o dono do PR clica em **"Merge pull request"** no GitHub.
Pronto — o código foi para a `main`!

---

### Passo 12 — Atualizar sua branch com o código novo da main
Quando um colega fizer merge, você precisa puxar as atualizações:

```bash
git checkout main
git pull origin main
git checkout feature/seu-nome-parte
git merge main
```

---

## O que cada um precisa implementar

---

### RODRIGO — `No.java` e `MinHeap.java`

> **Atenção:** Faça isso primeiro! André, Nicolas e Julia dependem do seu código.

#### `No.java`
O esqueleto já está pronto. Verifique que:
- O construtor de folha recebe `char` e `int frequencia`
- O construtor de nó interno recebe `int frequencia`, `No esquerda`, `No direita`
- O método `eFolha()` retorna `true` quando `esquerda == null && direita == null`
- O método `compareTo()` retorna `this.frequencia - outro.frequencia`

#### `MinHeap.java`
Implemente os métodos internos:

**`subir(int i)`** — após inserir, sobe o nó enquanto for menor que o pai:
```
enquanto i > 0:
    pai = (i - 1) / 2
    se heap[i] < heap[pai]:
        troca heap[i] com heap[pai]
        i = pai
    senão: para
```

**`descer(int i)`** — após remover, desce o nó enquanto for maior que algum filho:
```
enquanto true:
    menor = i
    esq = 2*i + 1
    dir = 2*i + 2
    se esq existe e heap[esq] < heap[menor]: menor = esq
    se dir existe e heap[dir] < heap[menor]: menor = dir
    se menor != i: troca, i = menor
    senão: para
```

**Como testar:**
```java
MinHeap heap = new MinHeap();
heap.inserir(new No('A', 3));
heap.inserir(new No('B', 1));
heap.inserir(new No('N', 2));
System.out.println(heap); // deve mostrar B(1) primeiro
System.out.println(heap.removerMinimo()); // deve ser No('B', 1)
System.out.println(heap.removerMinimo()); // deve ser No('N', 2)
System.out.println(heap.removerMinimo()); // deve ser No('A', 3)
```

---

### ANDRÉ — `ArvoreHuffman.java`

> **Aguarde Rodrigo terminar** `No.java` e `MinHeap.java` antes de começar.

#### `construir(int[] frequencias)`
```
para cada i de 0 a 255:
    se frequencias[i] > 0:
        inserir No((char)i, frequencias[i]) no heap

enquanto heap.tamanho() > 1:
    esq = heap.removerMinimo()
    dir = heap.removerMinimo()
    interno = new No(esq.frequencia + dir.frequencia, esq, dir)
    heap.inserir(interno)

raiz = heap.removerMinimo()
```

#### `gerarTabela()`
```
String[] tabela = new String[256]
chamar gerarTabelaRecursivo(raiz, "", tabela)
retornar tabela

gerarTabelaRecursivo(no, codigo, tabela):
    se no é folha:
        tabela[no.caractere] = codigo
        return
    chamar gerarTabelaRecursivo(no.esquerda, codigo + "0", tabela)
    chamar gerarTabelaRecursivo(no.direita,  codigo + "1", tabela)
```

#### `imprimir()`
Percorre a árvore em pré-ordem e imprime para o console no formato da ETAPA 3.

---

### NICOLAS — `Compressor.java`

> **Aguarde André terminar** `ArvoreHuffman.java` antes de começar.

#### `escreverArquivo()`
Este é o método principal que você precisa implementar:

```
1. Abrir FileOutputStream para o arquivo .huff

2. CABEÇALHO: escrever os 256 valores de frequência (cada int = 4 bytes)
   usar DataOutputStream para escrever int com writeInt()

3. CODIFICAÇÃO:
   buffer = ""  // acumula bits como string "01010..."
   para cada byte do arquivo original:
       codigo = tabela[byte]
       buffer += codigo
       enquanto buffer.length() >= 8:
           pegar os 8 primeiros chars do buffer
           converter para byte (Integer.parseInt(bits8, 2))
           escrever esse byte no arquivo
           remover esses 8 chars do buffer

4. PADDING: os bits que sobraram no buffer (menos de 8)
   padding = 8 - buffer.length()  (ou 0 se vazio)
   completar buffer com zeros até ter 8 bits
   escrever o byte final
   escrever o valor de padding como 1 byte no início (voltar e reescrever)

   DICA: escreva o padding ANTES dos dados (logo após o cabeçalho)
         assim você sabe onde está e escreve antes de começar
```

---

### JULIA — `Descompressor.java` e `Main.java`

> **Aguarde Nicolas terminar** `Compressor.java` para poder testar.

#### `descomprimir()`

```
1. Abrir FileInputStream do arquivo .huff

2. LER CABEÇALHO: ler 256 ints com DataInputStream.readInt()
   montar int[] frequencias

3. RECONSTRUIR ÁRVORE:
   ArvoreHuffman arvore = new ArvoreHuffman()
   arvore.construir(frequencias)
   No raiz = arvore.getRaiz()

4. LER PADDING: ler 1 byte = quantos bits ignorar no final

5. PERCURSO GUIADO:
   No atual = raiz
   para cada byte lido do arquivo:
       para cada bit do byte (do mais significativo ao menos):
           se bit == 0: atual = atual.esquerda
           se bit == 1: atual = atual.direita
           se atual.eFolha():
               escrever atual.caractere no arquivo de saída
               atual = raiz
   (ignorar os últimos `padding` bits do último byte)
```

#### `Main.java`
Já está quase pronto. Verifique se o arquivo de entrada existe antes de chamar o compressor/descompressor:
```java
File entrada = new File(args[1]);
if (!entrada.exists()) {
    System.err.println("Arquivo não encontrado: " + args[1]);
    System.exit(1);
}
```

---

## Ordem de Desenvolvimento

```
╔══════════════════════════════════════════╗
║  SEMANA 1                                ║
║  Rodrigo → No.java + MinHeap.java        ║
║      ↓ faz PR + merge                   ║
║  André   → ArvoreHuffman.java            ║
║      ↓ faz PR + merge                   ║
╠══════════════════════════════════════════╣
║  SEMANA 2                                ║
║  Nicolas → Compressor.java               ║
║  Julia   → Descompressor.java + Main.java║
║  (podem trabalhar em paralelo)           ║
║      ↓ ambos fazem PR + merge            ║
╠══════════════════════════════════════════╣
║  SEMANA 3                                ║
║  Rodrigo → integra, gera huffman.jar     ║
║  Todos   → testes finais + relatório     ║
╚══════════════════════════════════════════╝
```

---

## Como gerar o `huffman.jar` (Rodrigo faz no final)

```bash
# Na pasta raiz do projeto
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt

echo "Main-Class: huffman.Main" > MANIFEST.MF
jar cfm huffman.jar MANIFEST.MF -C out .

# Testar
java -jar huffman.jar -c arquivo.txt arquivo.huff
java -jar huffman.jar -d arquivo.huff arquivo-restaurado.txt
```

---

## Dúvidas Frequentes

**Errei algo e quero desfazer o último commit (não enviado ainda):**
```bash
git reset HEAD~1
```

**Quero ver o que mudou antes de commitar:**
```bash
git diff
```

**Quero ver o histórico de commits:**
```bash
git log --oneline
```

**Conflito ao fazer merge — o que fazer?**
Avise o Rodrigo — ele resolve junto com você.
