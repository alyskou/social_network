package org.alyskou.otus.queue;

import org.alyskou.otus.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsUpdateReceiver {
    private final NewsService newsService;

    @Autowired
    NewsUpdateReceiver(NewsService newsService) {
        this.newsService = newsService;
    }

    public void receiveMessage(String userEmail) {
        newsService.rebuildNews(userEmail);
    }
}
