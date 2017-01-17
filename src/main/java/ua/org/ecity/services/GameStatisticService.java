package ua.org.ecity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.org.ecity.entities.*;
import ua.org.ecity.repository.GameRepository;
import ua.org.ecity.repository.GameStatisticRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameStatisticService {

    @Autowired
    private GameStatisticRepository gameStatisticRepository;
    @Autowired
    private CityService cityService;

    @Autowired
    private GameRepository gameRepository;

    private Instant first = Instant.now();
    private Instant second = Instant.now();

    public List<GameStatistic> getGameStatisticsByGame(Game game) {
        return gameStatisticRepository.getGameStatisticByGame(game);
    }

    private int getLastMoveNumber(Game game) {
        List<GameStatistic> gameStatistic = gameStatisticRepository.getGameStatisticByGame(game);

        if (gameStatistic.isEmpty()) {
            return 0;
        }

        return gameStatistic.stream()
                .map(elem -> elem.getMoveNumber())
                .max(Integer::compare)
                .get();
    }

    private void addGameStatistic(Game game, City city) {
        GameStatistic gameStatistic = new GameStatistic();

        int moveNumber = getLastMoveNumber(game) + 1;

        gameStatistic.setMoveNumber(moveNumber);
        gameStatistic.setGame(game);
        gameStatistic.setCity(city);

        gameStatisticRepository.save(gameStatistic);
    }

    @Transactional
    public MoveResult giveUp(Game game) {
        if (game == null || game.isFinished()) {
            return new MoveResult(GameStatus.DOESNT_EXIST, null, null);
        }
        finish(game);
        return new MoveResult(GameStatus.WINNER_PLAYER_2, null, null);
    }

    @Transactional
    public MoveResult timeUp(Game game) {
        if (game == null || game.isFinished()) {
            return new MoveResult(GameStatus.DOESNT_EXIST, null, null);
        }
        finish(game);
        return new MoveResult(GameStatus.TIMES_IS_UP, null, null);
    }


    private void finish(Game game) {
        game.setFinished(true);
        gameRepository.save(game);
    }

    @Transactional
    public MoveResult step(Game game, List<City> clientCities) {

        if (game == null || game.isFinished()) {
            return new MoveResult(GameStatus.DOESNT_EXIST, null, null);
        }

        if (clientCities.size() == 0) {
            return new MoveResult(GameStatus.NO_CITY, null, null);
        }

        City clientCity = clientCities.get(0);

        List<City> usedCities = this.
                getGameStatisticsByGame(game).stream().map(GameStatistic::getCity).collect(Collectors.toList());

        if (usedCities.size() != 0) {
            if (clientCity.getName().charAt(0) != usedCities.get(usedCities.size() - 1).getLastChar()) {
                return new MoveResult(GameStatus.WRONG_CITY_LETTER, null, null);
            }
        }

        if (usedCities.contains(clientCity)) {
            return new MoveResult(GameStatus.CITY_USE, null, null);
        }

        second = Instant.now();
        Duration duration = Duration.between(first, second);
        if (duration.getSeconds() > 60) {
            System.out.println("Time: " + duration.getSeconds());
            first = Instant.now();
            second = Instant.now();
            return timeUp(game);
        }
        first = Instant.now();

        this.addGameStatistic(game, clientCity);
        usedCities.add(clientCity);

        City serverCity = this.getServerMove(clientCity, usedCities);

        if (serverCity == null) {
            finish(game);
            return new MoveResult(GameStatus.WINNER_PLAYER_1, null, clientCity);
        }

        this.addGameStatistic(game, serverCity);
        return new MoveResult(GameStatus.EXISTS, serverCity, clientCity);
    }

    private City getServerMove(City currentCity, List<City> usedCities) {
        List<City> cities = cityService.getCitiesByFirstLetter(currentCity.getLastChar());
        for (City city : cities) {
            if (!usedCities.contains(city)) {
                return city;
            }
        }
        return null;
    }
}
