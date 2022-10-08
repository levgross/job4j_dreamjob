package ru.job4j.dreamjob.util;

import ru.job4j.dreamjob.model.User;

import javax.servlet.http.HttpSession;

public final class Utility {
    private Utility() {
    }

    public static User check(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setEmail("Гость");
        }
        return user;
    }
}
