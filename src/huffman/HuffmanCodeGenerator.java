package huffman;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por gerar o dicionário de códigos a partir da árvore de Huffman.
 *
 * Entrada: raiz da árvore de Huffman.
 * Resultado: mapa símbolo -> código binário.
 * Pré-condição: a árvore deve ter sido construída a partir das frequências do texto.
 * Pós-condição: cada símbolo recebe um código de tamanho variável.
 * Observação: caminhos à esquerda recebem 0 e caminhos à direita recebem 1.
 */
public class HuffmanCodeGenerator {
    /**
     * Gera o dicionário completo de códigos Huffman.
     *
     * Entrada: raiz da árvore de Huffman.
     * Resultado: mapa em que cada símbolo possui um código composto por 0 e 1.
     * Pré-condição: root não pode ser nulo.
     * Pós-condição: o dicionário fica pronto para compactar o arquivo e para ser salvo no cabeçalho.
     * Observação: se a árvore tiver apenas um símbolo, o código atribuído será "0".
     */
    public Map<String, String> generate(HuffmanNode root) {
        if (root == null) {
            throw new IllegalArgumentException("A raiz da árvore não pode ser nula.");
        }

        Map<String, String> dictionary = new HashMap<>();
        generateRecursively(root, "", dictionary);
        return dictionary;
    }

    /**
     * Percorre a árvore recursivamente e registra o código de cada folha.
     *
     * Entrada: nó atual, código acumulado e dicionário em construção.
     * Resultado: void.
     * Pré-condição: dictionary deve estar inicializado; currentCode deve representar o caminho até o nó atual.
     * Pós-condição: quando uma folha é encontrada, seu símbolo e código são inseridos no dicionário.
     * Observação: a recursão para quando encontra uma folha, pois folhas representam símbolos reais.
     */
    private void generateRecursively(
            HuffmanNode node,
            String currentCode,
            Map<String, String> dictionary
    ) {
        if (node == null) {
            return;
        }

        if (node.isLeaf()) {
            dictionary.put(node.getSymbol(), currentCode.isEmpty() ? "0" : currentCode);
            return;
        }

        generateRecursively(node.getLeft(), currentCode + "0", dictionary);
        generateRecursively(node.getRight(), currentCode + "1", dictionary);
    }
}
