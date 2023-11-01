package Server.DataAccessing;

public class BaseDAO {

    private DataAccess myDatabase = new MemoryDataAccess();

    public BaseDAO() {}
    public BaseDAO(DataAccess database) {
        myDatabase = database;
    }

    /**
     * deletes all data from the database
     */
    public void clear() {
        myDatabase.clear();
    }
}
