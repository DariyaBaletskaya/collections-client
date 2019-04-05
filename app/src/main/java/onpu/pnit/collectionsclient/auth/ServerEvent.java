package onpu.pnit.collectionsclient.auth;

import onpu.pnit.collectionsclient.entities.User;

public class ServerEvent {
    private User user;

    public ServerEvent(User user) {
        this.user = user;
    }

    public User getServerResponse() {
        return user;
    }

    public void setServerResponse(User user) {
        this.user = user;
    }
}
