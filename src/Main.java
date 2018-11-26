import model.FullName;
import model.User;
import model.database.services.Connector;
import model.database.services.Database;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        try {
            var conn = new Connector().connect("databases.aii.avans.nl", "fjmelche", "Ab12345", "fjmelche_db2");
            System.out.println("Connected to database and active scheme is: " + conn.getCatalog());
            var _db = new Database(conn);


            var selectedUsers =_db.select(User.class, new ArrayList<>());

            for (User user : selectedUsers) {
                System.out.println(user.getInformation());
            }


            /* Insert examples */

            /* Simple insert, single user using annotations (automatic table resolving!)*/
            var user = new User("ab", "a@b.nl", new FullName("Martijn", "", "Melchers"));
            _db.insert(user);

            /* Multiple inserts, using annotations (automatic table resolving!) */
            /*var users = new ArrayList<User>();
            users.add(new User(51, "Test-In-List-1", "Test-In-List-1@test.nl"));
            users.add(new User("Test-In-List-2", "Test-In-List-2@test.nl"));
            users.add(new User(53, "Test-In-List-3", "Test-In-List-3@test.nl"));
            _db.insert(users);*/



            /* Update examples WARNING: DOES NOT SUPPORT PRIMARY KEY CHANGES YET! */
           /* var updatedUser = selectedUsers.get(0); // <-- Get first result from query that ran above

            updatedUser.setUsername("Peter pan " + new Random().nextInt(5000));
            updatedUser.setEmail("anale@bilhaar.gitaar");

            _db.update(updatedUser);
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
