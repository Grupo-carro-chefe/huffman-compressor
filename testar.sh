#!/bin/bash
# Uso: bash testar.sh "palavra1" "palavra2" "palavra3" ...

BASE="$(cd "$(dirname "$0")" && pwd)"
cd "$BASE"

CABECALHO=1024  # 256 ints × 4 bytes = tamanho fixo do cabeçalho .huff

# Verifica se o projeto está compilado
if [ ! -f "out/huffman/Main.class" ]; then
    echo "Compilando o projeto..."
    find src -name "*.java" | xargs javac -d out
fi

echo "| Palavra | Original (bytes) | Dados comprimidos (bytes) | Reducao | Status |"
echo "|---------|------------------|---------------------------|---------|--------|"

for palavra in "$@"; do
    echo "$palavra" > _tmp_input.txt

    erro=$(java -cp out huffman.Main -c _tmp_input.txt _tmp_output.huff 2>&1 > /dev/null)

    if [ ! -f "_tmp_output.huff" ]; then
        echo "| $palavra | ? | ? | ? | ERRO: $erro |"
        rm -f _tmp_input.txt
        continue
    fi

    java -cp out huffman.Main -d _tmp_output.huff _tmp_restored.txt > /dev/null 2>&1

    original=$(wc -c < _tmp_input.txt)
    total_huff=$(wc -c < _tmp_output.huff)
    comprimido=$((total_huff - CABECALHO))

    if [ "$original" -gt 0 ] && [ "$comprimido" -gt 0 ]; then
        reducao=$(awk "BEGIN { printf \"%.0f\", (1 - $comprimido / $original) * 100 }")
    else
        reducao=0
    fi

    if diff -q _tmp_input.txt _tmp_restored.txt > /dev/null 2>&1; then
        status="OK"
    else
        status="FALHOU"
    fi

    echo "| $palavra | $original | $comprimido | ${reducao}% | $status |"

    rm -f _tmp_input.txt _tmp_output.huff _tmp_restored.txt
done
