package com.senither.lilypad.minigame.network;

import com.google.gson.Gson;
import com.senither.lilypad.minigame.LilypadMinigameLobby;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.StatusCode;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class NetworkManager {

    private final LilypadMinigameLobby plugin;
    private final Gson gson;

    public NetworkManager(LilypadMinigameLobby plugin) {
        this.plugin = plugin;
        this.gson = new Gson();
    }

    @EventListener
    public void onLilyMessage(MessageEvent event) {
        if (!event.getChannel().equals("Lilypad-Minigame")) {
            return;
        }

        try {
            System.out.println(event.getSender() + " :: " + event.getMessageAsString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Error while listening to Lilypad messages:\n" + ex.getLocalizedMessage(), ex);
        }
    }

    public void messageRequest(String channel, String message, List<String> servers) {
        try {
            this.reconnectToNetwork();

            MessageRequest request = new MessageRequest(servers, channel, message);

            plugin.getConnect().request(request);
        } catch (UnsupportedEncodingException | RequestException ex) {
            throw new RuntimeException("Error while sending a message request." + ex);
        } catch (Throwable ex) {
            throw new RuntimeException("Error whilst redirecting a player. The connection seems to have been closed and won't open again." + ex);
        }
    }

    public boolean teleportRequest(String player, String server) {
        try {
            this.reconnectToNetwork();

            RedirectRequest request = new RedirectRequest(server, player);
            FutureResult future = plugin.getConnect().request(request);

            return future.await().getStatusCode().equals(StatusCode.SUCCESS);
        } catch (RequestException | InterruptedException ex) {
            throw new RuntimeException("Error whilst redirecting a player." + ex);
        } catch (Throwable ex) {
            throw new RuntimeException("Error whilst redirecting a player. The connection seems to have been closed and won't open again." + ex);
        }
    }

    private void reconnectToNetwork() throws Throwable {
        if (!plugin.getConnect().isConnected()) {
            plugin.getConnect().connect();
        }
    }
}
