package org.example;

import java.util.Random;
import java.util.Scanner;

public class Main {

    final static String CARACTERES_IDENTIFICADORES_ACEITOS = "XO0UC";
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        //Inicio do tabuleiro - inicio do Escopo
        System.out.println(bemvindo());
        System.out.print("Digite a proporção do tabuleiro: ");

        char[][] tabuleiro = inicializarTabuleiro(criarTabuleiro(teclado.nextInt()));


        char UsuarioCaracter = obterCaractereUsuario(teclado);
        char CpuCaracter = obterCaractereComputador(teclado, UsuarioCaracter);
        teclado.nextLine();
        //Inicio do tabuleiro - final do Escopo

        boolean vezUsuarioJogar = sortearValorBooleano();
        boolean jogoContinua;

        do {
            // controla se o jogo terminous
            jogoContinua = true;

            exibirTabuleiro(tabuleiro);
            if ( vezUsuarioJogar ){

                tabuleiro = processarVezUsuario(teclado, tabuleiro, UsuarioCaracter);
                // Verifica se o usuario venceu
                if ( teveGanhador(tabuleiro, UsuarioCaracter) ) {
                    System.out.println("WINNNNN USUARIO");
                    exibirTabuleiro(tabuleiro);
                    exibirVitoriaUsuario();
                    jogoContinua = false;
                }

                vezUsuarioJogar = false;
            } else {

                limparTela();
                tabuleiro = processarVezComputador(tabuleiro, CpuCaracter);
                // Verifica se o computador venceu
                if ( teveGanhador(tabuleiro, CpuCaracter)/*esreva aqui a chamada para teve ganhador*/ ) {
                    System.out.println("WINNNNN CPU");
                    exibirTabuleiro(tabuleiro);
                    exibirVitoriaComputador();
                    jogoContinua = false;
                }
                vezUsuarioJogar = true;
            }
            //ocorreu tempate. Utilize o metodo teveEmpate()
            if ( teveEmpate(tabuleiro)) {

                exibirEmpate();
                jogoContinua = false;
            }
        } while (jogoContinua);
        teclado.close();

    }

    public static char[][] criarTabuleiro(int tamanho) {
        char[][] tabuleiro = new char[tamanho][tamanho];
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                tabuleiro[i][j] = '-';
            }
        }
        return tabuleiro;
    }
    //Retorna o Tabuleiro vazio
    public static char[][] inicializarTabuleiro(char[][] tabuleiro) {
        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro[0].length; j++) {
                tabuleiro [i] [j]= ' ';
            }
        }
        return tabuleiro;
    }

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


    public static boolean jogadaValida(char[][] tabuleiro, int linha, int coluna, char caracter) {
        // Verificando se os índices estão dentro dos limites do tabuleiro
        if (linha < 0 || linha >= tabuleiro.length || coluna < 0 || coluna >= tabuleiro[0].length) {
            return false;
        }

        // Verificando se a posição no tabuleiro está vazia (representada por ' ')
        if (tabuleiro[linha][coluna] != ' ') {
            return false; // A posição já está ocupada
        }

        // Verificando se a posição já foi marcada pelo jogador (caso necessário)
        if (tabuleiro[linha][coluna] == caracter) {
            return false;
        }

        return true;
    }

    public static int[] obterJogadaUsuario(char[][] tabuleiro, Scanner teclado, char caracter) {
        final int TAMANHO_TABULEIRO = 3; // Facilita alterações no futuro
        int[] jogada = new int[2];
        boolean jogadaValida = false;

        while (!jogadaValida) {
            System.out.println("Digite a linha e a coluna separados por um espaço (exemplo: 1 2): " );
            String entrada = teclado.nextLine().trim();


            if (!entrada.matches("\\d+ \\d+")) {
                System.out.println("Erro: Você deve digitar dois números separados por um espaço. Tente novamente.");
                continue;
            }

            int[] tentativa = converterJogadaStringParaVetorInt(entrada);


            if (!jogadaValida(tabuleiro, tentativa[0] , tentativa[1], caracter)) {
                System.out.println("Erro: A posição escolhida já está ocupada ou é inválida. Tente novamente.");
                continue;
            }
            // Jogada válida
            jogada = tentativa;
            jogadaValida = true;
        }

        return jogada;
    }

    public static int[] obterJogadaComputador(char[][] tabuleiro, char caracter) {
        Random random = new Random(); // Instanciando o gerador de números aleatórios
        int[] jogadaComputador = new int[2]; // Vetor para armazenar linha e coluna sorteadas

        int linha, coluna;

        // Sorteia uma posição válida
        do {
            linha = random.nextInt(tabuleiro.length); // Sorteia linha entre 0 e número de linhas do tabuleiro
            coluna = random.nextInt(tabuleiro[0].length); // Sorteia coluna entre 0 e número de colunas do tabuleiro
        } while (!jogadaValida(tabuleiro, linha, coluna, caracter)); // Continua sorteando até encontrar uma posição válida

        // Armazena a jogada válida
        jogadaComputador[0] = linha;
        jogadaComputador[1] = coluna;

        return jogadaComputador;
    }


    public static int[] converterJogadaStringParaVetorInt(String jogada) {

        String[] partes = jogada.split(" ");

        // Converte os valores para inteiros e ajusta os índices para base 0
        int linha = Integer.parseInt(partes[0]) - 1;
        int coluna = Integer.parseInt(partes[1]) - 1;

        // Retorna a jogada em formato de vetor de inteiros
        return new int[]{linha, coluna};
    }

    public static void exibirTabuleiro(char[][] tabuleiro) {
        System.out.println(retornarPosicoesLivres(tabuleiro));
    }

    public static char[][] processarVezUsuario(Scanner teclado, char[][] tabuleiro, char caractereUsuario) {
        // Obtém a jogada do usuário
        int[] jogada = obterJogadaUsuario(tabuleiro, teclado, caractereUsuario);

        // Atualiza o tabuleiro com a jogada do usuário
        tabuleiro = retornarTabuleiroAtualizado(tabuleiro, jogada, caractereUsuario);

        limparTela();


        System.out.println("Agora é a vez do computador!");
        System.out.println();
        return tabuleiro;
    }

    //vez do computador
    public static char[][] processarVezComputador(char[][] tabuleiro, char caractereComputador) {

        // Obtém a jogada do computador
        int[] jogada = obterJogadaComputador(tabuleiro, caractereComputador);

        // Atualiza o tabuleiro com a jogada do computador

        tabuleiro = retornarTabuleiroAtualizado(tabuleiro, jogada, caractereComputador);

        limparTela();

        System.out.println("É a sua vez de jogar!");
        System.out.println();
        return tabuleiro;

    }

    public static char[][] retornarTabuleiroAtualizado(char[][] tabuleiro, int[] jogada, char caractereJogador) {
        int linha = jogada[0];
        int coluna = jogada[1];

        // Verificando se os índices estão dentro do limite do tabuleiro
        if (linha >= 0 && linha < tabuleiro.length && coluna >= 0 && coluna < tabuleiro[0].length) {
            tabuleiro[linha][coluna] = caractereJogador;
        } else {
            throw new RuntimeException("Jogada inválida! As coordenadas estão fora dos limites.");
        }
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
        boolean b;
        Random aleatorio = new Random();
        if (aleatorio.nextBoolean()) {
            b = true;
            System.out.println("Usuário começa");
        } else {
            b = false;
            System.out.println("Máquina começa");
        }
        return b;
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
        System.out.println("Ocorreu empate!");

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
        try{
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")){
                Runtime.getRuntime().exec("cls");

            }else{
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e){
            //  Tratar Exceptions
        }
    }

    public static String bemvindo(){
        String mensagemBoasVindas = """
████████████████████████████████████████████████
█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░██
█░░░ Bem-vindo ao Jogo da Velha!   ░░░░░░░░░░░░█
█░░░ O tabuleiro será do tamanho que você  ░░░░█
█░░░ escolher! Personalize e prepare-se para   █
█░░░ partidas ainda mais emocionantes!         █
█░░░                                           █
█░░░ Exemplo de um tabuleiro 3x3:              █
█░░░                                           █
█░░░    1 | 2 | 3                              █
█░░░   -----------                             █
█░░░    4 | 5 | 6                              █
█░░░   -----------                             █
█░░░    7 | 8 | 9                              █
█░░░ Boa sorte e divirta-se!                   █
█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░█░██
████████████████████████████████████████████████
""";
        return mensagemBoasVindas;
    }
}