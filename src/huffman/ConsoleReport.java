package huffman;

import java.util.Locale;

/**
 * Classe utilitária para imprimir resultados de compactação e descompactação no console.
 *
 * Entrada: objetos de resultado das operações.
 * Resultado: informações formatadas impressas na saída padrão.
 * Pré-condição: os objetos de resultado devem estar preenchidos.
 * Pós-condição: o usuário visualiza métricas úteis para análise e relatório.
 * Observação: a classe não possui estado e não deve ser instanciada.
 */
public class ConsoleReport {
    /**
     * Impede a instanciação da classe utilitária.
     *
     * Entrada: nenhuma.
     * Resultado: construtor privado.
     * Pré-condição: nenhuma.
     * Pós-condição: objetos ConsoleReport não podem ser criados externamente.
     * Observação: todos os métodos úteis da classe são estáticos.
     */
    private ConsoleReport() {
    }

    /**
     * Imprime as métricas de uma compactação concluída.
     *
     * Entrada: CompressionResult com dados da operação.
     * Resultado: void.
     * Pré-condição: result não deve ser nulo e deve conter métricas válidas.
     * Pós-condição: modo, tamanhos, taxa, redução, tempo, dicionário e bits válidos são exibidos no console.
     * Observação: esses dados podem ser copiados para a tabela do relatório.
     */
    public static void printCompressionResult(CompressionResult result) {
        System.out.println("\nOperação concluída com sucesso.");
        System.out.println("Modo: " + result.getMode().getDescription());
        System.out.println("Tamanho original: " + formatBytes(result.getOriginalSize()));
        System.out.println("Tamanho compactado: " + formatBytes(result.getCompressedSize()));
        System.out.printf(Locale.US, "Taxa compactado/original: %.2f%%%n", result.getCompressionRatio() * 100.0);
        System.out.printf(Locale.US, "Redução obtida: %.2f%%%n", result.getReductionPercentage() * 100.0);
        System.out.println("Tempo de compactação: " + result.getElapsedMilliseconds() + " ms");
        System.out.println("Símbolos no dicionário: " + result.getDictionarySize());
        System.out.println("Bits válidos gerados: " + result.getValidBits());
    }

    /**
     * Imprime as métricas de uma descompactação concluída.
     *
     * Entrada: DecompressionResult com dados da operação.
     * Resultado: void.
     * Pré-condição: result não deve ser nulo e deve conter métricas válidas.
     * Pós-condição: modo original, tamanhos e tempo de descompactação são exibidos no console.
     * Observação: confirma ao usuário qual tipo de arquivo compactado foi processado.
     */
    public static void printDecompressionResult(DecompressionResult result) {
        System.out.println("\nOperação concluída com sucesso.");
        System.out.println("Modo original do arquivo: " + result.getOriginalMode().getDescription());
        System.out.println("Tamanho do arquivo compactado: " + formatBytes(result.getCompressedSize()));
        System.out.println("Tamanho do arquivo descompactado: " + formatBytes(result.getDecompressedSize()));
        System.out.println("Tempo de descompactação: " + result.getElapsedMilliseconds() + " ms");
    }

    /**
     * Formata uma quantidade de bytes em megabytes e bytes absolutos.
     *
     * Entrada: quantidade de bytes.
     * Resultado: String no formato "X.XX MB (N bytes)".
     * Pré-condição: bytes deve ser maior ou igual a zero para representar tamanho de arquivo.
     * Pós-condição: o valor é convertido apenas para exibição; o número original não é alterado.
     * Observação: usa Locale.US para manter ponto decimal no formato numérico.
     */
    private static String formatBytes(long bytes) {
        return String.format(Locale.US, "%.2f MB (%d bytes)", bytes / 1024.0 / 1024.0, bytes);
    }
}
