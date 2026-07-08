package huffman;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Classe responsável por escrever códigos binários de Huffman em um fluxo de bytes.
 *
 * Entrada: códigos formados pelos caracteres '0' e '1'.
 * Resultado: bytes escritos em um OutputStream, compactando oito bits em cada byte.
 * Pré-condição: o OutputStream deve estar aberto e apto para escrita.
 * Pós-condição: os bits recebidos são armazenados em bytes, com preenchimento no último byte quando necessário.
 * Observação: esta classe evita gravar os caracteres '0' e '1' como texto, reduzindo o tamanho do arquivo compactado.
 */
public class BitWriter implements Closeable {
    private final OutputStream outputStream;
    private int currentByte;
    private int bitCount;
    private long totalBits;
    private boolean finished;

    /**
     * Cria um escritor de bits associado a um fluxo de saída.
     *
     * Entrada: OutputStream onde os bytes compactados serão gravados.
     * Resultado: nova instância de BitWriter.
     * Pré-condição: outputStream não deve ser nulo e deve estar aberto.
     * Pós-condição: o BitWriter fica pronto para receber códigos Huffman.
     * Observação: o fechamento do BitWriter também fecha o OutputStream recebido.
     */
    public BitWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Escreve um código Huffman completo no fluxo de saída.
     *
     * Entrada: String contendo apenas os caracteres '0' e '1'.
     * Resultado: void.
     * Pré-condição: code deve existir, não estar vazio e representar um código Huffman válido.
     * Pós-condição: todos os bits do código são adicionados ao buffer interno e escritos quando completam bytes.
     * Observação: cada caractere do código é processado individualmente pelo método writeBit().
     */
    public void writeCode(String code) throws IOException {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Código Huffman ausente.");
        }

        for (int i = 0; i < code.length(); i++) {
            writeBit(code.charAt(i));
        }
    }

    /**
     * Retorna a quantidade total de bits úteis escritos.
     *
     * Entrada: nenhuma.
     * Resultado: quantidade de bits reais gerados pela compactação.
     * Pré-condição: nenhuma.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: esse valor é salvo no cabeçalho para ignorar bits de preenchimento na descompactação.
     */
    public long getTotalBits() {
        return totalBits;
    }

    /**
     * Finaliza a escrita dos bits, completando o último byte se ele estiver incompleto.
     *
     * Entrada: nenhuma.
     * Resultado: void.
     * Pré-condição: outputStream deve estar aberto.
     * Pós-condição: todos os bits pendentes são gravados e o fluxo é descarregado com flush().
     * Observação: se o último byte tiver menos de oito bits, zeros são adicionados apenas como preenchimento.
     */
    public void finish() throws IOException {
        if (finished) {
            return;
        }

        if (bitCount > 0) {
            currentByte = currentByte << (8 - bitCount);
            outputStream.write(currentByte);
            currentByte = 0;
            bitCount = 0;
        }

        outputStream.flush();
        finished = true;
    }

    /**
     * Finaliza a escrita e fecha o fluxo de saída.
     *
     * Entrada: nenhuma.
     * Resultado: void.
     * Pré-condição: outputStream deve ter sido inicializado.
     * Pós-condição: os bits pendentes são gravados e o OutputStream é fechado.
     * Observação: chamado automaticamente quando usado em try-with-resources.
     */
    @Override
    public void close() throws IOException {
        finish();
        outputStream.close();
    }

    /**
     * Escreve um único bit no byte atualmente em construção.
     *
     * Entrada: caractere '0' ou '1'.
     * Resultado: void.
     * Pré-condição: bit deve ser obrigatoriamente '0' ou '1'.
     * Pós-condição: o bit é incorporado ao currentByte; ao completar oito bits, um byte é escrito no OutputStream.
     * Observação: o deslocamento à esquerda abre espaço para encaixar o novo bit no final do byte.
     */
    private void writeBit(char bit) throws IOException {
        if (bit != '0' && bit != '1') {
            throw new IllegalArgumentException("Bit inválido no código Huffman: " + bit);
        }

        currentByte = (currentByte << 1) | (bit == '1' ? 1 : 0);
        bitCount++;
        totalBits++;

        if (bitCount == 8) {
            outputStream.write(currentByte);
            currentByte = 0;
            bitCount = 0;
        }
    }
}
