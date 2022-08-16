<%@ page import="operation.CoinOperation" %>
<%@ page import="user.User" %>
<%@ page import="instance.CreateInstance"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SIGN UP</title>
<link rel="stylesheet" href="Signup.css">
</head>
<body>
<%
CoinOperation coin = CreateInstance.COINOPERATION.getCoinInstance();

String typeName = (String)request.getParameter("type");

if(typeName.equals("signup"))
{
%>
<div class="total">
<div class="container">
<form id="login_form">
<h2>SIGN UP </h2><br>
<p id="msg"></p>
<label>NAME</label><br>
<input type="text" placeholder="Name" name="username" id="name" required><br>
<label>MAIL ID</label><br>
<input type="text" placeholder="Email address" name="mail" id="mail" required><br>
<label>MOBILE NUMBER</label><br>
<input type="text" placeholder="Mobile Number" name="mobile" id="mobile"  required><br>
<label>HUMAN ID</label><br>
<input type="text" placeholder="Human Id" name="human_id" id="human_id" required><br>
<label>PASSWORD</label><br>
<input type="password" placeholder="Password" name="password" id="pass" required><br>
<label>AMOUNT</label><br>
<input type="text" placeholder="Amount" name="amount" id="amount" required><br><br>
<input type="button" id="loginbtn" value="SIGNUP" onclick="signup()"><br><br>
</form>
</div>
</div>
<%}
else
{
	int id = (int)session.getAttribute("user_id");
	
	
	User user = coin.getUser(id);

%>		
<jsp:include page='Menu.jsp'>
<jsp:param name="TRANSFER" value=" " />
</jsp:include>
<div class="total">
<div class="container">
<form id="login_form">
<h2>PROFILE</h2><br>
<p id="msg"></p>
<label>NAME</label><br>
<input type="text" id="name" value=<%=user.getName()%>><br>
<label>MAIL ID</label><br>
<input type="text" id="mail" value=<%=user.getMail()%>><br>
<label>MOBILE NUMBER</label><br>
<input type="text"  id="mobile" value=<%=user.getMobile()%>><br>
<label>HUMAN ID</label><br>
<input type="text" id="human_id" value=<%=user.getHuman_id()%>><br>
<label>AMOUNT</label><br>
<input type="text" id="amount" value=<%=user.getRc_amount()%>>
</form>
</div>
</div>
<%} %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">

		function signup()
		{
			var name=$('#name').val();
			var mail=$('#mail').val();
			var mobile=$('#mobile').val();
			var human_id=$('#human_id').val();
			var password=$('#pass').val();
			var amount=$('#amount').val();
			
			$.ajax({
				
				type : 'POST',
				url: 'signUp',
				data :{name : name, mail : mail,mobile : mobile,human_id : human_id,
					password : password,amount :amount},
					
				success:function(result)
				{
					if(result=="User already exists") 
					{
						 $("#msg").empty();
						 
					 $('#msg').append("User already exists");
					 
					 var successUrl = "Login.jsp";
					    window.location.href = successUrl;
					  
					}
					else
					{
						 $("#msg").empty();
						 
						 $('#msg').append("Successfully added!");
			
					}
					
				},
				
				error: function(xhr)
				{

					try
					{
					if(xhr.status==401)
					{
						throw "Oops! Connection failed! "; // No I18N
					}
					
					else if(xhr.status==402)
					{
						throw "Error! couldn't close Connection! "; // No I18N
					}
					else if(xhr.status==403)
					{
						throw "Username should not be empty!"; // No I18N
					}
					else if(xhr.status==404)
					{
						throw "Password should not be empty!";  // No I18N
					}
					else if(xhr.status==405)
					{
						throw "Mail id already exists"; // No I18N
					}
					else
					{
						throw "Error! Something went wrong"; // No I18N
					}
					}
					catch(err)
					{
					$("#msg").empty();
					  
			        
					 $('#smg').append(err);
					 	 
					 
					 $(".total").click(function()
						{
						  $("#msg").empty();
						});
					}
					}
				}); 
		}
</script>	
</body>
</html>