package main.com.ete.commom;

public class Constants {

	public static final int DATA_ENTITY_COLUMN_COUNT = 2;
	public static final String DATASOURCE_NAME = "java:comp/env/jdbc/RGT_DS";
	public static final String DATABASE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String DATABASE_CONNECTION_STRING = "jdbc:oracle:thin:@localhost:1521/oracle";
	public static final String DATABASE_CONNECTION_USER_NAME = "rgtapp";
	public static final String DATABASE_CONNECTION_PASSWORD = "Rgt2017";
	public static final String METHOD_ENTRY_LOG = " Entering method.";
	public static final String METHOD_EXIT_LOG = " Exiting method.";
	public static final String TIMESTAMP_FORMAT = "dd/MMM/yyyy HH:mm:ss";
	public static final String ACCESS_CREATE = "CREATE";
	public static final String ACCESS_READ = "READ";
	public static final String ACCESS_UPDATE = "UPDATE";
	public static final String ACCESS_DELETE = "DELETE";
	public static final String MODEL_ORGANIZATION = "ORGANIZATION";
	public static final String MODEL_USER = "USER";
	public static final String MODEL_REQUIREMENT = "REQUIREMENT";
	public static final String MODEL_DATA_ENTITY = "DATA_ENTITY";
	public static final String YES = "YES";
	public static final String NO = "NO";
	public static final String RESPONSE_STATUS = "status";
	public static final String RESPONSE_MESSAGE = "message";
	public static final String RESPONSE_OBJECT = "object";
	public static final String RESPONSE_STATUS_SUCCESS = "Success";
	public static final String RESPONSE_STATUS_FAILURE = "Failure";
	public static final String CREATION_COMMENT = "Created initial version";
	public static final String CREATION_INVALID_OBJECT_EXCEPTION_MESSAGE = "Required parameters for creation are missing.";
	public static final String UPDATE_INVALID_OBJECT_EXCEPTION_MESSAGE = "Required parameters for update are missing.";
	public static final String DELETION_INVALID_OBJECT_EXCEPTION_MESSAGE = "Required parameters for deletion are missing.";
	public static final String CREATION_COMMENT_UNSUCCESSFUL = "Creation Failed.";
	public static final String UPDATE_COMMENT_UNSUCCESSFUL = "Update Failed.";
	public static final String DELETION_COMMENT_UNSUCCESSFUL = "Deletion Failed.";
	public static final String INVALID_ACCESS_USER_ID_EXCEPTION_MESSAGE = "Invalid Access User Id ";
}
