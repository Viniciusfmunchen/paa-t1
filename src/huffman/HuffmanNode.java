package huffman;

/**
 * Nó usado na árvore de Huffman durante a compactação.
 *
 * Entrada: símbolo, frequência e ordem de criação.
 * Resultado: estrutura de árvore binária usada para gerar códigos de tamanho variável.
 * Pré-condição: frequências devem ser calculadas antes da criação da árvore.
 * Pós-condição: os nós folhas representam símbolos e os nós internos representam agrupamentos de frequências.
 * Observação: implementa Comparable para que a PriorityQueue escolha sempre os menores pesos primeiro.
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    private final String symbol;
    private final long frequency;
    private final long order;
    private HuffmanNode left;
    private HuffmanNode right;

    /**
     * Cria um nó da árvore de Huffman.
     *
     * Entrada: símbolo, frequência e ordem de desempate.
     * Resultado: nova instância de HuffmanNode.
     * Pré-condição: frequency deve representar a quantidade de ocorrências do símbolo ou soma de filhos.
     * Pós-condição: o nó é criado sem filhos inicialmente.
     * Observação: symbol é null em nós internos e diferente de null em folhas.
     */
    public HuffmanNode(String symbol, long frequency, long order) {
        this.symbol = symbol;
        this.frequency = frequency;
        this.order = order;
    }

    /**
     * Retorna o símbolo armazenado no nó.
     *
     * Entrada: nenhuma.
     * Resultado: símbolo do nó ou null em nó interno.
     * Pré-condição: nó deve estar inicializado.
     * Pós-condição: o estado do nó não é alterado.
     * Observação: apenas folhas representam símbolos reais do arquivo.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Retorna a frequência do nó.
     *
     * Entrada: nenhuma.
     * Resultado: frequência do símbolo ou soma das frequências dos filhos.
     * Pré-condição: nó deve estar inicializado.
     * Pós-condição: o estado do nó não é alterado.
     * Observação: a frequência define a prioridade do nó na construção da árvore.
     */
    public long getFrequency() {
        return frequency;
    }

    /**
     * Retorna o filho esquerdo do nó.
     *
     * Entrada: nenhuma.
     * Resultado: HuffmanNode à esquerda ou null.
     * Pré-condição: nenhuma.
     * Pós-condição: o estado do nó não é alterado.
     * Observação: durante a geração dos códigos, o caminho esquerdo recebe bit 0.
     */
    public HuffmanNode getLeft() {
        return left;
    }

    /**
     * Define o filho esquerdo do nó.
     *
     * Entrada: nó filho esquerdo.
     * Resultado: void.
     * Pré-condição: left deve ser um nó válido da árvore ou null.
     * Pós-condição: o campo left passa a apontar para o nó informado.
     * Observação: usado pelo construtor de árvore ao agrupar os dois menores nós.
     */
    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    /**
     * Retorna o filho direito do nó.
     *
     * Entrada: nenhuma.
     * Resultado: HuffmanNode à direita ou null.
     * Pré-condição: nenhuma.
     * Pós-condição: o estado do nó não é alterado.
     * Observação: durante a geração dos códigos, o caminho direito recebe bit 1.
     */
    public HuffmanNode getRight() {
        return right;
    }

    /**
     * Define o filho direito do nó.
     *
     * Entrada: nó filho direito.
     * Resultado: void.
     * Pré-condição: right deve ser um nó válido da árvore ou null.
     * Pós-condição: o campo right passa a apontar para o nó informado.
     * Observação: usado pelo construtor de árvore ao agrupar os dois menores nós.
     */
    public void setRight(HuffmanNode right) {
        this.right = right;
    }

    /**
     * Verifica se o nó é uma folha.
     *
     * Entrada: nenhuma.
     * Resultado: true se não possuir filhos; false caso contrário.
     * Pré-condição: nenhuma.
     * Pós-condição: o estado do nó não é alterado.
     * Observação: folhas são os nós que recebem códigos Huffman no dicionário.
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }

    /**
     * Compara este nó com outro para ordenação na fila de prioridade.
     *
     * Entrada: outro HuffmanNode.
     * Resultado: número negativo, zero ou positivo conforme a prioridade relativa.
     * Pré-condição: other não deve ser nulo.
     * Pós-condição: o estado dos nós não é alterado.
     * Observação: primeiro compara por frequência; em empate, usa order para manter comportamento determinístico.
     */
    @Override
    public int compareTo(HuffmanNode other) {
        int frequencyComparison = Long.compare(this.frequency, other.frequency);
        if (frequencyComparison != 0) {
            return frequencyComparison;
        }
        return Long.compare(this.order, other.order);
    }
}
