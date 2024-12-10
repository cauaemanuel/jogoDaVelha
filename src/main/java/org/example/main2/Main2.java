package org.example.main2;

import java.util.Random;
import java.util.Scanner;

public class Main2 {

    final static String CARACTERES_IDENTIFICADORES_ACEITOS = "XO0UC";
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

    }

    //Retorna o Tabuleiro vazio
    public static char[][] inicializarTabuleiro(char[][] tabuleiro) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro [i] [j]= ' ';
            }
        }
        return tabuleiro;
    }
    /*
     * Descrição: Utilizado para obter no início do jogo qual o caractere que o
     * usuário quer utilizar para representar ele próprio. Este método recebe o
     * teclado para permitir que o usuário digite o caractere desejado. Faça a
     * leitura do caractere desejado pelo usuário, através do teclado, realize
     * as validações para não aceitar caracteres que não estejam definidos pela
     * constante CARACTERES_IDENTIFICADORES_ACEITOS, e retorne o caractere lido
     * através do return.
     * Nível de complexidade: 4 de 10
     */

    //Retorna o caracter do Usuario
    public static char obterCaractereUsuario(Scanner teclado) {
        char caractereUsuario= ' ';
        //Tranforma os caracteres aceitos em um array
        char[] caracteresDisponiveis= CARACTERES_IDENTIFICADORES_ACEITOS.toCharArray();

        //Enquanto não identificar um caractere válido, mantém o laço
        while (true) {
            //Recebe o caractere selecionado pelo usuário
            try {
                System.out.print("Digite o caractere que você deseja utilizar (X, O, 0, U, C): ");
                caractereUsuario= teclado.next().toUpperCase().charAt(0);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            /*Compara o caractere selecionado com os aceitos, se encontrado,
            retorna o caractere selecionado*/
            for (int i = 0; i < caracteresDisponiveis.length; i++) {
                if (caractereUsuario == caracteresDisponiveis[i]) {
                    return caractereUsuario;
                }
            }
            System.out.printf("\n%sErro! Digite um dos caracteres informados anteriormente%s\n\n", "\033[0;31m", "\033[0m");
        }
    }

    /*
     * Descrição: Utilizado para obter no início do jogo qual o caractere que o
     * usuário quer utilizar para representar o computador. Este método recebe o
     * teclado e recebe o caractere que foi configurado para o usuário, pois o
     * usuário e o computador não podem jogar com o mesmo caractere. Por exemplo,
     * se o usuário configurou para ele o caractere X ele não pode escolher o X
     * como o caractere também para o computador. Neste método apenas os seguintes
     * caracteres definidos pela constante CARACTERES_IDENTIFICADORES_ACEITOS devem
     * ser aceitos. Lembre-se que o caractere armazenado em caractereUsuario não
     * pode ser aceito. Após realizar a leitura do caractere pelo teclado e
     * validá-lo, faça o return deste caractere.
     * Nível de complexidade: 4 de 10
     */
    //Retorna o Caracter Do CPU
    static char obterCaractereComputador(Scanner teclado, char caractereUsuario) {
        char caractereComputador= ' ';
        char[] caracteresDisponiveis= CARACTERES_IDENTIFICADORES_ACEITOS.toCharArray();
        /*Enquanto não identificar um caractere aceito ou um caractere
        diferente do escolhido para o usuário, mantém o laço*/
        while (true) {
            /*variável para exibir mensagens diferentes entre caractere
            já selecionado e caractere inválido*/
            boolean erroJaIndicado= false;
            //Recebe o valor escolhido pelo usuário
            try {
                System.out.print("Digite o caractere que o computador irá utilizar (X, O, 0, U, C): ");
                caractereComputador= teclado.next().toUpperCase().charAt(0);
            } catch (Exception e) {

            }
            /*For que exibe mensagem caso o caractere já esteja sendo utilizado,
            caso encontre um valor válido retorna o caractereComputador*/
            for (int i = 0; i < caracteresDisponiveis.length; i++) {
                if (caractereComputador == caractereUsuario) {
                    System.out.printf("\n%sErro! Você já está utilizando este caractere.%s\n\n", "\033[0;31m", "\033[0m");
                    erroJaIndicado = true;
                    break;
                } else if (caractereComputador == caracteresDisponiveis[i]) {
                    return caractereComputador;
                }
            }
            /*Se não encontrar um valor válido no for e não exibir o erro
            de valor já selecionado, exibe erro de caractere inválido*/
            if (!erroJaIndicado) {
                System.out.printf("\n%sErro! Digite um caractere disponível.%s\n\n", "\033[0;31m", "\033[0m");
            }
        }
    }


    public static boolean jogadaValida(String posicoesLivres, int linha, int coluna) {


        String posicao = linha + "" + coluna;
        return posicoesLivres.contains(posicao);
    }


    public static int[] obterJogadaUsuario(String posicoesLivres, Scanner teclado) {
        final int TAMANHO_TABULEIRO = 3; // Facilita alterações no futuro
        int[] jogada = new int[2];
        boolean jogadaValida = false;

        while (!jogadaValida) {
            System.out.println("Digite a linha e a coluna separados por um espaço (exemplo: 1 2):");
            String entrada = teclado.nextLine().trim();


            if (!entrada.matches("\\d+ \\d+")) {
                System.out.println("Erro: Você deve digitar dois números separados por um espaço. Tente novamente.");
                continue;
            }

            int[] tentativa = converterJogadaStringParaVetorInt(entrada);

            // Verifica limites do tabuleiro
            if (tentativa[0] < 0 || tentativa[0] >= TAMANHO_TABULEIRO || tentativa[1] < 0 || tentativa[1] >= TAMANHO_TABULEIRO) {
                System.out.println("Erro: A linha e a coluna devem estar entre 1 e 3. Tente novamente.");
                continue;
            }

            if (!jogadaValida(posicoesLivres, tentativa[0] + 1, tentativa[1] + 1)) {
                System.out.println("Erro: A posição escolhida já está ocupada ou é inválida. Tente novamente.");
                continue;
            }
            // Jogada válida
            jogada = tentativa;
            jogadaValida = true;
        }

        return jogada;
    }

    public static int[] obterJogadaComputador(String posicoesLivres, Scanner teclado) {
        Random random = new Random(); // Instanciando o gerador de números aleatórios
        int[] jogadaComputador = new int[2]; // Vetor para armazenar linha e coluna sorteadas

        // Converte as posições livres de String para um vetor de strings (ex: "1 1;1 2;2 3" -> ["1 1", "1 2", "2 3"])
        String[] listaPosicoes = posicoesLivres.split(";");

        // Sorteia uma posição aleatória dentro do vetor de posições livres
        int indiceSorteado = random.nextInt(listaPosicoes.length);

        // Obtém a posição sorteada (por exemplo, "2 3")
        String posicaoSorteada = listaPosicoes[indiceSorteado];

        // Converte a posição sorteada para um vetor de inteiros (linha, coluna)
        jogadaComputador = converterJogadaStringParaVetorInt(posicaoSorteada);

        // Retorna o vetor com a jogada sorteada
        return jogadaComputador;
    }

    public static int[] converterJogadaStringParaVetorInt(String jogada) {
        String posicao = "";
        String[] partes = posicao.split(" ");

        // Converte os valores para inteiros e ajusta os índices para base 0
        int linha = Integer.parseInt(partes[0]) - 1;
        int coluna = Integer.parseInt(partes[1]) - 1;

        // Retorna a jogada em formato de vetor de inteiros
        return new int[]{linha, coluna};
    }

    public static void exibirTabuleiro(char[][] tabuleiro) {
        // Primeira linha
      System.out.println(retornarPosicoesLivres(tabuleiro));
       /* System.out.println("               Coluna");
        System.out.println("             1    2    3");
        System.out.println(" ");
        System.out.println( "Linha 1     "+ " " + tabuleiro[0][0] + " |  " + tabuleiro[0][1] + "  | " + tabuleiro[0][2] + " ");
        System.out.println("           ----+-----+----");
        // Segunda linha
        System.out.println("Linha 2     "+ " " +  tabuleiro[1][0] + " |  " + tabuleiro[1][1] + "  | " + tabuleiro[1][2] + " ");
        System.out.println("           ----+-----+----");
        // Terceira linha
        System.out.println("Linha 3     "+ " " + tabuleiro[2][0] + " |  " + tabuleiro[2][1] + "  | " + tabuleiro[2][2] + " ");
*/
    }

   /* public static char[][] processarVezUsuario(Scanner teclado, char[][] tabuleiro, char caractereUsuario) {
        System.out.println("É a sua vez de jogar!");

        // Obtém a jogada do usuário
        int[] jogada = obterJogadaUsuario(retornarPosicoesLivres(tabuleiro), teclado);

        // Atualiza o tabuleiro com a jogada do usuário
        tabuleiro = retornarTabuleiroAtualizado(tabuleiro, jogada[0], jogada[1], caractereUsuario);

        return tabuleiro;
    }

    //vez do computador
    public static char[][] processarVezComputador(char[][] tabuleiro, char caractereComputador) {
        System.out.println("Agora é a vez do computador!");

        // Obtém a jogada do computador
        int[] jogada = obterJogadaComputador(tabuleiro);

        // Atualiza o tabuleiro com a jogada do computador
        tabuleiro = retornarTabuleiroAtualizado(tabuleiro, jogada[0], jogada[1], caractereComputador);

        return tabuleiro;
    }*/

    public static char[][] retornarTabuleiroAtualizado(char[][] tabuleiro, int[] jogada, char caractereJogador) {
        tabuleiro[jogada[0]] [jogada[1]] = caractereJogador;
        return tabuleiro;
    }


    static void exibirVitoriaComputador(){
        String desenhoComputador =
                "   +-------------------+\n"+
                        "   | +---------------+ |\n"+
                        "   | |               | |\n"+
                        "   | |     o   o     | |\n"+
                        "   | |       -       | |\n"+
                        "   | |     \\___/     | |\n"+
                        "   | |               | |\n"+
                        "   | +---------------+ |\n"+
                        "   +------+-----+------+\n"+
                        "    ______|/   \\|______\n"+
                        "   /                   \\ \n"+
                        "  / ******************* \\ \n"+
                        " / ********************  \\ \n"+
                        " +-----------------------+ \n";
        System.out.println("O computador venceu!" + desenhoComputador);
    }

    static void exibirVitoriaUsuario(){
        String desenho =
                "       ///////\n"+
                        "\\\\|////////// \n"+
                        " \\|/////////\n"+
                        " | ___ ___ |\n"+
                        "(|  o   o  |)\n"+
                        " |    +    |\n"+
                        " |  \\___/  |\n"+
                        "  \\_______/\n"+
                        "    \\\\|//\n";
        System.out.println("O usuário venceu! \n" + desenho);

    }

    public static boolean teveGanhador(char[][] tabuleiro, char caractereJogador) {
        boolean teveGanhador = false;
        if (teveGanhadorLinha(tabuleiro, caractereJogador) || teveGanhadorColuna(tabuleiro, caractereJogador) || teveGanhadorDiagonalPrincipal(tabuleiro, caractereJogador) || teveGanhadorDiagonalSecundaria(tabuleiro, caractereJogador) != false) {
            return teveGanhador = true;
        } else {
            return teveGanhador = false;
        }
    }

    public static boolean teveGanhadorLinha(char[][] tabuleiro, char caractereJogador) {
        for (int i = 0; i < tabuleiro.length; i++) {
            boolean linhaCompleta = true;
            for (int j = 0; j < tabuleiro[0].length; j++) {

                if (tabuleiro[i][j] != caractereJogador) {
                    linhaCompleta = false;
                    break;
                }
            }
            if (linhaCompleta) {
                return true;
            }
        }
        return false;
    }

    public static boolean teveGanhadorColuna(char[][] tabuleiro, char carctereJogador){

        for (int j = 0; j < tabuleiro[0].length; j++) {
            boolean colunaCompleta = true;
            for (int i = 0; i < tabuleiro.length; i++) {
                if(tabuleiro[i][j] != carctereJogador){
                    colunaCompleta = false;
                    break;
                }
            }
            if (colunaCompleta){
                return true;
            }
        }
        return false;
    }

    public static boolean teveGanhadorDiagonalPrincipal(char[][] tabuleiro, char carctereJogador){
        for (int i = 0; i < tabuleiro.length; i++){
            if(tabuleiro[i][i] != carctereJogador) {
                return false;
            }
        }
        return true;
    }

    public static boolean teveGanhadorDiagonalSecundaria(char[][] tabuleiro, char carctereJogador){
        for (int i = 0; i < tabuleiro.length; i++){
            if(tabuleiro[i][tabuleiro.length - i - 1] != carctereJogador) {
                return false;
            }
        }
        return true;
    }

    public static boolean sortearValorBooleano() {

        Random aleatorio = new Random();
        if (aleatorio.nextBoolean()) {
            System.out.println("Usuário começa");
        } else {
            System.out.println("Máquina começa");
        }

        return (sortearValorBooleano());
    }

    public static boolean teveEmpate(char[][]tabuleiro){

        for(int index = 0; index < tabuleiro.length; index++) {
            for(int n = 0; n < tabuleiro[index].length; n++){
                if (tabuleiro[index][n] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

public static void exibirEmpate() {
    System.out.print("Ocorreu empate!");

                 System.out.println("  +---------+                            +---------+"  );
                 System.out.println("  | +-----+ |      **            **      | +-----+ |"  );
                 System.out.println("  | |     | |        **        **        | +-----+ |"  );
                 System.out.println("  | |     | |          **    **          | +-----+ |"  );
                 System.out.println("  | |     | |             **             | +-----+ |"  );
                 System.out.println("  | |     | |          **    **          | +-----+ |"  );
                 System.out.println("  | +-----+ |        **        **        | +-----+ |"  );
                 System.out.println("  +---------+      **            **      +---------+"  );


 }

    public static String retornarPosicoesLivres(char[][] tabuleiro){
        StringBuilder resultSet = new StringBuilder();

        resultSet.append("Coluna:     ");
        for(int i = 0  ; i < tabuleiro.length; i++){
            resultSet.append((i + 1) + "   ");
        }
        resultSet.append("\n");

        for(int i = 0; i < tabuleiro.length; i++){
            resultSet.append("Linha "+ (i + 1) + "     ");

            for(int j = 0 ; j < tabuleiro[0].length; j++){
                resultSet.append(tabuleiro[i][j] + " | ");
            }

            resultSet.append("\n");
        }
        return resultSet.toString();
    }

    /*
     * Descrição: Utilizado para limpar a console, para que seja exibido apenas o
     * conteúdo atual do jogo. Dica: Pesquisa na internet por "Como limpar console
     * no java ProcessBuilder"
     * Nível de complexidade: 3 de 10
     */
    public static void limparTela() {
        /*Executa o comando de limpar o terminal baseado
        em qual sistema operacional é utilizado pelo usuário,
        exibindo uma mensagem de erro em caso de exceções*/
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Erro ao limpar a tela: " + e.getMessage());
        }
    }
}