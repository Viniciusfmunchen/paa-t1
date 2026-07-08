package huffman;

import java.io.IOException;

/**
 * Interface funcional usada para processar símbolos extraídos do texto.
 *
 * Entrada: um símbolo por chamada.
 * Resultado: permite aplicar uma ação ao símbolo, como contar frequência ou escrever código compactado.
 * Pré-condição: implementações devem saber tratar o símbolo recebido.
 * Pós-condição: a ação definida pela implementação é executada para cada símbolo.
 * Observação: permite que TextTokenizer leia o arquivo em fluxo sem saber o que será feito com cada símbolo.
 */
public interface SymbolConsumer {
    /**
     * Processa um símbolo extraído do arquivo.
     *
     * Entrada: símbolo como String.
     * Resultado: void.
     * Pré-condição: symbol deve representar um token válido segundo o modo de compressão.
     * Pós-condição: a implementação executa sua ação sobre o símbolo.
     * Observação: pode lançar IOException porque algumas ações escrevem em arquivos ou fluxos.
     */
    void accept(String symbol) throws IOException;
}
