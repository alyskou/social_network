package org.alyskou.otus.controller;

import org.alyskou.otus.data.News;
import org.alyskou.otus.data.User;
import org.alyskou.otus.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.alyskou.otus.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final NewsService newsService;

    @Autowired
    UserController(UserService userService, NewsService newsService) {
        this.userService = userService;
        this.newsService = newsService;
    }

    @GetMapping("/new")
    public String newUserPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "new";
    }

    @PostMapping("/new")
    public String saveNewUser(Model model, @ModelAttribute("user") @Valid User user) {
        try {
            userService.saveNewUser(user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "new";
        }

        model.addAttribute("isUserCreated", true);
        return "login";
    }

    @GetMapping("/user")
    public String getUser(
            @RequestParam("email") String email,
            Principal principal,
            Model model) {
        String selfEmail = principal.getName();
        User user = userService.getUser(email);
        List<String> selfFriends = userService.getUserFriends(selfEmail);
        List<News> news = newsService.getOwnNews(email);

        model.addAttribute("user", user);
        model.addAttribute("isOwnPage", selfEmail.equals(email));
        model.addAttribute("isFriend", selfFriends.contains(email));
        model.addAttribute("newsList", news);
        return "user";
    }

    @GetMapping("/me")
    public String getUser(Principal principal, Model model) {
        String email = principal.getName();
        User user = userService.getUser(email);
        List<News> news = newsService.getOwnNews(email);

        model.addAttribute("user", user);
        model.addAttribute("isOwnPage", true);
        model.addAttribute("newsList", news);
        return "user";
    }

    @GetMapping("/all_users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "all_users";
    }

    @PostMapping("/add_friend")
    public String addFriend(Principal principal, @ModelAttribute("toEmail") String toEmail) {
        String fromEmail = principal.getName();
        userService.addFriend(fromEmail, toEmail);
        return "redirect:/me";
    }

    @PostMapping("/generate_users")
    public String generateUsers(@ModelAttribute("userCount") int count) {
        userService.generateUsers(count);
        return "redirect:/all_users";
    }

    @GetMapping("/")
    public String getHomePage() {
        return "redirect:/all_users";
    }

    @GetMapping("/search")
    public String getSearch() {
        return "search";
    }

    @PostMapping("/search")
    public String doSearch(
            @ModelAttribute("firstNamePrefix") String firstNamePrefix,
            @ModelAttribute("lastNamePrefix") String lastNamePrefix,
            Model model) {
        List<User> users = userService.findUsersByName(firstNamePrefix, lastNamePrefix);
        model.addAttribute("users", users);
        return "search";
    }

    @PostMapping("/post_news")
    public String postNews(
            @ModelAttribute("text") String text,
            Principal principal) {
        String email = principal.getName();
        newsService.saveNews(email, text);
        return "redirect:/me";
    }

    @GetMapping("/news")
    public String newsFeed(Principal principal, Model model) {
        String email = principal.getName();
        List<News> newsList = newsService.getNewsFeed(email, false);
        model.addAttribute("newsList", newsList);
        return "news";
    }
}
