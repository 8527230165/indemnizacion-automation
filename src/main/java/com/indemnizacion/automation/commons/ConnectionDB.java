package com.indemnizacion.automation.commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.indemnizacion.automation.utils.DataUtil;
import com.indemnizacion.automation.utils.PropertyManager;

public class ConnectionDB {

	public static Connection myConnection;
	public static Statement sqlStatement;
	public static ResultSet myResultSet;
	
	

	public void DesConnectionx() throws SQLException {
		myConnection.close();
		System.out.println("********Base De Datos Desconecda***********");
	}

	public static void Konnection() {

		try {
			String dbURL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.1.6.110)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=SBRPAQA)))";
			String strUserID = "BOLAUTOMATION";
			String strPassword = "BOLAUTOMATION";
			Class.forName("oracle.jdbc.OracleDriver").newInstance();
			myConnection = DriverManager.getConnection(dbURL, strUserID, strPassword);
			sqlStatement = myConnection.createStatement();
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String InputDataExtraction(String table, String suite, String casex, String campo) {
		String val = null;
		try {

			String readRecordSQL = "select * from " + table + " where rownum<=10";
				
			myResultSet = sqlStatement.executeQuery(readRecordSQL);
			 //System.out.println(myResultSet.getNString(campo));
			while (myResultSet.next()) {
				val = myResultSet.getString(campo);
				 System.out.println(val);
			}
			// System.out.println("El valor que trajo myResultSet"+val);
			myResultSet.close();
			// myConnection.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return val;

	}

	public String MaxIdintfac(String sateliteMax) {
		String val = null;
		try {

			String readRecordSQL = "select  max(idintfac) as idintfac from aut_data_service_post_fe where SATELITE= '"
					+ sateliteMax + "'";
			System.out.println("eL QUERY DEL MAXIMO ID_FACTURA:  " + readRecordSQL);
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			System.out.println(myResultSet);

			while (myResultSet.next()) {
				val = myResultSet.getNString("idintfac");
				System.out.println("Maximo IDINTFAC: " + val);
			}
			myResultSet.close();
			// myConnection.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return val;

	}

	public String MaxIdExecutionServiceFE() {
		String val = null;
		try {

			String readRecordSQL = "select  max(ID_AUT_EXECUTION_SERVICE_FE) as ID_AUT_EXECUTION_SERVICE_FE from aut_execution_service_fe";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			System.out.println(myResultSet);

			while (myResultSet.next()) {
				val = myResultSet.getNString("ID_AUT_EXECUTION_SERVICE_FE");
				System.out.println("Maximo ID_AUT_EXECUTION_SERVICE_FE: " + val);
			}
			myResultSet.close();
			// myConnection.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return val;

	}

	public static String MaxIdExecutionGlobal() {
		String val = null;
		try {

			String readRecordSQL = "select nvl(max(ID),0) as ID from AUT_EXECUTION_INDEMNIZACION";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			//System.out.println(myResultSet);

			while (myResultSet.next()) {
				val = myResultSet.getNString("ID");
				//System.out.println("Maximo id_aut_execution: " + val);
			}
			myResultSet.close();
			// myConnection.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return val;

	}

	public static void InsertAutExecutionInicial(String request_type,String request,String response,String result,String comment, String date) {
		
		int id_aut_aplication=Integer.parseInt(PropertyManager.getConfigValueByKey("id_aut_aplication"));
		try {

			String readRecordSQL = "   insert INTO AUT_EXECUTION_INDEMNIZACION (ID,REQUEST_TYPE,REQUEST,RESPONSE,RESULT,COMMENTARIO,ID_AUT_APLICATION,CREATED_ON)\n"
					+ "    VALUES (SEQ_INDEMNIZACION.Nextval,'" + request_type + "','" + request + "','"
					+ response +"','"+result+"','"+comment+"','"+id_aut_aplication+"','"+date+ "')";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			myResultSet.close();


		} catch (Exception e) {
			System.out.println("Error de insercion sobre la tabla: aut_execution ");
			e.printStackTrace();
		}
		

	}

	public void UpdateAutExecutionFinal(int idAutExecution, String outcome) {
		String val = null;

		try {

			String readRecordSQL = "update aut_execution set status='3', outcome=" + outcome
					+ ", end_date= CURRENT_TIMESTAMP where id_aut_execution = " + idAutExecution + "";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			System.out.println(myResultSet);
			System.out.println("Update  final Correcta sobre la tabla : aut_execution ");
			myResultSet.close();

		} catch (Exception e) {
			System.out.println("Error de Update final sobre la tabla: aut_execution ");
		}

	}

	public int InsertAutExecutionServiceFEInicial(String suite, String casex, String ID_AUT_AUTOMATION,
			String STATUSCODE, int ID_AUT_EXECUTION, String RESPONSE, String SUIITE_NAME, String CASE_NAME) {
		String val = null;

		String maxIdExecutionServiceFE = MaxIdExecutionServiceFE();
		int intMaxIdExecutionServiceFEPlus = 1 + Integer.parseInt(maxIdExecutionServiceFE);
		System.out.println("EL maximo de los id_FE: " + intMaxIdExecutionServiceFEPlus);

		try {

			String readRecordSQL = "Insert INTO aut_execution_service_fe (ID_AUT_EXECUTION_SERVICE_FE,ID_SUITE,ID_CASE,ID_AUT_AUTOMATION,STATUSCODE,ID_AUT_EXECUTION,RESPONSE,SUITE_NAME,CASE_NAME)\n"
					+ "values (" + intMaxIdExecutionServiceFEPlus + "," + suite + "," + casex + "," + ID_AUT_AUTOMATION
					+ ",'0'," + ID_AUT_EXECUTION + ",'rESPONSE nULL iNICIAL'," + SUIITE_NAME + "," + CASE_NAME + ")";
			System.out.println("EL Insert del query inicial FE: " + readRecordSQL);

			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			System.out.println(myResultSet);
			System.out.println("insercion Correcta sobre la tabla: aut_execution_service_fe Ok");
			myResultSet.close();

		} catch (Exception e) {
			System.out.println("Error de insercion sobre la tabla: aut_execution_service_fe ");
		}
		return intMaxIdExecutionServiceFEPlus;

	}

	public void UpdateAutExecutionServiceFEFinal(int idAutExecutionServiceFE, int statusCode, String response) {
		String val = null;

		try {

			String readRecordSQL = " update aut_execution_service_fe set STATUSCODE ='" + statusCode + "',RESPONSE='"
					+ response + "'WHERE ID_AUT_EXECUTION_SERVICE_FE = " + idAutExecutionServiceFE + "";
			System.out.println("El Update: " + readRecordSQL);
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			System.out.println("El Update: " + readRecordSQL);
			System.out.println(myResultSet);

			System.out.println("Update final Correcto sobre la tabla : aut_execution_service_fe ");
			myResultSet.close();

		} catch (Exception e) {
			System.out.println("Error de Actualizacion final sobre la tabla: aut_execution_service_fe ");
		}

	}

	public void UpdateIdFactDataInsumo(String table, String suite, String casex, String idFact) {
		String val = null;

		try {

			String readRecordSQL = "update " + table + " set idintfac= '" + idFact + "' where id_suite= '" + suite
					+ "'  and id_case= '" + casex + "' ";
			System.out.println("?????????????????????????????????????? EL query de idefac insumo : " + readRecordSQL);
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			System.out.println(myResultSet);
			System.out.println("Update  final Correcta sobre IDINTFAC DE LA tabla : " + table + " ");
			myResultSet.close();

		} catch (Exception e) {
			System.out.println("Update  final FALLO sobre IDINTFAC DE LA tabla: " + table + " ");
		}

	}

	public void idIntFacPlus(String idIntFacPlus) {

		int entero = Integer.parseInt(idIntFacPlus);
		String idIntFacPlusx = String.valueOf(entero + 1);
		UpdateIdFactDataInsumo("aut_data_service_post_fe", "1", "1", idIntFacPlusx);

	}



}
