package huffman;

import java.io.Serializable;
import java.util.Map;

/**
 * Cabeçalho serializável salvo no início do arquivo compactado.
 *
 * Entrada: modo de compactação, dicionário Huffman e quantidade de bits válidos.
 * Resultado: objeto que pode ser gravado e lido por ObjectOutputStream/ObjectInputStream.
 * Pré-condição: o dicionário deve representar corretamente os símbolos compactados.
 * Pós-condição: a descompactação consegue recuperar as informações necessárias para reconstruir o texto.
 * Observação: salvar o dicionário evita depender de reconstrução por frequências, que pode variar quando há empates.
 */
public class CompressedFileHeader implements Serializable {
    private static final long serialVersionUID = 1L;

    private final CompressionMode mode;
    private final Map<String, String> dictionary;
    private final long validBits;

    /**
     * Cria o cabeçalho do arquivo compactado.
     *
     * Entrada: modo usado na compressão, dicionário símbolo-código e total de bits válidos.
     * Resultado: nova instância de CompressedFileHeader.
     * Pré-condição: mode e dictionary devem estar preenchidos; validBits deve ser maior ou igual a zero.
     * Pós-condição: os dados ficam disponíveis para gravação no arquivo compactado.
     * Observação: os bytes compactados são gravados depois deste cabeçalho.
     */
    public CompressedFileHeader(
            CompressionMode mode,
            Map<String, String> dictionary,
            long validBits
    ) {
        this.mode = mode;
        this.dictionary = dictionary;
        this.validBits = validBits;
    }

    /**
     * Retorna o modo de compressão usado no arquivo original.
     *
     * Entrada: nenhuma.
     * Resultado: CompressionMode associado ao arquivo compactado.
     * Pré-condição: o cabeçalho deve ter sido construído corretamente.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: usado apenas para exibição e registro, pois a descompactação usa o dicionário salvo.
     */
    public CompressionMode getMode() {
        return mode;
    }

    /**
     * Retorna o dicionário Huffman salvo no cabeçalho.
     *
     * Entrada: nenhuma.
     * Resultado: mapa em que a chave é o símbolo original e o valor é o código binário.
     * Pré-condição: o cabeçalho deve ter sido lido ou construído corretamente.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: este dicionário é usado para construir a árvore de decodificação.
     */
    public Map<String, String> getDictionary() {
        return dictionary;
    }

    /**
     * Retorna a quantidade de bits úteis no corpo compactado.
     *
     * Entrada: nenhuma.
     * Resultado: número de bits válidos que devem ser lidos na descompactação.
     * Pré-condição: validBits deve ter sido calculado na compactação.
     * Pós-condição: o estado do objeto não é alterado.
     * Observação: impede que a descompactação interprete zeros adicionados no último byte como dados reais.
     */
    public long getValidBits() {
        return validBits;
    }
}
