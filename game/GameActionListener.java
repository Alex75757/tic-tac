package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameActionListener implements ActionListener {
    private GameButton button;

    public GameActionListener(GameButton gButton) {
        button = gButton;  // ссылка на кнопку, к которой привязыаем наш GameActionListener
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GameBoard board = button.getBoard();
        int row = button.getButtonIndex() / GameBoard.dimension;
        int cell = button.getButtonIndex() % GameBoard.dimension;

        if (board.isTurnable(row, cell)) {
            board.getGame().getCurrentPlayer().updateByPlayersData(button);

            if (board.isFull()) {
                button.getBoard().getGame().showMessage("Ничья");
                board.emptyField();


            }else if (!board.getGame().getCurrentPlayer().isRealPlayer()) // устранение неоптимальностей
                 {
                button.getBoard().getGame().getCurrentPlayer().updateByAiData(button);
                     if (board.isFull()) {       // при поле 4х4 без такой проверки "беда", вывод - это оптимизация
                    button.getBoard().getGame().showMessage("Ничья");
                    board.emptyField();
                }
            }
        } else {
            button.getBoard().getGame().showMessage("Некорректный ход");
        }

    }


}