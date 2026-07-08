package huffman;

import java.io.Serializable;

/**
 * Enumeração dos modos de compressão suportados pelo programa.
 *
 * Entrada: nenhuma em tempo de execução; os valores são fixos no código.
 * Resultado: representação segura dos modos por caracter e por palavra.
 * Pré-condição: nenhuma.
 * Pós-condição: os demais componentes podem escolher o comportamento correto com base no modo.
 * Observação: implementar Serializable permite salvar o modo dentro do cabeçalho compactado.
 */
public enum CompressionMode implements Serializable {
    CHARACTER("caracter"),
    WORD("palavra");

    private final String description;

    /**
     * Associa uma descrição textual ao modo de compressão.
     *
     * Entrada: descrição curta do modo.
     * Resultado: instância do enum com descrição armazenada.
     * Pré-condição: description deve representar o modo de forma compreensível ao usuário.
     * Pós-condição: a descrição fica disponível pelo método getDescription().
     * Observação: construtores de enum são chamados automaticamente pela JVM.
     */
    CompressionMode(String description) {
        this.description = description;
    }

    /**
     * Retorna a descrição textual do modo de compressão.
     *
     * Entrada: nenhuma.
     * Resultado: texto usado nos relatórios de console.
     * Pré-condição: o modo deve existir.
     * Pós-condição: o estado do enum não é alterado.
     * Observação: evita repetir textos fixos em outras classes.
     */
    public String getDescription() {
        return description;
    }
}
