package com.litovchenko.carsapp.mySql;

import com.litovchenko.carsapp.dao.PassportDataDAO;
import com.litovchenko.carsapp.model.PassportData;

import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.litovchenko.carsapp.model.Constants.*;

public class MySqlPassportDataDAO extends MySqlGenericDAO<PassportData> implements PassportDataDAO {

    private static final String SQL_INSERT_QUERY;
    private static final String SQL_UPDATE_QUERY;
    private static final String SQL_DELETE_QUERY;
    private static final String SQL_SELECT_ALL_QUERY;
    private static final String SQL_SELECT_QUERY;

    static {
        SQL_SELECT_ALL_QUERY = "SELECT * FROM " + PASSPORT_DATA;
        SQL_SELECT_QUERY = SQL_SELECT_ALL_QUERY + " WHERE " + USER_ID + EQ_PARAM;
        SQL_DELETE_QUERY = "DELETE FROM " + PASSPORT_DATA + " WHERE " + USER_ID + EQ_PARAM;

        SQL_INSERT_QUERY = "INSERT INTO " + PASSPORT_DATA + "(" + USER_ID + COMA + PASSPORT_CODE + COMA +
                FIRST_NAME + COMA + MIDDLE_NAME + COMA + LAST_NAME + COMA + DATE_OF_BIRTH + COMA +
                PHONE + ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        SQL_UPDATE_QUERY = "UPDATE " + PASSPORT_DATA + " SET " + PASSPORT_CODE + EQ_COMA + FIRST_NAME +
                EQ_COMA + MIDDLE_NAME + EQ_COMA + LAST_NAME + EQ_COMA + DATE_OF_BIRTH + EQ_COMA +
                PHONE + EQ_PARAM + " WHERE " + USER_ID + EQ_PARAM;
    }

    public MySqlPassportDataDAO(Connection con) {
        super(con);
    }

    @Override
    public PassportData getByCode(String code) {
        List<PassportData> list;
        String sql = SQL_SELECT_ALL_QUERY + " WHERE " + PASSPORT_CODE + EQ_PARAM;
        list = getByStringParam(sql, code);
        if (list == null) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public PassportData getByUserLogin(String login) {
        List<PassportData> list;
        String sql = SQL_SELECT_ALL_QUERY + COMA + USERS + " WHERE " + ID + " = " + USER_ID +
                " AND " + LOGIN + EQ_PARAM;
        list = getByStringParam(sql, login);
        if (list == null) {
            return null;
        }
        return list.get(0);
    }

    @Override
    protected String getInsertQuery() {
        return SQL_INSERT_QUERY;
    }

    @Override
    protected String getDeleteQuery() {
        return SQL_DELETE_QUERY;
    }

    @Override
    protected String getSelectAllQuery() {
        return SQL_SELECT_ALL_QUERY;
    }

    @Override
    protected String getSelectQuery() {
        return SQL_SELECT_QUERY;
    }

    @Override
    protected String getUpdateQuery() {
        return SQL_UPDATE_QUERY;
    }

    @Override
    protected List<PassportData> parseResultSet(ResultSet rs) {
        List<PassportData> list = new ArrayList<>();
        try {
            while (rs.next()) {
                PassportData data = new PassportData(rs.getInt(USER_ID), rs.getString(PASSPORT_CODE),
                        rs.getString(FIRST_NAME), rs.getString(MIDDLE_NAME), rs.getString(LAST_NAME),
                        rs.getDate(DATE_OF_BIRTH), rs.getString(PHONE));
                list.add(data);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement st, PassportData object) {
        try {
            st.setInt(1, object.getId());
            st.setString(2, object.getPassportCode());
            st.setString(3, object.getFirstName());
            st.setString(4, object.getMiddleName());
            st.setString(5, object.getLastName());
            st.setDate(6, object.getDateOfBirth());
            st.setString(7, object.getPhone());
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement st, PassportData object) {
        try {
            st.setString(1, object.getPassportCode());
            st.setString(2, object.getFirstName());
            st.setString(3, object.getMiddleName());
            st.setString(4, object.getLastName());
            st.setDate(5, object.getDateOfBirth());
            st.setString(6, object.getPhone());
            st.setInt(7, object.getId());
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean insert(PassportData object){
        try (PreparedStatement pstm = con.prepareStatement(getInsertQuery())) {
            prepareStatementForInsert(pstm, object);
            if(pstm.executeUpdate() == 1){
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Cannot insert object" + line_sep + e);
            return false;
        }
        return false;
    }
}
