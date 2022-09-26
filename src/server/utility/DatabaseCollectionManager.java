package server.utility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Stack;

import common.data.Color;
import common.data.Coordinates;
import common.data.Country;
import common.data.EyeColor;
import common.data.Location;
import common.data.Person;
import common.data.Position;
import common.data.Status;
import common.data.Worker;
import common.exceptions.DatabaseHandlingException;
import common.interaction.User;
import common.interaction.WorkerObject;

public class DatabaseCollectionManager {
    // WORKER_TABLE
    private final String SELECT_ALL_WORKERS = "SELECT * FROM " + DatabaseHandler.WORKER_TABLE;
    private final String SELECT_WORKER_BY_ID = SELECT_ALL_WORKERS + " WHERE " +
            DatabaseHandler.WORKER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_WORKER_BY_ID_AND_USER_ID = SELECT_WORKER_BY_ID + " AND " +
            DatabaseHandler.WORKER_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_WORKER = "INSERT INTO " +
            DatabaseHandler.WORKER_TABLE + " (" +
            DatabaseHandler.WORKER_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.WORKER_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseHandler.WORKER_TABLE_SALARY_COLUMN + ", " +
            DatabaseHandler.WORKER_TABLE_POSITION_COLUMN + ", " +
            DatabaseHandler.WORKER_TABLE_STATUS_COLUMN + ", " +
            DatabaseHandler.WORKER_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, ?)";

    private final String DELETE_WORKER_BY_ID = "DELETE FROM " + DatabaseHandler.WORKER_TABLE +
            " WHERE " + DatabaseHandler.WORKER_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_WORKER_NAME_BY_ID = "UPDATE " + DatabaseHandler.WORKER_TABLE + " SET " +
            DatabaseHandler.WORKER_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.WORKER_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_WORKER_SALARY_BY_ID = "UPDATE " + DatabaseHandler.WORKER_TABLE + " SET " +
            DatabaseHandler.WORKER_TABLE_SALARY_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.WORKER_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_WORKER_POSITION_BY_ID = "UPDATE " + DatabaseHandler.WORKER_TABLE + " SET " +
            DatabaseHandler.WORKER_TABLE_POSITION_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.WORKER_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_WORKER_STATUS_BY_ID = "UPDATE " + DatabaseHandler.WORKER_TABLE + " SET " +
            DatabaseHandler.WORKER_TABLE_STATUS_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.WORKER_TABLE_ID_COLUMN + " = ?";
    // COORDINATES_TABLE
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseHandler.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_WORKER_ID = SELECT_ALL_COORDINATES +
            " WHERE " + DatabaseHandler.COORDINATES_TABLE_WORKER_ID_COLUMN + " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseHandler.COORDINATES_TABLE + " (" +
            DatabaseHandler.COORDINATES_TABLE_WORKER_ID_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?, ?)";
    private final String DELETE_COORDINATES_BY_WORKER_ID = "DELETE FROM " + DatabaseHandler.COORDINATES_TABLE +
            " WHERE " + DatabaseHandler.COORDINATES_TABLE_WORKER_ID_COLUMN + " = ?";
    private final String UPDATE_COORDINATES_BY_LAB_ID = "UPDATE " + DatabaseHandler.COORDINATES_TABLE + " SET " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.COORDINATES_TABLE_WORKER_ID_COLUMN + " = ?";
    // PERSON_TABLE
    private final String SELECT_ALL_PERSON = "SELECT * FROM " + DatabaseHandler.PERSON_TABLE;
    private final String SELECT_PERSON_BY_WORKER_ID = SELECT_ALL_PERSON +
            " WHERE " + DatabaseHandler.PERSON_TABLE_WORKER_ID_COLUMN + " = ?";
    private final String INSERT_PERSON = "INSERT INTO " +
            DatabaseHandler.PERSON_TABLE + " (" +
            DatabaseHandler.PERSON_TABLE_WORKER_ID_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_BIRTHDAY_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_EYE_COLOR_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_HAIR_COLOR_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_COUNTRY_COLUMN + 
            ") VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_PERSON_BY_WORKER_ID = "UPDATE " + DatabaseHandler.PERSON_TABLE + " SET " +
            DatabaseHandler.PERSON_TABLE_BIRTHDAY_COLUMN + " = ?, " +
            DatabaseHandler.PERSON_TABLE_EYE_COLOR_COLUMN + " = ?" +
            DatabaseHandler.PERSON_TABLE_HAIR_COLOR_COLUMN + " = ?" +
            DatabaseHandler.PERSON_TABLE_COUNTRY_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.PERSON_TABLE_WORKER_ID_COLUMN + " = ?";
    private final String DELETE_PERSON_BY_WORKER_ID = "DELETE FROM " + DatabaseHandler.PERSON_TABLE +
            " WHERE " + DatabaseHandler.PERSON_TABLE_WORKER_ID_COLUMN + " = ?";
    //LOCATION_TABLE
    private final String SELECT_ALL_LOCATION = "SELECT * FROM " + DatabaseHandler.LOCATION_TABLE;
    private final String SELECT_LOCATION_BY_WORKER_ID = SELECT_ALL_LOCATION +
            " WHERE " + DatabaseHandler.LOCATION_TABLE_WORKER_ID_COLUMN + " = ?";
    private final String INSERT_LOCATION = "INSERT INTO " +
            DatabaseHandler.LOCATION_TABLE + " (" +
            DatabaseHandler.LOCATION_TABLE_WORKER_ID_COLUMN + ", " +
            DatabaseHandler.LOCATION_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.LOCATION_TABLE_X_COLUMN + ", " +
            DatabaseHandler.LOCATION_TABLE_Y_COLUMN + ", " +
            DatabaseHandler.LOCATION_TABLE_Z_COLUMN + 
            ") VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_LOCATION_BY_WORKER_ID = "UPDATE " + DatabaseHandler.LOCATION_TABLE + " SET " +
            DatabaseHandler.LOCATION_TABLE_NAME_COLUMN + " = ?, " +
            DatabaseHandler.LOCATION_TABLE_X_COLUMN + " = ?" +
            DatabaseHandler.LOCATION_TABLE_Y_COLUMN + " = ?" +
            DatabaseHandler.LOCATION_TABLE_Z_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.LOCATION_TABLE_WORKER_ID_COLUMN + " = ?";
    private final String DELETE_LOCATION_BY_WORKER_ID = "DELETE FROM " + DatabaseHandler.LOCATION_TABLE +
            " WHERE " + DatabaseHandler.LOCATION_TABLE_WORKER_ID_COLUMN + " = ?";
    
            private DatabaseHandler databaseHandler;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseHandler databaseHandler, DatabaseUserManager databaseUserManager) {
        this.databaseHandler = databaseHandler;
        this.databaseUserManager = databaseUserManager;
    }

    
    private Worker createWorker(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(DatabaseHandler.WORKER_TABLE_ID_COLUMN);
        String name = resultSet.getString(DatabaseHandler.WORKER_TABLE_NAME_COLUMN);
        Coordinates coordinates = getCoordinatesByWorkerId(id);
        LocalDateTime creationDate = resultSet.getTimestamp(DatabaseHandler.WORKER_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        int salary = resultSet.getInt(DatabaseHandler.WORKER_TABLE_SALARY_COLUMN);
        Position position = Position.valueOf(resultSet.getString(DatabaseHandler.WORKER_TABLE_POSITION_COLUMN));
        Status status = Status.valueOf(resultSet.getString(DatabaseHandler.WORKER_TABLE_STATUS_COLUMN));
        Person person = getPersonByWorkerId(id);
        User owner = databaseUserManager.getUserById(resultSet.getLong(DatabaseHandler.WORKER_TABLE_USER_ID_COLUMN));
        return new Worker(
                id,
                name,
                coordinates,
                creationDate,
                salary,
                position,
                status,
                person,
                owner
        );
    }

    
    public Stack<Worker> getCollection() throws DatabaseHandlingException {
        Stack<Worker> workerList = new Stack<>();
        PreparedStatement preparedSelectAllStatement = null;
        try {
            preparedSelectAllStatement = databaseHandler.getPreparedStatement(SELECT_ALL_WORKERS, false);
            ResultSet resultSet = preparedSelectAllStatement.executeQuery();
            while (resultSet.next()) {
                workerList.add(createWorker(resultSet));
            }
        } catch (SQLException exception) {
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectAllStatement);
        }
        return workerList;
    }

    
    private Coordinates getCoordinatesByWorkerId(long workerId) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedSelectCoordinatesByWorkerIdStatement = null;
        try {
            preparedSelectCoordinatesByWorkerIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_COORDINATES_BY_WORKER_ID, false);
            preparedSelectCoordinatesByWorkerIdStatement.setLong(1, workerId);
            ResultSet resultSet = preparedSelectCoordinatesByWorkerIdStatement.executeQuery();
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getInt(DatabaseHandler.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getInt(DatabaseHandler.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectCoordinatesByWorkerIdStatement);
        }
        return coordinates;
    }

    
    private Person getPersonByWorkerId(long workerId) throws SQLException {
        Person person;
        PreparedStatement preparedSelectPersonByWorkerIdStatement = null;
        try {
            preparedSelectPersonByWorkerIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_PERSON_BY_WORKER_ID, false);
                    preparedSelectPersonByWorkerIdStatement.setLong(1, workerId);
            ResultSet resultSet = preparedSelectPersonByWorkerIdStatement.executeQuery();
            if (resultSet.next()) {
                person = new Person(
                        resultSet.getTimestamp(DatabaseHandler.PERSON_TABLE_BIRTHDAY_COLUMN).toLocalDateTime(),
                        EyeColor.valueOf(resultSet.getString(DatabaseHandler.PERSON_TABLE_EYE_COLOR_COLUMN)),
                        Color.valueOf(resultSet.getString(DatabaseHandler.PERSON_TABLE_HAIR_COLOR_COLUMN)),
                        Country.valueOf(resultSet.getString(DatabaseHandler.PERSON_TABLE_COUNTRY_COLUMN)),
                        getLocationByWorkerId(workerId)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectPersonByWorkerIdStatement);
        }
        return person;
    }

    private Location getLocationByWorkerId(long workerId) throws SQLException {
        Location location;
        PreparedStatement preparedSelectLocationByWorkerIdStatement = null;
        try {
            preparedSelectLocationByWorkerIdStatement =
                    databaseHandler.getPreparedStatement(SELECT_LOCATION_BY_WORKER_ID, false);
            preparedSelectLocationByWorkerIdStatement.setLong(1, workerId);
            ResultSet resultSet = preparedSelectLocationByWorkerIdStatement.executeQuery();
            if (resultSet.next()) {
                location = new Location(
                        resultSet.getDouble(DatabaseHandler.LOCATION_TABLE_X_COLUMN),
                        resultSet.getLong(DatabaseHandler.LOCATION_TABLE_Y_COLUMN),
                        resultSet.getFloat(DatabaseHandler.LOCATION_TABLE_Z_COLUMN),
                        resultSet.getString(DatabaseHandler.LOCATION_TABLE_NAME_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectLocationByWorkerIdStatement);
        }
        return location;
    }

    
    public Worker insertWorker(WorkerObject wObject, User user) throws DatabaseHandlingException {
        Worker worker;
        PreparedStatement preparedInsertWorkerStatement = null;
        PreparedStatement preparedInsertCoordinatesStatement = null;
        PreparedStatement preparedInsertPersonStatement = null;
        PreparedStatement preparedInsertLocationStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            LocalDateTime creationTime = LocalDateTime.now();

            preparedInsertWorkerStatement = databaseHandler.getPreparedStatement(INSERT_WORKER, true);
            preparedInsertCoordinatesStatement = databaseHandler.getPreparedStatement(INSERT_COORDINATES, false);
            preparedInsertLocationStatement = databaseHandler.getPreparedStatement(INSERT_LOCATION, false);
            preparedInsertPersonStatement = databaseHandler.getPreparedStatement(INSERT_PERSON, false);

            preparedInsertWorkerStatement.setString(1, wObject.getName());
            preparedInsertWorkerStatement.setTimestamp(2, Timestamp.valueOf(creationTime));
            preparedInsertWorkerStatement.setLong(3, wObject.getSalary());
            preparedInsertWorkerStatement.setString(4, wObject.getPosition().toString().toUpperCase());
            preparedInsertWorkerStatement.setString(5, wObject.getStatus().toString().toUpperCase());
            preparedInsertWorkerStatement.setLong(6, databaseUserManager.getUserIdByUser(user));
            if (preparedInsertWorkerStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedWorkerKeys = preparedInsertWorkerStatement.getGeneratedKeys();
            long workerId;
            if (generatedWorkerKeys.next()) {
                workerId = generatedWorkerKeys.getLong(1);
            } else throw new SQLException();

            preparedInsertCoordinatesStatement.setLong(1,workerId);
            preparedInsertCoordinatesStatement.setLong(2, wObject.getCoordinates().getX());
            preparedInsertCoordinatesStatement.setDouble(3, wObject.getCoordinates().getY());
            if (preparedInsertCoordinatesStatement.executeUpdate() == 0) throw new SQLException();

            preparedInsertWorkerStatement.setLong(1, workerId);
            preparedInsertPersonStatement.setTimestamp(2, Timestamp.valueOf(wObject.getPerson().getBirthday()));
            preparedInsertPersonStatement.setString(3, wObject.getPerson().getEyeColor().toString().toUpperCase());
            preparedInsertPersonStatement.setString(4, wObject.getPerson().getHairColor().toString().toUpperCase());
            preparedInsertPersonStatement.setString(5, wObject.getPerson().getEyeColor().toString().toUpperCase());
            preparedInsertPersonStatement.setString(6, wObject.getPerson().getNationality().toString().toUpperCase());
            if (preparedInsertPersonStatement.executeUpdate() == 0) throw new SQLException();

            preparedInsertLocationStatement.setLong(1, workerId);
            preparedInsertLocationStatement.setString(2, wObject.getPerson().getLocation().getName());
            preparedInsertLocationStatement.setDouble(3, wObject.getPerson().getLocation().getX());
            preparedInsertLocationStatement.setLong(4, wObject.getPerson().getLocation().getY());
            preparedInsertLocationStatement.setFloat(5, wObject.getPerson().getLocation().getZ());
            if (preparedInsertLocationStatement.executeUpdate() == 0) throw new SQLException();

            worker = new Worker(
                    workerId,
                    wObject.getName(),
                    wObject.getCoordinates(),
                    creationTime,
                    wObject.getSalary(),
                    wObject.getPosition(),
                    wObject.getStatus(),
                    wObject.getPerson(),
                    user
            );

            databaseHandler.commit();
            return worker;
        } catch (SQLException exception) {
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedInsertWorkerStatement);
            databaseHandler.closePreparedStatement(preparedInsertCoordinatesStatement);
            databaseHandler.closePreparedStatement(preparedInsertLocationStatement);
            databaseHandler.closePreparedStatement(preparedInsertPersonStatement);
            databaseHandler.setNormalMode();
        }
    }

   
    public void updateWorkerById(long workerId, WorkerObject newWorker) throws DatabaseHandlingException {
        PreparedStatement preparedUpdateWorkerNameByIdStatement = null;
        PreparedStatement preparedUpdateWorkerSalaryByIdStatement = null;
        PreparedStatement preparedUpdateWorkerPositionByIdStatement = null;
        PreparedStatement preparedUpdateWorkerStatusByIdStatement = null;
        PreparedStatement preparedUpdateCoordinatesByWorkerIdStatement = null;
        PreparedStatement preparedUpdatePersonByWorkerIdStatement = null;
        PreparedStatement preparedUpdateLocationByWorkerIdStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            preparedUpdateWorkerNameByIdStatement = databaseHandler.getPreparedStatement(UPDATE_WORKER_NAME_BY_ID, false);
            preparedUpdateWorkerSalaryByIdStatement = databaseHandler.getPreparedStatement(UPDATE_WORKER_SALARY_BY_ID, false);
            preparedUpdateWorkerPositionByIdStatement = databaseHandler.getPreparedStatement(UPDATE_WORKER_POSITION_BY_ID, false);
            preparedUpdateWorkerStatusByIdStatement = databaseHandler.getPreparedStatement(UPDATE_WORKER_STATUS_BY_ID, false);
            preparedUpdateCoordinatesByWorkerIdStatement = databaseHandler.getPreparedStatement(UPDATE_COORDINATES_BY_LAB_ID, false);
            preparedUpdatePersonByWorkerIdStatement = databaseHandler.getPreparedStatement(UPDATE_PERSON_BY_WORKER_ID, false);
            preparedUpdateLocationByWorkerIdStatement = databaseHandler.getPreparedStatement(UPDATE_LOCATION_BY_WORKER_ID, false);

            if (newWorker.getName() != null) {
                preparedUpdateWorkerNameByIdStatement.setString(1, newWorker.getName());
                preparedUpdateWorkerNameByIdStatement.setLong(2, workerId);
                if (preparedUpdateWorkerNameByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (newWorker.getSalary() != -1) {
                preparedUpdateWorkerSalaryByIdStatement.setLong(1, newWorker.getSalary());
                preparedUpdateWorkerSalaryByIdStatement.setLong(2, workerId);
                if (preparedUpdateWorkerSalaryByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (newWorker.getPosition() != null) {
                preparedUpdateWorkerPositionByIdStatement.setString(1, newWorker.getPosition().toString());
                preparedUpdateWorkerPositionByIdStatement.setLong(2, workerId);
                if (preparedUpdateWorkerPositionByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (newWorker.getStatus() != null) {
                preparedUpdateWorkerStatusByIdStatement.setString(1, newWorker.getStatus().toString());
                preparedUpdateWorkerStatusByIdStatement.setLong(2, workerId);
                if (preparedUpdateWorkerStatusByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (newWorker.getCoordinates() != null) {
                preparedUpdateCoordinatesByWorkerIdStatement.setInt(1, newWorker.getCoordinates().getX());
                preparedUpdateCoordinatesByWorkerIdStatement.setInt(2, newWorker.getCoordinates().getY());
                preparedUpdateCoordinatesByWorkerIdStatement.setLong(3, workerId);
                if (preparedUpdateCoordinatesByWorkerIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (newWorker.getPerson() != null) {
                preparedUpdatePersonByWorkerIdStatement.setTimestamp(1, Timestamp.valueOf(newWorker.getPerson().getBirthday()));
                preparedUpdatePersonByWorkerIdStatement.setString(2, newWorker.getPerson().getEyeColor().toString());
                preparedUpdatePersonByWorkerIdStatement.setString(3, newWorker.getPerson().getHairColor().toString());
                preparedUpdatePersonByWorkerIdStatement.setString(4, newWorker.getPerson().getEyeColor().toString());
                preparedUpdatePersonByWorkerIdStatement.setString(5, newWorker.getPerson().getNationality().toString());
            if (preparedUpdatePersonByWorkerIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (newWorker.getPerson().getLocation() != null) {
                preparedUpdateLocationByWorkerIdStatement.setString(1, newWorker.getPerson().getLocation().getName());
                preparedUpdateLocationByWorkerIdStatement.setDouble(2, newWorker.getPerson().getLocation().getX());
                preparedUpdateLocationByWorkerIdStatement.setLong(3, newWorker.getPerson().getLocation().getY());
                preparedUpdateLocationByWorkerIdStatement.setFloat(4, newWorker.getPerson().getLocation().getZ());
                if (preparedUpdateLocationByWorkerIdStatement.executeUpdate() == 0) throw new SQLException();
            }

            databaseHandler.commit();
        } catch (SQLException exception) {
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedUpdateWorkerNameByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateWorkerSalaryByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateWorkerPositionByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateWorkerStatusByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateCoordinatesByWorkerIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdatePersonByWorkerIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateLocationByWorkerIdStatement);
            databaseHandler.setNormalMode();
        }
    }

    public void deleteWorkerById(long workerId) throws DatabaseHandlingException {
        PreparedStatement preparedDeleteWorkerByIdStatement = null;
        PreparedStatement preparedDeleteCoordinatesByWorkerIdStatement = null;
        PreparedStatement preparedDeletePersonByWorkerIdStatement = null;
        PreparedStatement preparedDeleteLocationByWorkerIdStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            preparedDeleteCoordinatesByWorkerIdStatement = databaseHandler.getPreparedStatement(DELETE_COORDINATES_BY_WORKER_ID, false);
            preparedDeleteCoordinatesByWorkerIdStatement.setLong(1, workerId);
            if (preparedDeleteCoordinatesByWorkerIdStatement.executeUpdate() == 0) throw new SQLException();

            preparedDeleteLocationByWorkerIdStatement = databaseHandler.getPreparedStatement(DELETE_LOCATION_BY_WORKER_ID, false);
            preparedDeleteLocationByWorkerIdStatement.setLong(1, workerId);
            if (preparedDeleteLocationByWorkerIdStatement.executeUpdate() == 0) throw new SQLException();

            preparedDeletePersonByWorkerIdStatement = databaseHandler.getPreparedStatement(DELETE_PERSON_BY_WORKER_ID, false);
            preparedDeletePersonByWorkerIdStatement.setLong(1, workerId);
            if (preparedDeletePersonByWorkerIdStatement.executeUpdate() == 0) throw new SQLException();

            preparedDeleteWorkerByIdStatement = databaseHandler.getPreparedStatement(DELETE_WORKER_BY_ID, false);
            preparedDeleteWorkerByIdStatement.setLong(1, workerId);
            if (preparedDeleteWorkerByIdStatement.executeUpdate() == 0) throw new SQLException();

            databaseHandler.commit();
        } catch (SQLException exception) {
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedDeleteWorkerByIdStatement);
            databaseHandler.closePreparedStatement(preparedDeleteCoordinatesByWorkerIdStatement);
            databaseHandler.closePreparedStatement(preparedDeletePersonByWorkerIdStatement);
            databaseHandler.closePreparedStatement(preparedDeleteLocationByWorkerIdStatement);
            databaseHandler.setNormalMode();
        }
    }

    
    public boolean checkWorkerUserId(long workerId, User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectWorkerByIdAndUserIdStatement = null;
        try {
            preparedSelectWorkerByIdAndUserIdStatement = databaseHandler.getPreparedStatement(SELECT_WORKER_BY_ID_AND_USER_ID, false);
            preparedSelectWorkerByIdAndUserIdStatement.setLong(1, workerId);
            preparedSelectWorkerByIdAndUserIdStatement.setLong(2, databaseUserManager.getUserIdByUser(user));
            ResultSet resultSet = preparedSelectWorkerByIdAndUserIdStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectWorkerByIdAndUserIdStatement);
        }
    }

    
    public void clearCollection() throws DatabaseHandlingException {
        Stack<Worker> workers = getCollection();
        for (Worker worker : workers) {
            deleteWorkerById(worker.getID());
        }
    }
}
