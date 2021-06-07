package notesServer.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Settings {

    @Value("${rest_http_port}")
    private int restHttpPort;
    @Value("${max_name_length}")
    private int maxNameLength;
    @Value("${min_password_length}")
    private int minPasswordLength;
    @Value("60")
    private int userIdleTimeout;

    public int getRestHttpPort() {
        return restHttpPort;
    }

    public void setRestHttpPort(int restHttpPort) {
        this.restHttpPort = restHttpPort;
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    public int getUserIdleTimeout() {
        return userIdleTimeout;
    }

    public void setUserIdleTimeout(int userIdleTimeout) {
        this.userIdleTimeout = userIdleTimeout;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "restHttpPort=" + restHttpPort +
                ", maxNameLength=" + maxNameLength +
                ", minPasswordLength=" + minPasswordLength +
                ", userIdleTimeout=" + userIdleTimeout +
                '}';
    }
}
