/**
 * This file is part of GoldenGate Project (named also GoldenGate or GG).
 * 
 * Copyright 2009, Frederic Bregier, and individual contributors by the @author
 * tags. See the COPYRIGHT.txt in the distribution for a full listing of
 * individual contributors.
 * 
 * All GoldenGate Project is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * GoldenGate is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GoldenGate . If not, see <http://www.gnu.org/licenses/>.
 */
package goldengate.common.database;

import goldengate.common.logging.GgInternalLogger;
import goldengate.common.logging.GgInternalLoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import goldengate.common.database.exception.GoldenGateDatabaseNoConnectionException;
import goldengate.common.database.exception.GoldenGateDatabaseSqlException;

/**
 * Class to handle PrepareStatement
 * 
 * @author Frederic Bregier
 * 
 */
public class DbPreparedStatement {
    /**
     * Internal Logger
     */
    private static final GgInternalLogger logger = GgInternalLoggerFactory
            .getLogger(DbPreparedStatement.class);

    /**
     * Internal PreparedStatement
     */
    private PreparedStatement preparedStatement = null;

    /**
     * The Associated request
     */
    private String request = null;

    /**
     * Is this PreparedStatement ready
     */
    public boolean isReady = false;

    /**
     * The associated resultSet
     */
    private ResultSet rs = null;

    /**
     * The associated DB session
     */
    private final DbSession ls;

    /**
     * Create a DbPreparedStatement from DbSession object
     * 
     * @param ls
     * @throws GoldenGateDatabaseNoConnectionException
     */
    public DbPreparedStatement(DbSession ls)
            throws GoldenGateDatabaseNoConnectionException {
        if (ls == null) {
            logger.error("SQL Exception PreparedStatement no session");
            throw new GoldenGateDatabaseNoConnectionException(
                    "PreparedStatement no session");
        }
        if (ls.isDisconnected) {
            ls.checkConnection();
        }
        this.ls = ls;
        rs = null;
        preparedStatement = null;
        isReady = false;
    }

    /**
     * Create a DbPreparedStatement from DbSession object and a request
     * 
     * @param ls
     * @param request
     * @throws GoldenGateDatabaseNoConnectionException
     * @throws GoldenGateDatabaseSqlException
     */
    public DbPreparedStatement(DbSession ls, String request)
            throws GoldenGateDatabaseNoConnectionException,
            GoldenGateDatabaseSqlException {
        if (ls == null) {
            logger.error("SQL Exception PreparedStatement no session");
            throw new GoldenGateDatabaseNoConnectionException(
                    "PreparedStatement no session");
        }
        if (ls.isDisconnected) {
            ls.checkConnection();
        }
        this.ls = ls;
        rs = null;
        isReady = false;
        preparedStatement = null;
        if (request == null) {
            logger.error("SQL Exception PreparedStatement no request");
            throw new GoldenGateDatabaseNoConnectionException(
                    "PreparedStatement no request");
        }
        try {
            preparedStatement = this.ls.conn.prepareStatement(request);
            this.request = request;
            isReady = true;
        } catch (SQLException e) {
            ls.checkConnection();
            try {
                preparedStatement = this.ls.conn.prepareStatement(request);
                this.request = request;
                isReady = true;
            } catch (SQLException e1) {
                logger.error("SQL Exception PreparedStatement: " + request +
                        "\n" + e.getMessage());
                DbSession.error(e);
                preparedStatement = null;
                isReady = false;
                throw new GoldenGateDatabaseSqlException(
                        "SQL Exception PreparedStatement", e);
            }
        }
    }

