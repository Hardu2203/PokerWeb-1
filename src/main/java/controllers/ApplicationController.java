/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import Services.PokerService;
import com.google.inject.Inject;
import filters.SecureFilter;
import modal.java.Security.SecureHash;
import modal.java.cards.Deck;
import modal.java.cards.Hand;
import modal.java.evaluators.HandEvaluator;
import modal.java.game.Game;
import modal.java.game.GameUser;
import modal.java.game.MultiplayerGameList;
import modal.java.providers.GameProvider;
import modal.java.providers.GameUserProvider;
import modal.java.users.User;
import modal.java.providers.UserProvider;
import ninja.*;

import com.google.inject.Singleton;
import ninja.params.PathParam;
import org.eclipse.jetty.server.handler.HandlerList;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Singleton
public class ApplicationController {
    @Inject
    private PokerService pokerInstance;

    @Inject
    private UserProvider userProvider;

    @Inject
    private GameUserProvider gameUserProvider;

    @Inject
    private GameProvider gameProvider;

    @Inject
    private MultiplayerGameList multiplayerGames;

    @Inject
    private Router router;

    private volatile Long lastUpdateDate;

    public  Result lobbyUpdate(Context context)
    {
        String gamenumber = context.getSession().get("joinedGame");
        int gameNumber = Integer.parseInt(gamenumber);
        Long lastUpdateTime = lastUpdateDate;
        int counter = 0;
        while(lastUpdateDate == lastUpdateTime && counter < 1000)
        {
            try {
                counter++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("My name:" + context.getSession().get("username"));
        if(gameNumber != -1) {
            if(multiplayerGames.getGames().size() != 0) {
                for (User tempUser : multiplayerGames.getJoinedUsers().get(gameNumber)) {
                    System.out.println("other names:" + tempUser.getName());
                    if (tempUser.getName().equals(context.getSession().get("username"))) {
                        Result result = Results.html();
                        result.render("multiplayerGames", multiplayerGames.getGames());
                        result.render("hosts", multiplayerGames.getHosts());
                        result.render("joinedUsers", multiplayerGames.getJoinedUsers());
                        result.render("joinedDates", multiplayerGames.getJoinedDates());
                        result.render("username", context.getSession().get("username"));
                        return result;
                    }
                }
                context.getSession().put("joinedGame",""+-1);
                return Results.json().render("{\"redirect\":\"" + true + "\"}");
            }
            context.getSession().put("joinedGame",""+-1);
            return Results.json().render("{\"redirect\":\"" + true + "\"}");
        }
        Result result = Results.html();
        result.render("multiplayerGames",multiplayerGames.getGames());
        result.render("hosts",multiplayerGames.getHosts());
        result.render("joinedUsers",multiplayerGames.getJoinedUsers());
        result.render("joinedDates",multiplayerGames.getJoinedDates());
        result.render("username", context.getSession().get("username"));
        return result;
    }

    @FilterWith(SecureFilter.class)
    public synchronized Result multiplayergame(Context context, @PathParam("gamenumber") String gamenumber)
    {
        int gameNumber = Integer.parseInt(gamenumber);
        Result result = Results.html();
        result.render("username", context.getSession().get("username"));
        String folder = "/assets/images/cards/";
        result.render("folder",folder);
        Deck deck = new Deck();
        Game game = multiplayerGames.getGames().remove(gameNumber);
        game.setDate_time(new Date());
        List<Hand> handList = new LinkedList<Hand>();
        List<GameUser> gameUserList = new LinkedList<GameUser>();
        List<User> userList = new LinkedList<User>();
        //Current user
        User currentUser = multiplayerGames.getHosts().remove(gameNumber);

        userList.add(currentUser);
        Hand currentUserHand = pokerInstance.dealHand(deck);
        handList.add(currentUserHand);
        GameUser gameUser = new GameUser();
        gameUser.setGame(game);
        gameUser.setUser(currentUser);
        gameUser.setHand(currentUserHand.toString());
        gameUser.setType(currentUserHand.getHandType().toString());
        gameUserList.add(gameUser);
        //all other users
        LinkedList<User> users = multiplayerGames.getJoinedUsers().remove(gameNumber);
        for(User user : users)
        {
            Hand hand = pokerInstance.dealHand(deck);
            handList.add(hand);
            gameUser = new GameUser();
            gameUser.setGame(game);
            userList.add(user);
            gameUser.setUser(user);
            gameUser.setHand(hand.toString());
            gameUser.setType(hand.getHandType().toString());
            gameUserList.add(gameUser);
        }
        int winningPosition = HandEvaluator.findWinnerPosition(handList);
        User winner = userList.get(winningPosition);
        game.setWinner(winner);
        result.render("winner", winner.getName());
        gameUserList.get(winningPosition).setWinner(true);
        //Persist everything!
        gameProvider.persist(game);
        for(GameUser gu: gameUserList)
        {
            gameUserProvider.persist(gu);
        }
        //remove times from list
        multiplayerGames.getJoinedDates().remove(gameNumber);
        //render everything
        result.render("userlist",userList);
        result.render("handlist",handList);
        result.render("winninghand",handList.get(winningPosition));
        lastUpdateDate = (new Date()).getTime();
        return result;
    }

    @FilterWith(SecureFilter.class)
    public synchronized Result join(Context context, @PathParam("gamenumber") String gamenumber)
    {
        int gameNumber = Integer.parseInt(gamenumber);
        //Game game = multiplayerGames.getGames().get(Integer.parseInt(gamenumber));
        LinkedList<User> joinedUsers = multiplayerGames.getJoinedUsers().get(gameNumber);
        Optional<User> userOptional = userProvider.findUserByName(context.getSession().get("username"));
        User user = userOptional.get();
        joinedUsers.add(user);
        LinkedList<Date> joinedDate = multiplayerGames.getJoinedDates().get(gameNumber);
        joinedDate.add(new Date());
        lastUpdateDate = (new Date()).getTime();
        context.getSession().put("joinedGame",gamenumber);
        Result result = Results.redirect(router.getReverseRoute(ApplicationController.class, "lobby"));
        return result;
    }

    @FilterWith(SecureFilter.class)
    public synchronized Result host(Context context)
    {
        Game game = new Game();
        game.setGame_name(context.getParameter("gamename"));
        multiplayerGames.getGames().add(game);
        multiplayerGames.getJoinedDates().add(new LinkedList<Date>());
        multiplayerGames.getJoinedUsers().add(new LinkedList<User>());
        Optional<User> userOptional = userProvider.findUserByName(context.getSession().get("username"));
        User user = userOptional.get();
        multiplayerGames.getHosts().add(user);
        lastUpdateDate = (new Date()).getTime();
        //context.getSession().put("gamenumber",""+multiplayerGames.getGames().size());
        return Results.redirect("lobby");
    }


    @FilterWith(SecureFilter.class)
    public Result lobby(Context context)
    {
        Result result = Results.html();
        result.render("multiplayerGames",multiplayerGames.getGames());
        result.render("hosts",multiplayerGames.getHosts());
        result.render("joinedUsers",multiplayerGames.getJoinedUsers());
        result.render("joinedDates",multiplayerGames.getJoinedDates());
        result.render("username", context.getSession().get("username"));
        return result;
    }


    @FilterWith(SecureFilter.class)
    public Result choseType(Context context)
    {
        return Results.html();
    }

    @FilterWith(SecureFilter.class)
    public Result cpu(Context context)
    {
        Result result = Results.html();
        List<User> users = userProvider.findAllUsersExceptFor(context.getSession().get("username"));
        result.render("username", context.getSession().get("username"));
        result.render("users", users);
        return result;
    }

    @FilterWith(SecureFilter.class)
    public Result history(Context context) {
        Result result = Results.html();

        List<Game> games = gameProvider.findAllGames();
        result.render("games", games);
        List<GameUser> gameUsers = gameUserProvider.findAllGameUsers();
        result.render("gameusers",gameUsers);
        return result;
        //return Results.html();
    }


    @FilterWith(SecureFilter.class)
    public Result game(Context context) {
        Result result = Results.html();
        result.render("username", context.getSession().get("username"));
        String folder = "assets/images/cards/";
        result.render("folder",folder);
        String gameName = context.getParameter("gamename");
        Deck deck = new Deck();
        Game game = new Game();
        game.setGame_name(gameName);
        game.setDate_time(new Date());
        List<Hand> handList = new LinkedList<Hand>();
        List<GameUser> gameUserList = new LinkedList<GameUser>();
        List<User> userList = new LinkedList<User>();
        //Current user
        Optional<User> userOptional = userProvider.findUserByName(context.getSession().get("username"));
        User currentUser = userOptional.get();
        userList.add(currentUser);
        Hand currentUserHand = pokerInstance.dealHand(deck);
        handList.add(currentUserHand);
        GameUser gameUser = new GameUser();
        gameUser.setGame(game);
        gameUser.setUser(currentUser);
        gameUser.setHand(currentUserHand.toString());
        gameUser.setType(currentUserHand.getHandType().toString());
        gameUserList.add(gameUser);
        //all other users
        for(int i = 1; i < 6; i++)
        {
            Hand hand = pokerInstance.dealHand(deck);
            handList.add(hand);
            gameUser = new GameUser();
            gameUser.setGame(game);
            userOptional = userProvider.findUserByName(context.getParameter("u" + i));
            User user = userOptional.get();
            userList.add(user);
            gameUser.setUser(user);
            gameUser.setHand(hand.toString());
            gameUser.setType(hand.getHandType().toString());
            gameUserList.add(gameUser);
        }
        int winningPosition = HandEvaluator.findWinnerPosition(handList);
        User winner = userList.get(winningPosition);
        game.setWinner(winner);
        result.render("winner", winner.getName());
        gameUserList.get(winningPosition).setWinner(true);
        //Persist everything!
        gameProvider.persist(game);
        for(GameUser gu: gameUserList)
        {
            gameUserProvider.persist(gu);
        }
        //render everything
        result.render("userlist",userList);
        result.render("handlist",handList);
        result.render("winninghand",handList.get(winningPosition));
        return result;

    }

    public Result index() {
        return Results.html();
    }

    public Result register() {
        return Results.html();
    }

    public Result login(Context context)
    {
        Optional<User> user = userProvider.findUserByName(context.getParameter("username"));
        if(!user.isPresent())
        {
            context.getFlashScope().error("Incorrect username.");
            return Results.redirect("/");
        }
        try {
            if(!SecureHash.validatePassword(context.getParameter("password"),user.get().getPassword()))
            {
                context.getFlashScope().error("Incorrect password.");
                return Results.redirect("/");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        context.getSession().put("username",context.getParameter("username"));
        context.getSession().put("joinedGame","-1");
        return Results.redirect("/chosetype");
    }

    public Result registerNewUser(Context context) {
        String hashedPassword = null;
        try {
            hashedPassword = SecureHash.createHash(context.getParameter("password"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        User user = new User();
        user.setName(context.getParameter("username"));
        user.setPassword(hashedPassword);
        userProvider.persist(user);
        return Results.redirect("/login");

    }

    public Result logout(Context context) {
        context.getSession().clear();
        return Results.redirect("/login");
    }

    public Result username(Context context)
    {
        boolean result = userProvider.findUserByName(context.getParameter("username")).isPresent(); //userProvider.userExists(context.getParameter("username"));
        return Results.json().render("{\"result\":\""+ result +"\"}");
    }


}
