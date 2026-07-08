package huffman;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Classe responsável pelo fluxo completo de compactação Huffman.
 *
 * Entrada: arquivo de texto, arquivo de saída e modo de compressão.
 * Resultado: arquivo compactado contendo cabeçalho e bytes comprimidos.
 * Pré-condição: arquivo de entrada deve existir, ser legível e estar em UTF-8.
 * Pós-condição: arquivo compactado é criado e um CompressionResult é retornado com métricas da operação.
 * Observação: os bytes compactados são escritos primeiro em arquivo temporário para reduzir consumo de memória.
 */
public class HuffmanCompressor {
    private final TextTokenizer tokenizer;
    private final FrequencyCounter frequencyCounter;
    private final HuffmanTreeBuilder treeBuilder;
    private final HuffmanCodeGenerator codeGenerator;

    /**
     * Cria uma instância da compactadora com suas dependências padrão.
     *
     * Entrada: nenhuma.
     * Resultado: HuffmanCompressor pronta para uso.
     * Pré-condição: nenhuma.
     * Pós-condição: tokenizador, contador de frequências, construtor de árvore e gerador de códigos são inicializados.
     * Observação: mantém as responsabilidades separadas em classes menores.
     */
    public HuffmanCompressor() {
        this.tokenizer = new TextTokenizer();
        this.frequencyCounter = new FrequencyCounter(tokenizer);
        this.treeBuilder = new HuffmanTreeBuilder();
        this.codeGenerator = new HuffmanCodeGenerator();
    }

    /**
     * Compacta um arquivo usando o algoritmo de Huffman.
     *
     * Entrada: caminho do arquivo original, caminho do arquivo compactado e modo de compressão.
     * Resultado: CompressionResult com tamanhos, tempo, quantidade de símbolos e bits válidos.
     * Pré-condição: inputFile deve existir; outputFile deve apontar para local gravável; mode deve ser CHARACTER ou WORD.
     * Pós-condição: o arquivo compactado é gravado em outputFile contendo cabeçalho e corpo binário.
     * Observação: o cabeçalho salva o dicionário Huffman para permitir descompactação determinística.
     */
    public CompressionResult compress(
            Path inputFile,
            Path outputFile,
            CompressionMode mode
    ) throws IOException {
        long startTime = System.nanoTime();
        long originalSize = Files.size(inputFile);

        Map<String, Long> frequencies = frequencyCounter.count(inputFile, mode);
        if (frequencies.isEmpty()) {
            throw new IllegalArgumentException("O arquivo de entrada está vazio.");
        }

        HuffmanNode root = treeBuilder.build(frequencies);
        Map<String, String> dictionary = codeGenerator.generate(root);

        Path temporaryCompressedBytes = Files.createTempFile("huffman-bytes-", ".tmp");
        long validBits;

        try {
            validBits = writeCompressedBytes(inputFile, mode, dictionary, temporaryCompressedBytes);
            writeCompressedFile(outputFile, mode, dictionary, validBits, temporaryCompressedBytes);
        } finally {
            Files.deleteIfExists(temporaryCompressedBytes);
        }

        long elapsedMilliseconds = (System.nanoTime() - startTime) / 1_000_000L;
        long compressedSize = Files.size(outputFile);

        return new CompressionResult(
                mode,
                originalSize,
                compressedSize,
                elapsedMilliseconds,
                dictionary.size(),
                validBits
        );
    }

    /**
     * Gera o corpo binário compactado em um arquivo temporário.
     *
     * Entrada: arquivo original, modo de compressão, dicionário Huffman e caminho temporário.
     * Resultado: quantidade de bits válidos escritos.
     * Pré-condição: dictionary deve conter código para todos os símbolos produzidos pelo tokenizer.
     * Pós-condição: temporaryCompressedBytes passa a conter os bytes compactados, ainda sem cabeçalho.
     * Observação: escrever em arquivo temporário evita manter todo o conteúdo compactado em memória.
     */
    private long writeCompressedBytes(
            Path inputFile,
            CompressionMode mode,
            Map<String, String> dictionary,
            Path temporaryCompressedBytes
    ) throws IOException {
        try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(temporaryCompressedBytes));
             BitWriter bitWriter = new BitWriter(outputStream)) {

            tokenizer.forEachSymbol(inputFile, mode, symbol -> bitWriter.writeCode(dictionary.get(symbol)));
            bitWriter.finish();
            return bitWriter.getTotalBits();
        }
    }

    /**
     * Grava o arquivo compactado final com cabeçalho e corpo binário.
     *
     * Entrada: arquivo de saída, modo, dicionário, quantidade de bits válidos e arquivo temporário com bytes compactados.
     * Resultado: void.
     * Pré-condição: temporaryCompressedBytes deve existir e conter o corpo compactado.
     * Pós-condição: outputFile contém primeiro o cabeçalho serializado e depois os bytes compactados.
     * Observação: ObjectOutputStream é usado apenas para o cabeçalho; os bytes compactados são copiados em seguida.
     */
    private void writeCompressedFile(
            Path outputFile,
            CompressionMode mode,
            Map<String, String> dictionary,
            long validBits,
            Path temporaryCompressedBytes
    ) throws IOException {
        CompressedFileHeader header = new CompressedFileHeader(mode, dictionary, validBits);

        try (OutputStream fileOutput = new BufferedOutputStream(Files.newOutputStream(outputFile));
             ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput)) {
            objectOutput.writeObject(header);
            objectOutput.flush();
            Files.copy(temporaryCompressedBytes, objectOutput);
            objectOutput.flush();
        }
    }
}
