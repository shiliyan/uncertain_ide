package aurora.ide.editor.textpage.format.sqlformat;

import java.util.Arrays;

public class TokenUtil {
	public static final String NEW_LINE_SYSTEM = System.getProperty("line.separator");

	public static final String[] NEW_LINES = { "\r\n", "\r", "\n" };
	
	public static final String NEW_LINES_REGEX;
	public static final char[] NEW_LINE_CHAR;

	public static final int TYPE_BEGIN_SQL = 0;
	public static final int TYPE_KEYWORD = 10; 
	public static final int TYPE_SYMBOL = 20; 
	public static final int TYPE_OPERATOR = 30;
	public static final int TYPE_NAME = 40;
	public static final int TYPE_VALUE = 50; 
	public static final int TYPE_COMMENT = 60; 
	public static final int TYPE_NEW_LINE = 70; 
	public static final int TYPE_SQL_SEPARATE = 80; 
	public static final int TYPE_EMPTY_LINE = 90; 
	public static final int TYPE_END_SQL = 100;
	public static final int TYPE_UNKNOWN = -1; 

	public static final int SUBTYPE_DEFAULT = 0; 
	public static final int SUBTYPE_KEYWORD_DATATYPE = 11; 
	public static final int SUBTYPE_KEYWORD_FUNCTION = 12; 
	public static final int SUBTYPE_VALUE_STRING = 51;
	public static final int SUBTYPE_VALUE_NUMERIC = 52; 
	public static final int SUBTYPE_VALUE_BIND = 53; 
	public static final int SUBTYPE_COMMENT_SINGLE = 61; 
	public static final int SUBTYPE_COMMENT_MULTI = 62; 

	public static final String[] KEYWORD = { "ACCESS", "ADD", "ALL", "ALTER", "AND", "ANY", "AS", "ASC", "AUDIT", "BETWEEN", "BEGIN", "BOTH", "BY", "CACHE", "CASCADE", "CASE", "CHAR", "CHECK", "CLUSTER", "COLUMN", "COMMENT", "COMMIT", "COMPRESS", "CONNECT", "CONSTRAINT", "CREATE", "CROSS", "CURRENT", "CYCLE", "DATE", "DECIMAL", "DECLARE", "DEFAULT", "DELETE", "DESC", "DISTINCT", "DROP", "ELSE", "END", "ESCAPE", "EXCEPT", "EXCLUSIVE", "EXISTS", "FILE", "FLOAT", "FUNCTION", "FOR", "FOREIGN", "FROM", "GRANT", "GROUP", "HAVING", "IDENTIFIED", "IF", "IMMEDIATE", "IN", "INCREMENT", "INDEX", "INITIAL", "INNER", "INSERT", "INTEGER", "INTERSECT", "INTO", "IS", "JOIN", "KEY", "LEADING", "LEVEL", "LEFT", "LIKE", "LOCK", "LONG", "MERGE", "MATCH", "MATCHED", "MAXEXTENTS", "MAXVALUE", "MINUS",
			"MINVALUE", "MLSLABEL", "MODE", "MODIFY", "NATURAL", "NOAUDIT", "NOCOMPRESS", "NOCYCLE", "NOMAXVALUE", "NOMINVALUE", "NOT", "NOWAIT", "NULL", "NUMBER", "OF", "OFFLINE", "ON", "ONLINE", "ONLY", "OPTION", "OR", "ORDER", "OUTER", "OVER", "PACKAGE", "PARTITION", "PCTFREE", "PRIMARY", "PRIOR", "PRIVILEGES", "PROCEDURE", "PUBLIC", "RAW", "READ", "RENAME", "RESOURCE", "RETURN", "REVOKE", "RIGHT", "ROLLBACK", "ROW", "ROWID", "ROWNUM", "ROWS", "SCHEMA", "SELECT", "SEQUENCE", "SET", "SHARE", "SIZE", "SMALLINT", "SHOW", "START", "SUCCESSFUL", "SYNONYM", "SYSDATE", "TABLE", "TEMPORARY", "THEN", "TIME", "TIMESTAMP", "TO", "TRAILING", "TRIGGER", "TRUNCATE", "TYPE", "UID", "UNION", "UNIQUE", "UPDATE", "USER", "USING", "VALIDATE", "VALUES", "VARCHAR", "VARCHAR2", "VIEW",
			"WHENEVER", "WHEN", "WHERE", "WITH" };

