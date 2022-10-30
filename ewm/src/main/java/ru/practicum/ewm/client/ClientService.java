package ru.practicum.ewm.client;

public interface ClientService {
    void hit(String ip, String uri);

    int getStats(String uri);
}
