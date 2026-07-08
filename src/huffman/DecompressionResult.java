package huffman;

/**
 * Resultado estatístico de uma operação de descompactação.
 *
 * Entrada: dados coletados após a descompactação.
 * Resultado: objeto imutável com métricas de saída.
 * Pré-condição: a descompactação deve ter sido concluída com sucesso.
 * Pós-condição: modo original, tamanhos e tempo ficam disponíveis por getters.
 * Observação: usado para imprimir informações da operação no console.
 */
public class DecompressionResult {
    private final CompressionMode originalMode;
    private final long compressedSize;
    private final long decompressedSize;
    private final long elapsedMilliseconds;

    /**
     * Cria um resultado de descompactação.
     *
     * Entrada: modo original, tamanho compactado, tamanho descompactado e tempo de execução.
     * Resultado: nova instância de DecompressionResult.
     * Pré-condição: os dados devem ter sido medidos após a operação.
     * Pós-condição: as métricas ficam armazenadas para consulta.
     * Observação: o modo original vem do cabeçalho do arquivo compactado.
     */
    public DecompressionResult(
            CompressionMode originalMode,
            long compressedSize,
            long decompressedSize,
            long elapsedMilliseconds
    ) {
        this.originalMode = originalMode;
        this.compressedSize = compressedSize;
        this.decompressedSize = decompressedSize;
        this.elapsedMilliseconds = elapsedMilliseconds;
    }

    /**
     * Retorna o modo usado na compactação original.
     *
     * Entrada: nenhuma.
     * Resultado: CompressionMode lido do cabeçalho.
     * Pré-condição: objeto deve estar inicializado.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: permite exibir se o arquivo veio de compactação por caracter ou por palavra.
     */
    public CompressionMode getOriginalMode() {
        return originalMode;
    }

    /**
     * Retorna o tamanho do arquivo compactado lido.
     *
     * Entrada: nenhuma.
     * Resultado: tamanho em bytes do arquivo compactado.
     * Pré-condição: o arquivo compactado deve ter sido medido.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: usado apenas para relatório de console.
     */
    public long getCompressedSize() {
        return compressedSize;
    }

    /**
     * Retorna o tamanho do arquivo descompactado gerado.
     *
     * Entrada: nenhuma.
     * Resultado: tamanho em bytes do arquivo reconstruído.
     * Pré-condição: o arquivo de saída deve ter sido criado com sucesso.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: em uma descompactação correta, deve corresponder ao tamanho do texto original.
     */
    public long getDecompressedSize() {
        return decompressedSize;
    }

    /**
     * Retorna o tempo de descompactação.
     *
     * Entrada: nenhuma.
     * Resultado: tempo em milissegundos.
     * Pré-condição: a operação deve ter sido medida com System.nanoTime().
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: valor útil para a análise empírica de desempenho.
     */
    public long getElapsedMilliseconds() {
        return elapsedMilliseconds;
    }
}
