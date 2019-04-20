package parsing;

import model.User;

public interface ICommand {
    User execute(User user, String s);
}
