import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private final List<Entry> entries;

    public AuthService() {
        entries = new ArrayList<>();
        entries.add(new Entry("E1","P1","N1"));
        entries.add(new Entry("E2","P2","N2"));
        entries.add(new Entry("E3","P3","N3"));
        entries.add(new Entry("E4","P4","N4"));
        entries.add(new Entry("E5","P5","N5"));
    }

    public String getNickByLoginPuss (String login, String password){
        for (Entry entry : entries){
            if (entry.login.equals(login)&&entry.password.equals(password)) return entry.nickname;
        }
        return null;
    }

    private class Entry{
        private final String login;
        private final String password;
        private final String nickname;

        public Entry(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }
}
