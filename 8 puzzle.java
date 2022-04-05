import java.util.Arrays;
import java.util.Random;

/**
 *< Descrição: A classe Puzzle contém os campos puzzle, h1,h2, totalCost e direction, bem como os métodos getter/setter apropriados para eles
 * Também contém construtores para inicializar os campos e verificar se os valores que estão sendo inicializados serão solucionáveis ​​ou legais
 *
 */

public class Puzzle {

  //Variáveis ​​de campo
  private int[] puzzle; //Uma representação de matriz inteira do puzzle.
  private int h1; // telhas mal colocadas (HAMMING)
  private int h2; // soma da distância (MANHATTAN)
  private int totalCost; //Cust = PathCost - Custo estimado para a meta
  private int direction; // A direção de onde veio a ação de mover um índice para o espaço em branco.

  //Construtor padrão
  public Puzzle() {
    puzzle = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    puzzle = shuffle(puzzle, puzzle.length);
    //System.out.println(Arrays.toString(puzzle));
    h1 = Hamming(puzzle);
    h2 = Manhattan(puzzle);
    //totalCost = pathCost + h1;
    //totalCost = pathCost + h2;
    totalCost = 0;
    direction = 0;
  }

  //Construtor para o usuário dado o estado inicial do puzzle
  public Puzzle(int[] puzzle) {
    //Então esta configuração é legal e solucionável.
    if (isValid(puzzle) == true && isSolvable(puzzle) == true) {
      this.puzzle = puzzle;
      h1 = Hamming(puzzle);
      h2 = Manhattan(puzzle);
      direction = 0;
    } else {
      System.out.println("Sorry, this configuration is not solvable or valid.");
    }
  }

  // Imprime a configuração atual do puzzle em um formulário de grade.
  public void printPuzzle() {
    for (int r = 0; r <= 2; r++) { // linha
      System.out.print("[ ");
      for (int c = 0; c <= 2; c++) { // coluna
        System.out.print(puzzle[(r * 3) + c] + " ");
      }
      System.out.println("]");
    }
  }

  public int[] getPossibleSwappableIndex() {
    int blankSpaceIndex = getBlankSpaceIndex();
    int[] possibleSwappableIndex = new int[4];

    int upIndex = blankSpaceIndex - 3;
    int downIndex = blankSpaceIndex + 3;
    int leftIndex = blankSpaceIndex - 1;
    int rightIndex = blankSpaceIndex + 1;

    // 0 = left, 1 = right, 2 = up, 3 = down.
    if (blankSpaceIndex % 3 != 0 && leftIndex >= 0) {
      possibleSwappableIndex[0] = leftIndex;
    } else possibleSwappableIndex[0] = -1;

    if (rightIndex % 3 != 0 && rightIndex <= 8) {
      possibleSwappableIndex[1] = rightIndex;
    } else possibleSwappableIndex[1] = -1;

    if (upIndex < 8 && upIndex >= 0) {
      possibleSwappableIndex[2] = upIndex;
    } else possibleSwappableIndex[2] = -1;

    if (downIndex <= 8 && downIndex >= 0) {
      possibleSwappableIndex[3] = downIndex;
    } else possibleSwappableIndex[3] = -1;

    return possibleSwappableIndex;
  }

  // Localiza o índice do elemento 0 na matriz, que representa o espaço em branco.
  public int getBlankSpaceIndex() {
    int blankindex = 0;
    for (int i = 0; i < puzzle.length; i++) {
      if (puzzle[i] == 0) blankindex = i;
    }
    return blankindex;
  }

  // Retorna a matriz do puzzle

  public int[] getPuzzleArray() {
    return puzzle;
  }

  // Método getter para variável de campo local h1
  // Esta variável h1 representa o valor heurístico calculado pela distância de Hamming.
  public int getH1() {
    return h1;
  }

  // Método getter para variável de campo local h2
  // A variável h2 representa o valor heurístico calculado por Manhattan Distance.
  public int getH2() {
    return h2;
  }

  // Método getter para retornar a variável de campo local totalCost
  public int getTotalCost() {
    return totalCost;
  }

  // Método getter para retornar a direção da variável de campo local
  public int getDirection() {
    return direction;
  }

  // Método setter para definir a variável de campo local totalCost
  public void setTotalCost(int totalCost) {
    this.totalCost = totalCost;
  }