    /**
     * Create a DbPreparedStatement from DbSession object and a request
     * 
     * @param ls
     * @param request
     * @param nbFetch
     *            the number of pre fetch rows
     * @throws GoldenGateDatabaseNoConnectionException
     * @throws GoldenGateDatabaseSqlException
     */
    public DbPreparedStatement(DbSession ls, String request, int nbFetch)
            throws GoldenGateDatabaseNoConnectionException,
            GoldenGateDatabaseSqlException {
        if (ls == null) {
            logger.error("SQL Exception PreparedStatement no session");
            throw new GoldenGateDatabaseNoConnectionException(
                    "PreparedStatement no session");
        }
        if (ls.isDisconnected) {
            ls.checkConnection();
        }
        this.ls = ls;
        rs = null;
        isReady = false;
        preparedStatement = null;
        if (request == null) {
            logger.error("SQL Exception PreparedStatement no request");
            throw new GoldenGateDatabaseNoConnectionException(
                    "PreparedStatement no request");
        }
        try {
            preparedStatement = this.ls.conn.prepareStatement(request);
            this.request = request;
            this.preparedStatement.setFetchSize(nbFetch);
            isReady = true;
        } catch (SQLException e) {
            ls.checkConnection();
            try {
                preparedStatement = this.ls.conn.prepareStatement(request);
                this.request = request;
                this.preparedStatement.setFetchSize(nbFetch);
                isReady = true;
            } catch (SQLException e1) {
                logger.error("SQL Exception PreparedStatement: " + request +
                        "\n" + e.getMessage());
                DbSession.error(e);
                preparedStatement = null;
                isReady = false;
                throw new GoldenGateDatabaseSqlException(
                        "SQL Exception PreparedStatement", e);
            }
        }
    }

    /**
     * Create a preparedStatement from request
     * 
     * @param requestarg
     * @throws GoldenGateDatabaseNoConnectionException
     * @throws GoldenGateDatabaseSqlException
     */
    public void createPrepareStatement(String requestarg)
            throws GoldenGateDatabaseNoConnectionException,
            GoldenGateDatabaseSqlException {
        if (requestarg == null) {
            logger.error("createPreparedStatement no request");
            throw new GoldenGateDatabaseNoConnectionException(
                    "PreparedStatement no request");
        }
        if (preparedStatement != null) {
            realClose();
        }
        if (rs != null) {
            close();
        }
        if (ls.isDisconnected) {
            ls.checkConnection();
        }
        try {
            preparedStatement = ls.conn.prepareStatement(requestarg);
            request = requestarg;
            isReady = true;
        } catch (SQLException e) {
            ls.checkConnection();
            try {
                preparedStatement = ls.conn.prepareStatement(requestarg);
                request = requestarg;
                isReady = true;
            } catch (SQLException e1) {
                logger.error("SQL Exception createPreparedStatement:" +
                        requestarg + "\n" + e.getMessage());
                DbSession.error(e);
                realClose();
                preparedStatement = null;
                isReady = false;
                throw new GoldenGateDatabaseSqlException(
                        "SQL Exception createPreparedStatement: " + requestarg,
                        e);
            }
        }
    }

    /**
     * In case of closing database connection, it is possible to reopen a long
     * term preparedStatement as it was at creation.
     * 
     * @throws GoldenGateDatabaseSqlException
     * @throws GoldenGateDatabaseNoConnectionException
     */
    public void recreatePreparedStatement()
            throws GoldenGateDatabaseNoConnectionException,
            GoldenGateDatabaseSqlException {
        this.createPrepareStatement(request);
    }

    /**
     * Execute a Select preparedStatement
     * 
     * @throws GoldenGateDatabaseNoConnectionException
     * @throws GoldenGateDatabaseSqlException
     * 
     */
    public void executeQuery() throws GoldenGateDatabaseNoConnectionException,
            GoldenGateDatabaseSqlException {
        if (preparedStatement == null) {
            logger.error("executeQuery no request");
            throw new GoldenGateDatabaseNoConnectionException(
                    "executeQuery no request");
        }
        if (rs != null) {
            close();
        }
        if (ls.isDisconnected) {
            ls.checkConnection();
            throw new GoldenGateDatabaseSqlException(
                    "Request cannot be executed since connection was recreated between: " +
                            request);
        }
        try {
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.error("SQL Exception executeQuery:" + request + "\n" +
                    e.getMessage());
            DbSession.error(e);
            close();
            rs = null;
            ls.checkConnectionNoException();
            throw new GoldenGateDatabaseSqlException(
                    "SQL Exception executeQuery: " + request, e);
        }
    }

