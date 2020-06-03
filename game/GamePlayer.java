package game;

import java.util.Random;

public class GamePlayer {
    private final char playerSign;
    private boolean realPlayer = true;

    public GamePlayer(boolean isRealPlayer, char playerSign){
        this.realPlayer = isRealPlayer;
        this.playerSign = playerSign;
    }

    public boolean isRealPlayer(){
        return realPlayer;
    }

    public char getPlayerSign(){
        return playerSign;
    }

    /**
     * Ход человека
     *
     * @param button GameButton() - ссылка на ячейку поля
     */
    void updateByPlayersData(GameButton button) {
        int row = button.getButtonIndex() / GameBoard.dimension;
        int cell = button.getButtonIndex() % GameBoard.dimension;

        // Обновляем матрицу игры
        button.getBoard().updateGameField(row, cell);
        // Обновляем содержимое кнопки
        button.setText(Character.toString(button.getBoard().getGame().getCurrentPlayer().getPlayerSign()));
        // После хода проверим состояние победы
        if (button.getBoard().checkWin()) {
            button.getBoard().getGame().showMessage("Вы выиграли!");
            button.getBoard().emptyField();



        } else {
            button.getBoard().getGame().passTurn();
        }
    }

    /**
     * Ход комппьютера
     *
     * @param button GameButton() - ссылка на ячейку игрового поля
     */
    void updateByAiData(GameButton button) {


        // Генерация координат хода компьютера
        int x , y;
        Random rnd = new Random();
// пальцем в небо
        do {
            x = rnd.nextInt(GameBoard.dimension);
            y = rnd.nextInt(GameBoard.dimension);
        } while (!button.getBoard().isTurnable(x, y));

// проверка полей, где компьютер может выиграть (нападение)
        for(int i = 0; i < (GameBoard.dimension *GameBoard.dimension); i++) {
            int xx = i / GameBoard.dimension;
            int yy = i % GameBoard.dimension;
            if (button.getBoard().isTurnable(xx,yy)){
                if (button.getBoard().checkWinGameField(xx, yy)) {
                    x = xx;
                    y = yy;
                    i = GameBoard.dimension *GameBoard.dimension;
                }
            }
        }
// (защита, )
        if (!button.getBoard().checkWinGameField(x,y)){ // если нет шанса выиграть, то переходим к проверке возможностей у человека)
            for(int i = 0; i < (GameBoard.dimension *GameBoard.dimension); i++) {
                int xx = i / GameBoard.dimension;
                int yy = i % GameBoard.dimension;
                if (button.getBoard().isTurnable(xx,yy)) {
                    button.getBoard().getGame().passTurn();
                    if (button.getBoard().checkWinGameField(xx, yy)) {
                        x = xx;
                        y = yy;
                        i = GameBoard.dimension * GameBoard.dimension;
                    }
                    button.getBoard().getGame().passTurn();
                }
            }
        }

        // Обновим матрицу игры
        button.getBoard().updateGameField(x, y);

        // Обновим содержимое кнопки
        int cellIndex = GameBoard.dimension * x + y;
        button.getBoard().getButton(cellIndex).setText(Character.toString(button.getBoard().getGame().getCurrentPlayer().getPlayerSign()));

        // Проверить победу
        if (button.getBoard().checkWin()) {
            button.getBoard().getGame().showMessage("Компьютер выиграл!");
            button.getBoard().emptyField();
        } else {
            button.getBoard().getGame().passTurn();
        }
    }

}