  /*
   *  Método setter para especificar a direção de onde veio a ação de mover um índice para o espaço em branco.
   * Usado posteriormente para diferenciar um dos quatro nós filhos possíveis um do outro.
   * Deixe 0 representar o nó raiz ou nenhuma direção tomada.
   *  1 = left, 2 = right, 3 = up, 4 = down.
   */
  public void setDirection(int i) {
    if (0 <= i && i <= 4) { //Between 0-4
      direction = i;
    }
  }

  // Verifica se a configuração atual do puzzle é equivalente ao estado da meta.
  // Retorna verdadeiro se for, senão retorna falso.
  public boolean isGoal() {
    for (int i = 0; i < puzzle.length; i++) {
      if (puzzle[i] != i) {
        //System.out.println(puzzle[i] + " == " +i );
        return false;
      }
    }
    return true;
  }

  // Verifica se a entrada do puzzle é de 9 números diferentes e tem apenas 9 números ou se é "Grammat
  public boolean isValid(int[] arr) {
    boolean isValid = true;

    if (arr.length == 9) {
      int[] tempArr = arr.clone(); //Não queremos classificar o array original e, como o Java copia por referência, clonamos arr.
      Arrays.sort(tempArr); //Sort a ordem crescente da matriz
      for (int i = 0; i < tempArr.length; i++) { //Porque 0 - 8 só podem aparecer uma vez cada no quebra-cabeça
        if (tempArr[i] != i) { // Podemos garantir que os números de 0 a 8 apareçam e apareçam apenas uma vez, classificando-os
          isValid = false; // e comparando-o com um loop que itera de 0 a 8.
        }
      }
    }
    //System.out.println("This puzzle is grammatically valid.");
    return isValid;
  }

  // Dado um array e seu tamanho, embaralhe os índices até
  // uma configuração válida de 8 puzzle é encontrada.
  private int[] shuffle(int[] puzzle, int len) {
    Random r = new Random();

    for (int i = len - 1; i > 0; i--) {
      //índice aleatório de 0 a i
      int j = r.nextInt(i + 1);

      //troca
      int temp = puzzle[i];
      puzzle[i] = puzzle[j];
      puzzle[j] = temp;
    }

    //Se sob a heurística Hamming não puder ser resolvido, embar-alhe novamente a matriz..
    if (isSolvable(puzzle) == false) {
      return shuffle(puzzle, len);
    } else return puzzle;
  }

  /*
   * Heurística usando a Heurística de Distância de Hamming
   * Retorna verdadeiro se o puzzle puder ser resolvido, senão falso
   * Também nos fornece o valor heurístico a ser usado para avaliação e é definido para a variável de campo local h1.
   * Hamming é calculado pelo número de peças mal colocadas presentes.
   */
  public boolean isSolvable(int[] puzzle) {
    int counter = 0;

    for (int i = 0; i < puzzle.length - 1; i++) { // Não precisamos avaliar a última posição        .
      for (int j = i + 1; j < puzzle.length; j++) {
        //Então encontramos uma inversão.
        if (
          puzzle[i] > puzzle[j] && puzzle[j] != 0 && puzzle[i] != 0 && i != j
        ) {
          counter++;
        }
      }
    }
    //	System.out.println("Número de Inversões == " + contado);
    if (counter % 2 == 0) { // então é mesmo
      return true;
    } else return false; //então é estranho, rejeitar como insolúvel
  }

  //Contar telhas mal colocadas
  private int Hamming(int[] puzzle) {
    int counter = 0;
    for (int i = 0; i < 9; i++) {
      if (puzzle[i] != i) { // Então está mal colocado.
        counter++;
      }
    }
    return counter;
  }

  // Heurística usando Manhattan
  // Define a variável de campo local h2 para o valor heurístico calculado.
  // Manhattan representa a soma das distâncias dos ladrilhos de suas posições de objetivo.
  private int Manhattan(int[] puzzle) {
    int counter = 0;

    for (int i = 0; i < puzzle.length; i++) {
      //Não o espaço em branco, e não o estado de meta.
      if (puzzle[i] != 0 && puzzle[i] != i) {
        //System.out.println(i);
        int delta = Math.abs(i - puzzle[i]);
        counter += (delta % 3) + (delta / 3);
      }
    }
    return counter;
  }

  public int getHeuristicValue(int heuristic) {
    if (heuristic == 1) return h1; else return h2; // Então retorne Hamming Distance // senão Manhattan.
  }
}
