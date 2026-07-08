package huffman;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classe responsável pelo fluxo completo de descompactação Huffman.
 *
 * Entrada: arquivo compactado gerado por esta aplicação e caminho de saída.
 * Resultado: arquivo-texto reconstruído em UTF-8.
 * Pré-condição: o arquivo de entrada deve possuir cabeçalho CompressedFileHeader válido e bytes compactados compatíveis.
 * Pós-condição: o conteúdo original é reescrito no arquivo de saída.
 * Observação: a descompactação usa o dicionário salvo no cabeçalho, sem recalcular frequências.
 */
public class HuffmanDecompressor {
    private final DecodeTreeBuilder decodeTreeBuilder;

    /**
     * Cria uma instância da descompactadora.
     *
     * Entrada: nenhuma.
     * Resultado: HuffmanDecompressor pronta para uso.
     * Pré-condição: nenhuma.
     * Pós-condição: o construtor da árvore de decodificação é inicializado.
     * Observação: a árvore de decodificação é reconstruída para cada arquivo descompactado.
     */
    public HuffmanDecompressor() {
        this.decodeTreeBuilder = new DecodeTreeBuilder();
    }

    /**
     * Descompacta um arquivo gerado pela aplicação.
     *
     * Entrada: caminho do arquivo compactado e caminho do arquivo reconstruído.
     * Resultado: DecompressionResult com modo original, tamanhos e tempo de execução.
     * Pré-condição: inputFile deve ser um arquivo compactado válido; outputFile deve apontar para local gravável.
     * Pós-condição: o arquivo de saída contém o texto original reconstruído.
     * Observação: o método mede o tempo total da leitura do cabeçalho, decodificação e escrita do arquivo final.
     */
    public DecompressionResult decompress(Path inputFile, Path outputFile) throws IOException, ClassNotFoundException {
        long startTime = System.nanoTime();
        CompressionMode originalMode;

        try (ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(inputFile)))) {
            CompressedFileHeader header = readHeader(inputStream);
            originalMode = header.getMode();
            DecodeNode root = decodeTreeBuilder.build(header.getDictionary());

            try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
                decodeBytes(inputStream, writer, root, header.getValidBits());
            }
        }

        long elapsedMilliseconds = (System.nanoTime() - startTime) / 1_000_000L;
        return new DecompressionResult(
                originalMode,
                Files.size(inputFile),
                Files.size(outputFile),
                elapsedMilliseconds
        );
    }

    /**
     * Lê e valida o cabeçalho serializado no início do arquivo compactado.
     *
     * Entrada: ObjectInputStream posicionado no início do arquivo compactado.
     * Resultado: CompressedFileHeader validado.
     * Pré-condição: inputStream deve estar aberto e conter um objeto CompressedFileHeader na primeira posição.
     * Pós-condição: o cabeçalho é retornado e o fluxo fica posicionado no início dos bytes compactados.
     * Observação: validações básicas evitam tentar decodificar arquivos corrompidos ou incompatíveis.
     */
    private CompressedFileHeader readHeader(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        Object object = inputStream.readObject();
        if (!(object instanceof CompressedFileHeader)) {
            throw new IllegalArgumentException("Arquivo compactado inválido: cabeçalho não reconhecido.");
        }

        CompressedFileHeader header = (CompressedFileHeader) object;
        if (header.getMode() == null) {
            throw new IllegalArgumentException("Arquivo compactado inválido: modo de compressão ausente.");
        }
        if (header.getValidBits() < 0) {
            throw new IllegalArgumentException("Arquivo compactado inválido: quantidade de bits inválida.");
        }
        if (header.getDictionary() == null || header.getDictionary().isEmpty()) {
            throw new IllegalArgumentException("Arquivo compactado inválido: dicionário ausente.");
        }

        return header;
    }

    /**
     * Decodifica os bytes compactados e escreve os símbolos originais no arquivo de saída.
     *
     * Entrada: fluxo com bytes compactados, escritor de texto, raiz da árvore de decodificação e quantidade de bits válidos.
     * Resultado: void.
     * Pré-condição: root deve representar o dicionário usado na compactação; validBits deve indicar o total real de bits úteis.
     * Pós-condição: os símbolos recuperados são escritos no BufferedWriter na ordem original.
     * Observação: bits de preenchimento do último byte são ignorados porque o laço para em validBits.
     */
    private void decodeBytes(
            ObjectInputStream inputStream,
            BufferedWriter writer,
            DecodeNode root,
            long validBits
    ) throws IOException {
        DecodeNode current = root;
        long bitsRead = 0L;

        while (bitsRead < validBits) {
            int currentByte = inputStream.read();
            if (currentByte == -1) {
                throw new IllegalArgumentException("Arquivo compactado inválido: bytes insuficientes.");
            }

            for (int bitPosition = 7; bitPosition >= 0 && bitsRead < validBits; bitPosition--) {
                boolean bit = (currentByte & (1 << bitPosition)) != 0;
                current = bit ? current.getOne() : current.getZero();

                if (current == null) {
                    throw new IllegalArgumentException("Arquivo compactado inválido: sequência de bits não encontrada no dicionário.");
                }

                if (current.getSymbol() != null) {
                    writer.write(current.getSymbol());
                    current = root;
                }

                bitsRead++;
            }
        }

        if (current != root) {
            throw new IllegalArgumentException("Arquivo compactado inválido: terminou no meio de um código Huffman.");
        }
    }
}
