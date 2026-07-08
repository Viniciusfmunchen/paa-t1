package huffman;

/**
 * Resultado estatístico de uma operação de compactação.
 *
 * Entrada: dados coletados após a compactação.
 * Resultado: objeto imutável com métricas para console e relatório.
 * Pré-condição: a compactação deve ter sido concluída com sucesso.
 * Pós-condição: as métricas ficam disponíveis por métodos getters.
 * Observação: separa os dados estatísticos da lógica de impressão no console.
 */
public class CompressionResult {
    private final CompressionMode mode;
    private final long originalSize;
    private final long compressedSize;
    private final long elapsedMilliseconds;
    private final int dictionarySize;
    private final long validBits;

    /**
     * Cria um resultado de compactação.
     *
     * Entrada: modo, tamanho original, tamanho compactado, tempo, tamanho do dicionário e bits válidos.
     * Resultado: nova instância de CompressionResult.
     * Pré-condição: os tamanhos e tempos devem ter sido medidos após a operação.
     * Pós-condição: as informações ficam armazenadas para consulta posterior.
     * Observação: os valores são finais, portanto não podem ser modificados depois da criação.
     */
    public CompressionResult(
            CompressionMode mode,
            long originalSize,
            long compressedSize,
            long elapsedMilliseconds,
            int dictionarySize,
            long validBits
    ) {
        this.mode = mode;
        this.originalSize = originalSize;
        this.compressedSize = compressedSize;
        this.elapsedMilliseconds = elapsedMilliseconds;
        this.dictionarySize = dictionarySize;
        this.validBits = validBits;
    }

    /**
     * Retorna o modo usado na compactação.
     *
     * Entrada: nenhuma.
     * Resultado: CompressionMode da operação.
     * Pré-condição: objeto deve estar inicializado.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: usado para identificar se o teste foi por caracter ou por palavra.
     */
    public CompressionMode getMode() {
        return mode;
    }

    /**
     * Retorna o tamanho do arquivo original.
     *
     * Entrada: nenhuma.
     * Resultado: tamanho em bytes do arquivo antes da compactação.
     * Pré-condição: arquivo original deve ter sido medido pela classe compactadora.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: usado no cálculo da taxa de compressão.
     */
    public long getOriginalSize() {
        return originalSize;
    }

    /**
     * Retorna o tamanho do arquivo compactado.
     *
     * Entrada: nenhuma.
     * Resultado: tamanho em bytes do arquivo compactado gerado.
     * Pré-condição: arquivo de saída deve ter sido criado com sucesso.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: inclui cabeçalho, dicionário e bytes compactados.
     */
    public long getCompressedSize() {
        return compressedSize;
    }

    /**
     * Retorna o tempo de compactação.
     *
     * Entrada: nenhuma.
     * Resultado: tempo em milissegundos.
     * Pré-condição: a operação deve ter sido medida com System.nanoTime().
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: valor usado na análise empírica de desempenho.
     */
    public long getElapsedMilliseconds() {
        return elapsedMilliseconds;
    }

    /**
     * Retorna a quantidade de símbolos distintos no dicionário.
     *
     * Entrada: nenhuma.
     * Resultado: número de entradas no dicionário Huffman.
     * Pré-condição: dicionário deve ter sido gerado durante a compactação.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: em modo palavra, esse valor tende a ser maior que no modo caracter.
     */
    public int getDictionarySize() {
        return dictionarySize;
    }

    /**
     * Retorna a quantidade de bits úteis gerados.
     *
     * Entrada: nenhuma.
     * Resultado: total de bits válidos no corpo compactado.
     * Pré-condição: BitWriter deve ter contabilizado os bits durante a escrita.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: não conta bits de preenchimento adicionados no último byte.
     */
    public long getValidBits() {
        return validBits;
    }

    /**
     * Calcula a razão entre arquivo compactado e arquivo original.
     *
     * Entrada: nenhuma.
     * Resultado: compressedSize / originalSize como número decimal.
     * Pré-condição: originalSize deve ser maior que zero para cálculo normal.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: quanto menor a razão, melhor foi a compressão; se o original for zero, retorna 0.0.
     */
    public double getCompressionRatio() {
        if (originalSize == 0) {
            return 0.0;
        }
        return (double) compressedSize / (double) originalSize;
    }

    /**
     * Calcula a redução percentual obtida pela compactação.
     *
     * Entrada: nenhuma.
     * Resultado: 1 - taxa de compressão.
     * Pré-condição: os tamanhos original e compactado devem estar definidos.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: resultado positivo indica redução; resultado negativo indica que o compactado ficou maior.
     */
    public double getReductionPercentage() {
        return 1.0 - getCompressionRatio();
    }
}
