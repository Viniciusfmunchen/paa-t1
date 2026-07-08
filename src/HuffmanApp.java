import huffman.CompressionMode;
import huffman.CompressionResult;
import huffman.ConsoleReport;
import huffman.DecompressionResult;
import huffman.HuffmanCompressor;
import huffman.HuffmanDecompressor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Classe principal da aplicação.
 *
 * Entrada: comandos digitados pelo usuário no console.
 * Resultado: permite compactar e descompactar arquivos-texto usando Huffman.
 * Pré-condição: o Java deve estar instalado e os arquivos informados devem existir quando forem usados como entrada.
 * Pós-condição: conforme a opção escolhida, um arquivo compactado ou descompactado é criado no caminho informado.
 * Observação: esta classe concentra apenas o menu e a interação com o usuário; a lógica de Huffman fica nas classes do pacote huffman.
 */
public class HuffmanApp {
    private static final int OPTION_COMPRESS_CHARACTER = 1;
    private static final int OPTION_DECOMPRESS_CHARACTER = 2;
    private static final int OPTION_COMPRESS_WORD = 3;
    private static final int OPTION_DECOMPRESS_WORD = 4;
    private static final int OPTION_EXIT = 5;

    /**
     * Inicia o programa, exibe o menu principal e direciona a operação escolhida pelo usuário.
     *
     * Entrada: argumentos de linha de comando, não utilizados nesta aplicação.
     * Resultado: void.
     * Pré-condição: nenhuma específica além da JVM conseguir iniciar o programa.
     * Pós-condição: o menu é exibido repetidamente até o usuário escolher a opção de sair.
     * Observação: erros de execução são capturados para evitar que o programa encerre sem mensagem explicativa.
     */
    public static void main(String[] args) {
        HuffmanCompressor compressor = new HuffmanCompressor();
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        Scanner scanner = new Scanner(System.in);

        int option = 0;
        while (option != OPTION_EXIT) {
            printMenu();
            option = readOption(scanner);

            try {
                switch (option) {
                    case OPTION_COMPRESS_CHARACTER:
                        compress(scanner, compressor, CompressionMode.CHARACTER);
                        break;
                    case OPTION_DECOMPRESS_CHARACTER:
                        decompress(scanner, decompressor);
                        break;
                    case OPTION_COMPRESS_WORD:
                        compress(scanner, compressor, CompressionMode.WORD);
                        break;
                    case OPTION_DECOMPRESS_WORD:
                        decompress(scanner, decompressor);
                        break;
                    case OPTION_EXIT:
                        System.out.println("Programa encerrado.");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception exception) {
                System.out.println("Erro durante a operação: " + exception.getMessage());
                exception.printStackTrace();
            }
        }

        scanner.close();
    }

    /**
     * Exibe as opções disponíveis no menu principal.
     *
     * Entrada: nenhuma.
     * Resultado: void.
     * Pré-condição: saída padrão do console disponível.
     * Pós-condição: as opções do menu são impressas na tela.
     * Observação: o método não lê nem valida a opção; apenas apresenta o menu.
     */
    private static void printMenu() {
        System.out.println("\n=== MENU DE COMPRESSÃO HUFFMAN ===");
        System.out.println("1. Compactar arquivo (usando codificação por caracter)");
        System.out.println("2. Descompactar arquivo (usando codificação por caracter)");
        System.out.println("3. Compactar arquivo (usando codificação por palavra)");
        System.out.println("4. Descompactar arquivo (usando codificação por palavra)");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Lê e converte a opção digitada pelo usuário.
     *
     * Entrada: Scanner conectado à entrada padrão.
     * Resultado: número inteiro correspondente à opção; retorna -1 quando a entrada não é numérica.
     * Pré-condição: scanner deve estar inicializado e aberto.
     * Pós-condição: uma linha é consumida da entrada do usuário.
     * Observação: retornar -1 permite que o menu trate entradas inválidas sem lançar exceção.
     */
    private static int readOption(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    /**
     * Executa a compactação de um arquivo conforme o modo informado.
     *
     * Entrada: Scanner para ler caminhos, instância de HuffmanCompressor e modo de compressão.
     * Resultado: void.
     * Pré-condição: arquivo de entrada deve existir, ser legível e ser texto em UTF-8.
     * Pós-condição: um arquivo compactado é criado no caminho de saída informado.
     * Observação: após a compactação, imprime no console as métricas necessárias para o relatório.
     */
    private static void compress(
            Scanner scanner,
            HuffmanCompressor compressor,
            CompressionMode mode
    ) throws Exception {
        Path inputFile = readPath(scanner, "Informe o nome do arquivo de entrada: ");
        Path outputFile = readPath(scanner, "Informe o nome do arquivo de saída: ");

        CompressionResult result = compressor.compress(inputFile, outputFile, mode);
        ConsoleReport.printCompressionResult(result);
    }

    /**
     * Executa a descompactação de um arquivo gerado por este programa.
     *
     * Entrada: Scanner para ler caminhos e instância de HuffmanDecompressor.
     * Resultado: void.
     * Pré-condição: arquivo de entrada deve ser um arquivo compactado válido gerado por esta aplicação.
     * Pós-condição: o conteúdo original é reconstruído no caminho de saída informado.
     * Observação: o modo original de compactação é lido do próprio cabeçalho do arquivo compactado.
     */
    private static void decompress(
            Scanner scanner,
            HuffmanDecompressor decompressor
    ) throws Exception {
        Path inputFile = readPath(scanner, "Informe o nome do arquivo compactado: ");
        Path outputFile = readPath(scanner, "Informe o nome do arquivo de saída: ");

        DecompressionResult result = decompressor.decompress(inputFile, outputFile);
        ConsoleReport.printDecompressionResult(result);
    }

    /**
     * Lê um caminho de arquivo digitado pelo usuário.
     *
     * Entrada: Scanner aberto e mensagem que será exibida no console.
     * Resultado: objeto Path representando o caminho informado.
     * Pré-condição: scanner deve estar inicializado e aberto.
     * Pós-condição: uma linha é consumida da entrada do usuário e convertida para Path.
     * Observação: espaços antes e depois do caminho são removidos com trim().
     */
    private static Path readPath(Scanner scanner, String message) {
        System.out.print(message);
        return Paths.get(scanner.nextLine().trim());
    }
}
