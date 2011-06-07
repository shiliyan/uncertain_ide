package bm;

import helpers.ApplicationException;
import helpers.DBConnectionUtil;
import helpers.LocaleMessage;
import helpers.SystemException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;


public class AuroraDataBase implements IRunnableWithProgress {
	private Connection connection;
	private IProject project;
	private ApplicationException runtiemException ;
	public AuroraDataBase(IProject project) {
		this.project = project;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		monitor.beginTask(
				LocaleMessage.getString("try.to.get.database.connection.please.wait"),
				IProgressMonitor.UNKNOWN);
		try {
			connection = DBConnectionUtil.getDBConnection(project);
		} catch (ApplicationException e) {
			runtiemException = e;
		}
		monitor.done();

	}
	public Connection getDBConnection() throws ApplicationException{
		try {
			new ProgressMonitorDialog(null).run(true, true, this);
		} catch (InvocationTargetException e) {
			throw new SystemException(e);
		} catch (InterruptedException e) {
			throw new SystemException(e);
		}
		String errorMessage = "获取数据库失败";
		if(connection == null){
			if(runtiemException != null){
				throw runtiemException;
			}else{
				throw new ApplicationException(errorMessage);
			}
		}
		return connection;
	}
}
