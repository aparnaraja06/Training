package servlet;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import operation.Checker;
import operation.CustomException;
import operation.CreateInstance;
import operation.CoinOperation;
import operation.ErrorMsg;
import transaction.Transaction;


public class TransactionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
   	 response.setContentType("text/html");
   	 
   
   	
   	PrintWriter out=response.getWriter();
   	
   	HttpSession session = request.getSession();
	
	try
	{
		//CoinOperation coin=(CoinOperation)request.getServletContext().getAttribute("Instance");
		
		CoinOperation coin = CreateInstance.COINOPERATION.getCoinInstance();
		
		Checker check = CreateInstance.COINOPERATION.getCheckInstance();
		
		
		int id = (int)request.getSession().getAttribute("user_id");
		
		int acc_num = coin.getAccountNumById(id);
		
		
		String msg = "error";
		
		boolean result=false;
		
		if(request.getParameter("type").equals("withdraw"))
		{
		String name =  request.getParameter("name");
		check.validateAmount(name);
		double amount = Double.parseDouble(name);
		result = coin.withdrawRc(acc_num, amount);
	
		}
		else if(request.getParameter("type").equals("deposit"))
		{
			String name =  request.getParameter("name");
			check.validateAmount(name);
			double amount = Double.parseDouble(name);
			result=coin.depositRc(acc_num, amount);
		}
		else
		{
			String account1 = request.getParameter("account1");
			
		check.checkInteger(account1);
		
		int acc1 = Integer.parseInt(account1);
		   	
			String account2 = request.getParameter("account2");
			
			check.checkInteger(account2);
			
		int acc2 = Integer.parseInt(account2);	
			
			if(acc1 != acc2)
			{
				session.setAttribute("Error", msg);
				
				out.print(msg);
			}
			
			//int acc_num2 =Integer.parseInt(account2);
			
			String amountt = request.getParameter("amount");
			
			check.validateAmount(amountt);
			
			double get_amount = Double.parseDouble(amountt);
			
			result = coin.transferZCoin(acc_num, acc2, get_amount);
			
			if(!result)
			{
				session.setAttribute("Error", msg);
				
				out.print(msg);
			}
			
			Transaction transfer = new Transaction();
			
			transfer.setFrom_account(acc_num);
			transfer.setTo_account(acc2);
			transfer.setAmount(get_amount);
			transfer.setType("Transferred");
			transfer.setUser_id(id);
			
			String date = coin.getDate();
			
			transfer.setDate(date);
			
			coin.addTransaction(transfer);
			
		}
		
		
		
	}
	catch(CustomException e)
	{
		
		String msg=e.getMessage();
		
		ErrorMsg err = ErrorMsg.valueOf(msg);
		
		int code = err.getCode();
		
		response.sendError(code);
		
	}
   	}

}
