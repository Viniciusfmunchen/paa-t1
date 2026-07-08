package huffman;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classe responsável por percorrer o texto e separar os símbolos conforme o modo de compressão.
 *
 * Entrada: arquivo de texto em UTF-8 e modo CHARACTER ou WORD.
 * Resultado: cada símbolo encontrado é enviado para um SymbolConsumer.
 * Pré-condição: arquivo deve existir, ser legível e estar codificado em UTF-8.
 * Pós-condição: todos os símbolos do arquivo são processados sem carregar o arquivo inteiro em memória.
 * Observação: no modo palavra, separadores também são emitidos para permitir reconstrução exata do texto.
 */
public class TextTokenizer {
    private static final int BUFFER_SIZE = 8192;

    /**
     * Percorre o arquivo e envia cada símbolo ao consumidor informado.
     *
     * Entrada: arquivo de entrada, modo de compressão e SymbolConsumer.
     * Resultado: void.
     * Pré-condição: consumer deve estar inicializado; mode deve ser válido.
     * Pós-condição: todos os símbolos são processados de acordo com o modo escolhido.
     * Observação: funciona como ponto único de tokenização para compactação por caracter e por palavra.
     */
    public void forEachSymbol(
            Path inputFile,
            CompressionMode mode,
            SymbolConsumer consumer
    ) throws IOException {
        if (mode == CompressionMode.WORD) {
            forEachWordToken(inputFile, consumer);
        } else {
            forEachCharacter(inputFile, consumer);
        }
    }

    /**
     * Percorre o arquivo caractere por caractere.
     *
     * Entrada: arquivo de entrada e consumidor de símbolos.
     * Resultado: void.
     * Pré-condição: inputFile deve ser texto legível em UTF-8; consumer deve estar inicializado.
     * Pós-condição: cada caractere lido é enviado como uma String de tamanho 1 ao consumidor.
     * Observação: esse método implementa a codificação por caracter exigida no trabalho.
     */
    private void forEachCharacter(Path inputFile, SymbolConsumer consumer) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
            char[] buffer = new char[BUFFER_SIZE];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                for (int i = 0; i < read; i++) {
                    consumer.accept(String.valueOf(buffer[i]));
                }
            }
        }
    }

    /**
     * Percorre o arquivo separando tokens de palavra e tokens de não-palavra.
     *
     * Entrada: arquivo de entrada e consumidor de símbolos.
     * Resultado: void.
     * Pré-condição: inputFile deve ser texto legível em UTF-8; consumer deve estar inicializado.
     * Pós-condição: sequências de letras são emitidas como palavras e sequências de não-letras como separadores.
     * Observação: preservar separadores, espaços e pontuação garante que a descompactação reconstrua o arquivo exatamente.
     */
    private void forEachWordToken(Path inputFile, SymbolConsumer consumer) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
            StringBuilder token = new StringBuilder();
            Boolean tokenIsWord = null;
            char[] buffer = new char[BUFFER_SIZE];
            int read;

            while ((read = reader.read(buffer)) != -1) {
                for (int i = 0; i < read; i++) {
                    char current = buffer[i];
                    boolean currentIsWord = Character.isLetter(current);

                    if (token.length() == 0) {
                        token.append(current);
                        tokenIsWord = currentIsWord;
                    } else if (tokenIsWord != null && tokenIsWord == currentIsWord) {
                        token.append(current);
                    } else {
                        consumer.accept(token.toString());
                        token.setLength(0);
                        token.append(current);
                        tokenIsWord = currentIsWord;
                    }
                }
            }

            if (token.length() > 0) {
                consumer.accept(token.toString());
            }
        }
    }
}
