package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameBoard extends JFrame {
    static int dimension = 3; // Размерность поля, константа!
    static int cellSize = 150; // Размер клетки
    public char[][] gameField; // Матрица игры
    private GameButton[] gameButtons; // Массив кнопок
    private Game game; // Ссылка на игру
    static char nullSymbol = '\u0000'; // Пустой символ для очистки массива состояния игрового поля


    public GameBoard(Game currentGame){
        this.game = currentGame;
        initField();
    }

    /**
     * Метод инициализации и отрисовки игрового поля
     */
    private void initField(){
        // Задаем основные настройки окна игры
        setBounds(400,300,cellSize * dimension, cellSize * dimension);
        setTitle("Крестики-нолики");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(); // Панель управления игрой
        JButton newGameButton = new JButton("New game");  // Кнопка в панели управления "Новая игра"
        newGameButton.addActionListener(new ActionListener() {   // Новый листенер для кнопки "Новая игра"
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyField(); // Очистим поле
            }
        });

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS)); // Выравнивание панели управления по оси Y
        controlPanel.add(newGameButton);

        controlPanel.setSize(cellSize * dimension, 150); // Указываем размеры контрольной панели

        JPanel gameFieldPanel = new JPanel();  // панель самой игры
        gameFieldPanel.setLayout(new GridLayout(dimension,dimension)); // Вариант размещения - табличный (GridLayout)
        gameFieldPanel.setSize(cellSize * dimension, cellSize * dimension);

        gameField = new char[dimension][dimension];  // Инициализируем матрицу нашей игры
        gameButtons = new GameButton[dimension * dimension]; // Инициализируем массив кнопок игры

        // Инициализируем игровое поле и заполняем его кнопками
        for(int i = 0; i < (dimension * dimension); i++){
            GameButton fieldGamebutton = new GameButton(i, this);
            gameFieldPanel.add(fieldGamebutton);
            gameButtons[i] = fieldGamebutton;  // В массив кнопок добавляем полученную ссылку на кнопку
        }

        // Добавляем полученные панели в окно с вариантом размещения BorderLayout
        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(gameFieldPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Метод очистки игрового поля и матрицы игры (Новая игра)
     */
    void emptyField(){
        for(int i = 0; i < (dimension * dimension); i++){
            gameButtons[i].setText("");

            int x = i / dimension; //GameBoard.
            int y = i % dimension;

            gameField[x][y] = nullSymbol;
        }
        getGame().setPlayersTurn(); // оптимизация, иначе при ничьей далее начинал компьютер...
    }

    // Метод возвращает ссылку на экземляр нашей игры
    Game getGame(){
        return game;
    }

    /**
     * Метод проверки доступности клетки для хода
     * @param x по горизонтали
     * @param y по вертикали
     * @return boolean
     */
    boolean isTurnable(int x, int y){
        boolean result = false;

        if(gameField[y][x] == nullSymbol){
            result = true;
        }

        return result;
    }

    /**
     * Обновление матрицы после хода
     * @param x - по горизонтали
     * @param y - по вертикали
     */
    void updateGameField(int x, int y){
        gameField[y][x] = game.getCurrentPlayer().getPlayerSign();
    }

 //оптимизация для умного компьютера
    boolean checkWinGameField(int x, int y){
        boolean result = false;
        gameField[y][x] = game.getCurrentPlayer().getPlayerSign();
        if (checkWin())  {  result =  true;}
        gameField[y][x] = nullSymbol;
        return result;
    }

    /**
     * Проверка выигрышной комбинации
     */
    boolean checkWin(){
        boolean result = false;

        char playerSymbol = getGame().getCurrentPlayer().getPlayerSign();

        if(checkWinDiagonals(playerSymbol) || checkWinLines(playerSymbol)){
            result = true;
        }

        return result;
    }

    /**
     * Метод проверки заполненности поля
     * @return boolean
     */
    boolean isFull(){
        boolean result = true;

        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                if(gameField[i][j] == nullSymbol){
                    result = false;
                }
            }
        }
        return result;
    }


    // Получаем кнопку
    public GameButton getButton(int buttonIndex){
        return gameButtons[buttonIndex];
    }

    /**
     * Проверка победы по диагоналям
     * @return флаг победы
     */
    private boolean checkWinDiagonals(char playerSymbol){
        boolean leftRight, rightLeft, result;

        leftRight = true;
        rightLeft = true;
        result = false;

        for(int i = 0; i < dimension; i++){
            leftRight &= (gameField[i][i] == playerSymbol); //оптимизация кода, чтобы не было через if/else
            rightLeft &= (gameField[dimension - i - 1][i] == playerSymbol);
        }

        if(leftRight || rightLeft){
            result = true;
        }

        return result;

    }
    private boolean checkWinLines(char playerSymbol){
        boolean cols, rows, result;

        result = false;

        for(int col = 0; col < dimension; col++){
            cols = true;
            rows = true;

            for(int row = 0; row < dimension; row++){
                cols &= (gameField[col][row] == playerSymbol);
                rows &= (gameField[row][col] == playerSymbol);
            }
            if(cols || rows){
                result = true;
                break;
            }
            if(result) {
                break;
            }
        }
        return result;
    }
}
