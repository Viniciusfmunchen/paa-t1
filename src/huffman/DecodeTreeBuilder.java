package huffman;

import java.util.Map;

/**
 * Constrói a árvore de decodificação a partir do dicionário Huffman salvo no arquivo compactado.
 *
 * Entrada: mapa símbolo -> código binário.
 * Resultado: árvore que permite navegar bit a bit até recuperar símbolos originais.
 * Pré-condição: o dicionário deve existir, não estar vazio e conter apenas códigos com 0 e 1.
 * Pós-condição: retorna uma raiz de DecodeNode pronta para uso na descompactação.
 * Observação: a validação impede códigos duplicados, vazios ou que violem a regra de prefixo.
 */
public class DecodeTreeBuilder {
    /**
     * Constrói a árvore completa de decodificação.
     *
     * Entrada: dicionário Huffman salvo no cabeçalho do arquivo compactado.
     * Resultado: raiz da árvore de decodificação.
     * Pré-condição: dictionary deve conter todos os símbolos usados na compactação.
     * Pós-condição: todos os códigos do dicionário ficam representados como caminhos na árvore.
     * Observação: cada símbolo fica armazenado em uma folha da árvore.
     */
    public DecodeNode build(Map<String, String> dictionary) {
        if (dictionary == null || dictionary.isEmpty()) {
            throw new IllegalArgumentException("Dicionário Huffman ausente.");
        }

        DecodeNode root = new DecodeNode();

        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            insertCode(root, entry.getKey(), entry.getValue());
        }

        return root;
    }

    /**
     * Insere um único código Huffman na árvore de decodificação.
     *
     * Entrada: raiz da árvore, símbolo original e código binário associado.
     * Resultado: void.
     * Pré-condição: root deve existir; symbol e code devem ser válidos; code deve conter apenas 0 e 1.
     * Pós-condição: o caminho correspondente ao código termina em um nó folha com o símbolo informado.
     * Observação: se um código for prefixo de outro, a função lança erro para impedir decodificação ambígua.
     */
    private void insertCode(DecodeNode root, String symbol, String code) {
        if (symbol == null || code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Entrada inválida no dicionário Huffman.");
        }

        DecodeNode current = root;
        for (int i = 0; i < code.length(); i++) {
            if (current.getSymbol() != null) {
                throw new IllegalArgumentException("Dicionário inválido: um código é prefixo de outro.");
            }

            char bit = code.charAt(i);
            if (bit == '0') {
                current = current.createZeroIfAbsent();
            } else if (bit == '1') {
                current = current.createOneIfAbsent();
            } else {
                throw new IllegalArgumentException("Dicionário contém bit inválido: " + bit);
            }
        }

        if (current.getSymbol() != null || current.hasChildren()) {
            throw new IllegalArgumentException("Dicionário inválido: código duplicado ou prefixo incorreto.");
        }

        current.setSymbol(symbol);
    }
}
