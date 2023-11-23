package name.lattuada.trading.tests;
import lombok.Getter;
import lombok.Setter;
import name.lattuada.trading.model.dto.UserDTO;
import java.util.Arrays;

@Getter
@Setter
public class UserListResponse {

    private UserDTO[] users;
    public UserListResponse() {
    }
    public UserListResponse(UserDTO[] users) {
        this.users = users;
    }
    public UserDTO[] getUsers() {
        return users;
    }
    public void setUsers(UserDTO[] users) {
        this.users = users;
    }
    @Override
    public String toString() {
        return "UserListResponse{" +
                "users=" + Arrays.toString(users) +
                '}';
    }
}