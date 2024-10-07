#!/bin/bash

# Definir o nome do arquivo e do programa de saída
SOURCE_FILE="master.c"
OUTPUT_PROGRAM="master_program"

# Compilar o programa
gcc "$SOURCE_FILE" -o "$OUTPUT_PROGRAM" -lm

# Checar se o binário foi gerado
if [ -f "./$OUTPUT_PROGRAM" ]; then
    echo "Compilação bem-sucedida."

    # Executar o programa usando o terminal fictício "abc"
    abc ./"$OUTPUT_PROGRAM"
else
    echo "Falha na compilação."
    exit 1
fi
