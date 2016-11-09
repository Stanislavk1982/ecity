package ua.org.ecity.controllers;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.org.ecity.entities.City;
import ua.org.ecity.entities.Game;
import ua.org.ecity.entities.MoveResult;
import ua.org.ecity.entities.Result;
import ua.org.ecity.services.CityService;
import ua.org.ecity.services.GameService;
import ua.org.ecity.services.UserService;

@RestController
public class GameController {

    @Autowired
    CityService cityService;
    @Autowired
    GameService gameService;
    @Autowired
    UserService userService;

    private SessionFactory sessionFactory;

    @RequestMapping("/login")
    public Result login() {
        return new Result(true);
    }


    @RequestMapping("/game/new")
    public String createNewGame(@AuthenticationPrincipal final UserDetails user) {

        String userName = user.getUsername();
        System.out.println("userName = " + userName);

        int userId = userService.getUser(userName).getId();
        System.out.println("userId = " + userId);

        int id = gameService.newGame(userId);
        System.out.println("id = " + id);

        return "{\"id\":" + id + "}";
    }

    @RequestMapping(value = "/game/move", method = RequestMethod.POST)
    public MoveResult move(@RequestParam("game_id") int gameId, @RequestParam("city_name") String cityName) {
        MoveResult moveResult = new MoveResult();

        Game game = gameService.getGame(gameId);
        if (game == null) {
            moveResult.setSuccess(false);
            moveResult.setError("no such game");
            return moveResult;
        }

        if (cityService.getCity(cityName) == null) {
            moveResult.setSuccess(false);
            moveResult.setError("no such city in the game database");
            return moveResult;
        }

        moveResult.setSuccess(true);
        City generatedCity = cityService.getCity("Киев");
        moveResult.setGeneratedCity(generatedCity.getName());
        moveResult.setPositionX(generatedCity.getLatitude());
        moveResult.setPositionY(generatedCity.getLongitude());
        return moveResult;
    }

}