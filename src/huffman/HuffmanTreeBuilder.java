package huffman;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Classe responsável por construir a árvore de Huffman a partir das frequências.
 *
 * Entrada: mapa de símbolos e frequências.
 * Resultado: raiz da árvore de Huffman.
 * Pré-condição: mapa de frequências deve conter pelo menos um símbolo.
 * Pós-condição: a árvore resultante pode ser percorrida para gerar códigos binários.
 * Observação: símbolos menos frequentes tendem a ficar mais distantes da raiz e recebem códigos maiores.
 */
public class HuffmanTreeBuilder {
    /**
     * Constrói a árvore de Huffman usando uma fila de prioridade.
     *
     * Entrada: Map em que a chave é o símbolo e o valor é sua frequência.
     * Resultado: nó raiz da árvore de Huffman.
     * Pré-condição: frequencies não pode ser nulo nem vazio.
     * Pós-condição: retorna uma árvore binária em que cada folha representa um símbolo do texto.
     * Observação: as entradas são ordenadas por chave antes da fila para tornar o desempate mais previsível.
     */
    public HuffmanNode build(Map<String, Long> frequencies) {
        if (frequencies == null || frequencies.isEmpty()) {
            throw new IllegalArgumentException("Não é possível construir a árvore com mapa de frequências vazio.");
        }

        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        List<Map.Entry<String, Long>> entries = new ArrayList<>(frequencies.entrySet());
        entries.sort(Map.Entry.comparingByKey());

        long order = 0L;
        for (Map.Entry<String, Long> entry : entries) {
            queue.add(new HuffmanNode(entry.getKey(), entry.getValue(), order++));
        }

        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();
            HuffmanNode parent = new HuffmanNode(null, left.getFrequency() + right.getFrequency(), order++);
            parent.setLeft(left);
            parent.setRight(right);
            queue.add(parent);
        }

        return queue.poll();
    }
}
