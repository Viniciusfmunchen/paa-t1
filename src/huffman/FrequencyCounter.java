package huffman;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por contar frequências dos símbolos do arquivo.
 *
 * Entrada: arquivo de texto e modo de compressão.
 * Resultado: mapa contendo cada símbolo e sua frequência.
 * Pré-condição: arquivo deve existir, ser legível e estar em UTF-8.
 * Pós-condição: as frequências ficam prontas para a construção da árvore de Huffman.
 * Observação: o conceito de símbolo muda conforme o modo: caracter individual ou token de palavra/separador.
 */
public class FrequencyCounter {
    private final TextTokenizer tokenizer;

    /**
     * Cria um contador de frequências usando um tokenizador específico.
     *
     * Entrada: TextTokenizer responsável por percorrer os símbolos do arquivo.
     * Resultado: nova instância de FrequencyCounter.
     * Pré-condição: tokenizer deve estar inicializado.
     * Pós-condição: o contador fica pronto para contar símbolos.
     * Observação: usar injeção do tokenizador evita duplicar lógica de leitura de texto.
     */
    public FrequencyCounter(TextTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     * Conta a frequência de todos os símbolos encontrados no arquivo.
     *
     * Entrada: caminho do arquivo de entrada e modo de compressão.
     * Resultado: Map em que a chave é o símbolo e o valor é sua quantidade de ocorrências.
     * Pré-condição: inputFile deve apontar para um arquivo de texto legível; mode deve ser válido.
     * Pós-condição: retorna um mapa preenchido ou vazio caso o arquivo não contenha símbolos.
     * Observação: a leitura é feita em fluxo pelo TextTokenizer, evitando carregar o arquivo inteiro em memória.
     */
    public Map<String, Long> count(Path inputFile, CompressionMode mode) throws IOException {
        final Map<String, Long> frequencies = new HashMap<>();
        tokenizer.forEachSymbol(inputFile, mode, symbol -> addFrequency(frequencies, symbol));
        return frequencies;
    }

    /**
     * Incrementa a frequência de um símbolo no mapa.
     *
     * Entrada: mapa de frequências e símbolo encontrado no texto.
     * Resultado: void.
     * Pré-condição: frequencies deve estar inicializado; symbol deve representar um símbolo válido do texto.
     * Pós-condição: a contagem do símbolo é criada ou incrementada em uma unidade.
     * Observação: usa Long para suportar arquivos grandes com muitas ocorrências.
     */
    private void addFrequency(Map<String, Long> frequencies, String symbol) {
        Long currentFrequency = frequencies.get(symbol);
        if (currentFrequency == null) {
            frequencies.put(symbol, 1L);
        } else {
            frequencies.put(symbol, currentFrequency + 1L);
        }
    }
}