	public static final String[] KEYWORD_DATATYPE = { "BFILE", "BINARY_DOUBLE", "BINARY_FLOAT", "BLOB", "CHAR", "CHARACTER", "CHAR VARYING", "CHARACTER VARYING", "CLOB", "DATE", "DEC", "DECIMAL", "DOUBLE PRECISION", "INTERVAL YEAR TO MONTH", "INT", "INTEGER", "INTERVAL", "INTERVAL DAY TO SECOND", "LONG", "LONG RAW", "NATIONAL CHAR", "NATIONAL CHARACTER", "NATIONAL CHARACTER VARYING", "NATIONAL CHAR VARYING", "NCHAR", "NCHAR VARYING", "NUMBER", "NUMERIC", "NVARCHAR2", "RAW", "REAL", "ROWID", "SMALLINT", "TIME", "TIMESTAMP", "TIMESTAMP WITH LOCAL TIMEZONE", "TIMESTAMP WITH TIMEZONE", "VARCHAR", "VARCHAR2" };

	public static final String[] KEYWORD_FUNCTION = { "ABS", "ACOS", "ADD_MONTHS", "ASCII", "ASIN", "ATAN", "AVG", "CEIL", "CHARTOROWID", "CHECK", "CHR", "COALESCE", "CONCAT", "CONVERT", "COS", "COSH", "COUNT", "DECODE", "DUMP", "EXP", "FLOOR", "GREATEST", "HEXTORAW", "INITCAP", "INSTR", "INSTRB", "LAST_DAY", "LEAST", "LENGTH", "LENGTHB", "LN", "LOG", "LOWER", "LPAD", "LTRIM", "MAX", "MIN", "MOD", "MONTHS_BETWEEN", "NEXT_DAY", "NULLIF", "NVL", "NVL2", "POWER", "RAWTOHEX", "REPLACE", "ROUND", "ROWIDTOCHAR", "ROW_NUMBER", "RPAD", "RTRIM", "SIGN", "SIN", "SINH", "SQRT", "STDDEV", "SUBSTR", "SUBSTRB", "SUM", "SYSDATE", "TAN", "TANH", "TO_CHAR", "TO_DATE", "TO_MULTI_BYTE", "TO_NUMBER", "TO_SINGLE_BYTE", "TRIM", "TRUNC", "UID", "UPPER", "USER", "USERENV", "VARIANCE", "VSIZE" };

	public static final String[] BEGIN_SQL_KEYWORD = { "ALTER", "COMMENT", "CREATE", "DELETE", "DROP", "GRANT", "INSERT", "MARGE", "REVOKE", "SELECT", "TRUNCATE", "UPDATE" };

	public static final String[] MULTI_KEYWORD = { "CREATE OR REPLACE", "CREATE", "CROSS JOIN", "COMMENT ON", "FOR UPDATE", "FULL JOIN", "FULL OUTER JOIN", "GROUP BY", "INCREMENT BY", "INNER JOIN", "JOIN", "LEFT JOIN", "LEFT OUTER JOIN", "NATURAL JOIN", "ORDER BY", "PARTITION BY", "RIGHT JOIN", "RIGHT OUTER JOIN", "START WITH", "UNION ALL", "WHEN MATCHED THEN", "WHEN NOT MATCHED THEN", "WITH CHECK OPTION", "WITH READ ONLY" };

	public static final String[] SPECIAL_VALUE = { "NULL", "SYSDATE" };

	public static final String[] SYMBOL = { "(", ")", "||", ".", ",", "::" };
	public static final char[] SYMBOL_CHAR;

	public static final String[] OPERATOR = { "!=", "*", "+", "-", "/", "<", "<=", "<>", "=", ">", ">=", "^=" };
	public static final char[] OPERATOR_CHAR;

	public static final char[] BIND_VARIABLE = { ':', '?' };

	public static final String[] COMMENT = { "--", "/*", "*/" };

	public static final char[] WORD_SEPARATE = { ' ', '\t' };

	public static final char[] SQL_SEPARATE = { '/', ';' };

