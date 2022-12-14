package coinDb;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.util.List;

import operation.Checker;
import operation.ChooseDb;
import operation.CustomException;
import operation.MysqlOperation;
import operation.PsqlOperation;
import user.User;

public class MailDb {
	
	ChooseDb store = null;
	
	public void setMysql()
	{
		store = new MysqlOperation();
	}
	
	public void setPsql()
	{
		store = new PsqlOperation();
	}
	
	public boolean checkDomainInteger()throws CustomException
	{
		boolean domain=false;
		
		try (PreparedStatement statement = store.getConnection().
				prepareStatement(store.checkDomainInteger()))
		{
			try (ResultSet result = statement.executeQuery()) 
			{
				while (result.next()) 
				{
					 domain = result.getBoolean(1);
				}
				
				return domain;
			}
		}
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			
			throw new CustomException("Unable to get Domain for number");
		}
	}
	
	public boolean checkDomainMail()throws CustomException
	{
		boolean domain=false;
		
		try (PreparedStatement statement = store.getConnection().
				prepareStatement(store.checkDomainMail()))
		{
			try (ResultSet result = statement.executeQuery()) 
			{
				while (result.next()) 
				{
					 domain = result.getBoolean(1);
				}
				
				return domain;
			}
		}
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			
			throw new CustomException("Unable to get Domain for mail");
		}
	}
	
	public void createDomainInteger()throws CustomException
	{
		try (PreparedStatement statement = store.getConnection()
				.prepareStatement(store.createDomainInteger())) {
			statement.execute();
		} 
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			
			throw new CustomException("Unable to create Domain for number");
		}
	}
	
	public void createDomainMail()throws CustomException
	{
		try (PreparedStatement statement = store.getConnection()
				.prepareStatement(store.createDomainMail())) {
			statement.executeUpdate();
		} 
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			throw new CustomException("Unable to create Domain for mail");
		}
	}
	
	public void createSequenceId()throws CustomException
	{
		try (PreparedStatement statement = store.getConnection()
				.prepareStatement(store.createSequenceId())) {
			statement.executeUpdate();
		} 
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			
			throw new CustomException("Unable to create Sequence for Id");
		}
	}
	
	public void createSequenceAccount()throws CustomException
	{
		try (PreparedStatement statement = store.getConnection()
				.prepareStatement(store.createSequenceAccount())) {
			statement.executeUpdate();
		} 
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			
			throw new CustomException("Unable to create Sequence for account number");
		}
	}
	
	public void createDatabase()throws CustomException
	{
		
		try (PreparedStatement statement = store.getConnection()
				.prepareStatement(store.createDatabase())) {
			statement.executeUpdate();
		} 
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
		
			throw new CustomException("Unable to create Database");
		}
	}
	
	public void createDatabasePsql()throws CustomException
	{
		try (PreparedStatement statement = store.getConnection()
				.prepareStatement(store.createDatabase())) {
			statement.execute();
		} 
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			
			throw new CustomException("Unable to create Database");
		}
	}
	
	public void createTable()throws CustomException
	{
		
		
		try (PreparedStatement statement = store.getConnection()
				.prepareStatement(store.createMailTable())) {
			statement.executeUpdate();
		} 
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			throw new CustomException("Unable to create Mail Table");
		}
	}
	
	public int getId(String mail)throws CustomException
	{
		Checker check = new Checker();
		
		check.checkString(mail);
		
		
		int id=0;
		
		try (PreparedStatement statement = store.getConnection()
				.prepareStatement(store.getId(),
				PreparedStatement.RETURN_GENERATED_KEYS)) 
		{
			statement.setString(1, mail);
			
			try (ResultSet result = statement.executeQuery()) 
			{
				while (result.next()) 
				{
					id=result.getInt("user_id");
				}
				
				return id;
			}
		}
		catch(CustomException e)
		{
			throw new CustomException("USERNAME");
		}
		catch (Exception e) {
			throw new CustomException("Id not available");
		}
    }
	
	public void addMail(String mail,int id)throws CustomException
	{
		
		try (PreparedStatement statement = store.getConnection()
				.prepareStatement(store.addMail(),
				PreparedStatement.RETURN_GENERATED_KEYS)) 
		{
			statement.setInt(1, id);
			statement.setString(2, mail);
			
			statement.executeUpdate();
			
		}
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			throw new CustomException("MAIL"); // No I18N
		}
	}
	
	public String getMailById(int id)throws CustomException
	{
		
		
		String mail="";
		
		
		try (PreparedStatement statement = store.getConnection()
				.prepareStatement(store.getMailById(),
				PreparedStatement.RETURN_GENERATED_KEYS)) 
		{
			statement.setInt(1, id);
			
			try (ResultSet result = statement.executeQuery()) 
			{
				while (result.next()) 
				{
					mail=result.getString("mail_id");
				}
				
				return mail;
			}
		}
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			
			throw new CustomException("Mail id not available");
		}

		
	}
	
	public boolean checkMailExists(String mail)throws CustomException
	{
		
		String mail_id=null;
		boolean check=true;
		
		try (PreparedStatement statement = store.getConnection()
				.prepareStatement(store.checkMailExists(),
				PreparedStatement.RETURN_GENERATED_KEYS)) 
		{
			statement.setString(1, mail);
			
			try (ResultSet result = statement.executeQuery()) 
			{
				while (result.next()) 
				{
					mail_id = result.getString("mail_id");
					
					if(!mail_id.equals(mail))
					{
						throw new CustomException("MAIL");
					}
				}
				return check;
			}
		}
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			throw new CustomException("MAIL");
		}
	}
	

	

}
