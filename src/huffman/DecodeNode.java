package huffman;

/**
 * Nó usado na árvore de decodificação de Huffman.
 *
 * Entrada: códigos binários do dicionário Huffman salvo no cabeçalho.
 * Resultado: estrutura em árvore que permite converter bits novamente em símbolos.
 * Pré-condição: os códigos inseridos devem obedecer à propriedade de prefixo do Huffman.
 * Pós-condição: folhas da árvore armazenam símbolos; caminhos internos representam bits 0 e 1.
 * Observação: este nó é diferente do HuffmanNode, pois aqui o objetivo é apenas decodificar, não calcular frequências.
 */
public class DecodeNode {
    private String symbol;
    private DecodeNode zero;
    private DecodeNode one;

    /**
     * Retorna o símbolo armazenado neste nó, caso ele seja uma folha.
     *
     * Entrada: nenhuma.
     * Resultado: símbolo do nó ou null quando o nó é interno.
     * Pré-condição: o nó deve pertencer a uma árvore de decodificação.
     * Pós-condição: o estado do nó não é alterado.
     * Observação: um símbolo diferente de null indica que um código Huffman completo foi encontrado.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Define o símbolo armazenado neste nó.
     *
     * Entrada: símbolo original do arquivo de texto.
     * Resultado: void.
     * Pré-condição: o nó deve representar o final de um código Huffman válido.
     * Pós-condição: o nó passa a representar uma folha da árvore de decodificação.
     * Observação: símbolos podem ser caracteres, palavras ou separadores, dependendo do modo de compressão.
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Retorna o filho associado ao bit 0.
     *
     * Entrada: nenhuma.
     * Resultado: DecodeNode do caminho zero, ou null se não existir.
     * Pré-condição: nenhuma.
     * Pós-condição: o estado do nó não é alterado.
     * Observação: durante a leitura dos bits, bit falso/zero segue este caminho.
     */
    public DecodeNode getZero() {
        return zero;
    }

    /**
     * Cria e retorna o filho associado ao bit 0 quando ele ainda não existe.
     *
     * Entrada: nenhuma.
     * Resultado: filho zero existente ou recém-criado.
     * Pré-condição: o nó atual deve ser parte da árvore em construção.
     * Pós-condição: o campo zero passa a apontar para um DecodeNode válido.
     * Observação: usado ao inserir códigos do dicionário na árvore.
     */
    public DecodeNode createZeroIfAbsent() {
        if (zero == null) {
            zero = new DecodeNode();
        }
        return zero;
    }

    /**
     * Retorna o filho associado ao bit 1.
     *
     * Entrada: nenhuma.
     * Resultado: DecodeNode do caminho um, ou null se não existir.
     * Pré-condição: nenhuma.
     * Pós-condição: o estado do nó não é alterado.
     * Observação: durante a leitura dos bits, bit verdadeiro/um segue este caminho.
     */
    public DecodeNode getOne() {
        return one;
    }

    /**
     * Cria e retorna o filho associado ao bit 1 quando ele ainda não existe.
     *
     * Entrada: nenhuma.
     * Resultado: filho um existente ou recém-criado.
     * Pré-condição: o nó atual deve ser parte da árvore em construção.
     * Pós-condição: o campo one passa a apontar para um DecodeNode válido.
     * Observação: usado ao inserir códigos do dicionário na árvore.
     */
    public DecodeNode createOneIfAbsent() {
        if (one == null) {
            one = new DecodeNode();
        }
        return one;
    }

    /**
     * Informa se o nó possui pelo menos um filho.
     *
     * Entrada: nenhuma.
     * Resultado: true se existir filho zero ou filho um; false caso contrário.
     * Pré-condição: nenhuma.
     * Pós-condição: o estado do nó não é alterado.
     * Observação: usado para validar se um código é prefixo indevido de outro código.
     */
    public boolean hasChildren() {
        return zero != null || one != null;
    }
}