	static {
		SYMBOL_CHAR = getCharTable(SYMBOL).toCharArray();
		OPERATOR_CHAR = getCharTable(OPERATOR).toCharArray();
		NEW_LINE_CHAR = getCharTable(NEW_LINES).toCharArray();

		Arrays.sort(KEYWORD);
		Arrays.sort(KEYWORD_FUNCTION);
		Arrays.sort(KEYWORD_DATATYPE);
		Arrays.sort(BEGIN_SQL_KEYWORD);
		Arrays.sort(MULTI_KEYWORD);
		Arrays.sort(SPECIAL_VALUE);
		Arrays.sort(SYMBOL);
		Arrays.sort(SYMBOL_CHAR);
		Arrays.sort(BIND_VARIABLE);
		Arrays.sort(OPERATOR);
		Arrays.sort(OPERATOR_CHAR);
		Arrays.sort(COMMENT);
		Arrays.sort(WORD_SEPARATE);
		Arrays.sort(SQL_SEPARATE);
		Arrays.sort(NEW_LINE_CHAR);

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < NEW_LINES.length; i++) {
			if (i != 0)
				sb.append('|');
			sb.append(NEW_LINES[i]);
		}
		NEW_LINES_REGEX = sb.toString();
	};

	private static String getCharTable(String[] strs) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];
			int len = strs[i].length();
			for (int j = 0; j < len; j++) {
				char c = str.charAt(j);
				if (sb.indexOf(Character.toString(c)) == -1)
					sb.append(c);
			}
		}
		return sb.toString();
	}

	public static boolean isBeginSqlKeyword(String str) {
		return (Arrays.binarySearch(BEGIN_SQL_KEYWORD, str) >= 0);
	}

	public static boolean isMultiKeyword(String str) {
		return (Arrays.binarySearch(MULTI_KEYWORD, str) >= 0);
	}

	public static boolean isSpecialValue(String str) {
		return (Arrays.binarySearch(SPECIAL_VALUE, str) >= 0);
	}

	public static boolean isSymbol(String str) {
		return (Arrays.binarySearch(SYMBOL, str) >= 0);
	}

	public static boolean isBindVariable(char c) {
		return (Arrays.binarySearch(BIND_VARIABLE, c) >= 0);
	}

	public static boolean isValue(String str) {
		if (str == null)
			return false;
		return (str.startsWith("\"") && str.endsWith("\""));
	}

	public static boolean isComment(String str) {
		return (Arrays.binarySearch(COMMENT, str) >= 0);
	}

	public static boolean isWordSeparate(char c) {
		return (Arrays.binarySearch(WORD_SEPARATE, c) >= 0);
	}

	public static boolean isSqlSeparate(char c) {
		return (Arrays.binarySearch(SQL_SEPARATE, c) >= 0);
	}

	public static boolean isNameChar(char c) {
		if (Character.isLetterOrDigit(c))
			return true;
		return (c == '_' || c == '$' || c == '#');
	}

	public static boolean isNumberChar(char c) {
		if (Character.isDigit(c))
			return true;
		switch (c) {
		case '.':
		case '+':
		case '-':
		case 'd':
		case 'D':
		case 'e':
		case 'E':
		case 'f':
		case 'F':
			return true;

		default:
			return false;
		}
	}

	public static boolean isSymbolChar(char c) {
		return (Arrays.binarySearch(SYMBOL_CHAR, c) >= 0);
	}

	public static boolean isOperator(String str) {
		return (Arrays.binarySearch(OPERATOR, str) >= 0);
	}

	public static boolean isOperatorChar(char c) {
		return (Arrays.binarySearch(OPERATOR_CHAR, c) >= 0);
	}

	public static boolean isNewLineChar(char c) {
		return (Arrays.binarySearch(NEW_LINE_CHAR, c) >= 0);
	}

	public static boolean isValidToken(Token token) {
		if (token == null)
			return false;

		switch (token.getType()) {
		case TYPE_KEYWORD:
		case TYPE_NAME:
		case TYPE_OPERATOR:
		case TYPE_SYMBOL:
		case TYPE_VALUE:
		case TYPE_SQL_SEPARATE:
			return true;
		default:
			return false;
		}
	}
}