    /**
     * Execute the Update/Insert/Delete preparedStatement
     * 
     * @return the number of row
     * @throws GoldenGateDatabaseNoConnectionException
     * @throws GoldenGateDatabaseSqlException
     */
    public int executeUpdate() throws GoldenGateDatabaseNoConnectionException,
            GoldenGateDatabaseSqlException {
        if (preparedStatement == null) {
            logger.error("executeUpdate no request");
            throw new GoldenGateDatabaseNoConnectionException(
                    "executeUpdate no request");
        }
        if (rs != null) {
            close();
        }
        if (ls.isDisconnected) {
            ls.checkConnection();
            throw new GoldenGateDatabaseSqlException(
                    "Request cannot be executed since connection was recreated between:" +
                            request);
        }
        int retour = -1;
        try {
            retour = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQL Exception executeUpdate:" + request + "\n" +
                    e.getMessage());
            DbSession.error(e);
            ls.checkConnectionNoException();
            throw new GoldenGateDatabaseSqlException(
                    "SQL Exception executeUpdate: " + request, e);
        }
        return retour;
    }

    /**
     * Close the resultSet if any
     * 
     */
    public void close() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
            rs = null;
        }
    }

    /**
     * Really close the preparedStatement and the resultSet if any
     * 
     */
    public void realClose() {
        close();
        if (preparedStatement != null) {
            if (ls.isDisconnected) {
                ls.checkConnectionNoException();
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                ls.checkConnectionNoException();
            }
            preparedStatement = null;
        }
        isReady = false;
    }

    /**
     * Move the cursor to the next result
     * 
     * @return True if there is a next result, else False
     * @throws GoldenGateDatabaseNoConnectionException
     * @throws GoldenGateDatabaseSqlException
     */
    public boolean getNext() throws GoldenGateDatabaseNoConnectionException,
            GoldenGateDatabaseSqlException {
        if (rs == null) {
            logger.error("SQL ResultSet is Null into getNext");
            throw new GoldenGateDatabaseNoConnectionException(
                    "SQL ResultSet is Null into getNext");
        }
        if (ls.isDisconnected) {
            ls.checkConnection();
            throw new GoldenGateDatabaseSqlException(
                    "Request cannot be executed since connection was recreated between");
        }
        try {
            return rs.next();
        } catch (SQLException e) {
            logger.error("SQL Exception to getNextRow" + "\n" + e.getMessage());
            DbSession.error(e);
            ls.checkConnectionNoException();
            throw new GoldenGateDatabaseSqlException(
                    "SQL Exception to getNextRow: " + request, e);
        }
    }

    /**
     * 
     * @return The resultSet (can be used in conjunction of getNext())
     * @throws GoldenGateDatabaseNoConnectionException
     */
    public ResultSet getResultSet() throws GoldenGateDatabaseNoConnectionException {
        if (rs == null) {
            throw new GoldenGateDatabaseNoConnectionException(
                    "SQL ResultSet is Null into getResultSet");
        }
        return rs;
    }

    /**
     * 
     * @return The preparedStatement (should be used in conjunction of
     *         createPreparedStatement)
     * @throws GoldenGateDatabaseNoConnectionException
     */
    public PreparedStatement getPreparedStatement()
            throws GoldenGateDatabaseNoConnectionException {
        if (preparedStatement == null) {
            throw new GoldenGateDatabaseNoConnectionException(
                    "SQL PreparedStatement is Null into getPreparedStatement");
        }
        return preparedStatement;
    }

    /**
     * @return the dbSession
     */
    public DbSession getDbSession() {
        return ls;
    }

}
