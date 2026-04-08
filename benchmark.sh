#!/bin/bash
# Gera os arquivos de teste e mede tempo de compressão/descompressão
# Uso: bash benchmark.sh

BASE="$(cd "$(dirname "$0")" && pwd)"
cd "$BASE"
mkdir -p benchmark_files

# ─────────────────────────────────────────────
# Gerar arquivos de teste
# ─────────────────────────────────────────────
echo "Gerando arquivos de teste..."

python3 -c "
import random
chars = 'abcdefghijklmnopqrstuvwxyz AEIOU.,\n'
for nome, tam in [('texto_1kb',1024),('texto_100kb',102400),('texto_1mb',1048576),('texto_10mb',10485760)]:
    open(f'benchmark_files/{nome}.txt','w').write(''.join(random.choices(chars, k=tam)))
print('OK')
"

echo ""

# ─────────────────────────────────────────────
# QUADRO 2 – Tempo de execução por tamanho
# ─────────────────────────────────────────────
echo "════════════════════════════════════════════════════"
echo " QUADRO 2 – Tempo de execução (ms)"
echo "════════════════════════════════════════════════════"
printf "%-20s %-20s %-20s\n" "Tamanho" "Compressão (ms)" "Descompressão (ms)"
echo "----------------------------------------------------"

for par in "1 KB|$BASE/benchmark_files/texto_1kb.txt" \
           "100 KB|$BASE/benchmark_files/texto_100kb.txt" \
           "1 MB|$BASE/benchmark_files/texto_1mb.txt" \
           "10 MB|$BASE/benchmark_files/texto_10mb.txt"; do
    nome="${par%%|*}"
    arquivo="${par##*|}"

    t1=$(date +%s%3N)
    java -cp "$BASE/out" huffman.Main -c "$arquivo" "$BASE/benchmark_files/_tmp.huff" > /dev/null 2>&1
    t2=$(date +%s%3N)

    t3=$(date +%s%3N)
    java -cp "$BASE/out" huffman.Main -d "$BASE/benchmark_files/_tmp.huff" "$BASE/benchmark_files/_tmp_r" > /dev/null 2>&1
    t4=$(date +%s%3N)

    printf "%-20s %-20s %-20s\n" "$nome" "$((t2-t1)) ms" "$((t4-t3)) ms"
    rm -f "$BASE/benchmark_files/_tmp.huff" "$BASE/benchmark_files/_tmp_r"
done

echo ""
echo "Pronto! Copie os valores acima para o seu relatório."
rm -rf benchmark_files
