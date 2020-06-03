package game;

import javax.swing.*;

public class GameButton extends JButton {
    int buttonIndex; // Номер кнопки
    private GameBoard board; // Ссылка на игровое поле

    public GameButton(int gameButtonIndex, GameBoard currentGameBoard){
        buttonIndex = gameButtonIndex;
        board = currentGameBoard;

        int row = buttonIndex / GameBoard.dimension;    // Номер ряда, или номер строки
        int cell = buttonIndex % GameBoard.dimension;   // Номер столбца

        setSize(GameBoard.cellSize - 5, GameBoard.cellSize - 5);
        addActionListener(new GameActionListener(this));
    }

    public GameBoard getBoard(){
        return board;               // Возвращает ссылку на игровую доску для кнопки
    }

    public int getButtonIndex() { return buttonIndex; }
}
