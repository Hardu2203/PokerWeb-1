/**
 * Copyright (C) 2012 the original author or authors.
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

package conf;


import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import controllers.ApplicationController;

public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {  
        
        router.GET().route("/").with(ApplicationController.class, "index");
        router.POST().route("/game").with(ApplicationController.class, "game");
        router.GET().route("/cpu").with(ApplicationController.class, "cpu");
        router.GET().route("/register").with(ApplicationController.class, "register");
        router.POST().route("/registerNewUser").with(ApplicationController.class, "registerNewUser");
        router.POST().route("/login").with(ApplicationController.class, "login");
        router.GET().route("/logout").with(ApplicationController.class, "logout");
        router.GET().route("/history").with(ApplicationController.class, "history");
        router.GET().route("/username").with(ApplicationController.class, "username");
        router.GET().route("/chosetype").with(ApplicationController.class,"choseType");
        router.GET().route("/lobby").with(ApplicationController.class,"lobby");
        router.GET().route("/lobbyUpdate").with(ApplicationController.class,"lobbyUpdate");
        router.POST().route("/host").with(ApplicationController.class,"host");
        router.GET().route("/joingame/{gamenumber}").with(ApplicationController.class,"join");
        router.GET().route("/multiplayergame/{gamenumber}").with(ApplicationController.class,"multiplayergame");
        ///////////////////////////////////////////////////////////////////////
        // Assets (pictures / javascript)
        ///////////////////////////////////////////////////////////////////////    
        router.GET().route("/assets/webjars/{fileName: .*}").with(AssetsController.class, "serveWebJars");
        router.GET().route("/assets/{fileName: .*}").with(AssetsController.class, "serveStatic");
        
        ///////////////////////////////////////////////////////////////////////
        // Index / Catchall shows index page
        ///////////////////////////////////////////////////////////////////////
        router.GET().route("/.*").with(ApplicationController.class, "index");
    }

}