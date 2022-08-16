package coinDb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connect.MysqlConnection;
import custom.CustomException;
import transaction.Transaction;

public class TransactionDb 
{

	public void createTable()throws CustomException
	{
		String query="CREATE TABLE IF NOT EXISTS transaction(user_id int not null, from_account int not null,"
				+ " to_account int not null, type varchar(10) not null, amount double , date varchar(20),"
				+ " foreign key(user_id) references user(user_id), "
				+ "foreign key(from_account) references account(account_num))";
		
		try (PreparedStatement statement = MysqlConnection.CONNECTION.getConnection().prepareStatement(query)) {
			statement.executeUpdate();
		} 
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			throw new CustomException("Unable to create Transaction Table");
		}
	}
	
	public void addTransaction(Transaction transfer)throws CustomException
	{
		String query="INSERT INTO transaction(user_id,from_account,to_account,type,amount,date) "
				+ "VALUES(?,?,?,?,?,?)";
		
		try (PreparedStatement statement = MysqlConnection.CONNECTION.getConnection().prepareStatement(query)) {
		
			int user_id = transfer.getUser_id();
			int from_account = transfer.getFrom_account();
			int to_account = transfer.getTo_account();
			double amount = transfer.getAmount();
			String date = transfer.getDate();
			String type = transfer.getType();
			
			statement.setInt(1, user_id);
			statement.setInt(2, from_account);
			statement.setInt(3, to_account);
			statement.setString(4, type);
			statement.setDouble(5, amount);
			statement.setString(6, date);
			
			statement.executeUpdate();
		}
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			
			//System.out.println("trans : "+e.getMessage());
			throw new CustomException("Unable to add transaction details");
		}

	}
	
	public Map<Integer,List<Transaction>> getAllHistory()throws CustomException
	{
		String query = "SELECT * FROM transaction";
		
		Map<Integer,List<Transaction>> transactionMap = new HashMap<>();
		
		try (PreparedStatement statement = MysqlConnection.CONNECTION.getConnection().prepareStatement(query)) 
		{
			try (ResultSet result = statement.executeQuery()) 
			{
				while (result.next()) 
				{
					int user_id = result.getInt("user_id");
					
					List<Transaction> list = transactionMap.get(user_id);
					
					if(list==null)
					{
						list = new ArrayList<>();
					}

					Transaction transfer = new Transaction();
					
					transfer.setUser_id(user_id);
				    transfer.setFrom_account(result.getInt("from_account"));
					transfer.setTo_account(result.getInt("to_account"));
					transfer.setAmount(result.getDouble("amount"));
					transfer.setType(result.getString("type"));
					transfer.setDate(result.getString("date"));
					
					list.add(transfer);
					
					transactionMap.put(user_id, list);
					
				}
				
				return transactionMap;
		}
		}
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			throw new CustomException("Unable to get history");
		}
	}
	
	public List<Transaction> getHistoryByUserId(int user_id)throws CustomException
	{
		String query = "SELECT * FROM transaction WHERE user_id=?";
		
		List<Transaction> list = new ArrayList<>();
		
		try (PreparedStatement statement = MysqlConnection.CONNECTION.getConnection().prepareStatement(query)) 
		{
			statement.setInt(1, user_id);
			
			try (ResultSet result = statement.executeQuery()) 
			{
				while (result.next()) 
				{
					Transaction transfer = new Transaction();
					
					transfer.setTo_account(result.getInt("to_account"));
					transfer.setAmount(result.getDouble("amount"));
					transfer.setType(result.getString("type"));
					transfer.setDate(result.getString("date"));
					
					list.add(transfer);
				}
				
				return list;
			}
		}
		catch(CustomException e)
		{
			throw new CustomException(e.getMessage());
		}
		catch (Exception e) {
			throw new CustomException("Unable to get history");
		}
	}
}