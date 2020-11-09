package com.cp3.cloud.database.parsers;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.alibaba.druid.sql.ast.expr.SQLExistsExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 替换SQL
 *
 * @author cp3
 * @date 2020/5/8 上午11:43
 */
@Slf4j
public class ReplaceSql {

    public static String replaceSql(String schemaName, String sql) {
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement sqlStatement = parser.parseStatement();
        if (sqlStatement instanceof SQLSelectStatement) {
            SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) sqlStatement;
            SQLSelectQuery sqlSelectQuery = sqlSelectStatement.getSelect().getQuery();
            setSQLSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlStatement instanceof SQLUpdateStatement) {
            SQLUpdateStatement sqlUpdateStatement = (SQLUpdateStatement) sqlStatement;
            SQLTableSource sqlTableSource = sqlUpdateStatement.getTableSource();
            setSQLSchemaBySqlTableSource(schemaName, sqlTableSource);
            SQLExpr where = sqlUpdateStatement.getWhere();
            setSQLSchemaBySqlExpr(schemaName, where);
        }
        if (sqlStatement instanceof SQLInsertStatement) {
            SQLInsertStatement sqlInsertStatement = (SQLInsertStatement) sqlStatement;
            SQLExprTableSource tableSource = sqlInsertStatement.getTableSource();
            setSQLSchemaBySqlTableSource(schemaName, tableSource);
        }
        if (sqlStatement instanceof SQLDeleteStatement) {
            SQLDeleteStatement sqlDeleteStatement = (SQLDeleteStatement) sqlStatement;
            SQLTableSource tableSource = sqlDeleteStatement.getTableSource();
            setSQLSchemaBySqlTableSource(schemaName, tableSource);
            SQLExpr where = sqlDeleteStatement.getWhere();
            setSQLSchemaBySqlExpr(schemaName, where);
        }
        if (sqlStatement instanceof SQLCreateStatement) {
            SQLCreateTableStatement sqlCreateStatement = (SQLCreateTableStatement) sqlStatement;
            SQLExprTableSource tableSource = sqlCreateStatement.getTableSource();
            setSQLSchemaBySqlTableSource(schemaName, tableSource);
        }
        if (sqlStatement instanceof SQLCallStatement) {
            log.info("执行到 存储过程 这里了");
            SQLCallStatement sqlCallStatement = (SQLCallStatement) sqlStatement;
            SQLName expr = sqlCallStatement.getProcedureName();
            if (expr instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr procedureName = (SQLIdentifierExpr) expr;
                sqlCallStatement.setProcedureName(new SQLPropertyExpr(schemaName, procedureName.getName()));
            } else if (expr instanceof SQLPropertyExpr) {
                SQLPropertyExpr procedureName = (SQLPropertyExpr) expr;
                sqlCallStatement.setProcedureName(new SQLPropertyExpr(schemaName, procedureName.getName()));
            }
        }
        return sqlStatement.toString();
    }

    private static void setSQLSchemaBySqlTableSource(String schemaName, SQLTableSource sqlTableSource) {
        if (sqlTableSource instanceof SQLJoinTableSource) {
            SQLJoinTableSource sqlJoinTableSource = (SQLJoinTableSource) sqlTableSource;
            SQLTableSource sqlTableSourceLeft = sqlJoinTableSource.getLeft();
            setSQLSchemaBySqlTableSource(schemaName, sqlTableSourceLeft);
            SQLTableSource sqlTableSourceRight = sqlJoinTableSource.getRight();
            setSQLSchemaBySqlTableSource(schemaName, sqlTableSourceRight);
            SQLExpr condition = sqlJoinTableSource.getCondition();
            setSQLSchemaBySqlExpr(schemaName, condition);
        }
        if (sqlTableSource instanceof SQLSubqueryTableSource) {
            SQLSubqueryTableSource sqlSubqueryTableSource = (SQLSubqueryTableSource) sqlTableSource;
            SQLSelectQuery sqlSelectQuery = sqlSubqueryTableSource.getSelect().getQuery();
            setSQLSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlTableSource instanceof SQLUnionQueryTableSource) {
            SQLUnionQueryTableSource sqlUnionQueryTableSource = (SQLUnionQueryTableSource) sqlTableSource;
            SQLSelectQuery sqlSelectQueryLeft = sqlUnionQueryTableSource.getUnion().getLeft();
            setSQLSchemaBySelectQuery(schemaName, sqlSelectQueryLeft);
            SQLSelectQuery sqlSelectQueryRight = sqlUnionQueryTableSource.getUnion().getRight();
            setSQLSchemaBySelectQuery(schemaName, sqlSelectQueryRight);
        }
        if (sqlTableSource instanceof SQLExprTableSource) {
            SQLExprTableSource sqlExprTableSource = (SQLExprTableSource) sqlTableSource;
            SQLObject sqlObject = sqlExprTableSource.getParent();
            if (sqlObject instanceof MySqlDeleteStatement) {
                MySqlDeleteStatement mySqlDeleteStatement = (MySqlDeleteStatement) sqlObject;
                SQLExpr sqlExpr = mySqlDeleteStatement.getWhere();
                setSQLSchemaBySqlExpr(schemaName, sqlExpr);
            }
            if (sqlObject instanceof MySqlInsertStatement) {
                MySqlInsertStatement mySqlInsertStatement = (MySqlInsertStatement) sqlObject;
                SQLSelect sqlSelect = mySqlInsertStatement.getQuery();
                if (sqlSelect != null) {
                    SQLSelectQuery sqlSelectQuery = sqlSelect.getQuery();
                    setSQLSchemaBySelectQuery(schemaName, sqlSelectQuery);
                }
            }
            sqlExprTableSource.setSchema(schemaName);
        }
    }

    private static void setSQLSchemaBySqlBinaryExpr(String schemaName, SQLBinaryOpExpr sqlBinaryOpExpr) {
        SQLExpr sqlExprLeft = sqlBinaryOpExpr.getLeft();
        setSQLSchemaBySqlExpr(schemaName, sqlExprLeft);
        SQLExpr sqlExprRight = sqlBinaryOpExpr.getRight();
        setSQLSchemaBySqlExpr(schemaName, sqlExprRight);
    }

    private static void setSQLSchemaBySqlExpr(String schemaName, SQLExpr sqlExpr) {
        if (sqlExpr instanceof SQLInSubQueryExpr) {
            SQLInSubQueryExpr sqlInSubQueryExpr = (SQLInSubQueryExpr) sqlExpr;
            SQLSelectQuery sqlSelectQuery = sqlInSubQueryExpr.getSubQuery().getQuery();
            setSQLSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlExpr instanceof SQLExistsExpr) {
            SQLExistsExpr sqlExistsExpr = (SQLExistsExpr) sqlExpr;
            SQLSelectQuery sqlSelectQuery = sqlExistsExpr.getSubQuery().getQuery();
            setSQLSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlExpr instanceof SQLCaseExpr) {
            SQLCaseExpr sqlCaseExpr = (SQLCaseExpr) sqlExpr;
            List<SQLCaseExpr.Item> sqlCaseExprItemList = sqlCaseExpr.getItems();
            for (SQLCaseExpr.Item item : sqlCaseExprItemList) {
                SQLExpr sqlExprItem = item.getValueExpr();
                setSQLSchemaBySqlExpr(schemaName, sqlExprItem);
            }
        }
        if (sqlExpr instanceof SQLQueryExpr) {
            SQLQueryExpr sqlQueryExpr = (SQLQueryExpr) sqlExpr;
            SQLSelectQuery sqlSelectQuery = sqlQueryExpr.getSubQuery().getQuery();
            setSQLSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlExpr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) sqlExpr;
            setSQLSchemaBySqlBinaryExpr(schemaName, sqlBinaryOpExpr);
        }
        if (sqlExpr instanceof SQLAggregateExpr) {
            SQLAggregateExpr sqlAggregateExpr = (SQLAggregateExpr) sqlExpr;
            List<SQLExpr> arguments = sqlAggregateExpr.getArguments();
            for (SQLExpr argument : arguments) {
                setSQLSchemaBySqlExpr(schemaName, argument);
            }
        }
    }

    private static void setSQLSchemaBySelectQuery(String schemaName, SQLSelectQuery sqlSelectQuery) {
        if (sqlSelectQuery instanceof SQLUnionQuery) {
            SQLUnionQuery sqlUnionQuery = (SQLUnionQuery) sqlSelectQuery;
            SQLSelectQuery sqlSelectQueryLeft = sqlUnionQuery.getLeft();
            setSQLSchemaBySelectQuery(schemaName, sqlSelectQueryLeft);
            SQLSelectQuery sqlSelectQueryRight = sqlUnionQuery.getRight();
            setSQLSchemaBySelectQuery(schemaName, sqlSelectQueryRight);
        }
        if (sqlSelectQuery instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) sqlSelectQuery;
            SQLTableSource sqlTableSource = sqlSelectQueryBlock.getFrom();
            setSQLSchemaBySqlTableSource(schemaName, sqlTableSource);
            SQLExpr whereSqlExpr = sqlSelectQueryBlock.getWhere();
            if (whereSqlExpr instanceof SQLInSubQueryExpr) {
                SQLInSubQueryExpr sqlInSubQueryExpr = (SQLInSubQueryExpr) whereSqlExpr;
                SQLSelectQuery sqlSelectQueryIn = sqlInSubQueryExpr.getSubQuery().getQuery();
                setSQLSchemaBySelectQuery(schemaName, sqlSelectQueryIn);
            }
            if (whereSqlExpr instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) whereSqlExpr;
                setSQLSchemaBySqlBinaryExpr(schemaName, sqlBinaryOpExpr);
            }
            List<SQLSelectItem> sqlSelectItemList = sqlSelectQueryBlock.getSelectList();
            for (SQLSelectItem sqlSelectItem : sqlSelectItemList) {
                SQLExpr sqlExpr = sqlSelectItem.getExpr();
                setSQLSchemaBySqlExpr(schemaName, sqlExpr);

                //函数
                if (sqlExpr instanceof SQLMethodInvokeExpr) {
                    if (sqlSelectQuery instanceof SQLSelectQueryBlock && ((SQLSelectQueryBlock) sqlSelectQuery).getFrom() == null) {
                        log.info("执行到 函数 这里了");
                        ((SQLMethodInvokeExpr) sqlExpr).setOwner(new SQLIdentifierExpr(schemaName));
                    }
                }
            }
        }
    }
}
