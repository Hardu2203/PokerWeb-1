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

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
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
        List<User> users = userProvider.findAllUsers();
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
        Hand hand;
        String folder = "assets/images/cards/";
        Deck deck = new Deck();
        String gameName = "Random Name for now";
        Game game = new Game();
        game.setGame_name(gameName);
        game.setDate_time(new Date());
        gameProvider.persist(game);
        Optional<User> userOptional = userProvider.findUserByName(context.getSession().get("username"));
        User user = userOptional.get();
        for(int i = 1; i < 6; i++) {
            if(i != 1)
            {
                userOptional = userProvider.findUserByName(context.getParameter("u"+i));
                user = userOptional.get();
            }

            hand = pokerInstance.dealHand(deck);
            GameUser gameUser = new GameUser();
            gameUser.setGame(game);
            gameUser.setUser(user);
            gameUser.setHand(hand.toString());
            gameUserProvider.persist(gameUser);
            result.render("c1u"+i, folder + hand.getCards().get(0).toString() + ".png");
            result.render("c2u"+i, folder + hand.getCards().get(1).toString() + ".png");
            result.render("c3u"+i, folder + hand.getCards().get(2).toString() + ".png");
            result.render("c4u"+i, folder + hand.getCards().get(3).toString() + ".png");
            result.render("c5u"+i, folder + hand.getCards().get(4).toString() + ".png");

            result.render("type"+i, pokerInstance.evaluateHand(hand));

        }
        return result;
        //return Results.html();
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
        //context.getSession().put("username","username");
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
