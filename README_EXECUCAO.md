# Projeto Huffman - Execução

Projeto Java sem Maven/Gradle, organizado em classes separadas para facilitar compilação no computador do professor.

## Requisito

- Java 8 ou superior.

Verifique com:

```bash
java -version
javac -version
```

## Compilar

Na pasta raiz do projeto:

```bash
javac -encoding UTF-8 -d out src/HuffmanApp.java src/huffman/*.java
```

## Executar

```bash
java -cp out HuffmanApp
```

Para arquivos muito grandes, especialmente 200 MB, use mais memória:

```bash
java -Xmx2g -cp out HuffmanApp
```

Se ainda houver erro de memória:

```bash
java -Xmx4g -cp out HuffmanApp
```

## Exemplo de nomes de arquivos

Compactação por caractere:

- Entrada: `texto_10mb.txt`
- Saída: `texto_10mb_caracter.huff`

Descompactação por caractere:

- Entrada: `texto_10mb_caracter.huff`
- Saída: `texto_10mb_caracter_descompactado.txt`

Compactação por palavra:

- Entrada: `texto_10mb.txt`
- Saída: `texto_10mb_palavra.huff`

Descompactação por palavra:

- Entrada: `texto_10mb_palavra.huff`
- Saída: `texto_10mb_palavra_descompactado.txt`

## Conferir se descompactou corretamente

Linux/macOS:

```bash
cmp texto_10mb.txt texto_10mb_caracter_descompactado.txt
cmp texto_10mb.txt texto_10mb_palavra_descompactado.txt
```

Windows PowerShell:

```powershell
fc.exe /b texto_10mb.txt texto_10mb_caracter_descompactado.txt
fc.exe /b texto_10mb.txt texto_10mb_palavra_descompactado.txt
```

Se não aparecer diferença, o arquivo foi reconstruído corretamente.

## Observação sobre UTF-8

O programa lê e escreve arquivos-texto usando UTF-8, o que permite caracteres acentuados da língua portuguesa.
