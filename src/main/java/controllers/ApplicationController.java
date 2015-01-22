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
import modal.java.providers.GameProvider;
import modal.java.providers.GameUserProvider;
import modal.java.users.User;
import modal.java.providers.UserProvider;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;

import com.google.inject.Singleton;
import org.eclipse.jetty.server.handler.HandlerList;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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


    @FilterWith(SecureFilter.class)
    public Result newGame(Context context)
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
        return Results.redirect("/newgame");
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
