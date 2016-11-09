package ua.org.ecity.services;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.ecity.entities.Game;
import ua.org.ecity.repository.GameRepository;

import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    //private SessionFactory sessionFactory;

    public List<Game> getGameById(long id) {
        return gameRepository.findById(id);
    }

    public List<Game> getGameByMoves(Integer moves) {
        return gameRepository.findByMoves(moves);
    }


    public Long newGame(int player1) {
        //SessionFactory sessionFactory=null;
        Game game = new Game();
        game.setFinished(false);
        game.setPlayer1(999);
        game.setFirst_player(999);
        game.setPlayer2(777);
        gameRepository.save(game);
        //long temp = gameRepository.count();
        long id = game.getId();
        //long temp = gameRepository.getGameId(game);
        //game = (Game) gameRepository.findByPlayer1(999);
        //long temp = game.getId();
        //sessionFactory.getCurrentSession().save(game);
        //String str = String.valueOf(temp);
        //return str;
        //return "{\"id\":"+temp+"\"}";
        return id;
    }
}
